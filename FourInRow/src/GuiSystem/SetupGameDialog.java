package GuiSystem;

import SharedSystem.IConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SetupGameDialog extends JDialog implements IConstants {
    private JTextField jtfPlayer1 = new JTextField("Human", 10);
    private JTextField jtfPlayer2 = new JTextField("Computer", 10);
    private JComboBox<String> jcbPlayer1 = new JComboBox();
    private JComboBox<String> jcbPlayer2 = new JComboBox();
    private int selection;
    
    public SetupGameDialog() {
        setLayout(new BorderLayout());
        
        jcbPlayer1.addItem("Human");
        jcbPlayer1.addItem("Computer");
        jcbPlayer2.addItem("Human");
        jcbPlayer2.addItem("Computer");
        jcbPlayer2.setSelectedIndex(1);
        
        JButton button1 = new JButton("OK");
        JButton button2 = new JButton("CANCEL");
        button1.addActionListener(new ButtonOKListener());
        button2.addActionListener(new ButtonCancelListener());
        
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.add(jtfPlayer1);
        panel1.add(jcbPlayer1);
        panel1.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Player 1")));
        
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2.add(jtfPlayer2);
        panel2.add(jcbPlayer2);
        panel2.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 10, 10, 10), new TitledBorder("Player 2")));
        
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel3.add(button1);
        panel3.add(button2);
        panel3.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
        
        pack();
        
        setTitle("Setup Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
    
    public String getPlayer1Name() {
        return jtfPlayer1.getText();
    }
    
    public String getPlayer2Name() {
        return jtfPlayer2.getText();
    }
    
    public int getPlayer1Type() {
        return jcbPlayer1.getSelectedIndex();
    }
    
    public int getPlayer2Type() {
        return jcbPlayer2.getSelectedIndex();
    }
    
    public int getSelection() {
        return selection;
    }
    
    private class ButtonOKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SETUP_GAME_OK;
            dispose();
        }
    }
    
    private class ButtonCancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SETUP_GAME_CANCEL;
            dispose();
        }
    }
}
