/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naima
 */
public class Barbar extends Thread
{
    private final int _name;
    private final BarbarShop _shop;
    private String _barbarStatus;
    
    public Barbar(BarbarShop shop, int name)
    {
        _name = name;
        _shop = shop;
        _barbarStatus = "sleeping";
    }
    
    private synchronized void SetStatus(String s)
    {
        _barbarStatus = s;
    }
    
    public synchronized String GetStatus()
    {
        return _barbarStatus;
    }
    
    
    public void run()
    {
        try 
        {
            SetStatus("sleeping");
            Thread.sleep(1000);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Barbar.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true)
        {
            CheckForCashOut();
            DoHairCut();
        }
    }
    
    public int GetName()
    {
        return _name;
    }
    
    private void CheckForCashOut()
    {
        boolean getAccess = _shop.GetCashRegAccess();
        if(!getAccess)
        {
            SetStatus("waiting to get cash register");
            return;
        }
        Customer customer;
        while((customer = _shop.GetCustomerForCheckingOut())!=null)
        {
            _shop.MakeCashRegUnavailable();
            try 
            {
                SetStatus("cashing out");
                Thread.sleep(2000);//time for checkout
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Barbar.class.getName()).log(Level.SEVERE, null, ex);
            }
            _shop.CheckoutDone(customer);
        }
        _shop.MakeCashRegAvailable();
    }
    
    private void DoHairCut()
    {
        Customer customer = _shop.GetNextCustomer();
        if(customer == null)
        {
            SetStatus("sleeping");
            return;
        }
        try 
        {
            SetStatus("doing hair cut");
            Thread.sleep(5000);//time to cut hair
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Barbar.class.getName()).log(Level.SEVERE, null, ex);
        }
        _shop.HairCutDone(customer);
    }
}
