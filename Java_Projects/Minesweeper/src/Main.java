import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

public class Main extends JFrame {

    private Game game;
    private JPanel panel;
    private JLabel label;
    private final int COLS = 9;
    private final int ROWS = 9;
    private final int BOMBS = 10;
    private final int IMAGE_SIZE = 50;

    private JLabel timeLabel;
    private Timer timer;
    private long hours = 0;
    private long minutes = 0;
    private long seconds = 0;
    private long millis = 0;


    public Main() {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initTimer();
        initFrame();

    }

    private void initLabel() {
        label = new JLabel("Welcome");
        this.add(label, BorderLayout.SOUTH);
    }

    private void initFrame() {
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("icon"));
    }

    private void initTimer() {

        timeLabel = new JLabel(String.format("%02d:%02d:%02d.%03d", 0, 0, 0, 0));

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                millis++;
                if (millis == 10) {
                    millis = 0;
                    seconds++;
                }
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes == 60) {
                    minutes = 0;
                    hours++;
                }
                timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis));
            }
        });

        this.add(timeLabel, BorderLayout.NORTH);
    }

    private void initPanel() {

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image, coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                timer.start();
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    game.pressLeftButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    game.pressRightButton(coord);
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    game.start();
                }

                String currentMessage = getMessage();
                if (currentMessage == "YOU LOSE!") {
                    timer.stop();
                    millis = 0;
                    seconds = 0;
                    minutes = 0;
                    hours = 0;
                }
                label.setText(currentMessage);
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(Ranges.getSize().x * IMAGE_SIZE, Ranges.getSize().y * IMAGE_SIZE));
        this.add(panel);
    }

    private String getMessage() {
        switch (game.getState()) {
            case PLAYED:
                return "Think twice!";
            case BOMBED:
                return "YOU LOSE!";
            case WINNER:
                return "CONGRATULATIONS!";
            default:
                return "";
        }
    }

    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name().toLowerCase());
        }
    }

    private Image getImage(String name) {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

    public static void main(String[] args) {
        new Main();
    }
}
