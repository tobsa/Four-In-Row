package GuiSystem;

import SharedSystem.BlockQueue;
import SharedSystem.IConstants;
import SharedSystem.IGGListener;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements IConstants, IGGListener {
    private JLabel grid[][]  = new JLabel[BOARD_ROWS][BOARD_COLUMNS];
    private ImageIcon empty  = new ImageIcon("images/empty.png");
    private ImageIcon red    = new ImageIcon("images/red.png");
    private ImageIcon yellow = new ImageIcon("images/yellow.png");
    
    public GameBoard() {
        setLayout(new GridLayout(BOARD_ROWS, BOARD_COLUMNS));
        addMouseListener(new MousePressedListener());
        
        for(int row = 0; row < BOARD_ROWS; row++) {
            for(int column = 0; column < BOARD_COLUMNS; column++) {
                JLabel label = new JLabel(empty);
                grid[row][column] = label;
                add(label);
            }
        }
    }
    
    @Override
    public void updateMove(int row, int column, int markerID) {
        switch(markerID) {
            case EMPTY:
                grid[row][column].setIcon(empty);
                break;
            case PLAYER_1:
                grid[row][column].setIcon(red);
                break;
            case PLAYER_2:
                grid[row][column].setIcon(yellow);
                break;
        }
    }
    
    private class MousePressedListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            BlockQueue.getInstance().add(event.getX() / IMAGE_SIZE);
        }
    }
}
