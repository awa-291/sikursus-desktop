package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class InstrukturFrame extends JFrame {
    public InstrukturFrame() {
        setTitle("Master Instruktur");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sesuai aturan modul child form[cite: 52, 53]
        setLocationRelativeTo(null);
        add(new JLabel("Form Master Instruktur", SwingConstants.CENTER));
    }
}