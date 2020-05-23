package Admin;

import AStart.Wyjscie;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

public class Adresy extends JFrame{
    Connection con;
    JMenu menu;
    JMenuBar mb;
    JMenuItem i1,i2,i3,i4,i5;
    int id_adr = -1;
    Adresy(){
        String tab[][];
        List<String[]> list = new ArrayList<String[]>();
        
        setLocationRelativeTo(null);
        setSize(725, 200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mb = new JMenuBar(); 
        menu = new JMenu("Menu");
        i1 = new JMenuItem("Dodaj adres");
        i2 = new JMenuItem("Usuń adres");
        i3 = new JMenuItem("Powrót do menu");
        i4 = new JMenuItem("Wyjście");
        i5 = new JMenuItem("Aktualizuj adres");
        
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
            jtab.setEnabled(false);
            
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
    
    class Dodaj extends JFrame implements ActionListener{
        JFrame f;
        Dodaj(JFrame f) {
            this.f = f;
        }
        JLabel msc, pow, woj, kod, ul, nrb, nrl;
        JTextField msctf, powtf, wojtf, kodtf, ultf, nrbtf, nrltf;
        JButton dodaj,exit,powrot,menu;
        public void actionPerformed(ActionEvent e){
            f.dispose();
            setTitle("* pola wymagane");
            setVisible(true);
            setSize(600,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(9,2));
            setLocationRelativeTo(null);
            
            msc = new JLabel("Miejscowość*", SwingConstants.CENTER);
            pow = new JLabel("Powiat*", SwingConstants.CENTER);
            woj = new JLabel("Województwo*", SwingConstants.CENTER);
            kod = new JLabel("Kod*", SwingConstants.CENTER);
            ul = new JLabel("Ulica*", SwingConstants.CENTER); 
            nrb = new JLabel("Nr budynku*", SwingConstants.CENTER); 
            nrl = new JLabel("Nr lokalu", SwingConstants.CENTER);
            
            msctf = new JTextField(50); 
            powtf = new JTextField(50); 
            wojtf = new JTextField(50);
            kodtf = new JTextField(6);
            ultf = new JTextField(50);
            nrbtf = new JTextField(6);
            nrltf = new JTextField(6);
            
            dodaj = new JButton("Dodaj");
            exit = new JButton("Wyjście");
            menu = new JButton("Menu");
            powrot = new JButton("Powrot");
            
            add(msc);add(msctf);add(pow);add(powtf);add(woj);add(wojtf);add(kod);
            add(kodtf);add(ul);add(ultf);add(nrb);add(nrbtf);add(nrl);add(nrltf);
            
            add(dodaj);add(powrot);add(menu);add(exit);
            
            msc.setBorder(BorderFactory.createLineBorder(Color.black));
            pow.setBorder(BorderFactory.createLineBorder(Color.black));
            woj.setBorder(BorderFactory.createLineBorder(Color.black));
            kod.setBorder(BorderFactory.createLineBorder(Color.black));
            ul.setBorder(BorderFactory.createLineBorder(Color.black));
            nrb.setBorder(BorderFactory.createLineBorder(Color.black));
            nrl.setBorder(BorderFactory.createLineBorder(Color.black));
       
            dodaj.addActionListener(new Insert());
            powrot.addActionListener(new Powrot(this));
            exit.addActionListener(new Wyjscie());
            menu.addActionListener(new menu(this));
            
        }
    
    
        class Insert implements ActionListener{
         String msc,pow,woj,kod,ul;
         int nrb,nrl;
         Connection con1;
         Pattern pmsc,ppow,pwoj,pkod,pul;
         Matcher mmsc,mpow,mwoj,mkod,mul;
          public void actionPerformed(ActionEvent e) {
               msc = msctf.getText().toLowerCase();
               pow = powtf.getText().toLowerCase();
               woj = wojtf.getText().toLowerCase();
               kod = kodtf.getText();
               ul = ultf.getText().toLowerCase();
               
               try{
               nrb = Integer.parseInt(nrbtf.getText());}
               catch(NumberFormatException ex) {
               JOptionPane.showConfirmDialog(null, "Pole \"Nr budynku\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
               
               try{
               if(nrltf.getText().equals(""))
                    nrl = -1;
               else
                    nrl = Integer.parseInt(nrltf.getText());}
               catch(NumberFormatException ex) {
               JOptionPane.showConfirmDialog(null, "Pole \"Nr lokalu\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
               
               pmsc = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               ppow = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pwoj = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pkod = Pattern.compile("[0-9]{2}-[0-9]{3}");
               pul = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               
               mmsc = pmsc.matcher(msc);
               mpow = ppow.matcher(pow);
               mwoj = pwoj.matcher(woj);
               mkod = pkod.matcher(kod);
               mul = pul.matcher(ul);
               
               if(mmsc.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Miejscowość\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mpow.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Powiat\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwoj.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Województwo\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mkod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Kod pocztowy\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolony format to np. 11-111", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mul.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Ulica\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
              
          
               if(mmsc.matches() && mpow.matches() && mwoj.matches() && mkod.matches()
                       && mul.matches() && nrb > 0 && (nrl > 0 || nrl == -1)) {
                   msc = msc.substring(0, 1).toUpperCase() + msc.substring(1).toLowerCase();
                   woj = woj.substring(0, 1).toUpperCase() + woj.substring(1).toLowerCase();
                   pow = pow.substring(0, 1).toUpperCase() + pow.substring(1).toLowerCase();
                   ul = ul.substring(0, 1).toUpperCase() + ul.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "insert into adresy values"
                    + "('"+ msc +"','"+pow+"','"+woj+"','"+kod+"','"+ul+"',"+nrb+","+nrl+")";
            
            ask.addBatch(sql);
            ask.executeBatch();
            
            msctf.setText("");
            powtf.setText("");
            wojtf.setText("");
            kodtf.setText("");
            ultf.setText("");
            nrbtf.setText("");
            nrltf.setText("");
            
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
        String smsc, spow, swoj, skod, sul,snrb, snrl;
        Aktualizuj(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
            String tab[][];
            List<String[]> list = new ArrayList<String[]>();
            
            f.dispose();
            setTitle("Wybierz adres");
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
                String sql = "select * from adresy";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_adr = Integer.parseInt(wynik.getString(1));
                        smsc = wynik.getString(2); 
                        spow = wynik.getString(3); 
                        swoj = wynik.getString(4);
                        skod = wynik.getString(5);
                        sul = wynik.getString(6);
                        snrb = wynik.getString(7);
                        snrl = wynik.getString(8);
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
                f.setLayout(new GridLayout(9,2));
                f.setLocationRelativeTo(null);
                 dispose();                
             }
          }
        
        class AktualizujAdres extends JFrame{
            JLabel msc, pow, woj, kod, ul, nrb, nrl;
            JTextField msctf, powtf, wojtf, kodtf, ultf, nrbtf, nrltf;
            JButton upd,exit,powrot,menu;
            AktualizujAdres(){
                msc = new JLabel("Miejscowość*", SwingConstants.CENTER);
                pow = new JLabel("Powiat*", SwingConstants.CENTER);
                woj = new JLabel("Województwo*", SwingConstants.CENTER);
                kod = new JLabel("Kod*", SwingConstants.CENTER);
                ul = new JLabel("Ulica*", SwingConstants.CENTER); 
                nrb = new JLabel("Nr budynku*", SwingConstants.CENTER); 
                nrl = new JLabel("Nr lokalu", SwingConstants.CENTER);

                msctf = new JTextField(smsc,50); 
                powtf = new JTextField(spow,50); 
                wojtf = new JTextField(swoj,50);
                kodtf = new JTextField(skod,6);
                ultf = new JTextField(sul,50);
                nrbtf = new JTextField(snrb,6);
                nrltf = new JTextField(snrl,6);

                upd = new JButton("Aktualizuj");
                exit = new JButton("Wyjście");
                menu = new JButton("Menu");
                powrot = new JButton("Powrot");

                add(msc);add(msctf);add(pow);add(powtf);add(woj);add(wojtf);add(kod);
                add(kodtf);add(ul);add(ultf);add(nrb);add(nrbtf);add(nrl);add(nrltf);

                add(upd);add(powrot);add(menu);add(exit);

                msc.setBorder(BorderFactory.createLineBorder(Color.black));
                pow.setBorder(BorderFactory.createLineBorder(Color.black));
                woj.setBorder(BorderFactory.createLineBorder(Color.black));
                kod.setBorder(BorderFactory.createLineBorder(Color.black));
                ul.setBorder(BorderFactory.createLineBorder(Color.black));
                nrb.setBorder(BorderFactory.createLineBorder(Color.black));
                nrl.setBorder(BorderFactory.createLineBorder(Color.black));

                upd.addActionListener(new Update(this));
                powrot.addActionListener(new Powrot(this));
                exit.addActionListener(new Wyjscie());
                menu.addActionListener(new menu(this));
                
            }
        
        
        class Update implements ActionListener{
        JFrame f;
        Pattern pmsc,ppow,pwoj,pkod,pul;
        Matcher mmsc,mpow,mwoj,mkod,mul;
        Update(JFrame f) {
            this.f = f;
        }
        public void actionPerformed(ActionEvent e){
        String msc,pow,woj,kod,ul;
         int nrb,nrl=0;
         Connection con1;
               msc = msctf.getText().toLowerCase();
               pow = powtf.getText().toLowerCase();
               woj = wojtf.getText().toLowerCase();
               kod = kodtf.getText();
               ul = ultf.getText().toLowerCase();
               nrb = Integer.parseInt(nrbtf.getText());
               
               try{
               nrb = Integer.parseInt(nrbtf.getText());}
               catch(NumberFormatException ex) {
               JOptionPane.showConfirmDialog(null, "Pole \"Nr budynku\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko cyfr.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
               
               try{
               if(nrltf.getText().equals(""))
                    nrl = -1;
               else
                    nrl = Integer.parseInt(nrltf.getText());}
               catch(NumberFormatException ex) {
               JOptionPane.showConfirmDialog(null, "Pole \"Nr lokalu\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole może być puste lub dozwolone użycie tylko cyfr.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);}
               
               pmsc = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               ppow = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pwoj = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               pkod = Pattern.compile("[0-9]{2}-[0-9]{3}");
               pul = Pattern.compile("[A-Za-ząćęłńóśźż]+(.[A-Za-ząćęłńóśźż]+)?");
               
               mmsc = pmsc.matcher(msc);
               mpow = ppow.matcher(pow);
               mwoj = pwoj.matcher(woj);
               mkod = pkod.matcher(kod);
               mul = pul.matcher(ul);
               
               if(mmsc.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Miejscowość\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mpow.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Powiat\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mwoj.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Województwo\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mkod.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Kod pocztowy\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolony format to np. 11-111", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
               
               if(mul.matches() == false)
                   JOptionPane.showConfirmDialog(null, "Pole \"Ulica\" nie jest poprawnie wypełnione!\n\n"
                           + "Pole nie może być puste i dozwolone użycie tylko liter.", 
                    "Błąd", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
          
               if(mmsc.matches() && mpow.matches() && mwoj.matches() && mkod.matches()
                       && mul.matches() && nrb > 0 && (nrl > 0 || nrl == -1)) {
                   msc = msc.substring(0, 1).toUpperCase() + msc.substring(1).toLowerCase();
                   woj = woj.substring(0, 1).toUpperCase() + woj.substring(1).toLowerCase();
                   pow = pow.substring(0, 1).toUpperCase() + pow.substring(1).toLowerCase();
                   ul = ul.substring(0, 1).toUpperCase() + ul.substring(1).toLowerCase();
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con1 = DriverManager.getConnection("jdbc:sqlserver://"
                    + "localhost:1433;databaseName=bd_java;" +
                    "user=kkrol;password=2@sql2@;");
            Statement ask = con1.createStatement();
            String sql = "update adresy set miejscowosc = '"+ msc +"',"
                    + "powiat ='"+pow+"',wojewodztwo = '"+woj+"',"
                    + "kod_pocztowy = '"+kod+"',ulica = '"+ul+"',"
                    + "nr_budynku="+nrb+",nr_lokalu = "+nrl+" where id_adres = "+id_adr;
                    
            ask.addBatch(sql);
            ask.executeBatch();
            
            ask.close();
            con1.close();
            new Adresy();
            f.dispose();
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
            
            setTitle("Wybierz adres");
            setLocationRelativeTo(null);
            setSize(600, 200);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            mb = new JMenuBar(); 
            menu = new JMenu("Menu");
            i2 = new JMenuItem("Powrót do \"Adresy\"");
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
                String sql = "select id_adres from adresy";
                ResultSet wynik = ask.executeQuery(sql);

                while(wynik.next())
                    if(wynik.getRow() == row){
                        id_adr = Integer.parseInt(wynik.getString(1));
                        break;}
                ask.close();
                Statement ask2 = con1.createStatement();
                String sql2 = "delete adresy where id_adres = " + id_adr;
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
            new Adresy();
        }
    }
 }

