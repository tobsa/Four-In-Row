package GameSystem;

import NetworkSystem.NetworkManager;
import NetworkSystem.NetworkReadPlayer;
import NetworkSystem.NetworkSendPlayer;
import SharedSystem.IConstants;
import SharedSystem.IGGListener;
import SharedSystem.IGMListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements IConstants, Runnable {
    private GameGrid gameGrid = new GameGrid();
    private Player player1;
    private Player player2;
    private boolean paused = false;
    private List<IGMListener> listeners = new ArrayList();
    private NetworkManager networkManager;
    
    public GameManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    public void createPlayer1(int type, String name) {
        switch(type) {
            case PLAYER_HUMAN:
                player1 = new Human(PLAYER_1, name);
                break;
            case PLAYER_COMPUTER:
                player1 = new Computer(PLAYER_1, name);
                registerIGGListener((Computer)player1);
                break;
        }
    }
    
    public void createPlayer2(int type, String name) {
        switch(type) {
            case PLAYER_HUMAN:
                player2 = new Human(PLAYER_2, name);
                break;
            case PLAYER_COMPUTER:
                player2 = new Computer(PLAYER_2, name);
                registerIGGListener((Computer)player2);
                break;
        }
    }
    
    public void createNetworkPlayer1(int type, String name) {
        switch(type) {
            case PLAYER_NETWORK_SEND:
                player1 = new NetworkSendPlayer(PLAYER_1, name, networkManager);
                break;
            case PLAYER_NETWORK_READ:
                player1 = new NetworkReadPlayer(PLAYER_1, name, networkManager);
                break;
        }
    }
    
    public void createNetworkPlayer2(int type, String name) {
        switch(type) {
            case PLAYER_NETWORK_SEND:
                player2 = new NetworkSendPlayer(PLAYER_2, name, networkManager);
                break;
            case PLAYER_NETWORK_READ:
                player2 = new NetworkReadPlayer(PLAYER_2, name, networkManager);
                break;
        }
    }
        
    public void registerIGGListener(IGGListener listener) {
        gameGrid.registerListener(listener);
    }
    
    public void registerIGMListener(IGMListener listener) {
        listeners.add(listener);
    }
    
    public void clearIGGListeners() {
        gameGrid.clearListeners();
    }
    
    public void clearIGMListeners() {
        listeners.clear();
    }
    
    public void clearGrid() {
        gameGrid.clearGrid();
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    private boolean makeMove(Player player, int winner) throws InterruptedException {
        while(true) {        
            try {
                while(paused) Thread.sleep(100);
                
                for(IGMListener listener : listeners) 
                    listener.updatePlayerTurn(player.getName());
                
                gameGrid.addMarker(player.computeMove(), player.getMarkerID());
                
                int result = gameGrid.checkResult();
                if(result == winner || result == DRAW) {
                    for(IGMListener listener : listeners)
                        listener.updateResult(result, player1.getName(), player2.getName());

                    return true;
                }
                
                return false;
            } catch (InvalidMoveException ex) {
                for(IGMListener listener : listeners) 
                    listener.updateInvalidMove(player.getName());
            } catch (IOException ex) {
                for(IGMListener listener : listeners) 
                    listener.updateLostConnection();
                return true;
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(makeMove(player1, PLAYER_1_WON))
                    break;   
                if(makeMove(player2, PLAYER_2_WON))
                    break;            
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}
