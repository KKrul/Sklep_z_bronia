package Pracownik;

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
    int id_kl=-1;
    int id_prod=-1;
    int id_pr;
    Zamowienia(int id_pr){
        this.id_pr = id_pr;
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(780, 200);
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
        i3.addActionListener(new menu(this,id_pr));
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
        JLabel kl,prod,data_zl,czy_p,czy_zap,czy_zr,data_re;
        JTextField data_zltf,czy_ptf,czy_zaptf,czy_zrtf,data_retf;
        JButton klbtn,prodbtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setVisible(true);
            setSize(700,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(9,2));
            setLocationRelativeTo(null);
            
            prod = new JLabel("Produkt", SwingConstants.CENTER);
            kl = new JLabel("Klient", SwingConstants.CENTER);
            data_zl = new JLabel("Data złożenia(YYYY-MM-DD gg:mm:ss)", SwingConstants.CENTER);
            czy_p = new JLabel("Czy przyjęto(tak/nie)", SwingConstants.CENTER);
            czy_zap = new JLabel("Czy zapłacono(tak/nie)", SwingConstants.CENTER); 
            czy_zr = new JLabel("Czy zrealizowano(tak/nie)", SwingConstants.CENTER); 
            data_re = new JLabel("Data realizacji(YYYY-MM-DD gg:mm:ss)", SwingConstants.CENTER);
            
            data_zltf = new JTextField(30);
            czy_ptf = new JTextField(3);
            czy_zaptf = new JTextField(3);
            czy_zrtf = new JTextField(3);
            data_retf = new JTextField(30);
            
            prodbtn = new JButton("Wybierz produkt");
            klbtn = new JButton("Wybierz klienta");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(kl);add(klbtn);add(prod);add(prodbtn);add(data_zl);add(data_zltf);
            add(czy_p);add(czy_ptf);add(czy_zap);add(czy_zaptf);
            add(czy_zr);add(czy_zrtf);add(data_re);add(data_retf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            prod.setBorder(BorderFactory.createLineBorder(Color.black));
            kl.setBorder(BorderFactory.createLineBorder(Color.black));
            data_zl.setBorder(BorderFactory.createLineBorder(Color.black));
            czy_p.setBorder(BorderFactory.createLineBorder(Color.black));
            czy_zap.setBorder(BorderFactory.createLineBorder(Color.black));
            czy_zr.setBorder(BorderFactory.createLineBorder(Color.black));
            data_re.setBorder(BorderFactory.createLineBorder(Color.black));
            
            prodbtn.addActionListener(new WybierzProdukt());
            klbtn.addActionListener(new WybierzKlient());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this,id_pr));
            
        }
        
        class Insert implements ActionListener{
         String data_zl,czy_p,czy_zap,czy_zr,data_re,data;
         Pattern pdata_zl,pczy_p,pczy_zap,pczy_zr,pdata_re;
         Matcher mdata_zl,mczy_p,mczy_zap,mczy_zr,mdata_re;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               data_zl = data_zltf.getText();
               czy_p = czy_ptf.getText().toLowerCase();
               czy_zap = czy_zaptf.getText().toLowerCase();
               czy_zr = czy_zrtf.getText().toLowerCase();
               data_re = data_retf.getText();
                         
               data = "2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?)?";
               pdata_zl = Pattern.compile(data);
               pdata_re = Pattern.compile("(2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?)?)?");
               pczy_p = Pattern.compile("tak|nie");
               pczy_zap = Pattern.compile("tak|nie");
               pczy_zr = Pattern.compile("tak|nie");
               
               mdata_zl = pdata_zl.matcher(data_zl);
               mdata_re = pdata_re.matcher(data_re);
               mczy_p = pczy_zr.matcher(czy_p);
               mczy_zap = pczy_zr.matcher(czy_zap);
               mczy_zr = pczy_zr.matcher(czy_zr);
               
               if(id_kl == -1) {
                   JOptionPane.showConfirmDialog(null, "Klient nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_prod == -1) {
                   JOptionPane.showConfirmDialog(null, "Produkt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mdata_zl.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data złożenia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdata_re.matches() == false && czy_zr.equals("nie"))
                       JOptionPane.showConfirmDialog(null, "Pole \"Data realizacji\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               else if(mdata_re.matches() == false && czy_zr.equals("tak"))
                       JOptionPane.showConfirmDialog(null, "Pole \"Data realizacji\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_p.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy przyjęto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_zap.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy zapłacono\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_zr.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy zrealizowano\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mdata_zl.matches() && ((pdata_re.matcher("").matches() && czy_zr.equals("nie"))  
                       || (pdata_zl.matcher(data_re).matches() && czy_zr.equals("tak")))
                       && mczy_p.matches() && mczy_zap.matches() && mczy_zr.matches() 
                       && id_kl != -1 && id_prod != -1) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into zamowienia values"
                    + "("+ id_kl +","+id_pr+","+id_prod+",'"+data_zl+"','"+czy_p+"','"+
                    czy_zap+"','"+czy_zr+"','"+data_re+"')";
            
            ask.addBatch(sql);
            ask.executeBatch();  
        
            data_zltf.setText("");
            data_retf.setText("");
            czy_ptf.setText("");
            czy_zaptf.setText("");
            czy_zrtf.setText("");
            id_kl = -1;
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
        String sdata_zl,sczy_p,sczy_zap,sczy_zr,sdata_re;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz zamowienie");
            setLocationRelativeTo(null);
            setSize(780, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select data_zlozenia_zamowienia,czy_przyjeto_zamowienie,"
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia from zamowienia "
                    + "where id_pracownik = " + id_pr + " or id_pracownik is null";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("data_zlozenia_zamowienia"),wynik.getString("czy_przyjeto_zamowienie"),
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
            
            String[] clm = {"Data złożenia zamówienia","Czy przyjęto zamówienie",
            "Czy zapłacono","Czy zrealizowano zamówienie","Data realizacji zamówienia"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
                      
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(160);
            clmtab.getColumn(1).setPreferredWidth(160);
            clmtab.getColumn(2).setPreferredWidth(120);
            clmtab.getColumn(3).setPreferredWidth(180);
            clmtab.getColumn(4).setPreferredWidth(160); 
            
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
                String sql = "select * from zamowienia"
                        + "where id_pracownik = " + id_pr + " or id_pracownik is null";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_zam = Integer.parseInt(wynik.getString(1));
                        id_kl = Integer.parseInt(wynik.getString(2));
                        id_prod = Integer.parseInt(wynik.getString(4));
                        sdata_zl = wynik.getString(5); 
                        sczy_p = wynik.getString(6);
                        sczy_zap = wynik.getString(7); 
                        sczy_zr = wynik.getString(8);
                        sdata_re = wynik.getString(9);
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
                f.setSize(800,300);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(9,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujZamowienie extends JFrame{
            JLabel kl,prod,data_zl,czy_p,czy_zap,czy_zr,data_re;
            JTextField data_zltf,czy_ptf,czy_zaptf,czy_zrtf,data_retf;
            JButton klbtn,prodbtn,upd,exit,powrot,menu;
            AktualizujZamowienie(){
                prod = new JLabel("Produkt", SwingConstants.CENTER);
                kl = new JLabel("Klient", SwingConstants.CENTER);
                data_zl = new JLabel("Data złożenia(YYYY-MM-DD gg:mm:ss)", SwingConstants.CENTER);
                czy_p = new JLabel("Czy przyjęto(tak/nie)", SwingConstants.CENTER);
                czy_zap = new JLabel("Czy zapłacono(tak/nie)", SwingConstants.CENTER); 
                czy_zr = new JLabel("Czy zrealizowano(tak/nie)", SwingConstants.CENTER); 
                data_re = new JLabel("Data realizacji(YYYY-MM-DD gg:mm:ss)", SwingConstants.CENTER);

                data_zltf = new JTextField(sdata_zl,30);
                czy_ptf = new JTextField(sczy_p,3);
                czy_zaptf = new JTextField(sczy_zap,3);
                czy_zrtf = new JTextField(sczy_zr,3);
                data_retf = new JTextField(sdata_re,30);

                prodbtn = new JButton("Wybierz produkt");
                klbtn = new JButton("Wybierz klienta");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(kl);add(klbtn);add(prod);add(prodbtn);add(data_zl);add(data_zltf);
                add(czy_p);add(czy_ptf);add(czy_zap);add(czy_zaptf);
                add(czy_zr);add(czy_zrtf);add(data_re);add(data_retf);
            
                add(upd);add(powrot);add(menu);add(exit);

                prod.setBorder(BorderFactory.createLineBorder(Color.black));
                kl.setBorder(BorderFactory.createLineBorder(Color.black));
                data_zl.setBorder(BorderFactory.createLineBorder(Color.black));
                czy_p.setBorder(BorderFactory.createLineBorder(Color.black));
                czy_zap.setBorder(BorderFactory.createLineBorder(Color.black));
                czy_zr.setBorder(BorderFactory.createLineBorder(Color.black));
                data_re.setBorder(BorderFactory.createLineBorder(Color.black));
                
                prodbtn.addActionListener(new WybierzProdukt());
                klbtn.addActionListener(new WybierzKlient());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this,id_pr));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Pattern pdata_zl,pczy_p,pczy_zap,pczy_zr,pdata_re;
        Matcher mdata_zl,mczy_p,mczy_zap,mczy_zr,mdata_re;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String data_zl,czy_p,czy_zap,czy_zr,data_re,data;
         Connection con1;
               data_zl = data_zltf.getText();
               czy_p = czy_ptf.getText().toLowerCase();
               czy_zap = czy_zaptf.getText().toLowerCase();
               czy_zr = czy_zrtf.getText().toLowerCase();
               data_re = data_retf.getText();
               
               data = "2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?(\\.[0-9])?)?";
               pdata_zl = Pattern.compile(data);
               pdata_re = Pattern.compile("(2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?(\\.[0-9])?)?)?");
               pczy_p = Pattern.compile("tak|nie");
               pczy_zap = Pattern.compile("tak|nie");
               pczy_zr = Pattern.compile("tak|nie");
               
               mdata_zl = pdata_zl.matcher(data_zl);
               mdata_re = pdata_re.matcher(data_re);
               mczy_p = pczy_zr.matcher(czy_p);
               mczy_zap = pczy_zr.matcher(czy_zap);
               mczy_zr = pczy_zr.matcher(czy_zr);
               
               if(id_kl == -1) {
                   JOptionPane.showConfirmDialog(null, "Klient nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_prod == -1) {
                   JOptionPane.showConfirmDialog(null, "Produkt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mdata_zl.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data złożenia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdata_re.matches() == false && czy_zr.equals("nie"))
                       JOptionPane.showConfirmDialog(null, "Pole \"Data realizacji\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               else if(mdata_re.matches() == false && czy_zr.equals("tak"))
                       JOptionPane.showConfirmDialog(null, "Pole \"Data realizacji\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_p.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy przyjęto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_zap.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy zapłacono\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mczy_zr.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Czy zrealizowano\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mdata_zl.matches() && ((pdata_re.matcher("").matches() && czy_zr.equals("nie"))  
                       || (pdata_zl.matcher(data_re).matches() && czy_zr.equals("tak")))
                       && mczy_p.matches() && mczy_zap.matches() && mczy_zr.matches() 
                       && id_kl != -1 && id_prod != -1) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update zamowienia set id_klient = "+ id_kl +","
                    + "id_pracownik ="+id_pr+", id_produkt = "+ id_prod +","
                    + " data_zlozenia_zamowienia = '"+data_zl+"', czy_przyjeto_zamowienie = '"+czy_p+"',"
                    + "czy_zaplacono='"+czy_zap+"', czy_zrealizowano_zamowienie = '"+czy_zr+"', "
                    + "data_realizacji_zamowienia ='"+data_re+"' where id_zamowienie = "+id_zam;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            id_prod = -1;
            id_kl = -1;
            f.dispose();
            new Zamowienia(id_pr);
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
            
            setTitle("Wybierz zamówienie");
            setLocationRelativeTo(null);
            setSize(780, 200);
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
            i3.addActionListener(new menu(this,id_pr));
            i4.addActionListener(new Wyjscie());
            
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select data_zlozenia_zamowienia,czy_przyjeto_zamowienie,"
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia from zamowienia "
                    + "where id_pracownik = " + id_pr + " or id_pracownik is null";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("data_zlozenia_zamowienia"),wynik.getString("czy_przyjeto_zamowienie"),
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
            
            String[] clm = {"Data złożenia zamówienia","Czy przyjęto zamówienie",
            "Czy zapłacono","Czy zrealizowano zamówienie","Data realizacji zamówienia"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(160);
            clmtab.getColumn(1).setPreferredWidth(160);
            clmtab.getColumn(2).setPreferredWidth(120);
            clmtab.getColumn(3).setPreferredWidth(180);
            clmtab.getColumn(4).setPreferredWidth(160); 
            
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
                String sql = "select id_zamowienie from zamowienia "
                        + "where id_pracownik = " + id_pr + " or id_pracownik is null";
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
    
    class WybierzKlient extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
                List<String[]> list = new ArrayList<String[]>();
        
                setLocationRelativeTo(null);
                setSize(320, 200);
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwisko, imie, PESEL from klienci";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwisko"),wynik.getString("imie"),
                    wynik.getString("PESEL")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwisko","Imię","PESEL"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(140);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(90);
            
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
                String sql = "select id_adres from adresy";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kl = Integer.parseInt(wynik.getString(1));
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
            new Zamowienia(id_pr);
        }
    }
}
