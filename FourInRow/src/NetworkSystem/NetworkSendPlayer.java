package NetworkSystem;

import SharedSystem.BlockQueue;
import java.io.IOException;

public class NetworkSendPlayer extends NetworkPlayer {
    
    public NetworkSendPlayer(int markerID, String name, NetworkManager manager) {
        super(markerID, name, manager);
    }

    @Override
    public int computeMove() throws InterruptedException, IOException {
        int move = BlockQueue.getInstance().take();
        manager.sendMove(move);
        return move;
    }
}
