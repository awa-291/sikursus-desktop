package view;

import config.Database;
import report.ReportService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PembayaranFrame extends JFrame {

    private final Color warnaHeaderBar   = new Color(13, 71, 161);
    private final Color warnaHeaderTeks  = Color.WHITE;
    private final Color warnaBgForm      = new Color(255, 253, 231);
    private final Color warnaPanelTabel  = new Color(227, 242, 253);
    private final Color warnaBorder      = new Color(144, 202, 249);
    private final Color warnaTeksDongker = new Color(10, 25, 47);

    private final Color btnSimpanColor = new Color(25, 118, 210);
    private final Color btnCetakColor  = new Color(0, 137, 123);

    private final Font fontJudul  = new Font("Poppins", Font.BOLD, 18);
    private final Font fontLabel  = new Font("Poppins", Font.PLAIN, 13);
    private final Font fontTombol = new Font("Poppins", Font.BOLD, 12);

    private JComboBox<String> cmbPendaftaran, cmbMetode, cmbStatus;
    private JTextField txtTanggalBayar, txtNominal, txtTglAwal, txtTglAkhir;
    private JButton btnSimpan, btnCetakPeriode;
    private JTable tblPembayaran;
    private DefaultTableModel tableModel;

    public PembayaranFrame() {
        initComponents();
        configureTable();
        loadComboPendaftaran();
        loadTable();

        setSize(860, 680);
        setMinimumSize(new Dimension(780, 620));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi Pembayaran & Laporan Periode - SIKURSUS Desktop");

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(warnaBgForm);

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setBackground(warnaHeaderBar);
        panelHeader.setPreferredSize(new Dimension(100, 45));

        JLabel lblHeader = new JLabel("Transaksi Pembayaran Kursus");
        lblHeader.setFont(fontJudul);
        lblHeader.setForeground(warnaHeaderTeks);
        panelHeader.add(lblHeader);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBackground(warnaBgForm);
        panelCenter.setBorder(new EmptyBorder(10, 15, 10, 15));

        // 1. Form Input Pembayaran
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(warnaBgForm);
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Input Pembayaran Baru",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        cmbPendaftaran  = new JComboBox<>();
        txtTanggalBayar = new JTextField(LocalDate.now().toString(), 20);
        txtNominal      = new JTextField("350000", 20);
        cmbMetode       = new JComboBox<>(new String[]{"Transfer BNI", "QRIS", "Tunai"});
        cmbStatus       = new JComboBox<>(new String[]{"LUNAS", "BELUM LUNAS"});

        addGridRow(panelForm, 0, new JLabel("ID Pendaftaran"), cmbPendaftaran);
        addGridRow(panelForm, 1, new JLabel("Tanggal Bayar (YYYY-MM-DD)"), txtTanggalBayar);
        addGridRow(panelForm, 2, new JLabel("Nominal (Rp)"), txtNominal);
        addGridRow(panelForm, 3, new JLabel("Metode Bayar"), cmbMetode);
        addGridRow(panelForm, 4, new JLabel("Status"), cmbStatus);

        btnSimpan = createStyledButton("Simpan Pembayaran", btnSimpanColor, 175);
        GridBagConstraints cBtn = new GridBagConstraints();
        cBtn.gridx = 1; cBtn.gridy = 5;
        cBtn.anchor = GridBagConstraints.WEST;
        cBtn.insets = new Insets(8, 0, 5, 10);
        panelForm.add(btnSimpan, cBtn);

        // 2. Panel Filter & Cetak Laporan Berdasarkan Rentang Tanggal
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelFilter.setBackground(warnaPanelTabel);
        panelFilter.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Cetak Laporan Pembayaran Per Periode (YYYY-MM-DD)",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        txtTglAwal  = new JTextField("2026-11-01", 10);
        txtTglAkhir = new JTextField("2026-11-30", 10);
        txtTglAwal.setFont(fontLabel);
        txtTglAkhir.setFont(fontLabel);

        btnCetakPeriode = createStyledButton("Cetak Periode", btnCetakColor, 140);

        JLabel lblAwal = new JLabel("Dari Tanggal:");
        lblAwal.setFont(fontLabel);
        JLabel lblAkhir = new JLabel("Sampai Tanggal:");
        lblAkhir.setFont(fontLabel);

        panelFilter.add(lblAwal);
        panelFilter.add(txtTglAwal);
        panelFilter.add(lblAkhir);
        panelFilter.add(txtTglAkhir);
        panelFilter.add(btnCetakPeriode);

        // 3. Tabel Pembayaran
        JPanel panelTabel = new JPanel(new BorderLayout());
        panelTabel.setBackground(warnaPanelTabel);
        panelTabel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Riwayat Pembayaran",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        tblPembayaran = new JTable();
        tblPembayaran.setFont(fontLabel);
        JScrollPane scroll = new JScrollPane(tblPembayaran);
        scroll.setPreferredSize(new Dimension(780, 200));
        panelTabel.add(scroll, BorderLayout.CENTER);

        panelCenter.add(panelForm);
        panelCenter.add(Box.createVerticalStrut(8));
        panelCenter.add(panelFilter);
        panelCenter.add(Box.createVerticalStrut(8));
        panelCenter.add(panelTabel);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelCenter, BorderLayout.CENTER);
        setContentPane(panelUtama);

        btnSimpan.addActionListener(e -> simpanPembayaran());
        btnCetakPeriode.addActionListener(e -> cetakPeriode());
    }

    private JButton createStyledButton(String text, Color bg, int width) {
        JButton btn = new JButton(text);
        btn.setFont(fontTombol);
        btn.setBackground(bg);
        btn.setForeground(warnaTeksDongker);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width, 30));
        return btn;
    }

    private void addGridRow(JPanel panel, int row, JLabel label, Component comp) {
        label.setFont(fontLabel);
        label.setForeground(warnaTeksDongker);

        GridBagConstraints cLabel = new GridBagConstraints();
        cLabel.gridx = 0; cLabel.gridy = row;
        cLabel.anchor = GridBagConstraints.WEST;
        cLabel.insets = new Insets(4, 10, 4, 15);
        panel.add(label, cLabel);

        GridBagConstraints cComp = new GridBagConstraints();
        cComp.gridx = 1; cComp.gridy = row;
        cComp.fill = GridBagConstraints.HORIZONTAL;
        cComp.weightx = 1.0;
        cComp.insets = new Insets(4, 0, 4, 10);
        panel.add(comp, cComp);
    }

    private void configureTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Tanggal Bayar", "NIM", "Peserta", "Kursus", "Nominal", "Metode", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPembayaran.setModel(tableModel);
        tblPembayaran.getTableHeader().setBackground(warnaHeaderBar);
        tblPembayaran.getTableHeader().setForeground(warnaTeksDongker);
        tblPembayaran.getTableHeader().setFont(fontTombol);
    }

    private void loadComboPendaftaran() {
        cmbPendaftaran.removeAllItems();
        String sql = "SELECT p.id_pendaftaran, ps.nama, krs.nama_kursus FROM pendaftaran p " +
                     "JOIN peserta ps ON p.id_peserta = ps.id_peserta " +
                     "JOIN kelas kls ON p.id_kelas = kls.id_kelas " +
                     "JOIN kursus krs ON kls.id_kursus = krs.id_kursus ORDER BY p.id_pendaftaran";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cmbPendaftaran.addItem(rs.getInt("id_pendaftaran") + " - " + rs.getString("nama") + " (" + rs.getString("nama_kursus") + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pendaftaran: " + e.getMessage());
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        String sql = "SELECT pb.id_pembayaran, pb.tanggal_bayar, ps.nim, ps.nama AS nama_peserta, " +
                     "krs.nama_kursus, pb.nominal, pb.metode, pb.status " +
                     "FROM pembayaran pb " +
                     "JOIN pendaftaran p ON pb.id_pendaftaran = p.id_pendaftaran " +
                     "JOIN peserta ps ON p.id_peserta = ps.id_peserta " +
                     "JOIN kelas kls ON p.id_kelas = kls.id_kelas " +
                     "JOIN kursus krs ON kls.id_kursus = krs.id_kursus " +
                     "ORDER BY pb.tanggal_bayar ASC";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_pembayaran"),
                    rs.getDate("tanggal_bayar"),
                    rs.getString("nim"),
                    rs.getString("nama_peserta"),
                    rs.getString("nama_kursus"),
                    rs.getBigDecimal("nominal"),
                    rs.getString("metode"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat tabel pembayaran: " + e.getMessage());
        }
    }

    private void simpanPembayaran() {
        if (cmbPendaftaran.getSelectedItem() == null) return;
        int idPendaftaran = Integer.parseInt(cmbPendaftaran.getSelectedItem().toString().split(" - ")[0]);

        String sql = "INSERT INTO pembayaran (id_pendaftaran, tanggal_bayar, nominal, metode, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPendaftaran);
            ps.setDate(2, Date.valueOf(txtTanggalBayar.getText().trim()));
            ps.setDouble(3, Double.parseDouble(txtNominal.getText().trim()));
            ps.setString(4, cmbMetode.getSelectedItem().toString());
            ps.setString(5, cmbStatus.getSelectedItem().toString());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Pembayaran berhasil disimpan.");
            loadTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pembayaran: " + e.getMessage());
        }
    }

    private void cetakPeriode() {
        String strAwal = txtTglAwal.getText().trim();
        String strAkhir = txtTglAkhir.getText().trim();

        if (strAwal.isEmpty() || strAkhir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal awal dan tanggal akhir wajib diisi.");
            return;
        }

        try {
            Date tglAwal = Date.valueOf(strAwal);
            Date tglAkhir = Date.valueOf(strAkhir);

            if (tglAwal.after(tglAkhir)) {
                JOptionPane.showMessageDialog(this, "Tanggal awal tidak boleh setelah tanggal akhir.");
                return;
            }

            ReportService.cetakPembayaran(tglAwal, tglAkhir);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD (contoh: 2026-11-01).");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new PembayaranFrame().setVisible(true));
    }
}