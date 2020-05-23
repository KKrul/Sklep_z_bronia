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

public class Firmy extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_adr=-1, id_fir=-1, id_pr;
    Firmy(int id_pr){
        this.id_pr = id_pr;
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(720, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj firmę");
        i2 = new JMenuItem("Usuń firmę");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj firmę");
        
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
            jtab.setEnabled(false);
            
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
    
    class Dodaj extends JFrame implements ActionListener{
        JFrame f;
        Dodaj(JFrame f) {
            this.f = f;
        }
        JLabel adr,fir,reg,nip,im,naz;
        JTextField firtf,regtf,niptf,imtf,naztf;
        JButton adrbtn;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(600,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(8,2));
            setLocationRelativeTo(null);
            
            adr = new JLabel("Adres*", SwingConstants.CENTER);
            fir = new JLabel("Nazwa firmy*", SwingConstants.CENTER);
            reg = new JLabel("REGON*", SwingConstants.CENTER);
            nip = new JLabel("NIP*", SwingConstants.CENTER);
            im = new JLabel("Imię właściciela*", SwingConstants.CENTER); 
            naz = new JLabel("Nazwisko właściciela*", SwingConstants.CENTER); 
            
            firtf = new JTextField(100);
            regtf = new JTextField(9);
            niptf = new JTextField(10);
            imtf = new JTextField(40);
            naztf = new JTextField(50);
            
            adrbtn = new JButton("Wybierz adres");
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(adr);add(adrbtn);add(fir);add(firtf);add(reg);add(regtf);
            add(nip);add(niptf);add(im);add(imtf);add(naz);add(naztf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            adr.setBorder(BorderFactory.createLineBorder(Color.black));
            fir.setBorder(BorderFactory.createLineBorder(Color.black));
            reg.setBorder(BorderFactory.createLineBorder(Color.black));
            nip.setBorder(BorderFactory.createLineBorder(Color.black));
            im.setBorder(BorderFactory.createLineBorder(Color.black));
            naz.setBorder(BorderFactory.createLineBorder(Color.black));
            
            adrbtn.addActionListener(new Wybierz());
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this,id_pr));
            
        }
        
        class Insert implements ActionListener{
         String fir,reg,nip,im,naz;
         Pattern pfir,preg,pnip,pim,pnaz;
         Matcher mfir,mreg,mnip,mim,mnaz;
         Connection con1;
          public void actionPerformed(ActionEvent e) {
               
               fir = firtf.getText();
               reg = regtf.getText();
               nip = niptf.getText();
               im = imtf.getText().toLowerCase();
               naz = naztf.getText().toLowerCase();
               
               pfir = Pattern.compile("[^\\s].+");
               preg = Pattern.compile("[0-9]{9}");
               pnip = Pattern.compile("[0-9]{10}");
               pim = Pattern.compile("[A-Za-ząćęłńóśźż]+");
               pnaz = Pattern.compile("[A-Za-z]+(.[A-Za-ząćęłńóśźż]+)?");
               
               mfir = pfir.matcher(fir);
               mreg = preg.matcher(reg);
               mnip = pnip.matcher(nip);
               mim = pim.matcher(im);
               mnaz = pnaz.matcher(naz);
               
               if(id_adr == -1) {
                   JOptionPane.showConfirmDialog(null, "Adres nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mfir.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa firmy\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mreg.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"REGON\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnip.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"NIP\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 0123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mim.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Imię właściciela\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnaz.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwisko właściciela\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mfir.matches() && mreg.matches() && mnip.matches() && mim.matches()
                       && mnaz.matches() && id_adr != -1) {
                   im = im.substring(0, 1).toUpperCase() + im.substring(1).toLowerCase();
                   naz = naz.substring(0, 1).toUpperCase() + naz.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into firmy values"
                    + "("+ id_adr +",'"+fir+"','"+reg+"','"+nip+"','"+im+"','"+naz+"')";
            
            ask.addBatch(sql);
            ask.executeBatch();
            
            firtf.setText("");
            regtf.setText("");
            niptf.setText("");
            imtf.setText("");
            naztf.setText("");
        
            id_adr = -1;
            
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
        String sfir,sreg,snip,sim,snaz;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz firmę");
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
                String sql = "select * from firmy";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_fir = Integer.parseInt(wynik.getString(1));
                        id_adr = Integer.parseInt(wynik.getString(2));
                        sfir = wynik.getString(3); 
                        sreg = wynik.getString(4);
                        snip = wynik.getString(5);
                        sim = wynik.getString(6);
                        snaz = wynik.getString(7);
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujFirme();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(600,300);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(8,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujFirme extends JFrame{
            JLabel adr,fir,reg,nip,im,naz;
            JTextField firtf,regtf,niptf,imtf,naztf;
            JButton adrbtn,upd,exit,powrot,menu;
            AktualizujFirme(){
                adr = new JLabel("Adres*", SwingConstants.CENTER);
                fir = new JLabel("Nazwa firmy*", SwingConstants.CENTER);
                reg = new JLabel("REGON*", SwingConstants.CENTER);
                nip = new JLabel("NIP*", SwingConstants.CENTER);
                im = new JLabel("Imię właściciela*", SwingConstants.CENTER); 
                naz = new JLabel("Nazwisko właściciela*", SwingConstants.CENTER);

                firtf = new JTextField(sfir,100); 
                regtf = new JTextField(sreg,9); 
                niptf = new JTextField(snip,10);
                imtf = new JTextField(sim,30);
                naztf = new JTextField(snaz,40);
                
                adrbtn = new JButton("Wybierz adres");
                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(adr);add(adrbtn);add(fir);add(firtf);add(reg);add(regtf);
                add(nip);add(niptf);add(im);add(imtf);add(naz);add(naztf);

                add(upd);add(powrot);add(menu);add(exit);

                adr.setBorder(BorderFactory.createLineBorder(Color.black));
                fir.setBorder(BorderFactory.createLineBorder(Color.black));
                reg.setBorder(BorderFactory.createLineBorder(Color.black));
                nip.setBorder(BorderFactory.createLineBorder(Color.black));
                im.setBorder(BorderFactory.createLineBorder(Color.black));
                naz.setBorder(BorderFactory.createLineBorder(Color.black));

                adrbtn.addActionListener(new Wybierz());
                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this,id_pr));
            }
        
        
            
        class Update implements ActionListener{
        JFrame f;
        Pattern pfir,preg,pnip,pim,pnaz;
        Matcher mfir,mreg,mnip,mim,mnaz;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String fir,reg,nip,im,naz;
         Connection con1;
               fir = firtf.getText();
               reg = regtf.getText();
               nip = niptf.getText();
               im = imtf.getText().toLowerCase();
               naz = naztf.getText().toLowerCase();
          
               pfir = Pattern.compile("[^\\s].+");
               preg = Pattern.compile("[0-9]{9}");
               pnip = Pattern.compile("[0-9]{10}");
               pim = Pattern.compile("[A-Za-ząćęłńóśźż]+");
               pnaz = Pattern.compile("[A-Za-z]+(.[A-Za-ząćęłńóśźż]+)?");
               
               mfir = pfir.matcher(fir);
               mreg = preg.matcher(reg);
               mnip = pnip.matcher(nip);
               mim = pim.matcher(im);
               mnaz = pnaz.matcher(naz);
               
               if(id_adr == -1) {
                   JOptionPane.showConfirmDialog(null, "Adres nie został wybrany", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               }
               
               if(mfir.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwa firmy\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mreg.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"REGON\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnip.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"NIP\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 0123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mim.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Imię właściciela\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnaz.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nazwisko właściciela\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone są tylko litery.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mfir.matches() && mreg.matches() && mnip.matches() && mim.matches()
                       && mnaz.matches() && id_adr != -1) {
                   im = im.substring(0, 1).toUpperCase() + im.substring(1).toLowerCase();
                   naz = naz.substring(0, 1).toUpperCase() + naz.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update firmy set id_adres = "+ id_adr +","
                    + "nazwa_firma ='"+fir+"',REGON = '"+reg+"',"
                    + "NIP = '"+nip+"',imie_wlasciciel = '"+im+"',"
                    + "nazwisko_wlasciciel='"+naz+"' where id_firma = "+id_fir;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            id_adr = -1;
            
            ask.close();
            con1.close();
            f.dispose();
            new Firmy(id_pr);
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
            
            setTitle("Wybierz firmę");
            setLocationRelativeTo(null);
            setSize(720, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Firmy\"");
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
                String sql = "select id_firma from firmy";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_fir = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete firmy where id_firma = " + id_fir;
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
                setSize(600, 200);
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
            new Firmy(id_pr);
        }
    }
}

