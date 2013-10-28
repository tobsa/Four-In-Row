package GuiSystem;

import DatabaseSystem.Database;
import SharedSystem.IConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class HighscoreDialog extends JDialog implements IConstants {
    
    public HighscoreDialog(JFrame parent, Database database) {
        setLayout(new BorderLayout());
        
        List<Map.Entry<String, Integer>> wins   = database.getWins();
        List<Map.Entry<String, Integer>> losses = database.getLosses();
        List<Map.Entry<String, Integer>> draws  = database.getDraws();
         
        Collections.sort(wins,   Collections.reverseOrder(new MapEntryComparator()));
        Collections.sort(losses, Collections.reverseOrder(new MapEntryComparator()));
        Collections.sort(draws,  Collections.reverseOrder(new MapEntryComparator()));
        
        JTable table1 = new JTable(getModel(wins, "Wins"));
        JTable table2 = new JTable(getModel(losses, "Losses"));
        JTable table3 = new JTable(getModel(draws, "Draws"));
        
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(table1.getTableHeader(), BorderLayout.NORTH);
        panel1.add(table1, BorderLayout.CENTER);
        panel1.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 20, 20, 0), new EtchedBorder()));
                
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(table2.getTableHeader(), BorderLayout.NORTH);
        panel2.add(table2, BorderLayout.CENTER);
        panel2.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 0, 20, 0), new EtchedBorder()));
        
        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(table3.getTableHeader(), BorderLayout.NORTH);
        panel3.add(table3, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 0, 20, 20), new EtchedBorder()));
        
        JLabel label = new JLabel("Highscores");
        label.setFont(new Font("Arial", Font.PLAIN, 28));
        
        JButton button = new JButton("OK");
        button.addActionListener(new ButtonOKListener());
        
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel4.add(label);
        
        JPanel panel5 = new JPanel(new GridLayout(0, 3, 50, 0));
        panel5.add(panel1);
        panel5.add(panel2);
        panel5.add(panel3);
        panel5.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 20, 10, 20), new EtchedBorder()));
        
        JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel6.add(button);
        
        add(panel4, BorderLayout.NORTH);
        add(panel5, BorderLayout.CENTER);
        add(panel6, BorderLayout.SOUTH);
        
        pack();
        
        setTitle("Highscores!");
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setVisible(true);
    }
    
    private DefaultTableModel getModel(List<Map.Entry<String, Integer>> entries, String attribute) {
        Object[][] rowData = new Object[entries.size()][2];
        for(int i = 0; i < entries.size(); i++) {
            rowData[i][0] = entries.get(i).getKey();
            rowData[i][1] = entries.get(i).getValue();
        }
        
        DefaultTableModel model = new DefaultTableModel(rowData, new String[]{"Name", attribute});
        
        for(int i = 0; i < HIGHSCORE_MAX_ROWS - entries.size(); i++)      
            model.addRow(new Object[]{null, null, null});
        
        return model;
    }
    
    private class MapEntryComparator implements Comparator<Map.Entry<String, Integer>> {      
        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }
    
    private class ButtonOKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            dispose();
        }
    }
}
