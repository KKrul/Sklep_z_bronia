package Admin;

import AStart.Wyjscie;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

public class Kontakty extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_kon = -1;
    Kontakty(){
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(650, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj kontakt");
        i2 = new JMenuItem("Usuń kontakt");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj kontakt");
        
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
            jtab.setEnabled(false);
            
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
    
class Dodaj extends JFrame implements ActionListener{
        JFrame f;
        Dodaj(JFrame f) {
            this.f = f;
        }
        Dodaj(){}
        JLabel nr1,nr2,fax,mail,www;
        JTextField nr1tf,nr2tf,faxtf,mailtf,wwwtf;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(600,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(7,2));
            setLocationRelativeTo(null);
            
            nr1 = new JLabel("Nr telefonu 1*", SwingConstants.CENTER);
            nr2 = new JLabel("Nr telefonu 2", SwingConstants.CENTER);
            fax = new JLabel("Fax", SwingConstants.CENTER);
            mail = new JLabel("Email*", SwingConstants.CENTER);
            www = new JLabel("Strona www", SwingConstants.CENTER); 
            
            nr1tf = new JTextField(9); 
            nr2tf = new JTextField(9); 
            faxtf = new JTextField(9);
            mailtf = new JTextField(30);
            wwwtf = new JTextField(50);
            
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(nr1);add(nr1tf);add(nr2);add(nr2tf);add(fax);add(faxtf);
            add(mail);add(mailtf);add(www);add(wwwtf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            nr1.setBorder(BorderFactory.createLineBorder(Color.black));
            nr2.setBorder(BorderFactory.createLineBorder(Color.black));
            fax.setBorder(BorderFactory.createLineBorder(Color.black));
            mail.setBorder(BorderFactory.createLineBorder(Color.black));
            www.setBorder(BorderFactory.createLineBorder(Color.black));
       
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this));
        }
        
        class Insert implements ActionListener{
         String nr1,nr2,fax,mail,www;
         Connection con1;
         Pattern pnr1,pnr2,pfax,pmail,pwww;
         Matcher mnr1,mnr2,mfax,mmail,mwww;
          public void actionPerformed(ActionEvent e) {
               nr1 = nr1tf.getText();
               nr2 = nr2tf.getText();
               fax = faxtf.getText();
               mail = mailtf.getText().toLowerCase();
               www = wwwtf.getText().toLowerCase();
               
               pnr1 = Pattern.compile("[0-9]{9}");
               pnr2 = Pattern.compile("([0-9]{9})?");
               pfax = Pattern.compile("([0-9]{9})?");
               pmail = Pattern.compile("[A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-z]+)?@[A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-z]+){1,}");
               pwww = Pattern.compile("([A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-ząćęłńóśźż]+){1,})?");
               
               mnr1 = pnr1.matcher(nr1);
               mnr2 = pnr2.matcher(nr2);
               mfax = pfax.matcher(fax);
               mmail = pmail.matcher(mail);
               mwww = pwww.matcher(www);
               
               if(mnr1.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nr tel 1\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnr2.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nr tel 2\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mfax.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Fax\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmail.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Email\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolony format to np. kam@op.pl", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwww.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Strona www\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolony format to np. www.extremadamente.com", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnr1.matches() && mnr2.matches() && mfax.matches() && mmail.matches()
                       && mwww.matches()) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into kontakty values"
                    + "('"+ nr1 +"','"+nr2+"','"+fax+"','"+mail+"','"+www+"')";
            
            ask.addBatch(sql);
            ask.executeBatch(); 
            
            nr1tf.setText("");
            nr2tf.setText("");
            faxtf.setText("");
            mailtf.setText("");
            wwwtf.setText("");
            
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
        int id_kon;
        String snr1,snr2,sfax,smail,swww;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz kontakt");
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
                String sql = "select * from kontakty";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kon = Integer.parseInt(wynik.getString(1));
                        snr1 = wynik.getString(2); 
                        snr2 = wynik.getString(3); 
                        sfax = wynik.getString(4);
                        smail = wynik.getString(5);
                        swww = wynik.getString(6);
                        break;}
                con.close();
        }
        catch(SQLException error_polaczenie) {
            System.out.println("Błąd połączenia z bazą danych " + error_polaczenie);}
        catch(ClassNotFoundException error_sterownik) {
            System.out.println("Brak sterownika");}
                 f = new AktualizujAdres();
                 f.setTitle("* pola wymagane");
                f.setVisible(true);
                f.setSize(600,300);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new GridLayout(7,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujAdres extends JFrame{
            JLabel nr1,nr2,fax,mail,www;
            JTextField nr1tf,nr2tf,faxtf,mailtf,wwwtf;
            JButton upd,exit,powrot,menu;
            AktualizujAdres(){
                

                nr1 = new JLabel("Nr tel 1*", SwingConstants.CENTER);
                nr2 = new JLabel("Nr tel 2", SwingConstants.CENTER);
                fax = new JLabel("Fax", SwingConstants.CENTER);
                mail = new JLabel("Email*", SwingConstants.CENTER);
                www = new JLabel("Strona www", SwingConstants.CENTER); 

                nr1tf = new JTextField(snr1,50); 
                nr2tf = new JTextField(snr2,50); 
                faxtf = new JTextField(sfax,50);
                mailtf = new JTextField(smail,6);
                wwwtf = new JTextField(swww,50);

                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(nr1);add(nr1tf);add(nr2);add(nr2tf);add(fax);add(faxtf);
                add(mail);add(mailtf);add(www);add(wwwtf);

                add(upd);add(powrot);add(menu);add(exit);

                nr1.setBorder(BorderFactory.createLineBorder(Color.black));
                nr2.setBorder(BorderFactory.createLineBorder(Color.black));
                fax.setBorder(BorderFactory.createLineBorder(Color.black));
                mail.setBorder(BorderFactory.createLineBorder(Color.black));
                www.setBorder(BorderFactory.createLineBorder(Color.black));

                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this));
                
            }
        
        
        class Update implements ActionListener{
        JFrame f;
        Pattern pnr1,pnr2,pfax,pmail,pwww;
        Matcher mnr1,mnr2,mfax,mmail,mwww;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String nr1,nr2,fax,mail,www;
               Connection con1;
               nr1 = nr1tf.getText();
               nr2 = nr2tf.getText();
               fax = faxtf.getText();
               mail = mailtf.getText().toLowerCase();
               www = wwwtf.getText().toLowerCase();  
               
               pnr1 = Pattern.compile("[0-9]{9}");
               pnr2 = Pattern.compile("([0-9]{9})?");
               pfax = Pattern.compile("([0-9]{9})?");
               pmail = Pattern.compile("[A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-z]+)?@[A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-z]+){1,}");
               pwww = Pattern.compile("([A-Za-z0-9ąćęłńóśźż]+(\\.[A-Za-ząćęłńóśźż]+){1,})?");
               
               mnr1 = pnr1.matcher(nr1);
               mnr2 = pnr2.matcher(nr2);
               mfax = pfax.matcher(fax);
               mmail = pmail.matcher(mail);
               mwww = pwww.matcher(www);
               
               if(mnr1.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nr tel 1\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mnr2.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Nr tel 2\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mfax.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Fax\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr, np. 123456789", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mmail.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Email\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolony format to np. kam@op.pl", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwww.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Strona www\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolony format to np. www.extremadamente.com", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mnr1.matches() && mnr2.matches() && mfax.matches() && mmail.matches()
                       && mwww.matches()) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update kontakty set nr_tel_1 = '"+ nr1 +"',"
                    + "nr_tel_2 ='"+nr2+"',fax = '"+fax+"',"
                    + "email = '"+mail+"',strona_www = '"+www+"'"
                    + "where id_kontakt = "+id_kon;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            f.dispose();
            new Kontakty();
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
            
            setTitle("Wybierz kontakt");
            setLocationRelativeTo(null);
            setSize(600, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Kontakty\"");
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
                String sql = "select id_kontakt from kontakty";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_kon = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete kontakty where id_kontakt = " + id_kon;
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
            new Kontakty();
        }
    }
}
 
