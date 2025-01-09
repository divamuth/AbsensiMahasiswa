import javax.swing.*;
import java.io.File;
import views.*;

public class Main {
    public static void main(String[] args) {
        // Pastikan folder data ada
        try {
            String dataPath = System.getProperty("user.dir") + File.separator + "data";
            File dataFolder = new File(dataPath);

            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                System.err.println("Gagal membuat folder 'data' di path: " + dataPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Sistem Absensi Mahasiswa");
            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Absensi", new AbsensiPanel());
            tabbedPane.addTab("Mahasiswa", new MahasiswaPanel());
            tabbedPane.addTab("Laporan", new LaporanPanel());

            frame.add(tabbedPane);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}