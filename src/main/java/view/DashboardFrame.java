package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;
import util.UserSession;
import util.WindowManager;

public class DashboardFrame extends JFrame {

    // Component Declarations
    private JLabel lblUser;
    private JLabel lblRole;
    private JLabel lblTitle;
    private JLabel lblSubTitle;

    // Menu Components
    private JMenuBar menuBar;
    private JMenu menuMaster;
    private JMenu menuTransaksi;
    private JMenu menuLaporan;
    private JMenu menuKeluar;

    private JMenuItem miPeserta;
    private JMenuItem miInstruktur;
    private JMenuItem miKursus;
    private JMenuItem miKelas;

    private JMenuItem miPendaftaran;
    private JMenuItem miPembayaran;

    private JMenuItem miLaporanPeserta;
    private JMenuItem miLaporanPendaftaran;

    private JMenuItem miLogout;
    private JMenuItem miTutup;

    // Popup Menu
    private JPopupMenu popupDashboard;
    private JMenuItem miTentang;
    private JMenuItem miRefresh;

    // Custom Color Palette
    private static final Color COLOR_NAVY = new Color(26, 36, 66);
    private static final Color COLOR_SOFT_BLUE = new Color(224, 235, 248);
    private static final Color COLOR_LIGHT_YELLOW = new Color(254, 243, 199);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);

    public DashboardFrame() {
        initComponents();
        setLocationRelativeTo(null);
        applySession();
    }

    private void initComponents() {
        setTitle("Dashboard - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));

        // Fonts
        Font fontTitle = new Font("Poppins", Font.BOLD, 22);
        Font fontSubtitle = new Font("Poppins", Font.PLAIN, 12);
        Font fontLabelBold = new Font("Poppins", Font.BOLD, 13);
        Font fontLabelPlain = new Font("Poppins", Font.PLAIN, 13);
        Font fontMenu = new Font("Poppins", Font.PLAIN, 13);

        // --- MENU BAR ---
        menuBar = new JMenuBar();
        menuBar.setBackground(COLOR_NAVY);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        UIManager.put("Menu.font", fontMenu);
        UIManager.put("MenuItem.font", fontMenu);
        UIManager.put("Menu.foreground", COLOR_WHITE);

        // Menu Master
        menuMaster = new JMenu("Master");
        miPeserta = new JMenuItem("Peserta");
        miInstruktur = new JMenuItem("Instruktur");
        miKursus = new JMenuItem("Kursus");
        miKelas = new JMenuItem("Kelas");

        // Load Icon Portable
        miPeserta.setIcon(loadIcon("/icons/peserta.png"));
        miInstruktur.setIcon(loadIcon("/icons/instruktur.png"));
        miKursus.setIcon(loadIcon("/icons/kursus.png"));
        miKelas.setIcon(loadIcon("/icons/kelas.png"));

        miPeserta.addActionListener(this::miPesertaActionPerformed);
        miInstruktur.addActionListener(this::miInstrukturActionPerformed);
        miKursus.addActionListener(this::miKursusActionPerformed);
        miKelas.addActionListener(this::miKelasActionPerformed);

        menuMaster.add(miPeserta);
        menuMaster.add(miInstruktur);
        menuMaster.add(miKursus);
        menuMaster.add(miKelas);

        // Menu Transaksi
        menuTransaksi = new JMenu("Transaksi");
        miPendaftaran = new JMenuItem("Pendaftaran");
        miPembayaran = new JMenuItem("Pembayaran");

        miPendaftaran.addActionListener(this::miPendaftaranActionPerformed);
        miPembayaran.addActionListener(this::miPembayaranActionPerformed);

        menuTransaksi.add(miPendaftaran);
        menuTransaksi.add(miPembayaran);

        // Menu Laporan
        menuLaporan = new JMenu("Laporan");
        miLaporanPeserta = new JMenuItem("Laporan Peserta");
        miLaporanPendaftaran = new JMenuItem("Laporan Pendaftaran");

        miLaporanPeserta.addActionListener(this::miLaporanPesertaActionPerformed);
        miLaporanPendaftaran.addActionListener(this::miLaporanPendaftaranActionPerformed);

        menuLaporan.add(miLaporanPeserta);
        menuLaporan.add(miLaporanPendaftaran);

        // Menu Keluar
        menuKeluar = new JMenu("Keluar");
        miLogout = new JMenuItem("Logout");
        miTutup = new JMenuItem("Tutup Aplikasi");

        miLogout.addActionListener(this::miLogoutActionPerformed);
        miTutup.addActionListener(this::miTutupActionPerformed);

        menuKeluar.add(miLogout);
        menuKeluar.addSeparator();
        menuKeluar.add(miTutup);

        menuBar.add(menuMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);
        menuBar.add(menuKeluar);
        setJMenuBar(menuBar);

        // --- MAIN LAYOUT ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_SOFT_BLUE);

        // --- HEADER BAR (USER & ROLE INFO) ---
        JPanel headerInfoPanel = new JPanel(new BorderLayout());
        headerInfoPanel.setBackground(COLOR_WHITE);
        headerInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_LIGHT_YELLOW),
                new EmptyBorder(12, 25, 12, 25)
        ));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        lblTitle = new JLabel("SIKURSUS Desktop");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(COLOR_NAVY);

        lblSubTitle = new JLabel("Sistem Informasi Manajemen Kursus & Pelatihan");
        lblSubTitle.setFont(fontSubtitle);
        lblSubTitle.setForeground(COLOR_TEXT_MUTED);

        titleBox.add(lblTitle);
        titleBox.add(lblSubTitle);

        // User Badge
        JPanel userBadgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userBadgePanel.setOpaque(false);

        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        badge.setBackground(COLOR_LIGHT_YELLOW);
        badge.setBorder(BorderFactory.createLineBorder(COLOR_NAVY, 1, true));

        JLabel lblUserPrefix = new JLabel("User:");
        lblUserPrefix.setFont(fontLabelBold);
        lblUserPrefix.setForeground(COLOR_NAVY);

        lblUser = new JLabel("-");
        lblUser.setFont(fontLabelPlain);
        lblUser.setForeground(COLOR_NAVY);

        JLabel lblRolePrefix = new JLabel("| Role:");
        lblRolePrefix.setFont(fontLabelBold);
        lblRolePrefix.setForeground(COLOR_NAVY);

        lblRole = new JLabel("-");
        lblRole.setFont(fontLabelPlain);
        lblRole.setForeground(COLOR_NAVY);

        badge.add(lblUserPrefix);
        badge.add(lblUser);
        badge.add(lblRolePrefix);
        badge.add(lblRole);

        userBadgePanel.add(badge);

        headerInfoPanel.add(titleBox, BorderLayout.WEST);
        headerInfoPanel.add(userBadgePanel, BorderLayout.EAST);

        // --- CENTER DASHBOARD AREA ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(COLOR_SOFT_BLUE);
        centerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel summaryCard = new JPanel(new BorderLayout(15, 15));
        summaryCard.setBackground(COLOR_WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_LIGHT_YELLOW, 2, true),
                new EmptyBorder(40, 50, 40, 50)
        ));

        JLabel lblWelcome = new JLabel("Selamat Datang di SIKURSUS Desktop", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Poppins", Font.BOLD, 20));
        lblWelcome.setForeground(COLOR_NAVY);

        JLabel lblDesc = new JLabel("Silakan pilih menu di bagian atas untuk mengelola data master, transaksi, atau laporan.", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Poppins", Font.PLAIN, 13));
        lblDesc.setForeground(COLOR_TEXT_MUTED);

        summaryCard.add(lblWelcome, BorderLayout.NORTH);
        summaryCard.add(lblDesc, BorderLayout.CENTER);

        centerPanel.add(summaryCard);

        mainPanel.add(headerInfoPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // --- POPUP MENU ---
        popupDashboard = new JPopupMenu();
        miTentang = new JMenuItem("Tentang Aplikasi");
        miRefresh = new JMenuItem("Refresh");

        miTentang.addActionListener(this::miTentangActionPerformed);
        miRefresh.addActionListener(e -> repaint());

        popupDashboard.add(miTentang);
        popupDashboard.add(miRefresh);

        centerPanel.setComponentPopupMenu(popupDashboard);
    }

    // Load Icon Portable
    private ImageIcon loadIcon(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Icon tidak ditemukan: " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    private void applySession() {
        User user = UserSession.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Session tidak ditemukan. Silakan login ulang.");
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            return;
        }

        lblUser.setText(user.getUsername());
        lblRole.setText(user.getRole());

        boolean admin = user.isAdmin();
        miInstruktur.setEnabled(admin);
        miKursus.setEnabled(admin);
        miKelas.setEnabled(admin);
        miPeserta.setEnabled(true);
        menuTransaksi.setEnabled(true);
        menuLaporan.setEnabled(true);
    }

    // --- EVENT HANDLERS ---
    private void miPesertaActionPerformed(ActionEvent evt) {
        WindowManager.open(FormPeserta.class, FormPeserta::new);
    }

    private void miInstrukturActionPerformed(ActionEvent evt) {
        WindowManager.open(InstrukturFrame.class, InstrukturFrame::new);
    }

    private void miKursusActionPerformed(ActionEvent evt) {
        WindowManager.open(KursusFrame.class, KursusFrame::new);
    }

    private void miKelasActionPerformed(ActionEvent evt) {
        WindowManager.open(KelasFrame.class, KelasFrame::new);
    }

    private void miPendaftaranActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Modul Pendaftaran sedang dikembangkan pada tahap project berikutnya.");
    }

    private void miPembayaranActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Modul Pembayaran sedang dikembangkan pada tahap project berikutnya.");
    }

    private void miLaporanPesertaActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Modul laporan diaktifkan pada Pertemuan 12 dengan Jaspersoft Studio.");
    }

    private void miLaporanPendaftaranActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Modul laporan diaktifkan pada Pertemuan 12 dengan Jaspersoft Studio.");
    }

    private void miLogoutActionPerformed(ActionEvent evt) {
        int pilihan = JOptionPane.showConfirmDialog(this, "Logout dari aplikasi?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            WindowManager.closeAll();
            UserSession.clear();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private void miTutupActionPerformed(ActionEvent evt) {
        int pilihan = JOptionPane.showConfirmDialog(this, "Tutup aplikasi SIKURSUS?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void miTentangActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "SIKURSUS Desktop\nProject PBO Program Studi Informatika");
    }
}