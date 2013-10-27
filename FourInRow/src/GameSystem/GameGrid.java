package GameSystem;

import SharedSystem.IConstants;
import SharedSystem.IGGListener;
import java.util.ArrayList;
import java.util.List;

public class GameGrid implements IConstants {
    private int grid[][] = new int[BOARD_ROWS][BOARD_COLUMNS];
    private List<IGGListener> listeners = new ArrayList();
    private int lastRow;
    private int lastColumn;
    
    public GameGrid() {
        clearGrid();
    }
    
    public void addMarker(int column, int markerID) throws InvalidMoveException {
        if(column < 0 || column >= BOARD_COLUMNS || grid[0][column] != EMPTY)
            throw new InvalidMoveException();
        
        int row = BOARD_ROWS - 1;
        while(grid[row][column] != EMPTY && row > 0)
            row--;
        
        grid[row][column] = markerID;
        
        lastRow = row;
        lastColumn = column;
        
        for(IGGListener listener : listeners) 
            listener.updateMove(row, column, markerID);
    }
    
    public int checkResult() {
        if(checkHorizontal() || checkVertical() || checkDiagonal())
            return grid[lastRow][lastColumn];
        
        for(int row = 0; row < BOARD_ROWS; row++) {
            for(int column = 0; column < BOARD_COLUMNS; column++) {
                if(grid[row][column] == EMPTY)
                    return NO_OUTCOME;
            }
        }
        
        return DRAW;
    }
    
    public void registerListener(IGGListener listener) {
        listeners.add(listener);
    }
    
    public void clearListeners() {
        listeners.clear();
    }
    
    public final void clearGrid() {
        for(int row = 0; row < grid.length; row++) {
            for(int column = 0; column < grid[row].length; column++) {
                grid[row][column] = EMPTY;
                
                for(IGGListener listener : listeners) 
                    listener.updateMove(row, column, EMPTY);
            }
        }
    }
    
    private boolean checkHorizontal() {
        int markerID = grid[lastRow][lastColumn];
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
    
    private boolean checkVertical() {
        int markerID = grid[lastRow][lastColumn];
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
   
    private boolean checkDiagonal() {
        int markerID = grid[lastRow][lastColumn];
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
