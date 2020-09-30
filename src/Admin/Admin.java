package Admin;

import AStart.AStart;
import AStart.Wyjscie;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;


public class Admin extends JFrame {
    JButton kontakty, adresy, firmy, klienci, pracownicy, kategorie, produkty,
            zamowienia, faktury, wyloguj, wyjscie;
    Icon gun, contact, home, company, client, worker, guns, order, receipt, logout, exit;

    public Admin() {
        setVisible(true);
        setSize(300, 900);
        setTitle("Admin");
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gun = new ImageIcon(getClass().getResource("/images/gun.png"));
        contact = new ImageIcon(getClass().getResource("/images/contact.png"));
        home = new ImageIcon(getClass().getResource("/images/home.png"));
        company = new ImageIcon(getClass().getResource("/images/company.png"));
        client = new ImageIcon(getClass().getResource("/images/client.png"));
        worker = new ImageIcon(getClass().getResource("/images/workers.png"));
        guns = new ImageIcon(getClass().getResource("/images/guns.png"));
        order = new ImageIcon(getClass().getResource("/images/order.png"));
        receipt = new ImageIcon(getClass().getResource("/images/receipt.png"));
        logout = new ImageIcon(getClass().getResource("/images/logout.png"));
        exit = new ImageIcon(getClass().getResource("/images/exit.png"));


        kontakty = new JButton("Kontakty", contact);
        adresy = new JButton("Adresy", home);
        firmy = new JButton("Firmy", company);
        klienci = new JButton("Klienci", client);
        pracownicy = new JButton("Pracownicy", worker);
        kategorie = new JButton("Kategorie", guns);
        produkty = new JButton("Produkty", gun);
        zamowienia = new JButton("Zamówienia", order);
        faktury = new JButton("Faktury", receipt);
        wyloguj = new JButton("Wyloguj", logout);
        wyjscie = new JButton("Wyjdź", exit);

        add(kontakty);
        add(adresy);
        add(firmy);
        add(klienci);
        add(pracownicy);
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
        pracownicy.addActionListener(new PracownicyBTN());
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
        pracownicy.setHorizontalAlignment(SwingConstants.LEFT);
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
        pracownicy.setIconTextGap(55);
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
            new Kontakty();
        }
    }

    class AdresyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Adresy();
        }
    }

    class FirmyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Firmy();
        }
    }

    class KlienciBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Klienci();
        }
    }

    class PracownicyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Pracownicy();
        }
    }

    class KategorieBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Kategorie();
        }
    }

    class ProduktyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Produkty();
        }
    }

    class ZamowieniaBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Zamowienia();
        }
    }

    class FakturyBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Faktury();
        }
    }

    class WylogujBTN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            JFrame f = new AStart();
            f.setVisible(true);
            f.setSize(300, 200);
            f.setTitle("Logowanie");
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
