package Klient;

import AStart.Wyjscie;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class Faktury extends JFrame{
Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i3,i4;
    int id_kl;
    Faktury(int id_kl){
        this.id_kl = id_kl;
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(840, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        
        menu.add(i3);
        menu.add(i4);
        menu.setForeground(Color.white);
        
        mb.add(menu);
        mb.setBackground(Color.black);
        setJMenuBar(mb);
        
        i3.addActionListener(new menu(this,id_kl));
        i4.addActionListener(new Wyjscie());
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select data_wystawienia,data_sprzedazy,procent_VAT,"
                    + "wartosc_VAT,cena_netto,cena_brutto,miejsce_wystawienia,"
                    + "sposob_zaplaty from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_klient = " + id_kl;
            
            ResultSet wynik = ask.executeQuery(sql);
            wynik.getMetaData();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("data_wystawienia"),wynik.getString("data_sprzedazy"),
                    wynik.getString("procent_VAT"),wynik.getString("wartosc_VAT"),
                    wynik.getString("cena_netto"),wynik.getString("cena_brutto"),
                    wynik.getString("miejsce_wystawienia"), wynik.getString("sposob_zaplaty")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Data wystawienia","Data sprzedaży","VAT[%]",
                "Wartość VAT", "Cena netto", "Cena brutto", "Miejsce wystawienia", 
                "Sposób płatności"};
                       
            JTable jtab = new JTable(arr,clm);
            jtab.setEnabled(false);
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(140);
            clmtab.getColumn(1).setPreferredWidth(140);
            clmtab.getColumn(2).setPreferredWidth(50);
            clmtab.getColumn(3).setPreferredWidth(90);
            clmtab.getColumn(4).setPreferredWidth(90);
            clmtab.getColumn(5).setPreferredWidth(90);
            clmtab.getColumn(6).setPreferredWidth(130);
            clmtab.getColumn(7).setPreferredWidth(110);
            
            JScrollPane sp = new JScrollPane(jtab);
            add(sp);
            
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
        }
   
    class Powrot implements ActionListener{
        JFrame f;
        Powrot(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            f.dispose();
            new Faktury(id_kl);
        }
    }
}


