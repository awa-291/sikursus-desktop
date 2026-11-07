package view;

import dao.UserDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;
import util.UserSession;

public class LoginFrame extends JFrame {

    private final UserDAO userDAO = new UserDAO();

    // UI Components
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JButton btnLogin;
    private JButton btnKeluar;
    private JLabel lblTitle;
    private JLabel lblSubTitle;
    private JLabel lblUsername;
    private JLabel lblPassword;

    // Custom Color Palette
    private static final Color COLOR_NAVY = new Color(26, 36, 66);        // #1A2442 (Navy Utama)
    private static final Color COLOR_SOFT_BLUE = new Color(224, 235, 248); // #E0EBF8 (Biru Soft Background)
    private static final Color COLOR_LIGHT_YELLOW = new Color(254, 243, 199); // #FEF3C7 (Kuning Muda)
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_GRAY_BORDER = new Color(203, 213, 225);

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null); // Menampilkan form di tengah layar
    }

    private void initComponents() {
        setTitle("Login - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 520);
        setResizable(false);

        // Fonts (Poppins)
        Font fontTitle = new Font("Poppins", Font.BOLD, 22);
        Font fontSubtitle = new Font("Poppins", Font.PLAIN, 12);
        Font fontLabel = new Font("Poppins", Font.BOLD, 13);
        Font fontInput = new Font("Poppins", Font.PLAIN, 13);
        Font fontButton = new Font("Poppins", Font.BOLD, 13);

        // Main Background Panel (Biru Soft)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(COLOR_SOFT_BLUE);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Form Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(COLOR_WHITE);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_LIGHT_YELLOW, 3, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_WHITE);
        headerPanel.setLayout(new GridLayout(2, 1, 0, 5));

        lblTitle = new JLabel("SIKURSUS Desktop", SwingConstants.CENTER);
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(COLOR_NAVY);

        lblSubTitle = new JLabel("Sistem Informasi Manajemen Kursus & Pelatihan", SwingConstants.CENTER);
        lblSubTitle.setFont(fontSubtitle);
        lblSubTitle.setForeground(new Color(100, 116, 139));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubTitle);

        // Form Input Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(COLOR_WHITE);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        // Username Label & Input
        lblUsername = new JLabel("Username");
        lblUsername.setFont(fontLabel);
        lblUsername.setForeground(COLOR_NAVY);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(fontInput);
        txtUsername.setBackground(COLOR_LIGHT_YELLOW);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_GRAY_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);

        // Password Label & Input
        lblPassword = new JLabel("Password");
        lblPassword.setFont(fontLabel);
        lblPassword.setForeground(COLOR_NAVY);
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblPassword, gbc);

        pwdPassword = new JPasswordField();
        pwdPassword.setFont(fontInput);
        pwdPassword.setBackground(COLOR_LIGHT_YELLOW);
        pwdPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_GRAY_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(pwdPassword, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(COLOR_WHITE);
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnLogin = new JButton("Login");
        btnLogin.setFont(fontButton);
        btnLogin.setBackground(COLOR_NAVY);
        btnLogin.setForeground(COLOR_WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        btnKeluar = new JButton("Keluar");
        btnKeluar.setFont(fontButton);
        btnKeluar.setBackground(COLOR_LIGHT_YELLOW);
        btnKeluar.setForeground(COLOR_NAVY);
        btnKeluar.setFocusPainted(false);
        btnKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKeluar.setBorder(BorderFactory.createLineBorder(COLOR_NAVY, 1, true));
        btnKeluar.addActionListener(this::btnKeluarActionPerformed);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnKeluar);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(cardPanel);

        getRootPane().setDefaultButton(btnLogin);
    }

    // Event Handler Tombol Login
    private void btnLoginActionPerformed(ActionEvent evt) {
        String username = txtUsername.getText().trim();
        char[] password = pwdPassword.getPassword();

        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Username dan password wajib diisi.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);

        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Login gagal. Periksa username, password, atau status user.",
                        "Login Gagal",
                        JOptionPane.WARNING_MESSAGE);
                pwdPassword.setText("");
                pwdPassword.requestFocus();
                return;
            }

            UserSession.setCurrentUser(user);
            dispose();
            SwingUtilities.invokeLater(
                    () -> new DashboardFrame().setVisible(true));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat mengakses database: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
            btnLogin.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // Event Handler Tombol Keluar
    private void btnKeluarActionPerformed(ActionEvent evt) {
        System.exit(0);
    }
}