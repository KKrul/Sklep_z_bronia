package AStart;


import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import Admin.Admin;
import Klient.AKlient;
import Pracownik.APracownik;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class AStart extends JFrame implements ItemListener{
    JButton btn_pr, btn_kl;
    JLabel login_pr,haslo_pr,login_kl,haslo_kl;
    JTextField logintf_pr, logintf_kl;
    JPasswordField haslopf_pr, haslopf_kl;
    JPanel cards,card1,card2,combobox;
    
    public AStart(){
        login_pr = new JLabel("Login:",SwingConstants.LEFT);
        haslo_pr = new JLabel("Haslo:",SwingConstants.LEFT);
        login_kl = new JLabel("Login:",SwingConstants.LEFT);
        haslo_kl = new JLabel("Haslo:",SwingConstants.LEFT);
        
        logintf_pr = new JTextField(20);
        logintf_kl = new JTextField(20);
        
        haslopf_pr = new JPasswordField(20);
        haslopf_kl = new JPasswordField(20);
        haslopf_pr.setEchoChar('*');
        haslopf_kl.setEchoChar('*');
        
        btn_pr = new JButton("Zaloguj");
        btn_kl = new JButton("Zaloguj");
        
        cards = new JPanel(new CardLayout());
        
        card1 = new JPanel(new GridLayout(5,1));
        card2 = new JPanel(new GridLayout(5,1));
        cards.add(card1,"Pracownik");
        cards.add(card2,"Klient");
        
        combobox = new JPanel();
        String[] cbitems = {"Pracownik","Klient"};
        JComboBox cb = new JComboBox(cbitems);
        cb.addItemListener(this);
        combobox.add(cb);
        
        add(combobox, BorderLayout.PAGE_START);
        add(cards, BorderLayout.CENTER);
        
        card1.add(login_pr);
        card1.add(logintf_pr);
        card1.add(haslo_pr);
        card1.add(haslopf_pr);
        card1.add(btn_pr);
        
        card2.add(login_kl);
        card2.add(logintf_kl);
        card2.add(haslo_kl);
        card2.add(haslopf_kl);
        card2.add(btn_kl);
        
        btn_kl.addActionListener(new ZalogujKlient());
        btn_pr.addActionListener(new ZalogujPracownik());
        getRootPane().setDefaultButton(btn_pr);
        
    }
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
    
    class ZalogujPracownik implements ActionListener{
        Connection con;
        public void actionPerformed(ActionEvent e){
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select id_pracownik,login_pr,haslo_pr,status from pracownicy ";
            
            ResultSet wynik = ask.executeQuery(sql);
            String log = "";
            String has = "";
            int id_pr = -1;
            char[] pass = haslopf_pr.getPassword();
            while(wynik.next()) {
                log = wynik.getString("login_pr");
                has = wynik.getString("haslo_pr");
                id_pr = Integer.parseInt(wynik.getString("id_pracownik"));
                if(logintf_pr.getText().equals(log)){
                    if (Arrays.toString(pass).equals(Arrays.toString(has.toCharArray()))
                    && wynik.getString("status").equals("admin")){
                    dispose();
                    new Admin();
                    break;
                    }
                    else if(Arrays.toString(pass).equals(Arrays.toString(has.toCharArray()))
                    && wynik.getString("status").equals("pracownik")){
                    dispose();
                    new APracownik(id_pr);
                    break;
                    }
                }
            }
            if(logintf_pr.getText().equals(log) == false || 
                    Arrays.toString(pass).equals(Arrays.toString(has.toCharArray())) == false)
                JOptionPane.showConfirmDialog(null, "Błędny login lub hasło",
                        "", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            ask.close();
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika" + error_sterownik);}
        }
    }
    
    class ZalogujKlient implements ActionListener {
        Connection con;
            public void actionPerformed(ActionEvent e){
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select id_klient,login_kl,haslo_kl from klienci ";
            
            ResultSet wynik = ask.executeQuery(sql);
            String log = "";
            String has = "";
            char pass[] = haslopf_kl.getPassword();
            int id_kl;
            
            while(wynik.next()) {
                id_kl = Integer.parseInt(wynik.getString("id_klient"));
                log = wynik.getString("login_kl");
                has = wynik.getString("haslo_kl");
                if(logintf_kl.getText().equals(log))
                    if(Arrays.toString(pass).equals(Arrays.toString(has.toCharArray()))){
                    dispose();
                    new AKlient(id_kl);
                    break;
                }
            }
            
            
            if(logintf_kl.getText().equals(log) == false || 
                    Arrays.toString(pass).equals(Arrays.toString(has.toCharArray())) == false)
                JOptionPane.showConfirmDialog(null, "Błędny login lub hasło",
                        "", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            
            ask.close();
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}    
    }
   }
    
    
    public static void main(String[] args){
        JFrame f = new AStart();
        f.setVisible(true);
        f.setSize(300,200);
        f.setTitle("Logowanie");
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
