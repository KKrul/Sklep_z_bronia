package Admin;

import java.awt.event.*;
import javax.swing.JFrame;

public class menu implements ActionListener{
    JFrame f;
    menu(JFrame f){
        this.f = f;
    }
    public void actionPerformed(ActionEvent  e){
        f.dispose();
        new Admin();
    }
}
