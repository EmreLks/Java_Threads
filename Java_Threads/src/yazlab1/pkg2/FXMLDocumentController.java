/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1.pkg2;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author lks
 */
public class FXMLDocumentController implements Initializable {
    //////////////////////////////
    private Connection conn;
    private String url="jdbc:oracle:thin:@localhost:1521:XE";
    private String driver="oracle.jdbc.driver.OracleDriver";
    private String userAdi="yazlab";
    private String userPass="7561";
    public ResultSet res;
    public Statement st;
    private int ThreadSayi,normal,son,yeniSayi,basladimi=0;
    Stack<Integer> sayilar = new Stack<Integer>();
    Stack<Integer> sonDizi = new Stack<Integer>();
    Stack<Integer> ilkDizi = new Stack<Integer>();
    ///////////////////////////////
    
    public Statement baglanti_ac() throws Exception
    {
        Class.forName(driver).newInstance();
        conn=DriverManager.getConnection(url,userAdi,userPass);
        return (Statement) conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    }
     
    private Stack<Integer> Oku()
    {
        Stack<Integer> oku = new Stack<Integer>();
        
        try {
            Statement ifade=baglanti_ac();
            String sorgu="Select * From  asaltablo order by id asc";
            ResultSet sonuc=(ResultSet) ifade.executeQuery(sorgu);
            
            while(sonuc.next())
            { 
               oku.push(sonuc.getInt("sayi"));
            }
            
        } catch (Exception ex) {
            System.out.println("Okuma calismiyor");
        }
        
        return oku;
    }
    private void Yaz(Stack<Integer> giris,int ekle)
    {
        try {
            Statement ifade=baglanti_ac();
            Ekle_Kanal ek = new Ekle_Kanal(giris, ekle, ifade);
        } catch (Exception ex) {
            System.out.println("Yazma calismiyor");
        }
         
    }
    private void Sifirla()
    {
        try {
            Statement ifade=baglanti_ac();
            ifade.executeUpdate("delete from asaltablo where id > 6");
        } catch (Exception ex) {
            System.out.println("Sifirlama calismiyor");
        }
        
    }
    private void Basla()
    {
        lsonuc.setText("");
        sayilar = Oku();
        texkanal.setText("");
        texsonuc.setText("");
        sonDizi.removeAllElements();
        ilkDizi.removeAllElements();
        lsifirla.setText(""+sayilar.size());
        larastir.setText(""+yeniSayi);
        
       
        if(sayilar.size() < 100)
        {
            ThreadSayi = 2;
        }
        else
        {
            ThreadSayi = (int)(sayilar.size() /100) + 1;
        }

        normal = Math.round(sayilar.size() / (float)ThreadSayi);
        son = sayilar.size() - normal * (ThreadSayi-1);
       
        for(int j=0;j<=normal*(ThreadSayi-1);j++)
        {
            ilkDizi.push(sayilar.get(j));
        }
        
        for(int j=1;j<=son;j++)
        {
            sonDizi.push(sayilar.get(sayilar.size() - j));
        }
        
        if(sayilar.size() < 100)
        {
            ThreadSayi = 2;
        }
        else
        {
            ThreadSayi = (int)(sayilar.size() /100) + 1;
        }

        normal = Math.round(sayilar.size() / (float)ThreadSayi);
        son = sayilar.size() - normal * (ThreadSayi-1);
        
        
        texkanal.appendText("\t\t\tBas.Nokta\tSon.Nokta\tDurum\n");
        
        for(int i=0;i<ThreadSayi -1 ;i++)
        {
           texkanal.appendText("Thread_" + (i+1) +"\t\t\t"+ilkDizi.get(i*normal)+ "\t\t" + ilkDizi.get((i+1)*normal -1)  +"\t\t\tfalse\n");
        }
        texkanal.appendText("Thread_" + (ThreadSayi) +"\t\t\t"+sonDizi.get(0)+ "\t\t" + sonDizi.get(son-1) + "\t\t\tfalse\n");
        
        basladimi = 1;
        
    }
    private void Goster()
    {
       if(basladimi == 1 )
       {
            Stack<String> name =  new Stack<String>();
            Stack<Integer> sayi =  new  Stack<Integer>();

            texkanal.setText("");
            texsonuc.setText("");
            lsifirla.setText(""+sayilar.size());
            larastir.setText(""+yeniSayi);

            Yeni_Kanal y[] = new Yeni_Kanal[ThreadSayi];

            int bulundu =0;

            texkanal.appendText("\t\t\tBas.Nokta\tSon.Nokta\tDurum\n");
            for(int i=0;i<ThreadSayi;i++)
            {
                if(i == (ThreadSayi - 1))
                {
                    y[i] = new Yeni_Kanal(i, yeniSayi,0,(son-1), sonDizi);
                }
                else
                {
                    y[i] = new Yeni_Kanal(i, yeniSayi,i*normal, (i+1)*normal, ilkDizi);
                }

                try { 
                    y[i].t.sleep(50);
                } catch (InterruptedException ex) {
                    System.out.println("Uyuyamdai");
                }



                if(y[i].durum_dondur() == false)
                {
                    ++bulundu;
                }
                else
                {
                    name.push(y[i].getName());
                    sayi.push(y[i].sayi_dondur());
                }

            }

            if(bulundu == ThreadSayi)
            {
                texsonuc.setText(yeniSayi + " Asal Sayidir \nVeritabanina Eklenmiştir");
                Yaz(sayilar, yeniSayi);
            }
            else
            {
                texsonuc.setText("Sayi Asal Sayi Degildir \n");
                for(int k=0;k<name.size();k++)
                {
                   texsonuc.appendText(name.get(k)+ " de bulunan " + sayi.get(k) + " sayisina bolunmektedir.\n");
                }
            }
            
            for(int i=0;i<ThreadSayi;i++)
            {
               texkanal.appendText("Thread_" + i +"\t\t\t"+y[i].stack.get(0) +   "\t\t" + y[i].stack.get(y[i].stack.size() - 1)  +"\t\t\t"+y[i].durum_dondur()+"\n");
            }


            yeniSayi = yeniSayi + 2;
       }
       else
       {
           lsonuc.setText("Önce Basla Tuşuna Basmalısınız...");
       }
       basladimi = 0;
       
    }
    /////////////////////////////////
    
    @FXML
    private Label lsifirla;
     @FXML
    private Label lsonuc;
      @FXML
    private Label larastir;
    @FXML
    private TextArea texkanal;
     @FXML
    private TextArea texsonuc;
    
    
    @FXML
    private void handlebasla(ActionEvent event) {
        
        Basla(); 
    }
    @FXML
    private void handlebitir(ActionEvent event) {
       Goster();
        
    }
    @FXML
    private void handlecikis(ActionEvent event) {
        System.exit(0);
        
    }
    @FXML
    private void handledondur(ActionEvent event) {
        
        for(int i=0;i<30;i++)
        {
            Basla();
            Goster();
        }
        
        
    }
  
    @FXML
    private void handlesifirla(ActionEvent event) {
        
        Sifirla();
        sayilar = Oku();
        yeniSayi = sayilar.lastElement() + 2;
        Basla();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sayilar = Oku();
        yeniSayi = sayilar.lastElement() + 2;
    }    
    
}
