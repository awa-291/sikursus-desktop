package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import model.Peserta;
import dao.PesertaDAO;

public class FormPeserta extends JFrame {

    // Warna sesuai desain
    private final Color warnaHeader = new Color(204, 255, 255);  // cyan muda
    private final Color warnaIsi    = new Color(255, 255, 204);  // kuning muda
    private final Color warnaBingkai = new Color(214, 214, 214); // abu-abu pemisah

    // Font sesuai desain (serif bold)
    private final Font fontJudul  = new Font("Times New Roman", Font.BOLD, 22);
    private final Font fontLabel  = new Font("Times New Roman", Font.BOLD, 17);
    private final Font fontRadio  = new Font("Times New Roman", Font.BOLD, 14);
    private final Font fontTombol = new Font("Times New Roman", Font.BOLD, 14);

    private JPanel panelUtama, panelHeader, panelForm, panelJenis;

    private JLabel lblJudul, lblNim, lblNama, lblJenisKelamin, lblProdi, lblNoHp, lblEmail;
    private JTextField txtNim, txtNama, txtProdi, txtNoHp, txtEmail;
    private JRadioButton rbLaki, rbPerempuan;
    private ButtonGroup buttonGroupJenisKelamin;
    private JButton btnSimpan;

    public FormPeserta() {
        initComponents();
        getRootPane().setDefaultButton(btnSimpan);

        setSize(520, 495);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Form Peserta");

        // ===== Panel =====
        panelUtama = new JPanel(new BorderLayout(0, 5));
        panelUtama.setBackground(warnaBingkai);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(warnaHeader);
        panelHeader.setPreferredSize(new Dimension(100, 72));

        panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(warnaIsi);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 55, 20, 40));

        panelJenis = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        panelJenis.setBackground(warnaIsi);

        // ===== Header =====
        lblJudul = new JLabel("Form Peserta", SwingConstants.CENTER);
        lblJudul.setFont(fontJudul);
        panelHeader.add(lblJudul, BorderLayout.CENTER);

        // ===== Komponen =====
        lblNim = new JLabel("Nim");
        lblNama = new JLabel("Nama Peserta");
        lblJenisKelamin = new JLabel("Jenis Kelamin");
        lblProdi = new JLabel("Program studi");
        lblNoHp = new JLabel("Nomor HP");
        lblEmail = new JLabel("Email");

        JLabel[] allLabels = {lblNim, lblNama, lblJenisKelamin, lblProdi, lblNoHp, lblEmail};
        for (JLabel lbl : allLabels) {
            lbl.setFont(fontLabel);
        }

        txtNim = new JTextField();
        txtNama = new JTextField();
        txtProdi = new JTextField();
        txtNoHp = new JTextField();
        txtEmail = new JTextField();

        Dimension dimField = new Dimension(182, 26);
        txtNim.setPreferredSize(dimField);
        txtNama.setPreferredSize(dimField);
        txtProdi.setPreferredSize(dimField);
        txtNoHp.setPreferredSize(dimField);
        txtEmail.setPreferredSize(dimField);

        rbLaki = new JRadioButton("Laki-laki");
        rbPerempuan = new JRadioButton("Perempuan");
        rbLaki.setFont(fontRadio);
        rbPerempuan.setFont(fontRadio);
        rbLaki.setBackground(warnaIsi);
        rbPerempuan.setBackground(warnaIsi);
        rbLaki.setFocusPainted(false);
        rbPerempuan.setFocusPainted(false);

        buttonGroupJenisKelamin = new ButtonGroup();
        buttonGroupJenisKelamin.add(rbLaki);
        buttonGroupJenisKelamin.add(rbPerempuan);
        panelJenis.add(rbLaki);
        panelJenis.add(rbPerempuan);

        btnSimpan = new JButton("Simpan ke database");
        btnSimpan.setFont(fontTombol);
        btnSimpan.setPreferredSize(new Dimension(182, 28));
        btnSimpan.addActionListener(evt -> btnSimpanActionPerformed());

        // ===== Susun ke form =====
        addRow(0, lblNim, txtNim);
        addRow(1, lblNama, txtNama);
        addRow(2, lblJenisKelamin, panelJenis);
        addRow(3, lblProdi, txtProdi);
        addRow(4, lblNoHp, txtNoHp);
        addRow(5, lblEmail, txtEmail);

        // Tombol sejajar dengan kolom field
        GridBagConstraints cBtn = new GridBagConstraints();
        cBtn.gridx = 1;
        cBtn.gridy = 6;
        cBtn.anchor = GridBagConstraints.WEST;
        cBtn.insets = new Insets(4, 0, 0, 0);
        panelForm.add(btnSimpan, cBtn);

        // Dorong semua isi ke atas
        GridBagConstraints cSpacer = new GridBagConstraints();
        cSpacer.gridx = 0;
        cSpacer.gridy = 7;
        cSpacer.weighty = 1.0;
        panelForm.add(new JPanel() {{ setOpaque(false); }}, cSpacer);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelForm, BorderLayout.CENTER);
        setContentPane(panelUtama);
    }

    private void addRow(int row, JLabel label, Component component) {
        GridBagConstraints cLabel = new GridBagConstraints();
        cLabel.gridx = 0;
        cLabel.gridy = row;
        cLabel.anchor = GridBagConstraints.WEST;
        cLabel.insets = new Insets(7, 0, 7, 50);
        panelForm.add(label, cLabel);

        GridBagConstraints cField = new GridBagConstraints();
        cField.gridx = 1;
        cField.gridy = row;
        cField.anchor = GridBagConstraints.WEST;
        cField.insets = new Insets(7, 0, 7, 0);
        panelForm.add(component, cField);
    }

    private String getJenisKelamin() {
        if (rbLaki.isSelected()) return "L";
        if (rbPerempuan.isSelected()) return "P";
        return "";
    }

    private void clearForm() {
        txtNim.setText("");
        txtNama.setText("");
        buttonGroupJenisKelamin.clearSelection();
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtNim.requestFocus();
    }

    private void btnSimpanActionPerformed() {
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = txtProdi.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();
        String jk = getJenisKelamin();

        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty() || jk.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "NIM, nama, jenis kelamin, dan prodi wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Peserta peserta = new Peserta(nim, nama, jk, prodi, noHp, email);
        PesertaDAO dao = new PesertaDAO();

        try {
            boolean berhasilSimpan = dao.insert(peserta);
            if (berhasilSimpan) {
                JOptionPane.showMessageDialog(this,
                        "Data peserta berhasil disimpan.",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data tidak tersimpan.",
                        "Informasi", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Tidak memakai Nimbus supaya tampilan tetap Metal seperti di desain
        java.awt.EventQueue.invokeLater(() -> new FormPeserta().setVisible(true));
    }
}