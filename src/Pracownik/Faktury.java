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

public class Faktury extends JFrame{
Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_fak=-1;
    int id_zam=-1;
    int id_pr;
    Faktury(int id_pr){
        this.id_pr = id_pr;
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(840, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj fakturę");
        i2 = new JMenuItem("Usuń fakturę");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj fakturę");
        
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
            String sql = "select data_wystawienia,data_sprzedazy,procent_VAT,"
                    + "wartosc_VAT,cena_netto,cena_brutto,miejsce_wystawienia,"
                    + "sposob_zaplaty from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_pracownik = " + id_pr;
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
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
    
    class Dodaj extends JFrame implements ActionListener{
        JFrame f;
        Dodaj(JFrame f) {
            this.f = f;
        }
        JLabel zam,data_w,data_s,prv,warv,net,brut,msc,zap;
        JTextField data_wtf,data_stf,prvtf,warvtf,nettf,bruttf,msctf,zaptf;
        JButton zambtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(800,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(11,2));
            setLocationRelativeTo(null);
            
            zam = new JLabel("Zamówienie*", SwingConstants.CENTER);
            data_w = new JLabel("Data wystawienia(YYYY-MM-DD gg:mm:ss)*", SwingConstants.CENTER);
            data_s = new JLabel("Data sprzedaży(YYYY-MM-DD gg:mm:ss)*", SwingConstants.CENTER);
            prv = new JLabel("VAT[%]*", SwingConstants.CENTER);
            warv = new JLabel("Wartość VAT*", SwingConstants.CENTER); 
            net = new JLabel("Cena netto*", SwingConstants.CENTER); 
            brut = new JLabel("Cena brutto*", SwingConstants.CENTER);
            msc = new JLabel("Miejsce wystawienia*", SwingConstants.CENTER);
            zap = new JLabel("Sposób płatności*", SwingConstants.CENTER);
            
            data_wtf = new JTextField(30);
            data_stf = new JTextField(30);
            prvtf = new JTextField(2);
            warvtf = new JTextField(10);
            nettf = new JTextField(10);
            bruttf = new JTextField(10);
            msctf = new JTextField(50);
            zaptf = new JTextField(30);
            
            zambtn = new JButton("Wybierz zamówienie");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(zam);add(zambtn);add(data_w);add(data_wtf);add(data_s);add(data_stf);
            add(prv);add(prvtf);add(warv);add(warvtf);add(net);add(nettf);
            add(brut);add(bruttf);add(msc);add(msctf);add(zap);add(zaptf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            zam.setBorder(BorderFactory.createLineBorder(Color.black));
            data_w.setBorder(BorderFactory.createLineBorder(Color.black));
            data_s.setBorder(BorderFactory.createLineBorder(Color.black));
            prv.setBorder(BorderFactory.createLineBorder(Color.black));
            warv.setBorder(BorderFactory.createLineBorder(Color.black));
            net.setBorder(BorderFactory.createLineBorder(Color.black));
            brut.setBorder(BorderFactory.createLineBorder(Color.black));
            msc.setBorder(BorderFactory.createLineBorder(Color.black));
            zap.setBorder(BorderFactory.createLineBorder(Color.black));
            
            zambtn.addActionListener(new WybierzZamowienie());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this,id_pr));
            
        }
        
        class Insert implements ActionListener{
         String data_w,data_s,prv,warv,net,brut,msc,zap;
         Pattern pdata_w,pdata_s,pprv,pwarv,pnet,pbrut,pmsc,pzap;
         Matcher mdata_w,mdata_s,mprv,mwarv,mnet,mbrut,mmsc,mzap;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               data_w = data_wtf.getText();
               data_s = data_stf.getText();
               prv = prvtf.getText();
               warv = warvtf.getText();
               net = nettf.getText();
               brut = bruttf.getText();
               msc = msctf.getText().toLowerCase();
               zap = zaptf.getText();
               
               
               pdata_w = Pattern.compile("2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?)?");
               pdata_s = Pattern.compile("2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?)?");
               pprv = Pattern.compile("[0-9]+");
               pwarv = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pnet = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pbrut = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pmsc = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pzap = Pattern.compile("[A-Za-z]+");
               
               mdata_w = pdata_w.matcher(data_w);
               mdata_s = pdata_s.matcher(data_s);
               mprv = pprv.matcher(prv);
               mwarv = pwarv.matcher(warv);
               mnet = pnet.matcher(net);
               mbrut = pbrut.matcher(brut);
               mmsc = pmsc.matcher(msc);
               mzap = pzap.matcher(zap);
               
               if(id_zam == -1) {
                   JOptionPane.showConfirmDialog(null, "Zamówienie nie zostało wybrane", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mdata_w.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data wystawienia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdata_s.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data sprzedaży\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mprv.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Procent VAT\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 0123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwarv.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Wartość VAT\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnet.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena netto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.23", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mbrut.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena brutto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.23", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmsc.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Miejsce wystawienia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mzap.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Sposób zapłaty\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mdata_w.matches() && mdata_s.matches() && mprv.matches() && mwarv.matches()
                       && mnet.matches() && mbrut.matches() && mmsc.matches()
                       && mzap.matches() && id_zam != -1) {
                   msc = msc.substring(0, 1).toUpperCase() + msc.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into faktury values"
                    + "("+ id_zam +",'"+data_w+"','"+data_s+"','"+prv+"','"+
                    warv+"','"+net+"','"+brut+"','"+msc+"','"+zap+"')";
            
            ask.addBatch(sql);
            ask.executeBatch();
        
            data_wtf.setText("");
            data_stf.setText("");
            prvtf.setText("");
            warvtf.setText("");
            nettf.setText("");
            bruttf.setText("");
            msctf.setText("");
            zaptf.setText("");
            id_zam = -1;
            
            ask.close();
            con1.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");} } 
                  }
          }
    }
    
    class Aktualizuj extends JFrame implements ActionListener {
        JFrame f;
        String sdata_w,sdata_s,sprv,swarv,snet,sbrut,smsc,szap;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz fakturę");
            setLocationRelativeTo(null);
            setSize(840, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select data_wystawienia,data_sprzedazy,procent_VAT,"
                    + "wartosc_VAT,cena_netto,cena_brutto,miejsce_wystawienia,"
                    + "sposob_zaplaty from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_pracownik = " + id_pr;
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
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
            jtab.addMouseListener(new click(jtab));
                      
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
                String sql = "select * from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_pracownik = " + id_pr;
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_fak = Integer.parseInt(wynik.getString(1));
                        id_zam = Integer.parseInt(wynik.getString(2));
                        sdata_w = wynik.getString(3);
                        sdata_s = wynik.getString(4); 
                        sprv = wynik.getString(5);
                        swarv = wynik.getString(6); 
                        snet = wynik.getString(7);
                        sbrut = wynik.getString(8);
                        smsc = wynik.getString(9);
                        szap = wynik.getString(10);
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujFaktura();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(800,300);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(11,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujFaktura extends JFrame{
            JLabel zam,data_w,data_s,prv,warv,net,brut,msc,zap;
            JTextField data_wtf,data_stf,prvtf,warvtf,nettf,bruttf,msctf,zaptf;
            JButton zambtn,upd,exit,powrot,menu;
            AktualizujFaktura(){
                zam = new JLabel("Zamówienie*", SwingConstants.CENTER);
                data_w = new JLabel("Data wystawienia(YYYY-MM-DD gg:mm:ss)*", SwingConstants.CENTER);
                data_s = new JLabel("Data sprzedaży(YYYY-MM-DD gg:mm:ss)*", SwingConstants.CENTER);
                prv = new JLabel("VAT[%]*", SwingConstants.CENTER);
                warv = new JLabel("Wartość VAT*", SwingConstants.CENTER); 
                net = new JLabel("Cena netto*", SwingConstants.CENTER); 
                brut = new JLabel("Cena brutto*", SwingConstants.CENTER);
                msc = new JLabel("Miejsce wystawienia*", SwingConstants.CENTER);
                zap = new JLabel("Sposób płatności*", SwingConstants.CENTER);

                data_wtf = new JTextField(sdata_w,30);
                data_stf = new JTextField(sdata_s,30);
                prvtf = new JTextField(sprv,2);
                warvtf = new JTextField(swarv,10);
                nettf = new JTextField(snet,10);
                bruttf = new JTextField(sbrut,10);
                msctf = new JTextField(smsc,50);
                zaptf = new JTextField(szap,30);
                
                zambtn = new JButton("Wybierz zamówienie");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(zam);add(zambtn);add(data_w);add(data_wtf);add(data_s);add(data_stf);
                add(prv);add(prvtf);add(warv);add(warvtf);add(net);add(nettf);
                add(brut);add(bruttf);add(msc);add(msctf);add(zap);add(zaptf);

                add(upd);add(powrot);add(menu);add(exit);

                zam.setBorder(BorderFactory.createLineBorder(Color.black));
                data_w.setBorder(BorderFactory.createLineBorder(Color.black));
                data_s.setBorder(BorderFactory.createLineBorder(Color.black));
                prv.setBorder(BorderFactory.createLineBorder(Color.black));
                warv.setBorder(BorderFactory.createLineBorder(Color.black));
                net.setBorder(BorderFactory.createLineBorder(Color.black));
                brut.setBorder(BorderFactory.createLineBorder(Color.black));
                msc.setBorder(BorderFactory.createLineBorder(Color.black));
                zap.setBorder(BorderFactory.createLineBorder(Color.black));

                zambtn.addActionListener(new WybierzZamowienie());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this,id_pr));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Pattern pdata_w,pdata_s,pprv,pwarv,pnet,pbrut,pmsc,pzap;
        Matcher mdata_w,mdata_s,mprv,mwarv,mnet,mbrut,mmsc,mzap;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String data_w,data_s,prv,warv,net,brut,msc,zap;
         Connection con1;
               data_w = data_wtf.getText();
               data_s = data_stf.getText();
               prv = prvtf.getText();
               warv = warvtf.getText();
               net = nettf.getText();
               brut = bruttf.getText();
               msc = msctf.getText().toLowerCase();
               zap = zaptf.getText();
               
               pdata_w = Pattern.compile("2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?(\\.[0-9])?)?");
               pdata_s = Pattern.compile("2[01][0-9]{2}-(0[1-9]|1[12])-([0-2][0-9]|3[01])(((\\s[0-9]{2})?(:[0-9]{2})?)?(:[0-9]{2})?(\\.[0-9])?)?");
               pprv = Pattern.compile("[0-9]+");
               pwarv = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pnet = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pbrut = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pmsc = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pzap = Pattern.compile("[A-Za-ząćęłńóśźżĄĆĘŁŃÓŚĆŹŻ]+");
               
               mdata_w = pdata_w.matcher(data_w);
               mdata_s = pdata_s.matcher(data_s);
               mprv = pprv.matcher(prv);
               mwarv = pwarv.matcher(warv);
               mnet = pnet.matcher(net);
               mbrut = pbrut.matcher(brut);
               mmsc = pmsc.matcher(msc);
               mzap = pzap.matcher(zap);
               
               if(id_zam == -1) {
                   JOptionPane.showConfirmDialog(null, "Zamówienie nie zostało wybrane", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mdata_w.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data wystawienia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdata_s.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Data sprzedaży\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mprv.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Procent VAT\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 0123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwarv.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Wartość VAT\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnet.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena netto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.23", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mbrut.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena brutto\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.23", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmsc.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Miejsce wystawienia\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mzap.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Sposób zapłaty\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mdata_w.matches() && mdata_s.matches() && mprv.matches() && mwarv.matches()
                       && mnet.matches() && mbrut.matches() && mmsc.matches()
                       && mzap.matches() && id_zam != -1) {
                   msc = msc.substring(0, 1).toUpperCase() + msc.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update faktury set id_zamowienia = "+ id_zam +","
                    + "data_wystawienia ='"+data_w+"',"
                    + "data_sprzedazy = '"+data_s+"',procent_VAT = '"+prv+"',"
                    + "wartosc_VAT='"+warv+"', cena_netto = '"+net+"', cena_brutto='"+brut+"',"
                    + "miejsce_wystawienia = '"+msc+"',sposob_zaplaty= '"+zap+"' "
                    + "where id_faktura = "+id_fak;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            id_zam = -1;
            
            ask.close();
            con1.close();
            f.dispose();
            new Faktury(id_pr);
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
            
            setTitle("Wybierz fakturę");
            setLocationRelativeTo(null);
            setSize(840, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Faktury\"");
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
            String sql = "select data_wystawienia,data_sprzedazy,procent_VAT,"
                    + "wartosc_VAT,cena_netto,cena_brutto,miejsce_wystawienia,"
                    + "sposob_zaplaty from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_pracownik = " + id_pr;
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
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
            jtab.addMouseListener(new click(jtab));
            
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
                String sql = "select id_faktura from faktury inner join zamowienia on "
                    + "faktury.id_zamowienia = zamowienia.id_zamowienie where id_pracownik = " + id_pr;
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_fak = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete faktury where id_faktura = " + id_fak;
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
    
    class WybierzZamowienie extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
                List<String[]> list = new ArrayList<String[]>();
        
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
                    + "czy_zaplacono,czy_zrealizowano_zamowienie,data_realizacji_zamowienia "
                    + "from zamowienia where czy_zaplacono='nie' and id_pracownik =" + id_pr;
            
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
                String sql = "select id_zamowienie from zamowienia "
                        + "where czy_zaplacono='nie' and id_pracownik =" + id_pr;
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_zam = Integer.parseInt(wynik.getString(1));
                        break;}
                
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);
            JOptionPane.showConfirmDialog(null, "Nie udało się usunąć elementu!", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
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
            new Faktury(id_pr);
        }
    }
}

