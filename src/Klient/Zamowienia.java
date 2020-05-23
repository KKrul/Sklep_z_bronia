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

public class Zamowienia extends JFrame{
  Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_zam=-1;
    int id_prod=-1;
    int id_pr = -1;
    int id_kl;
    Zamowienia(int id_kl){
        this.id_kl = id_kl;
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(930, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj zamówienie");
        i2 = new JMenuItem("Usuń zamówienie");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj zamówienie");
        
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
        i3.addActionListener(new menu(this,id_kl));
        i4.addActionListener(new Wyjscie());
        i5.addActionListener(new Aktualizuj(this));
        
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_produkt, data_zlozenia_zamowienia,czy_przyjeto_zamowienie,"
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia "
                    + "from zamowienia inner join produkty on produkty.id_produkt = zamowienia.id_produkt "
                    + "where id_klient = " + id_kl;
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_produkt"),wynik.getString("data_zlozenia_zamowienia"),wynik.getString("czy_przyjeto_zamowienie"),
                    wynik.getString("czy_zaplacono"),wynik.getString("czy_zrealizowano_zamowienie"),
                    wynik.getString("data_realizacji_zamowienia")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Produkt","Data złożenia zamówienia","Czy przyjęto zamówienie",
            "Czy zapłacono","Czy zrealizowano zamówienie","Data realizacji zamówienia"};
                       
            JTable jtab = new JTable(arr,clm);
            jtab.setEnabled(false);
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(150);
            clmtab.getColumn(1).setPreferredWidth(160);
            clmtab.getColumn(2).setPreferredWidth(160);
            clmtab.getColumn(3).setPreferredWidth(120);
            clmtab.getColumn(4).setPreferredWidth(180);
            clmtab.getColumn(5).setPreferredWidth(160); 
            
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
        JLabel prod;
        JButton prodbtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setVisible(true);
            setSize(400,150);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(3,2));
            setLocationRelativeTo(null);
            
            prod = new JLabel("Produkt", SwingConstants.CENTER);
            
            prodbtn = new JButton("Wybierz produkt");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(prod);add(prodbtn);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            prod.setBorder(BorderFactory.createLineBorder(Color.black));
            
            prodbtn.addActionListener(new WybierzProdukt());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this,id_kl));
            
        }
        
        class Insert implements ActionListener{
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               if(id_prod == -1) {
                   JOptionPane.showConfirmDialog(null, "Produkt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_prod != -1) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into zamowienia values"
                    + "("+ id_kl +",null,"+id_prod+",null,null,null,null,null)";
            
            ask.addBatch(sql);
            ask.executeBatch();  
            
            id_prod = -1;
            
            ask.close();
            con1.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}}
                  }
          }
    }
    
    class Aktualizuj extends JFrame implements ActionListener {
        JFrame f;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz zamowienie");
            setLocationRelativeTo(null);
            setSize(930, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_produkt, data_zlozenia_zamowienia,czy_przyjeto_zamowienie,"
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia "
                    + "from zamowienia inner join produkty on produkty.id_produkt = zamowienia.id_produkt "
                    + "where id_klient = " + id_kl + "  and czy_zaplacono='nie' and czy_zrealizowano_zamowienie = 'nie'";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_produkt"),wynik.getString("data_zlozenia_zamowienia"),wynik.getString("czy_przyjeto_zamowienie"),
                    wynik.getString("czy_zaplacono"),wynik.getString("czy_zrealizowano_zamowienie"),
                    wynik.getString("data_realizacji_zamowienia")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Produkt","Data złożenia zamówienia","Czy przyjęto zamówienie",
            "Czy zapłacono","Czy zrealizowano zamówienie","Data realizacji zamówienia"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
                      
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(150);
            clmtab.getColumn(1).setPreferredWidth(160);
            clmtab.getColumn(2).setPreferredWidth(160);
            clmtab.getColumn(3).setPreferredWidth(120);
            clmtab.getColumn(4).setPreferredWidth(180);
            clmtab.getColumn(5).setPreferredWidth(160); 
            
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
                String sql = "select id_zamowienie, zamowienia.id_produkt from zamowienia inner join "
                        + "produkty on produkty.id_produkt = zamowienia.id_produkt "
                        + "where id_klient = " + id_kl + "  and czy_zaplacono='nie' "
                        + "and czy_zrealizowano_zamowienie = 'nie'";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_zam = Integer.parseInt(wynik.getString(1));
                        id_prod = Integer.parseInt(wynik.getString(2));
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujZamowienie();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(400,150);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(3,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujZamowienie extends JFrame{
            JLabel prod;
            JButton prodbtn,upd,exit,powrot,menu;
            AktualizujZamowienie(){
                prod = new JLabel("Produkt", SwingConstants.CENTER);
                
                prodbtn = new JButton("Wybierz produkt");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(prod);add(prodbtn);
            
                add(upd);add(powrot);add(menu);add(exit);

                prod.setBorder(BorderFactory.createLineBorder(Color.black));
                               
                prodbtn.addActionListener(new WybierzProdukt());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this,id_kl));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
         Connection con1;
               
               if(id_prod == -1) {
                   JOptionPane.showConfirmDialog(null, "Produkt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_prod != -1) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update zamowienia set id_produkt = "+ id_prod
                    + " where id_zamowienie = "+id_zam;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            id_prod = -1;
            f.dispose();
            new Zamowienia(id_kl);
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
        Usun(JFrame f) {
            this.f = f;
        }
        Usun(){}
        public void actionPerformed(ActionEvent e){
            List<String[]> list = new ArrayList<String[]>();
            
            if(f != null)
                f.dispose();
            
            setTitle("Wybierz zamówienie");
            setLocationRelativeTo(null);
            setSize(930, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Zamówienia\"");
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
            i3.addActionListener(new menu(this,id_kl));
            i4.addActionListener(new Wyjscie());
            
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_produkt, data_zlozenia_zamowienia,czy_przyjeto_zamowienie,"
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia "
                    + "from zamowienia inner join produkty on produkty.id_produkt = zamowienia.id_produkt "
                    + "where id_klient = " + id_kl + "  and czy_zaplacono='nie' and czy_zrealizowano_zamowienie = 'nie'";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_produkt"),wynik.getString("data_zlozenia_zamowienia"),wynik.getString("czy_przyjeto_zamowienie"),
                    wynik.getString("czy_zaplacono"),wynik.getString("czy_zrealizowano_zamowienie"),
                    wynik.getString("data_realizacji_zamowienia")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Produkt","Data złożenia zamówienia","Czy przyjęto zamówienie",
            "Czy zapłacono","Czy zrealizowano zamówienie","Data realizacji zamówienia"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
                      
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(150);
            clmtab.getColumn(1).setPreferredWidth(160);
            clmtab.getColumn(2).setPreferredWidth(160);
            clmtab.getColumn(3).setPreferredWidth(120);
            clmtab.getColumn(4).setPreferredWidth(180);
            clmtab.getColumn(5).setPreferredWidth(160); 
            
            JScrollPane sp = new JScrollPane(jtab);
            add(sp);
            
            
            con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
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
                String sql = "select id_zamowienie from zamowienia inner join produkty "
                        + "on produkty.id_produkt = zamowienia.id_produkt "
                        + "where id_klient = " + id_kl + "  and czy_zaplacono='nie' "
                        + "and czy_zrealizowano_zamowienie = 'nie'";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_zam = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete zamowienia where id_zamowienie = " + id_zam;
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
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);  }
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                }
            }
            }
            }
    
    
    class WybierzProdukt extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                List<String[]> list = new ArrayList<String[]>();
        
                setLocationRelativeTo(null);
                setSize(980, 200);
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_produkt,producent,model,kaliber,\"dlugosc[mm]\","
                    + "\"masa[g]\",\"dlugosc_lufy[mm]\",pojemnosc_magazynka,"
                    + "cena,\"stan[szt.]\" from produkty";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_produkt"),wynik.getString("producent"),
                    wynik.getString("model"),wynik.getString("kaliber"),
                    wynik.getString("dlugosc[mm]"),wynik.getString("masa[g]"),
                    wynik.getString("dlugosc_lufy[mm]"),wynik.getString("pojemnosc_magazynka"),
                    wynik.getString("cena"), wynik.getString("stan[szt.]"),};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwa produktu","Producent","Model","Kaliber","Długość[mm]",
                    "Masa[g]","Długość lufy[mm]", "Pojemność magazynka","Cena","Stan[szt.]"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
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
                String sql = "select id_produkt from produkty";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_prod = Integer.parseInt(wynik.getString(1));
                        break;}
                
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 dispose();                
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
            new Zamowienia(id_kl);
        }
    }
}

