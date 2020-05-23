package Pracownik;

import AStart.AStart;
import AStart.Wyjscie;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

public class APracownik extends JFrame{
    JButton kontakty,adresy,firmy,klienci,kategorie,produkty,
            zamowienia,faktury,wyloguj,wyjscie;
    Icon gun,contact,home,company,client,guns,order,receipt,logout,exit;
    int id_pr;
    public APracownik(int id_pr) {
        this.id_pr = id_pr;
        setVisible(true);
        setSize(300,850);
        setTitle("Pracownik");
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10,1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        gun = new ImageIcon(Class.class.getResource("/images/gun.png"));
        contact = new ImageIcon(Class.class.getResource("/images/contact.png"));
        home = new ImageIcon(Class.class.getResource("/images/home.png"));
        company = new ImageIcon(Class.class.getResource("/images/company.png"));
        client = new ImageIcon(Class.class.getResource("/images/client.png"));
        guns = new ImageIcon(Class.class.getResource("/images/guns.png"));
        order = new ImageIcon(Class.class.getResource("/images/order.png"));
        receipt = new ImageIcon(Class.class.getResource("/images/receipt.png"));
        logout = new ImageIcon(Class.class.getResource("/images/logout.png"));
        exit = new ImageIcon(Class.class.getResource("/images/exit.png"));
        
        kontakty = new JButton("Kontakty",contact);
        adresy = new JButton("Adresy",home);
        firmy = new JButton("Firmy",company);
        klienci = new JButton("Klienci",client);
        kategorie = new JButton("Kategorie",guns);
        produkty = new JButton("Produkty",gun);
        zamowienia = new JButton("Zamówienia",order);
        faktury = new JButton("Faktury",receipt);
        wyloguj = new JButton("Wyloguj",logout);
        wyjscie = new JButton("Wyjdź",exit);
        
        add(kontakty);
        add(adresy);
        add(firmy);
        add(klienci);
        add(kategorie);
        add(produkty);
        add(zamowienia);
        add(faktury);
        add(wyloguj);
        add(wyjscie);
        
        kontakty.addActionListener(new KontaktyBTN());
        adresy.addActionListener(new AdresyBTN());
        firmy.addActionListener(new FirmyBTN());
        klienci.addActionListener(new KlienciBTN());
        kategorie.addActionListener(new KategorieBTN());
        produkty.addActionListener(new ProduktyBTN());
        zamowienia.addActionListener(new ZamowieniaBTN());
        faktury.addActionListener(new FakturyBTN());
        wyloguj.addActionListener(new WylogujBTN());
        wyjscie.addActionListener(new Wyjscie());
        
        kontakty.setHorizontalAlignment(SwingConstants.LEFT);
        adresy.setHorizontalAlignment(SwingConstants.LEFT);
        firmy.setHorizontalAlignment(SwingConstants.LEFT);
        klienci.setHorizontalAlignment(SwingConstants.LEFT);
        kategorie.setHorizontalAlignment(SwingConstants.LEFT);
        produkty.setHorizontalAlignment(SwingConstants.LEFT);      
        zamowienia.setHorizontalAlignment(SwingConstants.LEFT);
        faktury.setHorizontalAlignment(SwingConstants.LEFT);
        wyloguj.setHorizontalAlignment(SwingConstants.LEFT);
        wyjscie.setHorizontalAlignment(SwingConstants.LEFT);
                    
        kontakty.setIconTextGap(63);
        adresy.setIconTextGap(63);
        firmy.setIconTextGap(70);
        klienci.setIconTextGap(67);
        kategorie.setIconTextGap(60);
        produkty.setIconTextGap(60);
        zamowienia.setIconTextGap(52);
        faktury.setIconTextGap(65);
        wyloguj.setIconTextGap(65);
        wyjscie.setIconTextGap(70);
        
        wyloguj.setBackground(Color.LIGHT_GRAY); 
        wyjscie.setBackground(Color.LIGHT_GRAY);
       }
    
    class KontaktyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Kontakty(id_pr);
        }
    }
    
    class AdresyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Adresy(id_pr);
        }
    }
    class FirmyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Firmy(id_pr);
        }
    }
    
    class KlienciBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Klienci(id_pr);
        }
    }
    
    class KategorieBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Kategorie(id_pr);
        }
    }
    
    class ProduktyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Produkty(id_pr);
        }
    }
    
    class ZamowieniaBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Zamowienia(id_pr);
        }
    }
    
    class FakturyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Faktury(id_pr);
        }
    }
    
    class WylogujBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            JFrame f = new AStart();
            f.setVisible(true);
            f.setSize(300,200);
            f.setTitle("Logowanie");
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
