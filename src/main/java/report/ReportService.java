package report;

import config.Database;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public final class ReportService {

    private ReportService() {}

    public static void tampilkanLaporanPeserta() {
        try (Connection conn = Database.getConnection();
             InputStream in = ReportService.class.getResourceAsStream("/report/laporan_peserta.jasper")) {
            if (in == null) {
                throw new IllegalStateException("File laporan_peserta.jasper tidak ditemukan.");
            }
            JasperPrint print = JasperFillManager.fillReport(in, new HashMap<>(), conn);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal membuka laporan: " + e.getMessage());
        }
    }

    public static void cetakPendaftaran(int idPendaftaran) {
        try (Connection conn = Database.getConnection();
             InputStream reportStream = ReportService.class.getResourceAsStream("/report/laporan_pendaftaran.jasper")) {
            if (reportStream == null) {
                throw new IllegalStateException("File laporan_pendaftaran.jasper tidak ditemukan.");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("P_ID_PENDAFTARAN", idPendaftaran);

            JasperPrint print = JasperFillManager.fillReport(reportStream, params, conn);

            if (print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data laporan pendaftaran tidak ditemukan.");
                return;
            }

            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + ex.getMessage());
        }
    }

    public static void cetakPembayaran(Date tanggalAwal, Date tanggalAkhir) {
        try (Connection conn = Database.getConnection();
             InputStream reportStream = ReportService.class.getResourceAsStream("/report/laporan_pembayaran.jasper")) {
            if (reportStream == null) {
                throw new IllegalStateException("File laporan_pembayaran.jasper tidak ditemukan.");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("P_TANGGAL_AWAL", tanggalAwal);
            params.put("P_TANGGAL_AKHIR", tanggalAkhir);

            JasperPrint print = JasperFillManager.fillReport(reportStream, params, conn);

            if (print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tidak ada transaksi pembayaran pada rentang tanggal tersebut.");
                return;
            }

            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pembayaran: " + ex.getMessage());
        }
    }
}