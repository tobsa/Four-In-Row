package NetworkSystem;

import GameSystem.Player;

public abstract class NetworkPlayer extends Player {
    protected NetworkManager manager;
    
    public NetworkPlayer(int markerID, String name, NetworkManager manager) {
        super(markerID, name);
        this.manager = manager;
    }
}
