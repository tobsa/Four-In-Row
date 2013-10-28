package DatabaseSystem;

import SharedSystem.IConstants;
import SharedSystem.IGMListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database implements IGMListener, IConstants, Serializable {
    private List<Map.Entry<String, Integer>> wins   = new ArrayList();
    private List<Map.Entry<String, Integer>> losses = new ArrayList();
    private List<Map.Entry<String, Integer>> draws  = new ArrayList();
    
    public Database() {
        try {
            new File("data/highscores.dat").createNewFile();          
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        load();
    }
    
    public List<Map.Entry<String, Integer>> getWins() {
        return wins;
    }
    
    public List<Map.Entry<String, Integer>> getLosses() {
        return losses;
    }
    
    public List<Map.Entry<String, Integer>> getDraws() {
        return draws;
    }
    
    private void save() {        
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("data/highscores.dat"));
            output.writeObject(wins);
            output.writeObject(losses);
            output.writeObject(draws);
            output.close();
        } catch (IOException ex) {
        }
    }
    
    private void load() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("data/highscores.dat"));
            wins    = (List<Map.Entry<String, Integer>>)input.readObject();
            losses  = (List<Map.Entry<String, Integer>>)input.readObject();
            draws   = (List<Map.Entry<String, Integer>>)input.readObject();
            input.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        
        while(wins.size() > HIGHSCORE_MAX_ROWS) removeLowest(wins);
        while(wins.size() > HIGHSCORE_MAX_ROWS) removeLowest(losses);
        while(wins.size() > HIGHSCORE_MAX_ROWS) removeLowest(draws);
        
        save();
    }
    
    private void updateWinScore(String name) {
        updateScore(wins, name);
        removeLowest(wins);
        
        save();
    }
    
    private void updateLoseScore(String name) {
        updateScore(losses, name);
        removeLowest(losses);
        save();
    }
    
    private void updateDrawScore(String name1, String name2) {
        updateScore(draws, name1);
        updateScore(draws, name2);
        removeLowest(draws);
        save();
    }
    
    private int find(List<Map.Entry<String, Integer>> entries, String name) {
        int index = -1;
        
        for(int i = 0; i < entries.size(); i++) {
            if(entries.get(i).getKey().equals(name)) {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    private void updateScore(List<Map.Entry<String, Integer>> entries, String name) {
        int index = find(entries, name);     
        if(index == -1)          
            entries.add(new AbstractMap.SimpleEntry<>(name, 1));
        else
            entries.get(index).setValue(entries.get(index).getValue() + 1);
    }
    
    private void removeLowest(List<Map.Entry<String, Integer>> entries) {
        if(entries.size() <= HIGHSCORE_MAX_ROWS)
            return;
        
        int lowest = 0;
        for(int i = 0; i < entries.size(); i++) {
            if(entries.get(i).getValue() < entries.get(lowest).getValue())
                lowest = i;
        }
        
        entries.remove(lowest);
    }
    
    @Override
    public void updateResult(int result, String name1, String name2) {
        switch(result) {
            case PLAYER_1_WON:
                updateWinScore(name1);
                updateLoseScore(name2);
                break;
            case PLAYER_2_WON:
                updateWinScore(name2);
                updateLoseScore(name1);
                break;
            case DRAW:
                updateDrawScore(name1, name2);
        }
        
        save();
    }

    @Override
    public void updatePlayerTurn(String name) {
    }

    @Override
    public void updateInvalidMove(String name) {
    }

    @Override
    public void updateLostConnection() {
    }
}
