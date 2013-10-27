package GameSystem;

import SharedSystem.IConstants;
import static SharedSystem.IConstants.BOARD_COLUMNS;
import static SharedSystem.IConstants.BOARD_ROWS;
import SharedSystem.IGGListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computer extends Player implements IConstants, IGGListener {
    private int grid[][] = new int[BOARD_ROWS][BOARD_COLUMNS];
    
    public Computer(int markerID, String name) {
        super(markerID, name);
    }

    @Override
    public int computeMove() throws InterruptedException {
        Thread.sleep(500);
        List<Integer> moves = getMoves();
        
        int opponentID = getMarkerID() == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        
        for(int column = 0; column < BOARD_COLUMNS; column++) {
            if(isWinningMove(moves, column, getMarkerID()))
                return column;
        }
        
        for(int column = 0; column < BOARD_COLUMNS; column++) {
            if(isWinningMove(moves, column, opponentID))
                return column;
        }
        
        while (true) {
            int random = new Random().nextInt(BOARD_COLUMNS);
            if (moves.get(random) != -1)
                return random;
        }
    }

    @Override
    public void updateMove(int row, int column, int markerID) {
        grid[row][column] = markerID;
    }
    
    private List<Integer> getMoves() {
        List<Integer> list = new ArrayList();
        
        for(int column = 0; column < BOARD_COLUMNS; column++) {
            if(grid[0][column] != EMPTY) {
                list.add(-1);
            }
            else {
                int row = BOARD_ROWS - 1;
                while(row > 0 && grid[row][column] != EMPTY)
                    row--;
                
                list.add(row);
            }
        }
        
        return list;
    }
    
    private boolean isWinningMove(List<Integer> moves, int column, int markerID) {
        int move = moves.get(column);
        if(move == -1)
            return false;

        if(isWinningMove(move, column, markerID))
            return true;
        
        return false;
    }
    
    private boolean isWinningMove(int lastRow, int lastColumn, int markerID) {
        if(checkHorizontal(lastRow, lastColumn, markerID) || checkVertical(lastRow, lastColumn, markerID) || checkDiagonal(lastRow, lastColumn, markerID))
            return true;
        
        return false;
    }
    
    private boolean checkHorizontal(int lastRow, int lastColumn, int markerID) {
        int row = lastRow;
        int column = lastColumn - 1;
        
        int count = 1;
        while(column >= 0 && grid[row][column--] == markerID)
            count++;
        
        column = lastColumn + 1;
        while(column < BOARD_COLUMNS && grid[row][column++] == markerID)
            count++;
        
        if(count >= 4)
            return true;
        
        return false;
    }
    
    private boolean checkVertical(int lastRow, int lastColumn, int markerID) {
        int row = lastRow - 1;
        int column = lastColumn;
        
        int count = 1;
        while(row > 0 && grid[row--][column] == markerID)
            count++;
        
        row = lastRow + 1;
        while(row < BOARD_ROWS && grid[row++][column] == markerID)
            count++;
        
        if(count >= 4)
            return true;
        
        return false;
    }
   
    private boolean checkDiagonal(int lastRow, int lastColumn, int markerID) {
        int row = lastRow - 1;
        int column = lastColumn - 1;
        
        int count = 1;
        while(row >= 0 && column >= 0 && grid[row--][column--] == markerID)
            count++;
        
        row = lastRow + 1;
        column = lastColumn + 1;
        while(row < BOARD_ROWS && column < BOARD_COLUMNS && grid[row++][column++] == markerID)
            count++;
        
        if(count >= 4)
            return true;
        
        count = 1;
        
        row = lastRow - 1;
        column = lastColumn + 1;
        while(row >= 0 && column < BOARD_COLUMNS && grid[row--][column++] == markerID)
            count++;
        
        row = lastRow + 1;
        column = lastColumn - 1;
        while(row < BOARD_ROWS && column >= 0 && grid[row++][column--] == markerID)
            count++;
        
        if(count >= 4)
            return true;
                
        return false;
    }
}
