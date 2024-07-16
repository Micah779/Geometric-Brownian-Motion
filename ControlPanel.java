import javax.swing.*;
import java.awt.event.*;

public class ControlPanel extends JPanel implements ActionListener {
    // fields
    private JButton startStopButton;
    private Timer timer;
    
    ControlPanel(Timer timer) {
        this.timer = timer;
        startStopButton = new JButton("Start");
        startStopButton.addActionListener(this);
        this.add(startStopButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (timer.isRunning()) {
            timer.stop();
            startStopButton.setText("Start");
        } else {
            timer.start();
            startStopButton.setText("Stop");
        }
    }
}
