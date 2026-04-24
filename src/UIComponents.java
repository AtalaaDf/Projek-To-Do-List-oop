import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

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

    // Nambah ui kategori
    public static final String[] KATEGORI = {
        "Kuliah", "Pribadi", "Kerja", "Belanja", "Kesehatan", "Umum"
    };
    public static JComboBox<String> createCategoryComboBox() {
        return new JComboBox<>(KATEGORI);
    }
    public static JLabel createCategoryLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        label.setForeground(COLOR_TEAL);
        return label;
    }
    public static class ModernShadowPanel extends JPanel {
        private int radius;
        private int shadowSize;
        private int shadowDrop;

        public ModernShadowPanel(int radius, int shadowSize, Color bgColor) {
            this.radius = radius;
            this.shadowSize = shadowSize;
            this.shadowDrop = 3; // Efek bayangan agak turun ke bawah
            
            setOpaque(false);
            setBackground(bgColor);
            
            // Memberikan padding agar bayangan tidak terpotong oleh batas panel
            setBorder(BorderFactory.createEmptyBorder(
                shadowSize, shadowSize, shadowSize + shadowDrop, shadowSize
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // 1. Menggambar Efek Shadow
            g2.setColor(new Color(0, 0, 0, 5)); // Hitam sangat transparan
            for (int i = 0; i < shadowSize; i++) {
                g2.fillRoundRect(
                    i, i + shadowDrop, 
                    width - (i * 2), height - (i * 2) - shadowDrop, 
                    radius + (shadowSize - i), radius + (shadowSize - i)
                );
            }

            // 2. Menggambar Background Utama
            g2.setColor(getBackground());
            g2.fillRoundRect(
                shadowSize, shadowSize, 
                width - (shadowSize * 2), height - (shadowSize * 2) - shadowDrop, 
                radius, radius
            );
        }
    }

    // 1. MODERN PANEL (Dengan Rounded Corners)
    public static class ModernPanel extends JPanel {
        private int radius;

        public ModernPanel(int radius, Color bgColor) {
            this.radius = radius;
            setOpaque(false); // Transparan agar background asli tidak menutupi lengkungan
            setBackground(bgColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    // 6. MODERN GRADIENT PANEL (Panel dengan dua perpaduan warna & sudut melengkung)
    public static class ModernGradientVerPanel extends JPanel {
        private int radius;
        private Color colorStart;
        private Color colorEnd;

        /**
         * @param radius Tingkat kelengkungan sudut
         * @param colorStart Warna pertama (dimulai dari atas/kiri)
         * @param colorEnd Warna kedua (berakhir di bawah/kanan)
         */
        public ModernGradientVerPanel(int radius, Color colorStart, Color colorEnd) {
            this.radius = radius;
            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            setOpaque(false); // Transparan agar background asli tidak menutupi lengkungan
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            // Anti-aliasing agar lengkungan halus
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Membuat efek gradient (Diagonal dari kiri atas ke kanan bawah)
            // Jika ingin horizontal: ganti getWidth(), getHeight() menjadi getWidth(), 0
            // Jika ingin vertikal: ganti getWidth(), getHeight() menjadi 0, getHeight()
            GradientPaint gradient = new GradientPaint(
                0, 0, colorStart, 
                0, getHeight(), colorEnd //ubah 0 menjadi getWidth() untuk gradient 
            );
            
            g2.setPaint(gradient);
            
            // Menggambar panel dengan warna gradient
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
    public static class ModernGradientHorPanel extends JPanel {
        private int radius;
        private Color colorStart;
        private Color colorEnd;

        /**
         * @param radius Tingkat kelengkungan sudut
         * @param colorStart Warna pertama (dimulai dari atas/kiri)
         * @param colorEnd Warna kedua (berakhir di bawah/kanan)
         */
        public ModernGradientHorPanel(int radius, Color colorStart, Color colorEnd) {
            this.radius = radius;
            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            setOpaque(false); // Transparan agar background asli tidak menutupi lengkungan
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            // Anti-aliasing agar lengkungan halus
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Membuat efek gradient (Diagonal dari kiri atas ke kanan bawah)
            // Jika ingin horizontal: ganti getWidth(), getHeight() menjadi getWidth(), 0
            // Jika ingin vertikal: ganti getWidth(), getHeight() menjadi 0, getHeight()
            GradientPaint gradient = new GradientPaint(
                0, 0, colorStart, 
                getWidth(), getHeight(), colorEnd //ubah 0 menjadi getWidth() untuk gradient 
            );
            
            g2.setPaint(gradient);
            
            // Menggambar panel dengan warna gradient
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }


    // 2. MODERN BUTTON (Dengan Rounded Corners & Hover Effect)
    public static class ModernButton extends JButton {
        private int radius;
        private Color colorNormal;
        private Color colorHover;
        private Color colorClick;

        public void setColorHover(Color hover){
            this.colorHover = hover;
        }
        public void setColorNormal(Color normal){
            this.colorNormal = normal;
        }
        public ModernButton(String text, int radius, Color normal, Color hover, Color click) {
            super(text);
            this.radius = radius;
            this.colorNormal = normal;
            this.colorHover = hover;
            this.colorClick = click;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBackground(colorNormal);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Menambahkan efek hover dan klik
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(colorHover);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(colorNormal);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setBackground(colorClick);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setBackground(colorHover);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
    
    // 3. MODERN TEXTFIELD (Dengan Padding & Rounded Outline)
    public static class ModernTextField extends JTextField {
        private int radius;
        private Color borderColor;

        public ModernTextField(int radius) {
            this.radius = radius;
            this.borderColor = new Color(200, 200, 200); // Warna border abu-abu default
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            // Memberikan padding/ruang kosong di dalam textfield agar teks tidak menempel ke garis
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Gambar background putih
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            // Gambar garis border
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            super.paintComponent(g);
        }
    }

    // 4. MODERN PASSWORDFIELD (Khusus untuk halaman Login)
    public static class ModernPasswordField extends JPasswordField {
        private int radius;
        private Color borderColor;

        public ModernPasswordField(int radius) {
            this.radius = radius;
            this.borderColor = new Color(200, 200, 200);
            setOpaque(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            super.paintComponent(g);
        }
    }

    // 5. MODERN LABEL (Wrapper untuk mempermudah setting font)
    public static class ModernLabel extends JLabel {
        public ModernLabel(String text, int fontSize, boolean isBold, Color color) {
            super(text);
            setForeground(color);
            int style = isBold ? Font.BOLD : Font.PLAIN;
            setFont(new Font("Segoe UI", style, fontSize));
        }
    }
}