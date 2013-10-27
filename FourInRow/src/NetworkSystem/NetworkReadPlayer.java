package NetworkSystem;

import java.io.IOException;

public class NetworkReadPlayer extends NetworkPlayer {
    
    public NetworkReadPlayer(int markerID, String name, NetworkManager manager) {
        super(markerID, name, manager);
    }

    @Override
    public int computeMove() throws InterruptedException, IOException {
        return manager.readMove();
    }
}
