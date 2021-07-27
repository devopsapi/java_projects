import BreakingBall.Gameplay;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static JFrame frame;

    Main() {
        initFrame();
        Gameplay gameplay = new Gameplay();
        frame.add(gameplay);
    }

    public static void main(String[] args) {
        new Main();
    }

    private void initFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Breaking Ball");
        frame.setBounds(10, 10, 700, 600);
        frame.setResizable(false);
        //frame.setSize(new Dimension(700,600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
