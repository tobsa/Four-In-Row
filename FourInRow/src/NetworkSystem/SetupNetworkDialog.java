package NetworkSystem;

import SharedSystem.IConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SetupNetworkDialog extends JDialog implements IConstants {
    private JComboBox<String> jcbPlayer = new JComboBox();
    private JTextField jtfPlayer = new JTextField("Server", 10);
    private JTextField jtfIP     = new JTextField("localhost", 10);
    private JTextField jtfPort   = new JTextField("8080", 10);
    private int selection;
    
    public SetupNetworkDialog() {
        setLayout(new BorderLayout());
        
        jcbPlayer.addItem("Host");
        jcbPlayer.addItem("Client");
        jcbPlayer.addItemListener(new ComboBoxItemListener());
        
        jtfIP.setEnabled(false);
                
        JButton button1 = new JButton("OK");
        JButton button2 = new JButton("Cancel");
        button1.addActionListener(new ButtonOKListener());
        button2.addActionListener(new ButtonCancelListener());
        
        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(jcbPlayer);
        panel1.setBorder(new TitledBorder("Type"));
        
        JPanel panel2 = new JPanel();
        panel2.add(jtfIP);
        panel2.setBorder(new TitledBorder("IP"));
        
        JPanel panel3 = new JPanel();
        panel3.add(jtfPlayer);
        panel3.setBorder(new TitledBorder("Name"));
        
        JPanel panel4 = new JPanel();
        panel4.add(jtfPort);
        panel4.setBorder(new TitledBorder("Port"));
        
        JPanel panel5 = new JPanel(new GridLayout(0, 2));
        panel5.add(panel1);
        panel5.add(panel2);
        panel5.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panel6 = new JPanel(new GridLayout(0, 2));
        panel6.add(panel3);
        panel6.add(panel4);
        panel6.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        JPanel panel7 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel7.add(button1);
        panel7.add(button2);
        panel7.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        add(panel5, BorderLayout.NORTH);
        add(panel6, BorderLayout.CENTER);
        add(panel7, BorderLayout.SOUTH);
        
        pack();
        
        setTitle("Setup Network Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
        setVisible(true);
    }
    
    public int getPlayerType() {
        return jcbPlayer.getSelectedIndex();
    }
    
    public String getPlayerName() {
        return jtfPlayer.getText();
    }
    
    public String getIP() {
        return jtfIP.getText();
    }
    
    public int getPort() {
        return Integer.parseInt(jtfPort.getText());
    }
    
    public int getSelection() {
        return selection;
    }
    
    private class ButtonOKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SETUP_NETWORK_OK;
            dispose();
        }
    }
    
    private class ButtonCancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            selection = SETUP_NETWORK_CANCEL;
            dispose();
        }
    }
    
    private class ComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if(((String)event.getItem()).equals("Host")) {
                jtfIP.setEnabled(false);
                jtfPlayer.setText("Server");
            }
            else {
                jtfIP.setEnabled(true);
                jtfPlayer.setText("Client");
            }
        }
    }
}
