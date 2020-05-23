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
import javax.swing.table.TableColumnModel;

public class Klienci extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_kl =-1; 
    int id_adr=-1;
    int id_kon=-1;
    int id_fir=-1; 
    Klienci(){
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(520, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj klienta");
        i2 = new JMenuItem("Usuń klienta");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Zaktualizuj klienta");
        
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
            String sql = "select nazwisko, imie, PESEL, login_kl, haslo_kl from klienci";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwisko"),wynik.getString("imie"),
                    wynik.getString("PESEL"),wynik.getString("login_kl"),
                    wynik.getString("haslo_kl")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwisko","Imię","PESEL","Login","Hasło"};
                       
            JTable jtab = new JTable(arr,clm);
            jtab.setEnabled(false);
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(140);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(90);
            clmtab.getColumn(3).setPreferredWidth(100);
            clmtab.getColumn(4).setPreferredWidth(100); 
            
            
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
        JLabel adr,kont,fir,naz,im,pes,log,has;
        JTextField naztf,imtf,pestf,logtf,hastf;
        JButton adrbtn,kontbtn,firbtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(600,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(10,2));
            setLocationRelativeTo(null);
            
            adr = new JLabel("Adres*", SwingConstants.CENTER);
            kont = new JLabel("Kontakt*", SwingConstants.CENTER);
            fir = new JLabel("Firma", SwingConstants.CENTER);
            log = new JLabel("Login*", SwingConstants.CENTER);
            pes = new JLabel("PESEL*", SwingConstants.CENTER);
            im = new JLabel("Imię*", SwingConstants.CENTER); 
            naz = new JLabel("Nazwisko*", SwingConstants.CENTER); 
            has = new JLabel("Hasło*", SwingConstants.CENTER);
            
            logtf = new JTextField(20);
            pestf = new JTextField(11);
            imtf = new JTextField(40);
            naztf = new JTextField(50);
            hastf = new JTextField(20);
            
            adrbtn = new JButton("Wybierz adres");
            kontbtn = new JButton("Wybierz kontakt");
            firbtn = new JButton("Wybierz firmę");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(adr);add(adrbtn);add(kont);add(kontbtn);add(fir);add(firbtn);
            add(im);add(imtf);add(naz);add(naztf);add(pes);add(pestf);add(log);
            add(logtf);add(has);add(hastf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            adr.setBorder(BorderFactory.createLineBorder(Color.black));
            fir.setBorder(BorderFactory.createLineBorder(Color.black));
            kont.setBorder(BorderFactory.createLineBorder(Color.black));
            log.setBorder(BorderFactory.createLineBorder(Color.black));
            im.setBorder(BorderFactory.createLineBorder(Color.black));
            naz.setBorder(BorderFactory.createLineBorder(Color.black));
            has.setBorder(BorderFactory.createLineBorder(Color.black));
            pes.setBorder(BorderFactory.createLineBorder(Color.black));
            
            adrbtn.addActionListener(new WybierzAdres());
            kontbtn.addActionListener(new WybierzKontakt());
            firbtn.addActionListener(new WybierzFirma());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this));
            
        }
        
        class Insert implements ActionListener{
         String im,naz,pes,log,has;
         Pattern pim,pnaz,ppes,plog,phas;
         Matcher mim,mnaz,mpes,mlog,mhas;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               pes = pestf.getText();
               log = logtf.getText();
               has = hastf.getText();
               im = imtf.getText().toLowerCase();
               naz = naztf.getText().toLowerCase();
                         
               pnaz = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pim = Pattern.compile("[a-zA-Ząćęłńóśźż]+");
               ppes = Pattern.compile("[0-9]{11}");
               plog = Pattern.compile("[^\\s].+");
               phas = Pattern.compile("[^\\s].+");
               
               mnaz = pnaz.matcher(naz);
               mim = pim.matcher(im);
               mpes = ppes.matcher(pes);
               mlog = plog.matcher(log);
               mhas = phas.matcher(has);
               
               if(id_adr == -1) {
                   JOptionPane.showConfirmDialog(null, "Adres nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_kon == -1) {
                   JOptionPane.showConfirmDialog(null, "Kontakt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_fir == -1) {
                   JOptionPane.showConfirmDialog(null, "Firma nie została wybrana", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mpes.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"PESEL\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mlog.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Login\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mhas.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Hasło\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnaz.matches() && mim.matches() && mpes.matches() && mlog.matches()
                       && mhas.matches() && id_adr != -1
                       && id_kon != -1 && id_fir != -1) {
                   im = im.substring(0, 1).toUpperCase() + im.substring(1).toLowerCase();
                   naz = naz.substring(0, 1).toUpperCase() + naz.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            System.out.println(id_adr);
            String sql = "insert into klienci values"
                    + "("+ id_adr +","+id_kon+","+id_fir+",'"+naz+"','"+im+"','"+pes+"','"+log+"','"+has+"')";
            
            ask.addBatch(sql);
            ask.executeBatch();
            
            logtf.setText("");
            hastf.setText("");
            pestf.setText("");
            imtf.setText("");
            naztf.setText("");
            id_kon = -1;
            id_adr = -1;
            id_fir = -1;
            
            ask.close();
            con1.close();
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
        String snaz,sim,spes,slog,shas;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz klienta");
            setLocationRelativeTo(null);
            setSize(520, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwisko, imie, PESEL, login_kl, haslo_kl from klienci";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwisko"),wynik.getString("imie"),
                    wynik.getString("PESEL"),wynik.getString("login_kl"),
                    wynik.getString("haslo_kl")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwisko","Imię","PESEL","Login","Hasło"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(140);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(90);
            clmtab.getColumn(3).setPreferredWidth(100);
            clmtab.getColumn(4).setPreferredWidth(100);
            
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
                String sql = "select * from klienci";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kl = Integer.parseInt(wynik.getString(1));
                        id_adr = Integer.parseInt(wynik.getString(2));
                        id_kon = Integer.parseInt(wynik.getString(3));
                        id_fir = Integer.parseInt(wynik.getString(4));
                        snaz = wynik.getString(5); 
                        sim = wynik.getString(6);
                        spes = wynik.getString(7);
                        slog = wynik.getString(8);
                        shas = wynik.getString(9);
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujKlient();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(600,300);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(10,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujKlient extends JFrame{
            JLabel adr,kon,fir,naz,im,pes,log,has;
            JTextField naztf,imtf,pestf,logtf,hastf;
            JButton adrbtn,konbtn,firbtn,upd,exit,powrot,menu;
            AktualizujKlient(){
                adr = new JLabel("Adres*", SwingConstants.CENTER);
                kon = new JLabel("Kontakt*", SwingConstants.CENTER);
                fir = new JLabel("Firma*", SwingConstants.CENTER);
                naz = new JLabel("Nazwisko*", SwingConstants.CENTER);
                im = new JLabel("Imię*", SwingConstants.CENTER);
                pes = new JLabel("PESEL*", SwingConstants.CENTER); 
                log = new JLabel("Login*", SwingConstants.CENTER);
                has = new JLabel("Hasło*", SwingConstants.CENTER);
                
                pestf = new JTextField(spes,11); 
                logtf = new JTextField(slog,20); 
                hastf = new JTextField(shas,20);
                imtf = new JTextField(sim,30);
                naztf = new JTextField(snaz,40);
                
                adrbtn = new JButton("Wybierz adres");
                konbtn = new JButton("Wybierz kontakt");
                firbtn = new JButton("Wybierz firmę");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(adr);add(adrbtn);add(kon);add(konbtn);add(fir);add(firbtn);
                add(naz);add(naztf);add(im);add(imtf);add(pes);add(pestf);
                add(log);add(logtf);add(has);add(hastf);

                add(upd);add(powrot);add(menu);add(exit);

                adr.setBorder(BorderFactory.createLineBorder(Color.black));
                fir.setBorder(BorderFactory.createLineBorder(Color.black));
                kon.setBorder(BorderFactory.createLineBorder(Color.black));
                pes.setBorder(BorderFactory.createLineBorder(Color.black));
                im.setBorder(BorderFactory.createLineBorder(Color.black));
                naz.setBorder(BorderFactory.createLineBorder(Color.black));
                log.setBorder(BorderFactory.createLineBorder(Color.black));
                has.setBorder(BorderFactory.createLineBorder(Color.black));
                
                adrbtn.addActionListener(new WybierzAdres());
                konbtn.addActionListener(new WybierzKontakt());
                firbtn.addActionListener(new WybierzFirma());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Pattern pim,pnaz,ppes,plog,phas;
        Matcher mim,mnaz,mpes,mlog,mhas;
        
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String naz,im,pes,log,has;
         Connection con1;
               has = hastf.getText();
               pes = pestf.getText();
               log = logtf.getText();
               im = imtf.getText().toLowerCase();
               naz = naztf.getText().toLowerCase();
          
               pnaz = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pim = Pattern.compile("[a-zA-Ząćęłńóśźż]+");
               ppes = Pattern.compile("[0-9]{11}");
               plog = Pattern.compile("[^\\s].+");
               phas = Pattern.compile("[^\\s].+");
               
               mnaz = pnaz.matcher(naz);
               mim = pim.matcher(im);
               mpes = ppes.matcher(pes);
               mlog = plog.matcher(log);
               mhas = phas.matcher(has);
               
               if(id_adr == -1) {
                   JOptionPane.showConfirmDialog(null, "Adres nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_kon == -1) {
                   JOptionPane.showConfirmDialog(null, "Kontakt nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(id_fir == -1) {
                   JOptionPane.showConfirmDialog(null, "Firma nie została wybrana", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mpes.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"PESEL\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko cyfry.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mlog.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Login\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mhas.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Hasło\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnaz.matches() && mim.matches() && mpes.matches() && mlog.matches()
                       && mhas.matches() && id_adr != -1
                       && id_kon != -1 && id_fir != -1) {
                   im = im.substring(0, 1).toUpperCase() + im.substring(1).toLowerCase();
                   naz = naz.substring(0, 1).toUpperCase() + naz.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update klienci set id_adres = "+ id_adr +","
                    + "id_kontakt ="+id_kon+",id_firma = "+id_fir+","
                    + "nazwisko = '"+naz+"',imie = '"+im+"',"
                    + "PESEL='"+pes+"', login_kl = '"+log+"',"
                    + "haslo_kl = '"+has+"' where id_klient = "+id_kl;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            id_fir = -1;
            id_kon = -1;
            id_adr = -1;
            f.dispose();
            new Klienci();
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
            
            setTitle("Wybierz klienta");
            setLocationRelativeTo(null);
            setSize(520, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Klienci\"");
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
            String sql = "select nazwisko, imie, PESEL, login_kl, haslo_kl from klienci";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwisko"),wynik.getString("imie"),
                    wynik.getString("PESEL"),wynik.getString("login_kl"),
                    wynik.getString("haslo_kl")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwisko","Imię","PESEL","Login","Hasło"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(140);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(90);
            clmtab.getColumn(3).setPreferredWidth(100);
            clmtab.getColumn(4).setPreferredWidth(100);
            
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
                String sql = "select id_klient from klienci";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kl = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete klienci where id_klient = " + id_kl;
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
    
    class WybierzAdres extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
                List<String[]> list = new ArrayList<String[]>();
        
                setLocationRelativeTo(null);
                setSize(725, 200);
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select miejscowosc, powiat, wojewodztwo, kod_pocztowy,"
                    + "ulica, nr_budynku, nr_lokalu from adresy";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("miejscowosc"),wynik.getString("powiat"),
                    wynik.getString("wojewodztwo"),wynik.getString("kod_pocztowy"),
                    wynik.getString("ulica"), wynik.getString("nr_budynku"), wynik.getString("nr_lokalu")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Miejscowość","Powiat","Województwo","Kod pocztowy",
                            "Ulica", "Nr budynku", "Nr lokalu"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(160);
            clmtab.getColumn(1).setPreferredWidth(90);
            clmtab.getColumn(2).setPreferredWidth(90);
            clmtab.getColumn(3).setPreferredWidth(85);
            clmtab.getColumn(4).setPreferredWidth(140);
            clmtab.getColumn(5).setPreferredWidth(80);
            clmtab.getColumn(6).setPreferredWidth(80);
                        
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
                        id_adr = Integer.parseInt(wynik.getString(1));
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
        
        class WybierzFirma extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(720, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nazwa_firma,REGON,NIP,imie_wlasciciel,"
                    + "nazwisko_wlasciciel from firmy";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nazwa_firma"),wynik.getString("REGON"),
                    wynik.getString("NIP"),wynik.getString("imie_wlasciciel"),
                    wynik.getString("nazwisko_wlasciciel")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            String[] clm = {"Nazwa Firmy","REGON","NIP","Imię właściciela",
                            "Nazwisko właściciela"};
                       
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(300);
            clmtab.getColumn(1).setPreferredWidth(80);
            clmtab.getColumn(2).setPreferredWidth(80);
            clmtab.getColumn(3).setPreferredWidth(110);
            clmtab.getColumn(4).setPreferredWidth(150);
                        
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
            String sql = "select id_firma from firmy";
            ResultSet wynik = ask.executeQuery(sql);
            
            while(wynik.next())
                if(wynik.getRow() == row){
                    id_fir = Integer.parseInt(wynik.getString(1));
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
        
        class WybierzKontakt extends JFrame implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(650, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            
            Statement ask = con.createStatement();
            String sql = "select nr_tel_1,nr_tel_2,fax,email,strona_www from kontakty";
            
            ResultSet wynik = ask.executeQuery(sql);
            ResultSetMetaData kolumny = wynik.getMetaData();
            int k = kolumny.getColumnCount();
            
            while(wynik.next()) {
                String[] t = {wynik.getString("nr_tel_1"),wynik.getString("nr_tel_2"),
                    wynik.getString("fax"),wynik.getString("email"),
                    wynik.getString("strona_www")};
                list.add(t);
            }
            
            String arr[][] = new String[list.size()][];
            for(int i=0;i<arr.length;i++){
                String[] row = list.get(i);
                arr[i] = row;
            }
            
            ask.close();
            
            
            String[] clm = {"Nr tel 1","Nr tel 2","Fax","Email","Strona www"};
            
            JTable jtab = new JTable(arr,clm);
            jtab.addMouseListener(new click(jtab));
            
            TableColumnModel clmtab = jtab.getColumnModel();
            clmtab.getColumn(0).setPreferredWidth(80);
            clmtab.getColumn(1).setPreferredWidth(80);
            clmtab.getColumn(2).setPreferredWidth(80);
            clmtab.getColumn(3).setPreferredWidth(160);
            clmtab.getColumn(4).setPreferredWidth(250);
            
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
            String sql = "select id_kontakt from kontakty";
            ResultSet wynik = ask.executeQuery(sql);
            
            while(wynik.next())
                if(wynik.getRow() == row){
                    id_kon = Integer.parseInt(wynik.getString(1));
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
            new Klienci();
        }
    }
}
