package FourInRow;

import GuiSystem.GameFrame;
import javax.swing.JFrame;

public class FourInRow {
    public static void main(String args[]) {
        GameFrame frame = new GameFrame();
        frame.setTitle("Four in a row!");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
