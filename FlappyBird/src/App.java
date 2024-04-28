import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private static JFrame gameFrame;

    public static void main(String[] args) {
        showStartForm();
    }

    private static void showStartForm() {
        JFrame startFrame = new JFrame("Flappy Bird");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(360, 640);
        startFrame.setLocationRelativeTo(null);
        startFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(109, 211, 234));

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mengatur warna latar belakang tombol menjadi hijau
        startButton.setBackground(Color.GREEN);

        // Mengatur ukuran tombol
        Dimension buttonSize = new Dimension(200, 80);
        startButton.setPreferredSize(buttonSize);

        // Mengatur ukuran font teks
        Font buttonFont = new Font("Arial", Font.BOLD, 24);
        startButton.setFont(buttonFont);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose(); // Menutup form start

                // Membuat dan menampilkan frame game FlappyBird
                createGameFrame();
                gameFrame.setVisible(true);
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());

        startFrame.add(panel);
        startFrame.setVisible(true);
    }

    private static void createGameFrame() {
        gameFrame = new JFrame("Flappy Bird");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(360, 640);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);

        FlappyBird flappyBird = new FlappyBird();
        gameFrame.add(flappyBird);
        gameFrame.pack();
        flappyBird.requestFocus();
    }
}