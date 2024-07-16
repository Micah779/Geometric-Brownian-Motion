// java swing
import javax.swing.*;

public class MyFrame extends JFrame {

    // fields
    MyPanel panel;

    // constructors
    public MyFrame() {
        panel = new MyPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
}
