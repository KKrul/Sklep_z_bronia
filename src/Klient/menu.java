package Klient;

import java.awt.event.*;
import javax.swing.JFrame;

public class menu implements ActionListener{
    JFrame f;
    int id_kl;
    menu(JFrame f, int id_kl){
        this.f = f;
        this.id_kl = id_kl;
    }
    public void actionPerformed(ActionEvent  e){
        f.dispose();
        new AKlient(id_kl);
    }
}
