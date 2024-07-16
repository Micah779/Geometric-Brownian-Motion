// java swing
import java.awt.BorderLayout;

import javax.swing.*;

public class MyFrame extends JFrame {

    // fields
    MyPanel panel;
    ControlPanel control;

    // constructors
    public MyFrame() {
        panel = new MyPanel();
        control = new ControlPanel(panel.timer);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.NORTH);
        this.add(control, BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
}
