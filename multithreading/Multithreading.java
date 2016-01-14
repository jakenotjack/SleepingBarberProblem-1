/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

import java.util.ArrayList;

/**
 *
 * @author Naima
 */
public class Multithreading 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        if (args.length != 2)
        {
            IllegalArgumentException ex = new IllegalArgumentException("Two arguments needed");
            throw ex;
        }
        int numOfCustomer = Integer.parseInt(args[0]);
        int timeInSec = Integer.parseInt(args[1]);
        
        Multithreading mt = new Multithreading();
        BarbarShop bShop = new BarbarShop();
        ArrayList<Barbar> barbarList = mt.CreateBarbars(bShop);
        CustomerManager cm = new CustomerManager(bShop, numOfCustomer);
        cm.start();
        Printer printer = new Printer(bShop, cm, barbarList, timeInSec);
        printer.start();
        while(printer.isAlive())
            ;
        System.out.println("Exiting");
        System.exit(0);
    }
    
    private ArrayList<Barbar> CreateBarbars(BarbarShop bShop)
    {
        ArrayList<Barbar> barbarList = new ArrayList<Barbar>();
        for(int i=0; i<3; i++)
        {
            Barbar b = new Barbar(bShop, i);
            barbarList.add(b);
            b.start();
        }
        return barbarList;
    }
}
