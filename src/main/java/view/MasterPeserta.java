package view;

import dao.PesertaDAO;
import model.Peserta;

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
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class MasterPeserta extends JFrame {

    // ===== SKEMA WARNA BIRU & KUNING =====
    private final Color warnaHeaderBar   = new Color(13, 71, 161);   // Biru Gelap Header
    private final Color warnaHeaderTeks  = Color.WHITE;
    private final Color warnaBgForm      = new Color(255, 253, 231); // Kuning Muda Lembut
    private final Color warnaPanelTabel  = new Color(227, 242, 253); // Biru Muda Soft
    private final Color warnaBorder      = new Color(144, 202, 249); // Biru Soft Border

    // Warna Teks Dongker / Navy
    private final Color warnaTeksDongker = new Color(10, 25, 47);    // Navy / Dongker

    // Warna Tombol Aksi
    private final Color btnSimpanColor = new Color(25, 118, 210);  // Biru Utama
    private final Color btnUbahColor   = new Color(30, 136, 229);  // Biru Cerah
    private final Color btnHapusColor  = new Color(211, 47, 47);   // Merah Hapus
    private final Color btnResetColor  = new Color(120, 144, 156); // Abu-Biru Reset
    private final Color btnCetakColor  = new Color(0, 137, 123);   // Teal Cetak
    private final Color btnCariColor   = new Color(21, 101, 192);  // Biru Gelap Cari

    // FONT POPPINS WARNA DONGKER
    private final Font fontJudul  = new Font("Poppins", Font.BOLD, 18);
    private final Font fontLabel  = new Font("Poppins", Font.PLAIN, 13);
    private final Font fontRadio  = new Font("Poppins", Font.PLAIN, 13);
    private final Font fontTombol = new Font("Poppins", Font.BOLD, 12);

    // Komponen Form Input
    private JTextField txtNim, txtNama, txtProdi, txtNoHp, txtEmail, txtCari;
    private JRadioButton rbLaki, rbPerempuan;
    private ButtonGroup buttonGroupJenisKelamin;
    private JButton btnSimpan, btnUbah, btnHapus, btnReset, btnCetak, btnCari;
    private JTable tblPeserta;
    private DefaultTableModel tableModel;

    // Object DAO & State ID
    private final PesertaDAO dao = new PesertaDAO();
    private int selectedId = -1;

    public MasterPeserta() {
        initComponents();
        configureTable();
        loadTable();
        clearForm();

        setSize(800, 680);
        setMinimumSize(new Dimension(750, 620));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Master Data Peserta - SIKURSUS Desktop");

        // Panel Container Utama
        JPanel panelUtama = new JPanel(new BorderLayout(0, 0));
        panelUtama.setBackground(warnaBgForm);

        // Header Title Bar
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelHeader.setBackground(warnaHeaderBar);
        panelHeader.setPreferredSize(new Dimension(100, 45));
        
        JLabel lblHeader = new JLabel("Master Data Peserta");
        lblHeader.setFont(fontJudul);
        lblHeader.setForeground(warnaHeaderTeks);
        panelHeader.add(lblHeader);

        // Center Area (Form & Table)
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBackground(warnaBgForm);
        panelCenter.setBorder(new EmptyBorder(10, 15, 10, 15));

        // 1. Group Box Form Peserta
        JPanel panelFormGroup = new JPanel(new GridBagLayout());
        panelFormGroup.setBackground(warnaBgForm);
        panelFormGroup.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Form Peserta",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        JLabel lblNim = new JLabel("NIM");
        JLabel lblNama = new JLabel("Nama Peserta");
        JLabel lblJenis = new JLabel("Jenis Kelamin");
        JLabel lblProdi = new JLabel("Program Studi");
        JLabel lblNoHp = new JLabel("Nomor HP");
        JLabel lblEmail = new JLabel("Email");

        txtNim = new JTextField(22);
        txtNama = new JTextField(22);
        txtProdi = new JTextField(22);
        txtNoHp = new JTextField(22);
        txtEmail = new JTextField(22);

        JTextField[] allFields = {txtNim, txtNama, txtProdi, txtNoHp, txtEmail};
        for (JTextField tf : allFields) {
            tf.setFont(fontLabel);
            tf.setForeground(warnaTeksDongker);
        }

        // ===== RADIO BUTTON JENIS KELAMIN =====
        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        rbLaki.setFont(fontRadio);
        rbPerempuan.setFont(fontRadio);
        rbLaki.setForeground(warnaTeksDongker);
        rbPerempuan.setForeground(warnaTeksDongker);
        rbLaki.setBackground(warnaBgForm);
        rbPerempuan.setBackground(warnaBgForm);
        rbLaki.setFocusPainted(false);
        rbPerempuan.setFocusPainted(false);

        buttonGroupJenisKelamin = new ButtonGroup();
        buttonGroupJenisKelamin.add(rbLaki);
        buttonGroupJenisKelamin.add(rbPerempuan);

        JPanel panelJenis = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelJenis.setBackground(warnaBgForm);
        panelJenis.add(rbLaki);
        panelJenis.add(rbPerempuan);

        // Susun komponen ke Form
        addGridRow(panelFormGroup, 0, lblNim, txtNim);
        addGridRow(panelFormGroup, 1, lblNama, txtNama);
        addGridRow(panelFormGroup, 2, lblJenis, panelJenis);
        addGridRow(panelFormGroup, 3, lblProdi, txtProdi);
        addGridRow(panelFormGroup, 4, lblNoHp, txtNoHp);
        addGridRow(panelFormGroup, 5, lblEmail, txtEmail);

        // Baris Tombol Aksi
        JPanel panelBtnAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBtnAction.setBackground(warnaBgForm);

        btnSimpan = createStyledButton("Simpan", btnSimpanColor);
        btnUbah   = createStyledButton("Ubah", btnUbahColor);
        btnHapus  = createStyledButton("Hapus", btnHapusColor);
        btnReset  = createStyledButton("Reset", btnResetColor);
        btnCetak  = createStyledButton("Cetak Semua", btnCetakColor);

        panelBtnAction.add(btnSimpan);
        panelBtnAction.add(btnUbah);
        panelBtnAction.add(btnHapus);
        panelBtnAction.add(btnReset);
        panelBtnAction.add(btnCetak);

        GridBagConstraints cBtn = new GridBagConstraints();
        cBtn.gridx = 0; cBtn.gridy = 6;
        cBtn.gridwidth = 2;
        cBtn.insets = new Insets(10, 0, 5, 0);
        panelFormGroup.add(panelBtnAction, cBtn);

        // 2. Group Box Data Peserta (Table + Search)
        JPanel panelTabelGroup = new JPanel(new BorderLayout(5, 5));
        panelTabelGroup.setBackground(warnaPanelTabel);
        panelTabelGroup.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(warnaBorder, 1),
                "Data Peserta",
                TitledBorder.LEFT, TitledBorder.TOP,
                fontTombol, warnaTeksDongker));

        // Area Search
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelSearch.setBackground(warnaPanelTabel);
        JLabel lblCari = new JLabel("Pencarian");
        lblCari.setFont(fontLabel);
        lblCari.setForeground(warnaTeksDongker);
        
        txtCari = new JTextField(25);
        txtCari.setFont(fontLabel);
        txtCari.setForeground(warnaTeksDongker);

        btnCari = createStyledButton("Cari", btnCariColor);
        btnCari.setPreferredSize(new Dimension(80, 28));

        panelSearch.add(lblCari);
        panelSearch.add(txtCari);
        panelSearch.add(btnCari);

        // Table Component
        tblPeserta = new JTable();
        tblPeserta.setFont(fontLabel);
        tblPeserta.setForeground(warnaTeksDongker);
        
        JScrollPane scrollTable = new JScrollPane(tblPeserta);
        scrollTable.setPreferredSize(new Dimension(720, 210));

        panelTabelGroup.add(panelSearch, BorderLayout.NORTH);
        panelTabelGroup.add(scrollTable, BorderLayout.CENTER);

        // Susun ke Panel Utama
        panelCenter.add(panelFormGroup);
        panelCenter.add(Box.createVerticalStrut(10));
        panelCenter.add(panelTabelGroup);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelCenter, BorderLayout.CENTER);

        setContentPane(panelUtama);

        // Event Listener Tombol
        btnSimpan.addActionListener(e -> btnSimpanActionPerformed());
        btnUbah.addActionListener(e -> btnUbahActionPerformed());
        btnHapus.addActionListener(e -> btnHapusActionPerformed());
        btnReset.addActionListener(e -> btnResetActionPerformed());
        btnCari.addActionListener(e -> btnCariActionPerformed());
        btnCetak.addActionListener(e -> JOptionPane.showMessageDialog(this, 
                "Modul Laporan (JasperReports) akan diaktifkan pada Pertemuan 11/12.", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE));

        // Event Klik Baris Tabel
        tblPeserta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tblPesertaMouseClicked();
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(fontTombol);
        btn.setBackground(bg);
        btn.setForeground(warnaTeksDongker);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 30));
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

    // ===== LOGIKA LOGIS METODE CRUD =====

    private void configureTable() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "NIM", "Nama", "JK", "Prodi", "No. HP", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPeserta.setModel(tableModel);
        tblPeserta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // KONFIGURASI HEADER TABEL (WARNA DONGKER)
        tblPeserta.getTableHeader().setBackground(warnaHeaderBar);
        tblPeserta.getTableHeader().setForeground(warnaTeksDongker);
        tblPeserta.getTableHeader().setFont(fontTombol);

        tblPeserta.getColumnModel().getColumn(0).setPreferredWidth(35);  // ID
        tblPeserta.getColumnModel().getColumn(1).setPreferredWidth(90);  // NIM
        tblPeserta.getColumnModel().getColumn(2).setPreferredWidth(140); // Nama
        tblPeserta.getColumnModel().getColumn(3).setPreferredWidth(45);  // JK
    }

    private void fillTable(List<Peserta> data) {
        tableModel.setRowCount(0);
        for (Peserta p : data) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNim(),
                p.getNama(),
                p.getJenisKelamin(),
                p.getProdi(),
                p.getNoHp(),
                p.getEmail()
            });
        }
    }

    private void loadTable() {
        try {
            fillTable(dao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        loadTable();
        clearForm();
        txtCari.setText("");
    }

    private String getJenisKelamin() {
        if (rbLaki.isSelected()) return "L";
        if (rbPerempuan.isSelected()) return "P";
        return null;
    }

    private boolean validateForm() {
        if (txtNim.getText().trim().isEmpty() ||
            txtNama.getText().trim().isEmpty() ||
            txtProdi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan Prodi wajib diisi.", 
                    "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (getJenisKelamin() == null) {
            JOptionPane.showMessageDialog(this, "Pilih jenis kelamin.", 
                    "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Peserta getFormData() {
        Peserta p = new Peserta();
        p.setId(selectedId);
        p.setNim(txtNim.getText().trim());
        p.setNama(txtNama.getText().trim());
        p.setJenisKelamin(getJenisKelamin());
        p.setProdi(txtProdi.getText().trim());
        p.setNoHp(txtNoHp.getText().trim());
        p.setEmail(txtEmail.getText().trim());
        return p;
    }

    private void clearForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        buttonGroupJenisKelamin.clearSelection();
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        tblPeserta.clearSelection();
        
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        txtNim.requestFocus();
    }

    // ===== EVENT HANDLERS DENGAN KONFIRMASI JOPTIONPANE =====

    private void tblPesertaMouseClicked() {
        int row = tblPeserta.getSelectedRow();
        if (row == -1) return;

        selectedId = Integer.parseInt(tblPeserta.getValueAt(row, 0).toString());
        txtNim.setText(tblPeserta.getValueAt(row, 1).toString());
        txtNama.setText(tblPeserta.getValueAt(row, 2).toString());
        
        String jk = tblPeserta.getValueAt(row, 3).toString();
        rbLaki.setSelected("L".equalsIgnoreCase(jk));
        rbPerempuan.setSelected("P".equalsIgnoreCase(jk));

        txtProdi.setText(tblPeserta.getValueAt(row, 4).toString());
        txtNoHp.setText(tblPeserta.getValueAt(row, 5) != null ? tblPeserta.getValueAt(row, 5).toString() : "");
        txtEmail.setText(tblPeserta.getValueAt(row, 6) != null ? tblPeserta.getValueAt(row, 6).toString() : "");

        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    private void btnSimpanActionPerformed() {
        if (!validateForm()) return;

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menyimpan data peserta baru ini?",
                "Konfirmasi Simpan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        try {
            Peserta p = getFormData();
            if (dao.insert(p)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil disimpan.", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak tersimpan.", 
                        "Informasi", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data:\n" + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnUbahActionPerformed() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih data peserta pada tabel terlebih dahulu.", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin mengubah data peserta ini?",
                "Konfirmasi Ubah Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        try {
            Peserta p = getFormData();
            if (dao.update(p)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil diubah.", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau tidak berubah.", 
                        "Informasi", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data:\n" + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnHapusActionPerformed() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this, "Pilih data peserta yang akan dihapus.", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = txtNama.getText().trim();
        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin menghapus peserta: " + nama + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        try {
            if (dao.delete(selectedId)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil dihapus.", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data:\n" + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCariActionPerformed() {
        String keyword = txtCari.getText().trim();
        try {
            if (keyword.isEmpty()) {
                loadTable();
            } else {
                fillTable(dao.search(keyword));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Pencarian gagal:\n" + ex.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnResetActionPerformed() {
        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin mengosongkan form dan memulihkan data tabel?",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        txtCari.setText("");
        loadTable();
        clearForm();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MasterPeserta().setVisible(true));
    }
}