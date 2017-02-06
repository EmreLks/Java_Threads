/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1.pkg2;


import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lks
 */
public class Ekle_Kanal implements Runnable{

    Thread k;
    Stack<Integer> ara = new Stack<Integer>();
    int ekle;
    Statement ifade;
    
    public Ekle_Kanal(Stack<Integer> giris,int ek,Statement ifade2) {
        
        ifade = ifade2;
        ara = giris;
        ekle = ek;
        k = new Thread(this,"Ekle Kanal");
        k.start();
    }

    
    public void run() {
        
        try {
            ifade.executeUpdate("insert into asaltablo(id,sayi) values('"+(ara.size()+1)+"' , '"+ekle+"' )");
        } catch (SQLException ex) {
            Logger.getLogger(Ekle_Kanal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

