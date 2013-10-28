package GuiSystem;

import GameSystem.GameManager;
import NetworkSystem.NetworkManager;
import NetworkSystem.SetupNetworkDialog;
import SharedSystem.BlockQueue;
import SharedSystem.IConstants;
import SharedSystem.IGMListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class GameFrame extends JFrame implements IConstants, IGMListener {
    private NetworkManager networkManager = new NetworkManager();
    private GameManager gameManager = new GameManager(networkManager);
    private GameBoard gameBoard = new GameBoard();
    private Thread gameThread = new Thread(gameManager);
    private JLabel label = new JLabel();
    
    public GameFrame() {
        setLayout(new BorderLayout());
        
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(Color.DARK_GRAY);
        
        JButton button1 = new JButton("New Game");
        JButton button2 = new JButton("Network Game");
        JButton button3 = new JButton("Highscore");
        JButton button4 = new JButton("Exit Game");
        button1.addActionListener(new ButtonNewGameListener());
        button2.addActionListener(new ButtonNetworkGameListener());
        button3.addActionListener(new ButtonHighscoreListener());
        button4.addActionListener(new ButtonExitGameListener());
        
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.add(button4);
        panel1.add(label);
        panel1.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 0, 10), new EtchedBorder()));
        
        JPanel panel2 = new JPanel();
        panel2.add(gameBoard);
        panel2.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 10, 10, 10), new EtchedBorder()));
        
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        
        pack();
    }
    
    private void initializeGame() {
        gameThread.interrupt();
        gameManager.clearGrid();
        gameManager.clearIGGListeners();
        gameManager.clearIGMListeners();
        gameManager.registerIGGListener(gameBoard);
        gameManager.registerIGMListener(this);
        networkManager.closeConnection();
        BlockQueue.getInstance().clear();
    }
    
    private void executeGame() {
        gameThread = new Thread(gameManager);
        gameThread.start();
    }
    
    private void executeNetworkGame(final String name, final int port) {       
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerAcceptFrame frame = new ServerAcceptFrame(GameFrame.this, networkManager);
                
                int result = networkManager.createServer(name, port);
                if(result == NETWORK_SERVER_CREATE){
                    gameManager.createNetworkPlayer1(PLAYER_NETWORK_SEND, networkManager.getServerName());
                    gameManager.createNetworkPlayer2(PLAYER_NETWORK_READ, networkManager.getClientName());

                    gameThread = new Thread(gameManager);
                    gameThread.start();
                }
                
                frame.dispose();
            }
        }).start();
    }

    @Override
    public void updateResult(int result, String name1, String name2) {
        switch(result) {
            case PLAYER_1_WON:
                new WinnerDialog(this, name1);
                break;
            case PLAYER_2_WON:
                new WinnerDialog(this, name2);
                break;
            case DRAW:
                new DrawDialog(this, name1, name2);
                break;
        }
    }

    @Override
    public void updatePlayerTurn(String name) {
        label.setText("Turn: " + name);
    }
    
    @Override 
    public void updateInvalidMove(String name) {
        new MessageDialog(this, name + " tried to make an invalid move!");
    }
    
    @Override
    public void updateLostConnection() {
        networkManager.closeConnection();
        new MessageDialog(this, "Lost connection!");
    }
    
    private class ButtonNewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            gameManager.setPaused(true);
            
            SetupGameDialog dialog = new SetupGameDialog(GameFrame.this);
            if(dialog.getSelection() == SETUP_GAME_OK) {
                initializeGame();
                gameManager.createPlayer1(dialog.getPlayer1Type(), dialog.getPlayer1Name());
                gameManager.createPlayer2(dialog.getPlayer2Type(), dialog.getPlayer2Name());
                executeGame();
            }
            
            gameManager.setPaused(false);
        }
    }
    
    private class ButtonNetworkGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            gameManager.setPaused(true);
            
            SetupNetworkDialog dialog = new SetupNetworkDialog(GameFrame.this);
            if(dialog.getSelection() == SETUP_NETWORK_OK) {
                initializeGame();
               
                if(dialog.getPlayerType() == HOST) {                    
                    if(networkManager.isPortAvailable(dialog.getPort())) {
                        executeNetworkGame(dialog.getPlayerName(), dialog.getPort());
                    }
                    else 
                        new MessageDialog(GameFrame.this, "Port already in use!");
                }
                else {
                    if(networkManager.createClient(dialog.getPlayerName(), dialog.getIP(), dialog.getPort())) {
                        gameManager.createNetworkPlayer1(PLAYER_NETWORK_READ, networkManager.getServerName());
                        gameManager.createNetworkPlayer2(PLAYER_NETWORK_SEND, networkManager.getClientName());
                        executeGame();
                    }
                    else 
                        new MessageDialog(GameFrame.this, "Couldn't connect to server!");
                }
            }
             
            gameManager.setPaused(false);
        }
    }
    
    private class ButtonHighscoreListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            
        }
    }
    
    private class ButtonExitGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }
}


