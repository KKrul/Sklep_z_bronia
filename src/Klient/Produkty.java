package Klient;

import AStart.Wyjscie;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

public class Produkty extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i3,i4;
    int id_kl = -1;
    Produkty(int id_kl){
        this.id_kl = id_kl;
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(1180, 200);
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
            String sql = "select nazwa_produkt,producent,model,kaliber,\"dlugosc[mm]\","
                    + "\"masa[g]\",\"dlugosc_lufy[mm]\",pojemnosc_magazynka,"
                    + "cena,\"stan[szt.]\",nazwa_kategoria from produkty "
                    + "inner join kategorie on kategorie.id_kategoria = produkty.id_kategoria";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_produkt"),wynik.getString("producent"),
                    wynik.getString("model"),wynik.getString("kaliber"),
                    wynik.getString("dlugosc[mm]"),wynik.getString("masa[g]"),
                    wynik.getString("dlugosc_lufy[mm]"),wynik.getString("pojemnosc_magazynka"),
                    wynik.getString("cena"), wynik.getString("stan[szt.]"), wynik.getString("nazwa_kategoria")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwa produktu","Producent","Model","Kaliber","Długość[mm]",
                    "Masa[g]","Długość lufy[mm]", "Pojemność magazynka","Cena","Stan[szt.]","Kategoria"};
                       
            JTable jtab = new JTable(arr,clm);
            jtab.setEnabled(false);
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(150);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(100);
            clmtab.getColumn(3).setPreferredWidth(60);
            clmtab.getColumn(4).setPreferredWidth(100);
            clmtab.getColumn(5).setPreferredWidth(70);
            clmtab.getColumn(6).setPreferredWidth(110);
            clmtab.getColumn(7).setPreferredWidth(150);
            clmtab.getColumn(8).setPreferredWidth(80);
            clmtab.getColumn(9).setPreferredWidth(70);
            clmtab.getColumn(10).setPreferredWidth(200);
            
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
            new Produkty(id_kl);
        }
    }
}


