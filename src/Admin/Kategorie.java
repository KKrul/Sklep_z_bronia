package Admin;

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

public class Kategorie extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_kat = -1;
    
    Kategorie(){
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(400, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj kategorię");
        i2 = new JMenuItem("Usuń kategorię");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj kategorię");
        
        menu.add(i1);
        menu.add(i5);
        menu.add(i2);
        menu.add(i3);
        menu.add(i4);
        menu.setForeground(Color.white);
        
        mb.add(menu);
        mb.setBackground(Color.black);
        setJMenuBar(mb);
        
        i1.addActionListener(new Dodaj(this));
        i2.addActionListener(new Usun(this));
        i3.addActionListener(new menu(this));
        i4.addActionListener(new Wyjscie());
        i5.addActionListener(new Aktualizuj(this)); 
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_kategoria from kategorie";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_kategoria")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            
            String[] clm = {"Kategoria"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.setEnabled(false);
            JScrollPane sp = new JScrollPane(jtab);
            add(sp);
            
            
            con.close();
            }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
        }
    
class Dodaj extends JFrame implements ActionListener{
        JFrame f;
        Dodaj(JFrame f) {
            this.f = f;
        }
        JLabel kat;
        JTextField kattf;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(400,150);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(3,2));
            setLocationRelativeTo(null);
            
            kat = new JLabel("*Nazwa kategorii", SwingConstants.CENTER);
            
            kattf = new JTextField(50);
            
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(kat);add(kattf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            kat.setBorder(BorderFactory.createLineBorder(Color.black));
                   
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this));
        }
        
        class Insert implements ActionListener{
         String kat;
         Pattern pkat;
         Matcher mkat;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               kat = kattf.getText().toLowerCase();
               
               pkat = Pattern.compile("[A-Za-ząćęłńóśźż]+(\\s[A-Za-ząćęłńóśźż]+)?");
               
               mkat = pkat.matcher(kat);
               
               if(mkat.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa kategorii\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               else{
                   kat = kat.substring(0, 1).toUpperCase() + kat.substring(1).toLowerCase();
         try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into kategorie values"
                    + "('"+ kat +"')";
            
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            
            kattf.setText("");
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}  }
          }
        }
    }

    
    
    class Aktualizuj extends JFrame implements ActionListener {
        JFrame f;
        String skat;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz kategorię");
            setLocationRelativeTo(null);
            setSize(400,150);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_kategoria from kategorie";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_kategoria")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            
            String[] clm = {"Kategoria"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            JScrollPane sp = new JScrollPane(jtab);
            add(sp);
            
            
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
        
            }
        
        class click extends MouseAdapter{
            JTable jtab;
            int row;
            Connection con1;
            click(JTable jtab){
            this.jtab = jtab;
            }
            public void mouseClicked(MouseEvent e){
                     row = jtab.getSelectedRow() + 1;
                     try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con1 = DriverManager.getConnection("jdbc:sqlserver://"
                        + "localhost:1433;databaseName=bd_java;" +
                        "user=kkrol;password=2@sql2@;");
                Statement ask = con1.createStatement();
                String sql = "select * from kategorie";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kat = Integer.parseInt(wynik.getString(1));
                        skat = wynik.getString(2); 
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujKategoria();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(400,150);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(3,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujKategoria extends JFrame{
            JLabel kat;
            JTextField kattf;
            JButton upd,exit,powrot,menu;
            AktualizujKategoria(){
                

                kat = new JLabel("*Nazwa kategorii", SwingConstants.CENTER);
            
                kattf = new JTextField(skat,50);

                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(kat);add(kattf);

                add(upd);add(powrot);add(menu);add(exit);

                kat.setBorder(BorderFactory.createLineBorder(Color.black));

                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this));
                
            }
        
        
        class Update implements ActionListener{
        JFrame f;
        Pattern pkat;
        Matcher mkat;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String kat;
               Connection con1;
               kat = kattf.getText().toLowerCase();  
               
               pkat = Pattern.compile("[A-Za-ząćęłńóśźż]+(\\s[A-Za-ząćęłńóśźż]+)?");
               
               mkat = pkat.matcher(kat);
               
               if(mkat.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa kategorii\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               else{
                   kat = kat.substring(0, 1).toUpperCase() + kat.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update kategorie set nazwa_kategoria = '"+ kat +"' "
                    + "where id_kategoria = "+id_kat;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            f.dispose();
            new Kategorie();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}}
        }
        
        }
    }
    }
    
    class Usun extends JFrame implements ActionListener {
        JFrame f;
        JMenu menu;
        JMenuBar mb;
        JMenuItem i3,i4;
        Usun(JFrame f) {
            this.f = f;
        }
        Usun(){}
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            if(f != null)
                f.dispose();
            
            setTitle("Wybierz kategorię");
            setLocationRelativeTo(null);
            setSize(400,150);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Kategorie\"");
            i3 = new JMenuItem("Powrót do menu");
            i4 = new JMenuItem("Wyjście");

            menu.add(i2);
            menu.add(i3);
            menu.add(i4);
            menu.setForeground(Color.white);

            mb.add(menu);
            mb.setBackground(Color.black);
            setJMenuBar(mb);
            
            i2.addActionListener(new Powrot(this));
            i3.addActionListener(new menu(this));
            i4.addActionListener(new Wyjscie());
            
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_kategoria from kategorie";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_kategoria")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            
            String[] clm = {"Kategoria"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            JScrollPane sp = new JScrollPane(jtab);
            add(sp);
            
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie); }
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
        
            }
        
        class click extends MouseAdapter implements ActionListener{
            JTable jtab;
            JButton yes,no;
            int row;
            Connection con1;
            click(JTable jtab){
                this.jtab = jtab;
            }
            public void mouseClicked(MouseEvent e) {
                row = jtab.getSelectedRow()+1;
                yes = new JButton("Tak");
                no = new JButton("Nie");
                
                yes.addActionListener(new Usun());
                yes.addActionListener(this);
                no.addActionListener(this);
                Object opt[] = {yes,no};
                
                JOptionPane.showOptionDialog(null,"Czy na pewno chcesz usunąć ten element?",
                        "",JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,opt,opt[0]);
            }
            
            public void actionPerformed(ActionEvent e){
                Window w = SwingUtilities.getWindowAncestor(no);
                w.dispose();
                if(e.getSource().equals(yes)){
                    try{
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con1 = DriverManager.getConnection("jdbc:sqlserver://"
                        + "localhost:1433;databaseName=bd_java;" +
                        "user=kkrol;password=2@sql2@;");
                Statement ask = con1.createStatement();
                String sql = "select id_kategoria from kategorie";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kat = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete kategorie where id_kategoria = " + id_kat;
                ask2.addBatch(sql2);
                ask2.executeBatch();
                
                ask2.close();
                con1.close();
                dispose();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);
            dispose();
            JOptionPane.showConfirmDialog(null, "Nie udało się usunąć elementu!", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                }
            }
            }
            }
    
    class Powrot implements ActionListener{
        JFrame f;
        Powrot(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            f.dispose();
            new Kategorie();
        }
    }
}

