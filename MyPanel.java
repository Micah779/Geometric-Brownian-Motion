// java swing & time
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class MyPanel extends JPanel implements ActionListener {
    // class constants
    final int PANEL_WIDTH = 500;
    final int PANEL_HEIGHT = 500;
    final int PARTICLE_COUNT = 100;
    final double PARTICLE_RADIUS = 10;
    final double SPECIAL_PARTICLE_RADIUS = PARTICLE_RADIUS*3;

    // fields
    Image particle;
    Timer timer;
    ArrayList<Particle> particles;
    Particle specialParticle;
    int particlesToShow = 0;
    Random random;

    // particle subclass
    class Particle {
        // particle fields
        double x, y;
        double vx, vy;

        // particle constructor
        Particle(double x, double y, double vx, double vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }
    }

    class specialParticle extends Particle {
        specialParticle(double x, double y, double vx, double vy) {
            super(x, y, vx, vy);
        }
    }

    // myPanel constructor
    MyPanel() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.lightGray);
        particle = new ImageIcon("particle.png").getImage();
        timer = new Timer(1, this); // 10ms frame change rates
        timer.start();
        particles = new ArrayList<>();
        random = new Random();
        initilizeParticles();
        initializeSpecialParticle();
    }

    // init/create regular particles
    private void initilizeParticles() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double velocitiesScale = 0.1;
            particles.add(new Particle(random.nextInt(PANEL_WIDTH), 
                                       random.nextInt(PANEL_HEIGHT), 
                                       (random.nextDouble() * 4 - 2) * velocitiesScale, 
                                       (random.nextDouble() * 4 - 2) * velocitiesScale));
        }
    }

    // Initialize the special particle
    private void initializeSpecialParticle() {
        double velocityScale = 0.1; // Slow down the initial velocities
        specialParticle = new specialParticle(
            250, // initilizing in the center of the panel
            250,
            (random.nextDouble() * 4 - 2) * velocityScale,
            (random.nextDouble() * 4 - 2) * velocityScale
        );
    }

    // changes made every time repaint() is called
    public void paint(Graphics g) {
        super.paint(g); // background color
        Graphics2D g2D = (Graphics2D) g;
        for (Particle p : particles) {
            g2D.drawImage(particle, (int)p.x, (int)p.y, null); // drawing new particles
        }

        // painting the special particle
        g2D.setColor(Color.red);
        g2D.fillOval((int)specialParticle.x, (int)specialParticle.y, (int)(SPECIAL_PARTICLE_RADIUS), (int)(SPECIAL_PARTICLE_RADIUS));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateParticles();
        updateSpecialParticle();
        checkCollisions();
        repaint();
    }

    // update particle position
    private void updateParticles() {
        for (Particle p : particles) {
            p.x += p.vx;
            p.y += p.vy;

            if (p.x < 0 || p.x > PANEL_WIDTH - particle.getWidth(null)) {
                p.vx = -p.vx;
                p.x = Math.max(0, Math.min(PANEL_WIDTH - particle.getWidth(null), p.x));
            }
            if (p.y < 0 || p.y > PANEL_HEIGHT - particle.getHeight(null)) {
                p.vy = -p.vy;
                p.y = Math.max(0, Math.min(PANEL_HEIGHT - particle.getHeight(null), p.y));
            }
        }
    }

    // update special particle position
    private void updateSpecialParticle() {
        specialParticle.x += specialParticle.vx;
        specialParticle.y += specialParticle.vy;

        if (specialParticle.x < 0 || specialParticle.x > PANEL_WIDTH - PARTICLE_RADIUS * 3) {
            specialParticle.vx = -specialParticle.vx;
            specialParticle.x = Math.max(0, Math.min(PANEL_WIDTH - PARTICLE_RADIUS * 3, specialParticle.x));
        }
        if (specialParticle.y < 0 || specialParticle.y > PANEL_HEIGHT - PARTICLE_RADIUS * 3) {
            specialParticle.vy = -specialParticle.vy;
            specialParticle.y = Math.max(0, Math.min(PANEL_HEIGHT - PARTICLE_RADIUS * 3, specialParticle.y));
        }
    }

    // check for collisions
    private void checkCollisions() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            Particle p1 = particles.get(i);
            for (int j = i + 1; j < PARTICLE_COUNT; j++) {
                Particle p2 = particles.get(j);
                checkCollision(p1, p2, PARTICLE_RADIUS * 2);
            }
            checkCollision(p1, specialParticle, PARTICLE_RADIUS + SPECIAL_PARTICLE_RADIUS);
        }
    }

    // check for collision with other
    private void checkCollision(Particle p1, Particle p2, double minDistance) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < minDistance) {
            double angle = Math.atan2(dy, dx);
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            // Rotate particle1's velocity
            double vx1 = p1.vx * cos + p1.vy * sin;
            double vy1 = p1.vy * cos - p1.vx * sin;

            // Rotate particle2's velocity
            double vx2 = p2.vx * cos + p2.vy * sin;
            double vy2 = p2.vy * cos - p2.vx * sin;

            // Exchange velocities
            double tempVx = vx1;
            vx1 = vx2;
            vx2 = tempVx;

            // Rotate velocities back
            p1.vx = vx1 * cos - vy1 * sin;
            p1.vy = vy1 * cos + vx1 * sin;
            p2.vx = vx2 * cos - vy2 * sin;
            p2.vy = vy2 * cos + vx2 * sin;

            // Separate the particles to avoid sticking
            double overlap = (minDistance - distance) / 2;
            p1.x -= overlap * cos;
            p1.y -= overlap * sin;
            p2.x += overlap * cos;
            p2.y += overlap * sin;
        }
    }
}
