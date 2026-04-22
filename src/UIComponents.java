import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Factory class for creating and styling all UI components.
 * Keeps visual setup out of the main form logic.
 */
public class UIComponents {

    // ── Shared colour palette ─────────────────────────────────────────────────
    public static final Color COLOR_BROWN       = new Color(102, 51, 0);
    public static final Color COLOR_WHITE       = Color.WHITE;
    public static final Color COLOR_TEAL        = new Color(90, 130, 126);
    public static final Color COLOR_BACKGROUND  = new Color(254, 248, 238);

    // ── Buttons ───────────────────────────────────────────────────────────────

    /** Creates a styled action button with the app's brown theme. */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_BROWN);
        button.setForeground(COLOR_WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createTambahButton() {
        return createStyledButton("✚ Tambah");
    }

    public static JButton createHapusButton() {
        return createStyledButton("🗑 Hapus");
    }

    public static JButton createStartButton() {
        return createStyledButton("▶ Start");
    }

    public static JButton createEditButton() {
        return createStyledButton("🖉 Edit");
    }

    // ── Panels ────────────────────────────────────────────────────────────────

    /** Creates the teal header panel containing the app title. */
    public static JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_TEAL);
        return panel;
    }

    /** Creates the main background panel. */
    public static JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_BACKGROUND);
        panel.setPreferredSize(new java.awt.Dimension(655, 400));
        return panel;
    }

    /** Creates the small panel that shows the selected task's deadline. */
    public static JPanel createDeadlinePanel() {
        return new JPanel();
    }

    // ── Labels ────────────────────────────────────────────────────────────────

    /** Creates the "To-Do-List" title label. */
    public static JLabel createTitleLabel() {
        JLabel label = new JLabel("To-Do-List");
        label.setFont(new Font("Rockwell", Font.BOLD, 48));
        label.setForeground(COLOR_WHITE);
        return label;
    }

    /** Creates the deadline display label. */
    public static JLabel createDeadlineLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return label;
    }

    // ── List & ScrollPane ─────────────────────────────────────────────────────

    /** Creates the task JList with standard styling. */
    public static JList<String> createTaskList() {
        JList<String> list = new JList<>();
        list.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        list.setBorder(BorderFactory.createEmptyBorder());
        list.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return list;
    }

    /** Wraps a JList inside a JScrollPane. */
    public static JScrollPane createScrollPane(JList<String> list) {
        return new JScrollPane(list);
    }

    // ── Pomodoro components ───────────────────────────────────────────────────

    /** Background color for the green timer panel. */
    public static final Color COLOR_TIMER_BG = new Color(185, 212, 170);

    /** Creates the green panel that displays the countdown timer. */
    public static JPanel createTimerPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_TIMER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    /** Creates the large timer display label (e.g. "25:00"). */
    public static JLabel createTimerDisplayLabel() {
        JLabel label = new JLabel("00:00");
        label.setFont(new Font("Rockwell", Font.BOLD, 80));
        label.setForeground(COLOR_WHITE);
        return label;
    }

    /** Creates the "POMODORO" header label. */
    public static JLabel createPomodoroTitleLabel() {
        JLabel label = new JLabel("POMODORO");
        label.setFont(new Font("Rockwell", Font.BOLD, 48));
        label.setForeground(COLOR_WHITE);
        return label;
    }

    /** Creates the label that shows the active task name. */
    public static JLabel createTaskNameLabel() {
        JLabel label = new JLabel("Nama tugas");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    /** Creates an input field styled for the green timer area. */
    public static JTextField createTimerInputField() {
        JTextField field = new JTextField(4);
        field.setBackground(COLOR_TIMER_BG);
        field.setForeground(COLOR_WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    /** Creates the colon separator label between menit and detik fields. */
    public static JLabel createColonLabel() {
        JLabel label = new JLabel(":");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return label;
    }

    public static JButton createStartPauseButton() {
        return createStyledButton("Start");
    }

    public static JButton createResetButton() {
        return createStyledButton(" Reset");
    }

    public static JButton createBackButton() {
        return createStyledButton("Back");
    }

    public static JButton createSelesaiButton() {
        return createStyledButton("Selesai");
    }

    // ── Dialog helpers ────────────────────────────────────────────────────────

    /**
     * Builds the input panel used in the "Tambah Tugas" dialog.
     *
     * @param inputKegiatan pre-created text field for the task name
     * @param inputDeadline pre-created text field for the deadline
     * @return a 2×2 GridLayout panel ready to pass into JOptionPane
     */
    public static JPanel createAddTaskPanel(JTextField inputKegiatan, JTextField inputDeadline) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Nama Kegiatan:"));
        panel.add(inputKegiatan);
        panel.add(new JLabel("Deadline (dd/MM/yyyy):"));
        panel.add(inputDeadline);
        return panel;
    }
}