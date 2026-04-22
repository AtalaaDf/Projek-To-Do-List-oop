import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * Main application window for the To-Do-List app.
 *
 * Responsibilities:
 *  - Assemble UI components (via UIComponents)
 *  - Wire up button actions
 *  - Delegate data operations to TaskManager & FileManager
 */
public class MainForm extends JFrame {

    // ── Helpers ───────────────────────────────────────────────────────────────
    private final TaskManager taskManager  = new TaskManager();
    private final FileManager fileManager  = new FileManager(taskManager);
    private final SoundManager soundManager = new SoundManager();

    // ── UI components ─────────────────────────────────────────────────────────
    private final JList<String> taskList    = UIComponents.createTaskList();
    private final JLabel deadlineLabel      = UIComponents.createDeadlineLabel();

    private final JButton tambahButton  = UIComponents.createTambahButton();
    private final JButton hapusButton   = UIComponents.createHapusButton();
    private final JButton startButton   = UIComponents.createStartButton();
    private final JButton editButton    = UIComponents.createEditButton();

    // ── Date format ───────────────────────────────────────────────────────────
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Constructor ───────────────────────────────────────────────────────────
    public MainForm() {
        setTitle("To-Do-List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Attach models
        taskList.setModel(taskManager.getTaskModel());

        // Load persisted data
        fileManager.loadTasks();
        fileManager.loadDeadlines();

        // Build layout
        buildLayout();

        // Wire listeners
        wireListeners();

        pack();
        setLocationRelativeTo(null);
    }

    // ── Layout ────────────────────────────────────────────────────────────────

    private void buildLayout() {
        JPanel mainPanel   = UIComponents.createMainPanel();
        JPanel headerPanel = UIComponents.createHeaderPanel();
        JPanel deadlinePanel = UIComponents.createDeadlinePanel();

        JLabel titleLabel  = UIComponents.createTitleLabel();
        JScrollPane scrollPane = UIComponents.createScrollPane(taskList);

        // Header
        headerPanel.add(titleLabel);

        // Deadline strip
        deadlinePanel.add(deadlineLabel);

        // Button grid (2 × 2)
        JPanel buttonPanel = new JPanel(new java.awt.GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(tambahButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(editButton);
        buttonPanel.add(startButton);

        // Left column
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, javax.swing.BoxLayout.Y_AXIS));
        leftPanel.add(buttonPanel);
        leftPanel.add(javax.swing.Box.createVerticalStrut(16));
        leftPanel.add(deadlinePanel);

        // Assemble main panel
        mainPanel.setLayout(new java.awt.BorderLayout(20, 10));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        mainPanel.add(leftPanel, java.awt.BorderLayout.WEST);

        setContentPane(mainPanel);
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    private void wireListeners() {
        tambahButton.addActionListener(e -> onTambah());
        hapusButton.addActionListener(e  -> onHapus());
        editButton.addActionListener(e   -> onEdit());
        startButton.addActionListener(e  -> onStart());

        taskList.addListSelectionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx != -1 && idx < taskManager.getDeadlineModel().getSize()) {
                deadlineLabel.setText("Deadline : " + taskManager.getDeadline(idx));
            } else {
                deadlineLabel.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileManager.saveAll();
            }
        });
    }

    // ── Button actions ────────────────────────────────────────────────────────

    private void onTambah() {
        JTextField inputKegiatan = new JTextField();
        JTextField inputDeadline = new JTextField();
        JPanel panel = UIComponents.createAddTaskPanel(inputKegiatan, inputDeadline);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Tambah Tugas Baru",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String kegiatan      = inputKegiatan.getText().trim();
        String deadlineInput = inputDeadline.getText().trim();

        if (kegiatan.isEmpty() || deadlineInput.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Isi kegiatan dan deadline dengan lengkap!");
            return;
        }

        String parsedDeadline = parseDeadline(deadlineInput);
        if (parsedDeadline == null) return;

        taskManager.addTask(kegiatan, parsedDeadline);
        fileManager.saveAll();
    }

    private void onHapus() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih tugas yang ingin dihapus!",
                    "Tidak ada tugas dipilih", JOptionPane.WARNING_MESSAGE);
            return;
        }

        taskManager.removeTask(index);
        fileManager.saveAll();
        deadlineLabel.setText("");
    }

    private void onEdit() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih tugas yang ingin diedit!",
                    "Tidak ada tugas dipilih", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentTask     = taskManager.getTask(index);
        String currentDeadline = taskManager.getDeadline(index);

        String newTask = JOptionPane.showInputDialog(this, "Edit tugas:", currentTask);
        if (newTask == null || newTask.trim().isEmpty()) return;

        String newDeadlineInput = JOptionPane.showInputDialog(
                this, "Masukkan deadline baru (format: dd/MM/yyyy):", currentDeadline);
        if (newDeadlineInput == null) return;

        String parsedDeadline = parseDeadline(newDeadlineInput.trim());
        if (parsedDeadline == null) return;

        taskManager.editTask(index, newTask.trim(), parsedDeadline);
        deadlineLabel.setText("Deadline : " + parsedDeadline);
        fileManager.saveAll();
    }

    private void onStart() {
        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih tugas dulu sebelum mulai Pomodoro!");
            return;
        }

        PomodoroForm pomodoro = new PomodoroForm(index, this);
        pomodoro.setVisible(true);
        setVisible(false);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Validates and parses a deadline string.
     *
     * @param input raw string from user (expected dd/MM/yyyy)
     * @return formatted date string, or {@code null} if invalid
     */
    private String parseDeadline(String input) {
        try {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT);
            return date.format(DATE_FORMAT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Deadline harus berformat dd/MM/yyyy\nContoh: 07/12/2025",
                    "Format Salah", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Exposes FileManager so PomodoroForm can trigger saveAll(). */
    public FileManager getFileManager() {
        return fileManager;
    }

    /** Exposes SoundManager so other forms (e.g. PomodoroForm) can reuse it. */
    public SoundManager getSoundManager() {
        return soundManager;
    }

    /** Exposes TaskManager so other forms can access task data. */
    public TaskManager getTaskManager() {
        return taskManager;
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fall back to default L&F silently
        }

        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }
}