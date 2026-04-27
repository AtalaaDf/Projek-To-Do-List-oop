import java.awt.*;
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
 *  - Support per-user data isolation via username
 */

public class MainForm extends JFrame {

    // ── Helpers ───────────────────────────────────────────────────────────────
    private final TaskManager taskManager   = new TaskManager();
    private final FileManager fileManager;
    private final SoundManager soundManager = new SoundManager();
    private final String username;

    // ── UI components ─────────────────────────────────────────────────────────
    private final JList<String> taskList    = UIComponents.createTaskList();
    private final JLabel deadlineLabel      = UIComponents.createDeadlineLabel();
    private final JLabel categoryLabel      = UIComponents.createCategoryLabel();

    // Modern buttons (pakai ModernButton dari UIComponents)
    private final UIComponents.ModernButton tambahButton = new UIComponents.ModernButton(
        "Tambah", 20,
        new Color(101, 77, 100), new Color(130, 100, 130), new Color(80, 57, 80)
    );
    private final UIComponents.ModernButton hapusButton = new UIComponents.ModernButton(
        "Hapus", 20,
        new Color(180, 70, 70), new Color(210, 90, 90), new Color(150, 50, 50)
    );
    private final UIComponents.ModernButton editButton = new UIComponents.ModernButton(
        "Edit", 20,
        new Color(60, 120, 160), new Color(80, 150, 190), new Color(45, 100, 140)
    );
    private final UIComponents.ModernButton startButton = new UIComponents.ModernButton(
        "Start", 20,
        new Color(60, 150, 90), new Color(80, 180, 110), new Color(45, 125, 70)
    );
    private final JComboBox<String> filterKategori = new JComboBox<>();

    // ── Date format ───────────────────────────────────────────────────────────
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Constructor (dengan username untuk per-user data) ─────────────────────
    public MainForm(String username) {
        this.username = username;
        this.fileManager = new FileManager(taskManager, username);

        setTitle("To-Do-List — " + username);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Attach models
        taskList.setModel(taskManager.getTaskModel());

        // Load persisted data (per-user)
        fileManager.loadTasks();
        fileManager.loadDeadlines();
        fileManager.loadCategories();

        // Build layout
        buildLayout();

        // Wire listeners
        wireListeners();

        pack();
        setLocationRelativeTo(null);
    }

    /** Backward-compatible no-arg constructor (default user). */
    public MainForm() {
        this("user");
    }

    // ── Layout ────────────────────────────────────────────────────────────────

    private void buildLayout() {
        // Background utama: gradient vertikal seperti LoginForm
        UIComponents.ModernGradientVerPanel mainBackground = new UIComponents.ModernGradientVerPanel(
            0,
            new Color(230, 210, 220),
            new Color(150, 120, 150)
        );
        mainBackground.setLayout(new BorderLayout(0, 0));
        mainBackground.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── HEADER ────────────────────────────────────────────────────────────
        UIComponents.ModernGradientHorPanel headerPanel = new UIComponents.ModernGradientHorPanel(
            20,
            new Color(101, 77, 100),
            new Color(60, 40, 70)
        );
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        UIComponents.ModernLabel titleLabel = new UIComponents.ModernLabel(
            "📋  To-Do-List", 32, true, Color.WHITE
        );
        UIComponents.ModernLabel userLabel = new UIComponents.ModernLabel(
            "👤  " + username, 14, false, new Color(220, 200, 220)
        );
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        // ── CENTER: Task list card ────────────────────────────────────────────
        UIComponents.ModernShadowPanel listCard = new UIComponents.ModernShadowPanel(
            20, 12, new Color(255, 250, 255)
        );
        listCard.setLayout(new BorderLayout());
        listCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Style task list
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        taskList.setBackground(new Color(255, 250, 255));
        taskList.setSelectionBackground(new Color(180, 140, 180));
        taskList.setSelectionForeground(Color.WHITE);
        taskList.setCellRenderer(new UIComponents.TaskListCellRenderer(taskManager.getCategoryModel()));
        taskList.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = UIComponents.createScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(255, 250, 255));
        // Panel filter kategori (di atas list)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter:"));

        filterKategori.addItem("Semua");
        for (String kat : UIComponents.KATEGORI) filterKategori.addItem(kat);
        filterKategori.setPreferredSize(new Dimension(130, 28));
        filterPanel.add(filterKategori);

        listCard.add(filterPanel, BorderLayout.NORTH);
        listCard.add(scrollPane, BorderLayout.CENTER);

        // ── WEST: Buttons + info panel ────────────────────────────────────────
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));

        // Tombol-tombol (ModernButton, full width di kolom kiri)
        Dimension btnSize = new Dimension(180, 44);
        tambahButton.setPreferredSize(btnSize);
        tambahButton.setMaximumSize(btnSize);
        tambahButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        hapusButton.setPreferredSize(btnSize);
        hapusButton.setMaximumSize(btnSize);
        hapusButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        editButton.setPreferredSize(btnSize);
        editButton.setMaximumSize(btnSize);
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.setPreferredSize(btnSize);
        startButton.setMaximumSize(btnSize);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(tambahButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(hapusButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(editButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(startButton);
        leftPanel.add(Box.createVerticalStrut(20));

        // Info card: deadline + kategori
        UIComponents.ModernShadowPanel infoCard = new UIComponents.ModernShadowPanel(
            16, 10, new Color(240, 230, 245)
        );
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        infoCard.setPreferredSize(new Dimension(180, 90));
        infoCard.setMaximumSize(new Dimension(180, 90));
        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        deadlineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        deadlineLabel.setForeground(new Color(80, 50, 80));
        deadlineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        categoryLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        categoryLabel.setForeground(new Color(101, 77, 100));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoCard.add(deadlineLabel);
        infoCard.add(Box.createVerticalStrut(6));
        infoCard.add(categoryLabel);

        leftPanel.add(infoCard);

        // ── CENTER wrapper ────────────────────────────────────────────────────
        JPanel centerWrapper = new JPanel(new BorderLayout(16, 0));
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        centerWrapper.add(leftPanel, BorderLayout.WEST);
        centerWrapper.add(listCard, BorderLayout.CENTER);

        mainBackground.add(headerPanel, BorderLayout.NORTH);
        mainBackground.add(centerWrapper, BorderLayout.CENTER);

        setContentPane(mainBackground);
        setPreferredSize(new Dimension(720, 480));
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    private void wireListeners() {
        tambahButton.addActionListener(e -> onTambah());
        hapusButton.addActionListener(e  -> onHapus());
        editButton.addActionListener(e   -> onEdit());
        startButton.addActionListener(e  -> onStart());
        filterKategori.addActionListener(e -> applyFilter());

        taskList.addListSelectionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx != -1 && idx < taskManager.getDeadlineModel().getSize()) {
                deadlineLabel.setText("📅 " + taskManager.getDeadline(idx));
                categoryLabel.setText("🏷 " + taskManager.getCategory(idx));
            } else {
                deadlineLabel.setText("");
                categoryLabel.setText("");
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
        JComboBox<String> kategoriBox = UIComponents.createCategoryComboBox();
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nama Kegiatan:"));
        panel.add(inputKegiatan);
        panel.add(new JLabel("Deadline (dd/MM/yyyy):"));
        panel.add(inputDeadline);
        panel.add(new JLabel("Kategori:"));
        panel.add(kategoriBox);

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

        String kategori = (String) kategoriBox.getSelectedItem();
        taskManager.addTask(kegiatan, parsedDeadline, kategori);
        applyFilter();
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
        applyFilter();
        fileManager.saveAll();
        deadlineLabel.setText("");
        categoryLabel.setText("");
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
        String currentCategory = taskManager.getCategory(index);

        String newTask = JOptionPane.showInputDialog(this, "Edit tugas:", currentTask);
        if (newTask == null || newTask.trim().isEmpty()) return;

        String newDeadlineInput = JOptionPane.showInputDialog(
                this, "Masukkan deadline baru (format: dd/MM/yyyy):", currentDeadline);
        if (newDeadlineInput == null) return;

        String parsedDeadline = parseDeadline(newDeadlineInput.trim());
        if (parsedDeadline == null) return;

        JComboBox<String> kategoriBox = UIComponents.createCategoryComboBox();
        kategoriBox.setSelectedItem(currentCategory);

        int resultKategori = JOptionPane.showConfirmDialog(
                this, kategoriBox, "Pilih Kategori",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultKategori != JOptionPane.OK_OPTION) return;

        String newCategory = (String) kategoriBox.getSelectedItem();

        taskManager.editTask(index, newTask.trim(), parsedDeadline, newCategory);
        deadlineLabel.setText("📅 " + parsedDeadline);
        categoryLabel.setText("🏷 " + newCategory);
        applyFilter();
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

    private void applyFilter() {
    String selected = (String) filterKategori.getSelectedItem();
    if (selected == null || selected.equals("Semua")) {
        taskList.setModel(taskManager.getTaskModel());
        // Kembalikan renderer ke model utama
        taskList.setCellRenderer(
            new UIComponents.TaskListCellRenderer(taskManager.getCategoryModel())
        );
        return;
    }

    // Buat model sementara hanya untuk tampilan filter
    DefaultListModel<String> filtered = new DefaultListModel<>();
    for (int i = 0; i < taskManager.getTaskModel().getSize(); i++) {
        if (selected.equals(taskManager.getCategory(i))) {
            filtered.addElement(taskManager.getTask(i));
        }
    }

    // Buat categoryModel sementara yang selaras dengan filtered
    DefaultListModel<String> filteredCat = new DefaultListModel<>();
    for (int i = 0; i < taskManager.getTaskModel().getSize(); i++) {
        if (selected.equals(taskManager.getCategory(i))) {
            filteredCat.addElement(taskManager.getCategory(i));
        }
    }

    taskList.setModel(filtered);
    taskList.setCellRenderer(
        new UIComponents.TaskListCellRenderer(filteredCat)
    );
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

        java.awt.EventQueue.invokeLater(() -> new MainForm("user").setVisible(true));
    }
}