package GameSystem;

import SharedSystem.BlockQueue;

public class Human extends Player {
    
    public Human(int markerID, String name) {
        super(markerID, name);
    }

    @Override
    public int computeMove() throws InterruptedException {
        return BlockQueue.getInstance().take();
    }
}
