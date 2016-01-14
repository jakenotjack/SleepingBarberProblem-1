/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naima
 */
public class Printer extends Thread
{
    private final BarbarShop _shop;
    private final CustomerManager _cm;
    private final ArrayList<Barbar> _barbarList;
    private final int _totalTime; 
    
    public Printer(BarbarShop bShop, CustomerManager cm, ArrayList<Barbar> barbarList, int time)
    {
        _shop = bShop;
        _cm = cm;
        _barbarList = barbarList;
        _totalTime = time*1000;//to make it milisec
    }
    
    public void run()
    {
        long t= System.currentTimeMillis();
        long end = t+_totalTime;
        while(System.currentTimeMillis()< end) 
            ;
        synchronized(this)
        {
            int customerInChair = _shop.GetCustCountInChair();
            System.out.println("Number of customer in chair : " + customerInChair);

            int customerWaitingForCashOut = _shop.GetNumOfCustomerWaitingForCashOut();
            System.out.println("Number of customer in waiting for cashout : " + customerWaitingForCashOut);

            int customerTotal = _shop.GetCurrentTotalCustomerNum();
            int customerInWaiting = customerTotal - (customerInChair + customerWaitingForCashOut);
            int customerInSofa = 0;
            if(customerInWaiting < 4 && customerInWaiting>=0)
                customerInSofa = customerInWaiting;
            else if(customerInWaiting < 0)
                customerInSofa = 0;
            else
                customerInSofa = 4;
            System.out.println("Number of customer in sofa : " + customerInSofa);

            int customerInStanding = customerInWaiting - customerInSofa;
            if(customerInStanding < 0)
                customerInStanding = 0;
            System.out.println("Number of customer in standing : " + customerInStanding);

            int customerCreated = _cm.GetTotalNumOfCustomerCreated();
            System.out.println("Number of customer created : " + customerCreated);

            int customerLeftWithoutHairCut = _cm.GetNumOfCustomerLeftWithoutHairCut();
            System.out.println("Number of customer in left without having haircut : " + customerLeftWithoutHairCut);

            Barbar barbar0 = _barbarList.get(0);
            System.out.println("Barbar #0 is " + barbar0.GetStatus());

            Barbar barbar1 = _barbarList.get(1);
            System.out.println("Barbar #1 is " + barbar1.GetStatus());

            Barbar barbar2 = _barbarList.get(2);
            System.out.println("Barbar #2 is " + barbar2.GetStatus());
        }
    } 
}
