import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import breakingBall.*;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends JFrame {

    private JPanel panel;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;

    private Element[][] bricks;
    private Element platform;
    private Element ball;

    private final int platformPrimaryCoordX = 270;
    private final int platformPrimaryCoordY = -10;
    private final int ballPrimaryCoordX = 350;
    private final int ballPrimaryCoordY = 370;

    private Timer move;
    private int dx = 1;
    private int dy = 1;
    private int speed = 5;


    Main() {
        bricks = new Element[6][7];

        platform = new Element();
        Coord platformPrimaryCoord = new Coord();
        platformPrimaryCoord.x = platformPrimaryCoordX;
        platformPrimaryCoord.y = platformPrimaryCoordY;
        platform.coord = platformPrimaryCoord;

        ball = new Element();
        Coord ballPrimaryCoord = new Coord();
        ballPrimaryCoord.x = ballPrimaryCoordX;
        ballPrimaryCoord.y = ballPrimaryCoordY;
        ball.coord = ballPrimaryCoord;

        initBricks();
        initPanel();
        initFrame();

        move = new Timer();
    }

    public static void main(String[] args) {
        new Main();
    }


    private void initFrame() {
        pack();
        setTitle("Breaking Ball");
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // setIconImage(getImage("ball"));
    }


    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int i = 0; i < bricks.length; i++) {
                    for (int j = 0; j < bricks[0].length; j++) {
                        g.drawImage((Image) bricks[i][j].image, bricks[i][j].coord.x, bricks[i][j].coord.y, this);
                    }
                }

                platform.image = getImage("platform");
                g.drawImage((Image) platform.image, platform.coord.x, platform.coord.y, this);

                ball.image = getImage("ball");
                g.drawImage((Image) ball.image, ball.coord.x, ball.coord.y, this);
            }
        };

        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {

                    // MOVE PLATFORM TO THE RIGHT
                    case KeyEvent.VK_RIGHT:
                        if (platform.coord.x < WIDTH - 220) {
                            platform.coord.x += 15;
                        }
                        break;

                    // MOVE PLATFORM TO THE LEFT
                    case KeyEvent.VK_LEFT:
                        if (platform.coord.x > 0) {
                            platform.coord.x -= 15;
                        }
                        break;

                    // RESTART GAME;
                    case KeyEvent.VK_ESCAPE:

                        initBricks();
                        platform.coord.x = platformPrimaryCoordX;
                        ball.coord.x = ballPrimaryCoordX;
                        ball.coord.y = ballPrimaryCoordY;
                        move.cancel();

                        // START MOVING THE BALL
                    case KeyEvent.VK_UP:

                        move.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                if (ball.coord.x + dx < 0)
                                    dx = speed;
                                if (ball.coord.x + dx > WIDTH - 50)
                                    dx = -speed;
                                if (ball.coord.y + dy < -40)
                                    dy = speed;
                               /* if (ball.coord.y + dy > HEIGHT)
                                    gameOver();*/
                                if (ball.coord.x > platform.coord.x || ball.coord.x < platform.coord.x + 50 && ball.coord.y == HEIGHT-120)
                                    dy = -speed;

                                ball.coord.x += dx;
                                ball.coord.y += dy;

                                panel.repaint();
                            }
                        }, 0, 20);
                }

                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(panel);
    }

    private void gameOver() {
        JDialog d = new JDialog(this, true);
        d.setTitle("Game over");
        d.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT / 2));
        d.setLocationRelativeTo(null);
        d.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        d.setVisible(true);
    }

    private int test(Image i) {
        ImageIcon t = new ImageIcon(i);
        return t.getIconWidth();
    }


    private void initBricks() {
        int x = 10;
        int y = -420;

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                Coord coord = new Coord();
                coord.x = x;
                coord.y = y;
                Element brick = new Element();
                brick.coord = coord;
                brick.image = getImage("brick");
                bricks[i][j] = brick;
                x += 100;
            }
            x = 10;
            y += 35;
        }
    }


    private Image getImage(String name) {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
