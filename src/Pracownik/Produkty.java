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

public class Produkty extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_prd = -1;
    int id_kat=-1, id_pr;
    Produkty(int id_pr){
        this.id_pr = id_pr;
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(980, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj produkt");
        i2 = new JMenuItem("Usuń produkt");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj produkt");
        
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
        JLabel kat,naz,prod,mod,kal,dl_kar,masa,dl_luf,mag,cena,stan;
        JTextField naztf,prodtf,modtf,kaltf,dl_kartf,masatf,dl_luftf,magtf,cenatf,stantf;
        JButton katbtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(900,500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(13,2));
            setLocationRelativeTo(null);
            
            kat = new JLabel("Kategoria*", SwingConstants.CENTER);
            naz = new JLabel("Nazwa produktu*", SwingConstants.CENTER);
            prod = new JLabel("Producent*", SwingConstants.CENTER);
            mod = new JLabel("Model*", SwingConstants.CENTER);
            kal = new JLabel("Kaliber*", SwingConstants.CENTER); 
            dl_kar = new JLabel("Długość karabinu[mm]*", SwingConstants.CENTER); 
            masa = new JLabel("Masa[g]*", SwingConstants.CENTER);
            dl_luf = new JLabel("Długość lufy[mm]*", SwingConstants.CENTER);
            mag = new JLabel("Pojemność magazynka*", SwingConstants.CENTER);
            cena = new JLabel("Cena brutto*", SwingConstants.CENTER);
            stan = new JLabel("Stan[szt.]*", SwingConstants.CENTER);
            
            naztf = new JTextField(20);
            prodtf = new JTextField(11);
            modtf = new JTextField(40);
            kaltf = new JTextField(50);
            dl_kartf = new JTextField(20);
            masatf = new JTextField(20);
            dl_luftf = new JTextField(20);
            magtf = new JTextField(20);
            cenatf = new JTextField(20);
            stantf = new JTextField(20);
            
            katbtn = new JButton("Wybierz kategorię");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(kat);add(katbtn);add(naz);add(naztf);add(prod);add(prodtf);
            add(mod);add(modtf);add(kal);add(kaltf);add(dl_kar);add(dl_kartf);
            add(masa);add(masatf);add(dl_luf);add(dl_luftf);add(mag);add(magtf);
            add(cena);add(cenatf);add(stan);add(stantf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            kat.setBorder(BorderFactory.createLineBorder(Color.black));
            naz.setBorder(BorderFactory.createLineBorder(Color.black));
            prod.setBorder(BorderFactory.createLineBorder(Color.black));
            mod.setBorder(BorderFactory.createLineBorder(Color.black));
            kal.setBorder(BorderFactory.createLineBorder(Color.black));
            dl_kar.setBorder(BorderFactory.createLineBorder(Color.black));
            masa.setBorder(BorderFactory.createLineBorder(Color.black));
            dl_luf.setBorder(BorderFactory.createLineBorder(Color.black));
            mag.setBorder(BorderFactory.createLineBorder(Color.black));
            cena.setBorder(BorderFactory.createLineBorder(Color.black));
            stan.setBorder(BorderFactory.createLineBorder(Color.black));
            
            katbtn.addActionListener(new Wybierz());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this,id_pr));
            
        }
        
        class Insert implements ActionListener{
         String naz,prod,mod,kal,dl_kar,masa,dl_luf,mag,cena,stan;
         Pattern pnaz,pprod,pmod,pkal,pdl_kar,pmasa,pdl_luf,pmag,pcena,pstan;
         Matcher mnaz,mprod,mmod,mkal,mdl_kar,mmasa,mdl_luf,mmag,mcena,mstan;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               prod = prodtf.getText();
               mod = modtf.getText();
               kal = kaltf.getText();
               dl_kar = dl_kartf.getText();
               naz = naztf.getText();
               masa = masatf.getText();
               dl_luf = dl_luftf.getText();
               mag = magtf.getText();
               cena = cenatf.getText();
               stan = stantf.getText();
               
               pnaz = Pattern.compile("[^\\s].+");
               pprod = Pattern.compile("[^\\s].+");
               pmod = Pattern.compile("[^\\s].+");
               pkal = Pattern.compile("[^\\s].+");
               pdl_kar = Pattern.compile("[0-9]+");
               pmasa = Pattern.compile("[0-9]+");
               pdl_luf = Pattern.compile("[0-9]+");
               pmag = Pattern.compile("[0-9]+");
               pcena = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pstan = Pattern.compile("[0-9]+");
               
               mnaz = pnaz.matcher(naz);
               mprod = pprod.matcher(prod);
               mmod = pmod.matcher(mod);
               mkal = pkal.matcher(kal);
               mdl_kar = pdl_kar.matcher(dl_kar);
               mmasa = pmasa.matcher(masa);
               mdl_luf = pdl_luf.matcher(dl_luf);
               mmag = pmag.matcher(mag);
               mcena = pcena.matcher(cena);
               mstan = pstan.matcher(stan);
               
               if(id_kat == -1) {
                   JOptionPane.showConfirmDialog(null, "Kategoria nie została wybrana", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mnaz.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa produktu\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mprod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Producent\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Model\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.",
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mkal.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Kaliber\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdl_kar.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Długość[mm]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmasa.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Masa[g]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdl_luf.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Długość lufy[mm]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmag.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Pojemność magazynka\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mcena.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.67", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mstan.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Stan[szt.]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnaz.matches() && mprod.matches() && mmod.matches() && mkal.matches()
                       && mdl_kar.matches() && mmasa.matches() && mdl_luf.matches()
                       && mmag.matches() && mcena.matches() && mstan.matches() 
                       && id_kat != -1) {
                         
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into produkty values"
                    + "("+ id_kat +",'"+naz+"','"+prod+"','"+mod+"','"+
                    kal+"',"+dl_kar+","+masa+","+dl_luf+","+
                    mag+","+cena+","+stan+")";
            
            ask.addBatch(sql);
            ask.executeBatch();
            
            naztf.setText("");
            prodtf.setText("");
            modtf.setText("");
            kaltf.setText("");
            dl_kartf.setText("");
            masatf.setText("");
            dl_luftf.setText("");
            magtf.setText("");
            cenatf.setText("");
            stantf.setText("");
            id_kat = -1;
            
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
        String snaz,sprod,smod,skal,sdl_kar,smasa,sdl_luf,smag,scena,sstan;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz produkt");
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
                String sql = "select * from produkty";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_prd = Integer.parseInt(wynik.getString(1));
                        id_kat = Integer.parseInt(wynik.getString(2));
                        snaz = wynik.getString(3);
                        sprod = wynik.getString(4); 
                        smod = wynik.getString(5);
                        skal = wynik.getString(6); 
                        sdl_kar = wynik.getString(7);
                        smasa = wynik.getString(8);
                        sdl_luf = wynik.getString(9);
                        smag = wynik.getString(10);
                        scena = wynik.getString(11);
                        sstan = wynik.getString(12);
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujProdukt();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(900,500);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(13,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujProdukt extends JFrame{
            JLabel kat,naz,prod,mod,kal,dl_kar,masa,dl_luf,mag,cena,stan;
            JTextField naztf,prodtf,modtf,kaltf,dl_kartf,masatf,dl_luftf,magtf,cenatf,stantf;
            JButton katbtn,upd,exit,powrot,menu;
            AktualizujProdukt(){
                kat = new JLabel("Kategoria*", SwingConstants.CENTER);
                naz = new JLabel("Nazwa produktu*", SwingConstants.CENTER);
                prod = new JLabel("Producent*", SwingConstants.CENTER);
                mod = new JLabel("Model*", SwingConstants.CENTER);
                kal = new JLabel("Kaliber*", SwingConstants.CENTER); 
                dl_kar = new JLabel("Długość karabinu[mm]*", SwingConstants.CENTER); 
                masa = new JLabel("Masa[g]*", SwingConstants.CENTER);
                dl_luf = new JLabel("Długość lufy[mm]*", SwingConstants.CENTER);
                mag = new JLabel("Pojemność magazynka*", SwingConstants.CENTER);
                cena = new JLabel("Cena brutto*", SwingConstants.CENTER);
                stan = new JLabel("Stan[szt.]*", SwingConstants.CENTER);

                naztf = new JTextField(snaz,20);
                prodtf = new JTextField(sprod,11);
                modtf = new JTextField(smod,40);
                kaltf = new JTextField(skal,50);
                dl_kartf = new JTextField(sdl_kar,20);
                masatf = new JTextField(smasa,20);
                dl_luftf = new JTextField(sdl_luf,20);
                magtf = new JTextField(smag,20);
                cenatf = new JTextField(scena,20);
                stantf = new JTextField(sstan,20);

                katbtn = new JButton("Wybierz kategorię");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(kat);add(katbtn);add(naz);add(naztf);add(prod);add(prodtf);
                add(mod);add(modtf);add(kal);add(kaltf);add(dl_kar);add(dl_kartf);
                add(masa);add(masatf);add(dl_luf);add(dl_luftf);add(mag);add(magtf);
                add(cena);add(cenatf);add(stan);add(stantf);
            
                add(upd);add(powrot);add(menu);add(exit);

                kat.setBorder(BorderFactory.createLineBorder(Color.black));
                naz.setBorder(BorderFactory.createLineBorder(Color.black));
                prod.setBorder(BorderFactory.createLineBorder(Color.black));
                mod.setBorder(BorderFactory.createLineBorder(Color.black));
                kal.setBorder(BorderFactory.createLineBorder(Color.black));
                dl_kar.setBorder(BorderFactory.createLineBorder(Color.black));
                masa.setBorder(BorderFactory.createLineBorder(Color.black));
                dl_luf.setBorder(BorderFactory.createLineBorder(Color.black));
                mag.setBorder(BorderFactory.createLineBorder(Color.black));
                cena.setBorder(BorderFactory.createLineBorder(Color.black));
                stan.setBorder(BorderFactory.createLineBorder(Color.black));

                katbtn.addActionListener(new Wybierz());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this,id_pr));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Pattern pnaz,pprod,pmod,pkal,pdl_kar,pmasa,pdl_luf,pmag,pcena,pstan;
        Matcher mnaz,mprod,mmod,mkal,mdl_kar,mmasa,mdl_luf,mmag,mcena,mstan;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String naz,prod,mod,kal,dl_kar,masa,dl_luf,mag,cena,stan;
         Connection con1;
               prod = prodtf.getText();
               mod = modtf.getText();
               kal = kaltf.getText();
               dl_kar = dl_kartf.getText();
               naz = naztf.getText();
               masa = masatf.getText();
               dl_luf = dl_luftf.getText();
               mag = magtf.getText();
               cena = cenatf.getText();
               stan = stantf.getText();
               
               pnaz = Pattern.compile("[^\\s].+");
               pprod = Pattern.compile("[^\\s].+");
               pmod = Pattern.compile("[^\\s].+");
               pkal = Pattern.compile("[^\\s].+");
               pdl_kar = Pattern.compile("[0-9]+");
               pmasa = Pattern.compile("[0-9]+");
               pdl_luf = Pattern.compile("[0-9]+");
               pmag = Pattern.compile("[0-9]+");
               pcena = Pattern.compile("[0-9]+(\\.[0-9]{1,2})?");
               pstan = Pattern.compile("[0-9]+");
               
               mnaz = pnaz.matcher(naz);
               mprod = pprod.matcher(prod);
               mmod = pmod.matcher(mod);
               mkal = pkal.matcher(kal);
               mdl_kar = pdl_kar.matcher(dl_kar);
               mmasa = pmasa.matcher(masa);
               mdl_luf = pdl_luf.matcher(dl_luf);
               mmag = pmag.matcher(mag);
               mcena = pcena.matcher(cena);
               mstan = pstan.matcher(stan);
               
               if(id_kat == -1) {
                   JOptionPane.showConfirmDialog(null, "Kategoria nie została wybrana", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mnaz.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa produktu\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mprod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Producent\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Model\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.",
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mkal.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Kaliber\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdl_kar.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Długość[mm]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmasa.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Masa[g]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mdl_luf.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Długość lufy[mm]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmag.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Pojemność magazynka\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mcena.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Cena\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry, format np. 32.67", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mstan.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Stan[szt.]\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnaz.matches() && mprod.matches() && mmod.matches() && mkal.matches()
                       && mdl_kar.matches() && mmasa.matches() && mdl_luf.matches()
                       && mmag.matches() && mcena.matches() && mstan.matches() 
                       && id_kat != -1) {
          
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update produkty set id_kategoria = "+ id_kat +","
                    + "nazwa_produkt ='"+naz+"', producent = '"+prod+"',"
                    + "model = '"+mod+"', kaliber = '"+kal+"',"
                    + "\"dlugosc[mm]\"='"+dl_kar+"', \"masa[g]\" = '"+masa+"', "
                    + "\"dlugosc_lufy[mm]\" ='"+dl_luf+"',"
                    + "pojemnosc_magazynka = '"+mag+"',cena = '"+cena+"', "
                    + "\"stan[szt.]\" = '"+stan+"' where id_produkt = "+id_prd;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            f.dispose();
            id_kat = -1;
            new Produkty(id_pr);
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
            
            setTitle("Wybierz produkt");
            setLocationRelativeTo(null);
            setSize(980, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Produkty\"");
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
                String sql = "select id_produkt from produkty";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_prd = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete produkty where id_produkt = " + id_prd;
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
    
    class Wybierz extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
                List<String[]> list = new ArrayList<String[]>();
        
                setLocationRelativeTo(null);
                setSize(200,150);
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
                String sql = "select id_kategoria from kategorie";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kat = Integer.parseInt(wynik.getString(1));
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
            new Produkty(id_pr);
        }
    }
}

