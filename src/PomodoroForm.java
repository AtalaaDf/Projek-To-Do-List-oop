import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
 * Pomodoro timer form.
 *
 * Responsibilities:
 *  - Show a countdown timer that the user configures (menit + detik)
 *  - Start / Pause / Resume / Reset the timer
 *  - Play alarm via SoundManager when time runs out
 *  - "Selesai" → remove the task from MainForm and return
 *  - "Back"    → return to MainForm without removing the task
 */
public class PomodoroForm extends JFrame {

    // ── References ────────────────────────────────────────────────────────────
    private final int        taskIndex;
    private final MainForm   mainFormRef;
    private final SoundManager soundManager;

    // ── Timer state ───────────────────────────────────────────────────────────
    private final Timer timerSwing;
    private int     totalDetik = 0;
    private boolean isRunning  = false;

    // ── UI components ─────────────────────────────────────────────────────────
    private final JLabel     lblTimerDisplay = UIComponents.createTimerDisplayLabel();
    private final JLabel     lblTaskName     = UIComponents.createTaskNameLabel();
    private final JTextField txtMenit        = UIComponents.createTimerInputField();
    private final JTextField txtDetik        = UIComponents.createTimerInputField();
    private final JButton    btnStartPause   = UIComponents.createStartPauseButton();
    private final JButton    btnReset        = UIComponents.createResetButton();
    private final JButton    btnBack         = UIComponents.createBackButton();
    private final JButton    btnSelesai      = UIComponents.createSelesaiButton();

    // ── Constructor ───────────────────────────────────────────────────────────

    public PomodoroForm(int taskIndex, MainForm mainFormRef) {
        this.taskIndex   = taskIndex;
        this.mainFormRef = mainFormRef;
        this.soundManager = mainFormRef.getSoundManager();

        setTitle("Pomodoro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Show the selected task name
        if (taskIndex != -1 && taskIndex < mainFormRef.getTaskManager().getSize()) {
            lblTaskName.setText(mainFormRef.getTaskManager().getTask(taskIndex));
        }

        // Build 1-second countdown timer
        timerSwing = new Timer(1000, e -> onTick());

        buildLayout();
        wireListeners();

        pack();
        setLocationRelativeTo(null);
    }

    // ── Layout ────────────────────────────────────────────────────────────────

    private void buildLayout() {
        JPanel mainPanel   = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIComponents.COLOR_BACKGROUND);
        mainPanel.setPreferredSize(new java.awt.Dimension(665, 400));

        // Header
        JPanel headerPanel = UIComponents.createHeaderPanel();
        headerPanel.add(UIComponents.createPomodoroTitleLabel());

        // Timer panel (green box with big countdown)
        JPanel timerPanel = UIComponents.createTimerPanel();
        timerPanel.setLayout(new java.awt.GridBagLayout());
        timerPanel.add(lblTimerDisplay);
        timerPanel.setPreferredSize(new java.awt.Dimension(270, 152));

        // Input row: [txtMenit] : [txtDetik]
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        inputRow.setOpaque(false);
        inputRow.add(txtMenit);
        inputRow.add(UIComponents.createColonLabel());
        inputRow.add(txtDetik);

        // Button row: [Start/Pause] [Reset]
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        btnRow.setOpaque(false);
        btnRow.add(btnStartPause);
        btnRow.add(btnReset);

        // Right column: task name + timer panel + inputs + buttons
        JPanel rightCol = new JPanel();
        rightCol.setOpaque(false);
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.add(Box.createVerticalStrut(8));
        rightCol.add(lblTaskName);
        rightCol.add(Box.createVerticalStrut(6));
        rightCol.add(timerPanel);
        rightCol.add(Box.createVerticalStrut(8));
        rightCol.add(inputRow);
        rightCol.add(btnRow);

        // Bottom nav: [Back] ............. [Selesai]
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setOpaque(false);
        navBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        navBar.add(btnBack,    BorderLayout.WEST);
        navBar.add(btnSelesai, BorderLayout.EAST);

        // Assemble
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(rightCol,    BorderLayout.CENTER);
        mainPanel.add(navBar,      BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    private void wireListeners() {
        btnStartPause.addActionListener(e -> onStartPause());
        btnReset.addActionListener(e      -> onReset());
        btnBack.addActionListener(e       -> onBack());
        btnSelesai.addActionListener(e    -> onSelesai());
    }

    // ── Timer tick ────────────────────────────────────────────────────────────

    private void onTick() {
        totalDetik--;
        updateTimerDisplay();

        if (totalDetik <= 0) {
            timerSwing.stop();
            isRunning = false;
            btnStartPause.setText("Start");
            lblTimerDisplay.setText("00:00");

            Clip alarm = soundManager.playAlarm();
            JOptionPane.showMessageDialog(getRootPane(), "Waktu Habis!");
            soundManager.stopAlarm(alarm);
        }
    }

    // ── Button actions ────────────────────────────────────────────────────────

    private void onStartPause() {
        if (isRunning) {
            // Pause
            timerSwing.stop();
            isRunning = false;
            btnStartPause.setText("Resume");
        } else {
            // First start: read input fields
            if ("Start".equals(btnStartPause.getText())) {
                Integer parsed = parseTimeInput();
                if (parsed == null) return;
                totalDetik = parsed;
            }

            if (totalDetik > 0) {
                timerSwing.start();
                isRunning = true;
                btnStartPause.setText("Pause");
            }
        }
    }

    private void onReset() {
        timerSwing.stop();
        isRunning = false;
        btnStartPause.setText("Start");

        Integer parsed = parseTimeInputSafe();
        totalDetik = (parsed != null) ? parsed : 0;
        updateTimerDisplay();
    }

    private void onBack() {
        timerSwing.stop();
        mainFormRef.setVisible(true);
        dispose();
    }

    private void onSelesai() {
        if (mainFormRef == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: Referensi ke MainForm tidak ditemukan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        timerSwing.stop();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tugas selesai! Hapus dari daftar beserta deadlinenya?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            mainFormRef.getTaskManager().removeTask(taskIndex);
            // FileManager.saveAll() is called via MainForm reference
            // We access it through the public getter added in MainForm
            mainFormRef.getFileManager().saveAll();

            mainFormRef.setVisible(true);
            dispose();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Updates the timer label from current {@code totalDetik}. */
    private void updateTimerDisplay() {
        int menit = totalDetik / 60;
        int detik = totalDetik % 60;
        lblTimerDisplay.setText(String.format("%02d:%02d", menit, detik));
    }

    /**
     * Reads menit and detik fields, validates, and returns total seconds.
     * Shows an error dialog and returns {@code null} on bad input.
     */
    private Integer parseTimeInput() {
        try {
            int menit = Integer.parseInt(txtMenit.getText().trim());
            int detik = Integer.parseInt(txtDetik.getText().trim());
            int total = (menit * 60) + detik;

            if (total <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Input waktu harus lebih besar dari 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return total;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Input menit dan detik harus berupa angka!",
                    "Error Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Same as {@link #parseTimeInput()} but returns 0 (instead of null)
     * on bad input, used for reset where we just want a fallback.
     */
    private Integer parseTimeInputSafe() {
        try {
            String m = txtMenit.getText().isEmpty() ? "0" : txtMenit.getText().trim();
            String d = txtDetik.getText().isEmpty() ? "0" : txtDetik.getText().trim();
            return (Integer.parseInt(m) * 60) + Integer.parseInt(d);
        } catch (NumberFormatException e) {
            txtMenit.setText("0");
            txtDetik.setText("0");
            return 0;
        }
    }
}