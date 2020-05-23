package Klient;

import AStart.AStart;
import AStart.Wyjscie;
import Pracownik.Adresy;
import Pracownik.Firmy;
import Pracownik.Kategorie;
import Pracownik.Klienci;
import Pracownik.Kontakty;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

public class AKlient extends JFrame{
    JButton produkty,zamowienia,faktury,wyloguj,wyjscie;
    Icon gun,order,receipt,logout,exit;
    int id_kl;
    public AKlient(int id_kl) {
        this.id_kl = id_kl;
        setVisible(true);
        setSize(300,450);
        setTitle("Klient");
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5,1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        gun = new ImageIcon(Class.class.getResource("/images/gun.png"));
        order = new ImageIcon(Class.class.getResource("/images/order.png"));
        receipt = new ImageIcon(Class.class.getResource("/images/receipt.png"));
        logout = new ImageIcon(Class.class.getResource("/images/logout.png"));
        exit = new ImageIcon(Class.class.getResource("/images/exit.png"));
        
        produkty = new JButton("Produkty",gun);
        zamowienia = new JButton("Zamówienia",order);
        faktury = new JButton("Faktury",receipt);
        wyloguj = new JButton("Wyloguj",logout);
        wyjscie = new JButton("Wyjdź",exit);
        
        add(produkty);
        add(zamowienia);
        add(faktury);
        add(wyloguj);
        add(wyjscie);
        
        produkty.addActionListener(new ProduktyBTN());
        zamowienia.addActionListener(new ZamowieniaBTN());
        faktury.addActionListener(new FakturyBTN());
        wyloguj.addActionListener(new WylogujBTN());
        wyjscie.addActionListener(new Wyjscie());
        
        produkty.setHorizontalAlignment(SwingConstants.LEFT);      
        zamowienia.setHorizontalAlignment(SwingConstants.LEFT);
        faktury.setHorizontalAlignment(SwingConstants.LEFT);
        wyloguj.setHorizontalAlignment(SwingConstants.LEFT);
        wyjscie.setHorizontalAlignment(SwingConstants.LEFT);
                    
        produkty.setIconTextGap(60);
        zamowienia.setIconTextGap(52);
        faktury.setIconTextGap(65);
        wyloguj.setIconTextGap(65);
        wyjscie.setIconTextGap(70);
        
        wyloguj.setBackground(Color.LIGHT_GRAY); 
        wyjscie.setBackground(Color.LIGHT_GRAY);
       }
    
    class ProduktyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Produkty(id_kl);
        }
    }
    
    class ZamowieniaBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Zamowienia(id_kl);
        }
    }
    
    class FakturyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Faktury(id_kl);
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