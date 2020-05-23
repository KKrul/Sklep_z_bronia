package Pracownik;

import java.awt.event.*;
import javax.swing.JFrame;

public class menu implements ActionListener{
    JFrame f;
    int id_pr;
    menu(JFrame f, int id_pr){
        this.f = f;
        this.id_pr = id_pr;
    }
    public void actionPerformed(ActionEvent  e){
        f.dispose();
        new APracownik(id_pr);
    }
}
