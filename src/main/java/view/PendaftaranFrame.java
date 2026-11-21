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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PendaftaranFrame extends JFrame {

    // ===== SKEMA WARNA SERASI DENGAN MASTER PESERTA =====
    private final Color warnaHeaderBar   = new Color(13, 71, 161);
    private final Color warnaHeaderTeks  = Color.WHITE;
    private final Color warnaBgForm      = new Color(255, 253, 231);
    private final Color warnaPanelTabel  = new Color(227, 242, 253);
    private final Color warnaBorder      = new Color(144, 202, 249);
    private final Color warnaTeksDongker = new Color(10, 25, 47);

    private final Color btnSimpanColor = new Color(25, 118, 210);
    private final Color btnHapusColor  = new Color(211, 47, 47);
    private final Color btnResetColor  = new Color(120, 144, 156);
    private final Color btnCetakColor  = new Color(0, 137, 123);

    private final Font fontJudul  = new Font("Poppins", Font.BOLD, 18);
    private final Font fontLabel  = new Font("Poppins", Font.PLAIN, 13);
    private final Font fontTombol = new Font("Poppins", Font.BOLD, 12);

    // Komponen Form
    private JComboBox<String> cmbPeserta, cmbKelas, cmbStatus;
    private JTextField txtTanggalDaftar;
    private JButton btnSimpan, btnHapus, btnReset, btnCetakTerpilih;
    private JTable tblPendaftaran;
    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public PendaftaranFrame() {
        initComponents();
        configureTable();
        loadComboPeserta();
        loadComboKelas();
        loadTable();
        clearForm();

        setSize(850, 660);
        setMinimumSize(new Dimension(780, 600));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transaksi Pendaftaran Kursus - SIKURSUS Desktop");

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(warnaBgForm);

        // Header
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setBackground(warnaHeaderBar);
        panelHeader.setPreferredSize(new Dimension(100, 45));

        JLabel lblHeader = new JLabel("Transaksi Pendaftaran Kursus");
        lblHeader.setFont(fontJudul);
        lblHeader.setForeground(warnaHeaderTeks);
        panelHeader.add(lblHeader);

        // Center
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBackground(warnaBgForm);
        panelCenter.setBorder(new EmptyBorder(10, 15, 10, 15));

        // 1. Form Pendaftaran
        JPanel panelFormGroup = new JPanel(new GridBagLayout());
        panelFormGroup.setBackground(warnaBgForm);
        panelFormGroup.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Form Pendaftaran Peserta ke Kelas",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        cmbPeserta = new JComboBox<>();
        cmbKelas = new JComboBox<>();
        txtTanggalDaftar = new JTextField(LocalDate.now().toString(), 22);
        cmbStatus = new JComboBox<>(new String[]{"AKTIF", "SELESAI", "BATAL"});

        cmbPeserta.setFont(fontLabel);
        cmbKelas.setFont(fontLabel);
        txtTanggalDaftar.setFont(fontLabel);
        cmbStatus.setFont(fontLabel);

        addGridRow(panelFormGroup, 0, new JLabel("Pilih Peserta"), cmbPeserta);
        addGridRow(panelFormGroup, 1, new JLabel("Pilih Kelas"), cmbKelas);
        addGridRow(panelFormGroup, 2, new JLabel("Tanggal Daftar (YYYY-MM-DD)"), txtTanggalDaftar);
        addGridRow(panelFormGroup, 3, new JLabel("Status Pendaftaran"), cmbStatus);

        // Tombol Aksi
        JPanel panelBtnAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panelBtnAction.setBackground(warnaBgForm);

        btnSimpan        = createStyledButton("Simpan", btnSimpanColor, 110);
        btnHapus         = createStyledButton("Hapus", btnHapusColor, 110);
        btnReset         = createStyledButton("Reset", btnResetColor, 110);
        btnCetakTerpilih = createStyledButton("Cetak", btnCetakColor, 145);

        panelBtnAction.add(btnSimpan);
        panelBtnAction.add(btnHapus);
        panelBtnAction.add(btnReset);
        panelBtnAction.add(btnCetakTerpilih);

        GridBagConstraints cBtn = new GridBagConstraints();
        cBtn.gridx = 0; cBtn.gridy = 4;
        cBtn.gridwidth = 2;
        cBtn.insets = new Insets(10, 0, 5, 0);
        panelFormGroup.add(panelBtnAction, cBtn);

        // 2. Tabel Data Pendaftaran
        JPanel panelTabelGroup = new JPanel(new BorderLayout(5, 5));
        panelTabelGroup.setBackground(warnaPanelTabel);
        panelTabelGroup.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Daftar Pendaftaran",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        tblPendaftaran = new JTable();
        tblPendaftaran.setFont(fontLabel);
        tblPendaftaran.setForeground(warnaTeksDongker);

        JScrollPane scrollTable = new JScrollPane(tblPendaftaran);
        scrollTable.setPreferredSize(new Dimension(780, 240));
        panelTabelGroup.add(scrollTable, BorderLayout.CENTER);

        panelCenter.add(panelFormGroup);
        panelCenter.add(Box.createVerticalStrut(10));
        panelCenter.add(panelTabelGroup);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelCenter, BorderLayout.CENTER);
        setContentPane(panelUtama);

        // Events
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> clearForm());
        btnCetakTerpilih.addActionListener(e -> cetakTerpilih());

        tblPendaftaran.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPendaftaran.getSelectedRow();
                if (row != -1) {
                    selectedId = Integer.parseInt(tblPendaftaran.getValueAt(row, 0).toString());
                    btnHapus.setEnabled(true);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bg, int width) {
        JButton btn = new JButton(text);
        btn.setFont(fontTombol);
        btn.setBackground(bg);
        btn.setForeground(warnaTeksDongker);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(width, 32));
        return btn;
    }

    private void addGridRow(JPanel panel, int row, JLabel label, Component comp) {
        label.setFont(fontLabel);
        label.setForeground(warnaTeksDongker);

        GridBagConstraints cLabel = new GridBagConstraints();
        cLabel.gridx = 0; cLabel.gridy = row;
        cLabel.anchor = GridBagConstraints.WEST;
        cLabel.insets = new Insets(5, 10, 5, 15);
        panel.add(label, cLabel);

        GridBagConstraints cComp = new GridBagConstraints();
        cComp.gridx = 1; cComp.gridy = row;
        cComp.fill = GridBagConstraints.HORIZONTAL;
        cComp.weightx = 1.0;
        cComp.insets = new Insets(5, 0, 5, 10);
        panel.add(comp, cComp);
    }

    private void configureTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "NIM", "Nama Peserta", "Kelas", "Kursus", "Tanggal Daftar", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPendaftaran.setModel(tableModel);
        tblPendaftaran.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPendaftaran.getTableHeader().setBackground(warnaHeaderBar);
        tblPendaftaran.getTableHeader().setForeground(warnaTeksDongker);
        tblPendaftaran.getTableHeader().setFont(fontTombol);

        tblPendaftaran.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblPendaftaran.getColumnModel().getColumn(1).setPreferredWidth(85);
        tblPendaftaran.getColumnModel().getColumn(2).setPreferredWidth(150);
    }

    private void loadComboPeserta() {
        cmbPeserta.removeAllItems();
        String sql = "SELECT id_peserta, nim, nama FROM peserta ORDER BY id_peserta ASC";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cmbPeserta.addItem(rs.getInt("id_peserta") + " - " + rs.getString("nim") + " (" + rs.getString("nama") + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat peserta: " + e.getMessage());
        }
    }

    private void loadComboKelas() {
        cmbKelas.removeAllItems();
        String sql = "SELECT k.id_kelas, k.nama_kelas, kr.nama_kursus FROM kelas k JOIN kursus kr ON k.id_kursus = kr.id_kursus ORDER BY k.id_kelas ASC";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cmbKelas.addItem(rs.getInt("id_kelas") + " - " + rs.getString("nama_kelas") + " (" + rs.getString("nama_kursus") + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat kelas: " + e.getMessage());
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        String sql = "SELECT p.id_pendaftaran, ps.nim, ps.nama AS nama_peserta, " +
                     "kls.nama_kelas, krs.nama_kursus, p.tanggal_daftar, p.status " +
                     "FROM pendaftaran p " +
                     "INNER JOIN peserta ps ON p.id_peserta = ps.id_peserta " +
                     "INNER JOIN kelas kls ON p.id_kelas = kls.id_kelas " +
                     "INNER JOIN kursus krs ON kls.id_kursus = krs.id_kursus " +
                     "ORDER BY p.id_pendaftaran ASC";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_pendaftaran"),
                    rs.getString("nim"),
                    rs.getString("nama_peserta"),
                    rs.getString("nama_kelas"),
                    rs.getString("nama_kursus"),
                    rs.getDate("tanggal_daftar"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat tabel pendaftaran: " + e.getMessage());
        }
    }

    private void simpanData() {
        if (cmbPeserta.getSelectedItem() == null || cmbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Data peserta atau kelas masih kosong.");
            return;
        }
        int idPeserta = Integer.parseInt(cmbPeserta.getSelectedItem().toString().split(" - ")[0]);
        int idKelas   = Integer.parseInt(cmbKelas.getSelectedItem().toString().split(" - ")[0]);
        String tgl    = txtTanggalDaftar.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();

        String sql = "INSERT INTO pendaftaran (id_peserta, id_kelas, tanggal_daftar, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeserta);
            ps.setInt(2, idKelas);
            ps.setDate(3, java.sql.Date.valueOf(tgl));
            ps.setString(4, status);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Pendaftaran berhasil disimpan.");
            loadTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pendaftaran: " + e.getMessage());
        }
    }

    private void hapusData() {
        if (selectedId <= 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus pendaftaran ID " + selectedId + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM pendaftaran WHERE id_pendaftaran = ?")) {
            ps.setInt(1, selectedId);
            ps.executeUpdate();
            loadTable();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus (mungkin sudah ada data pembayaran): " + e.getMessage());
        }
    }

    private void clearForm() {
        selectedId = -1;
        txtTanggalDaftar.setText(LocalDate.now().toString());
        if (cmbStatus.getItemCount() > 0) cmbStatus.setSelectedIndex(0);
        tblPendaftaran.clearSelection();
        btnHapus.setEnabled(false);
    }

    // ===== TAHAP 6 & 7: AMBIL ID JTABLE & KIRIM PARAMETER KE REPORT =====
    private void cetakTerpilih() {
        int row = tblPendaftaran.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih satu data pendaftaran pada tabel terlebih dahulu.");
            return;
        }
        int idPendaftaran = Integer.parseInt(tblPendaftaran.getValueAt(row, 0).toString());
        System.out.println("ID terpilih: " + idPendaftaran);
        ReportService.cetakPendaftaran(idPendaftaran);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new PendaftaranFrame().setVisible(true));
    }
}