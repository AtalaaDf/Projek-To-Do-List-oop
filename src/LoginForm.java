import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class LoginForm extends JFrame {

    // Variabel untuk melacak status form saat ini (True = Login, False = Register)
    private boolean isLoginMode = true;

    public LoginForm() {
        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 1. BACKGROUND UTAMA: Menggunakan Gradient Panel
        UIComponents.ModernGradientVerPanel mainBackground = new UIComponents.ModernGradientVerPanel(
            0, 
            new Color(212,187,193), // Biru Atas
            new Color(101,77,100) // Biru Muda Bawah
        );
        mainBackground.setLayout(new GridBagLayout()); 
        setContentPane(mainBackground);

        // 2. CARD TENGAH: Menggunakan Shadow Panel
        UIComponents.ModernShadowPanel loginCard = new UIComponents.ModernShadowPanel(30, 15, new Color(212,187,193));
        loginCard.setPreferredSize(new Dimension(350, 450));
        loginCard.setLayout(null);

        // --- Isi di dalam Login Card ---

        // Judul (Dibuat lebar agar teks "REGISTER" yang lebih panjang tetap muat di tengah)
        UIComponents.ModernLabel lblTitle = new UIComponents.ModernLabel("LOGIN", 45, true, new Color(44, 62, 80));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 40, 350, 40);
        loginCard.add(lblTitle);

        // Username Field (Label lama di-comment)
        // UIComponents.ModernLabel lblUser = new UIComponents.ModernLabel("Username", 14, false, Color.BLACK);
        // lblUser.setBounds(40, 100, 100, 20);
        // loginCard.add(lblUser);

        // --- PENAMBAHAN PLACEHOLDER PADA USERNAME ---
        // Y-coordinate diturunkan dari 125 menjadi 140
        UIComponents.ModernTextField txtUsername = new UIComponents.ModernTextField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Jika textfield kosong, gambar tulisan abu-abu di dalamnya
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());
                    g2.setColor(Color.GRAY);
                    int padding = (getHeight() - getFontMetrics(getFont()).getHeight()) / 2;
                    g2.drawString("Username", getInsets().left, getHeight() - padding - getFontMetrics(getFont()).getDescent());
                    g2.dispose();
                }
            }
        };
        txtUsername.setBounds(40, 140, 270, 40);
        loginCard.add(txtUsername);

        // Password Field (Label lama di-comment)
        // UIComponents.ModernLabel lblPass = new UIComponents.ModernLabel("Password", 14, false, Color.BLACK);
        // lblPass.setBounds(40, 180, 100, 20);
        // loginCard.add(lblPass);

        // --- PENAMBAHAN PLACEHOLDER PADA PASSWORD ---
        // Y-coordinate diubah ke 190 (gap ke username menjadi 10px: 190 - (140+40))
        UIComponents.ModernPasswordField txtPassword = new UIComponents.ModernPasswordField(15) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Jika password kosong, gambar tulisan abu-abu di dalamnya
                if (String.valueOf(getPassword()).isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setFont(getFont());
                    g2.setColor(Color.GRAY);
                    int padding = (getHeight() - getFontMetrics(getFont()).getHeight()) / 2;
                    g2.drawString("Password", getInsets().left, getHeight() - padding - getFontMetrics(getFont()).getDescent());
                    g2.dispose();
                }
            }
        };
        txtPassword.setBounds(40, 190, 270, 40);
        loginCard.add(txtPassword);

        // --- PENAMBAHAN KODE CHECKBOX DIMULAI DI SINI ---
        // Y-coordinate diubah ke 235 (gap dengan password dipertahankan sama)
        JCheckBox chkShowPassword = new JCheckBox("Tampilkan Password");
        chkShowPassword.setBounds(40, 235, 150, 20);
        chkShowPassword.setOpaque(false); // Transparan agar menyatu dengan background card
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPassword.setForeground(Color.BLACK);
        loginCard.add(chkShowPassword);

        // Listener untuk menyembunyikan/menampilkan password
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0); // Menampilkan teks asli
            } else {
                txtPassword.setEchoChar('\u2022'); // Mengembalikan ke karakter bullet (titik hitam)
            }
        });
        // --- PENAMBAHAN KODE CHECKBOX SELESAI ---

        // Tombol Utama (Awalnya MASUK)
        // Y-coordinate diubah ke 265 (menggeser turun dengan gap yang proporsional)
        UIComponents.ModernButton btnUtama = new UIComponents.ModernButton(
            "MASUK", 20, 
            new Color(101, 77, 100), new Color(111, 87, 110), new Color(90, 67, 90)
        );
        btnUtama.setBounds(40, 265, 270, 45);
        loginCard.add(btnUtama);

        // Tombol Switch/Ganti Mode (Kecil di pojok kanan bawah)
        // Posisi ini saya biarkan tetap di 380 agar tetap menempel di sudut bawah card, 
        // sehingga desain card tidak terasa kosong di bagian bawahnya.
        UIComponents.ModernButton btnSwitch = new UIComponents.ModernButton(
            "Buat Akun", 10,
            new Color(127, 140, 141), new Color(149, 165, 166), new Color(110, 120, 120)
        );
        btnSwitch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSwitch.setBounds(210, 380, 100, 30);
        loginCard.add(btnSwitch);

        // --- Action Listeners ---

        // Logika untuk Tombol Switch (Mengubah mode form)
        btnSwitch.addActionListener(e -> {
            if (isLoginMode) {
                // Berubah ke mode Register
                isLoginMode = false;
                lblTitle.setText("REGISTER");
                btnUtama.setText("BUAT AKUN");
                btnSwitch.setText("Login");
                btnUtama.setBackground(new Color(58, 161, 61));
                btnUtama.setColorHover(new Color(50, 210, 119));
                btnUtama.setColorNormal(new Color(58, 161, 61)); // Ubah warna jadi hijau saat register
            } else {
                // Kembali ke mode Login
                isLoginMode = true;
                lblTitle.setText("LOGIN");
                btnUtama.setText("MASUK");
                btnSwitch.setText("Buat Akun");
                btnUtama.setBackground(new Color(101, 77, 100));
                btnUtama.setColorHover(new Color(111, 87, 110));
                btnUtama.setColorNormal(new Color(101, 77, 100)); // Kembalikan warna biru
            }
            
            // Bersihkan kolom teks setiap kali berganti halaman
            txtUsername.setText("");
            txtPassword.setText("");
            // Opsional: Uncheck checkbox saat berganti halaman
            chkShowPassword.setSelected(false);
            txtPassword.setEchoChar('\u2022');
        });

        // Logika untuk Tombol Utama (Bisa sebagai Login ATAU Register tergantung mode)
        btnUtama.addActionListener(e -> {
            String user = txtUsername.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isLoginMode) {
                // --- PROSES LOGIN ---
                if (cekLoginData(user, pass)) {
                    JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + user + ".");
                    new MainForm(user).setVisible(true); // Pindah ke form utama aplikasi
                    this.dispose(); // Tutup form login
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau Password salah! (Silakan 'Buat Akun' terlebih dahulu)", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // --- PROSES REGISTRASI ---
                simpanKeFile(user, pass);
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan masuk menggunakan akun tersebut.");
                
                // Otomatis klik tombol switch agar kembali ke tampilan Login setelah berhasil daftar
                btnSwitch.doClick(); 
            }
        });

        mainBackground.add(loginCard);
    }

    // --- Fungsi Bantuan untuk File System ---

    // Menyimpan data registrasi ke file dengan format "username,password"
    private void simpanKeFile(String user, String pass) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("akun.txt", true))) {
            writer.write(user + "," + pass);
            writer.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Membaca file.txt baris demi baris dan mencocokkan kredensial
    private boolean cekLoginData(String inputUser, String inputPass) {
        File file = new File("akun.txt");
        
        // Jika file belum ada, berarti belum ada yang mendaftar sama sekali
        if (!file.exists()) {
            return false; 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Memisahkan teks berdasarkan koma
                String[] data = line.split(","); 
                if (data.length == 2) {
                    String savedUser = data[0];
                    String savedPass = data[1];
                    
                    if (savedUser.equals(inputUser) && savedPass.equals(inputPass)) {
                        return true; // Data ditemukan dan cocok!
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return false; // Data tidak ditemukan
    }

    public static void main(String[] args) {
        // Menerapkan UI System bawaan OS agar font dan window terlihat rapi
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}