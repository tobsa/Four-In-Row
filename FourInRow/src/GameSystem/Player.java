package GameSystem;

import java.io.IOException;

public abstract class Player {
    private int markerID;
    private String name;
    
    public Player(int markerID, String name) {
        this.markerID = markerID;
        this.name = name;
    }
    
    public int getMarkerID() {
        return markerID;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract int computeMove() throws InterruptedException, IOException;
}
