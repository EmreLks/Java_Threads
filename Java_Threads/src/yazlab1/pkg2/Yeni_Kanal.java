/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1.pkg2;

import java.util.Stack;

/**
 *
 * @author lks
 */
public class Yeni_Kanal implements Runnable{

    Thread t;
    private boolean durum=false;
    private int sira,arastir,sayi,son,bas;
    Stack<Integer> stack= new Stack<Integer>();
    
    Yeni_Kanal(int sayi,int ara,int bsira,int bitis,Stack<Integer> s) {
        
         sira = sayi;
         arastir = ara;
         son = bitis;
         bas = bsira;
         for(int i=bsira;i<bitis;i++)
         {
             stack.push(s.get(i));
         }
         t = new Thread(this,"Thread " + sayi);
         t.start();
    }
    public String  getName()
    {
        return t.getName();
    }
    public void run() {
        
        for(int i=0;i<son - bas;i++)
        {
                
                if(arastir%stack.get(i) == 0)
                {
                    durum = true;
                    sayi = stack.get(i);
                    break;
                }
        }
    }
    
    public boolean durum_dondur()
    {
        return durum;
    }
    public int sayi_dondur()
    {
        return sayi;
    }

           
    
}
