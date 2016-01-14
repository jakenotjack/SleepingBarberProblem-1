/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naima
 */
public class CustomerManager extends Thread
{
    private final BlockingQueue<Customer> _waitingQueue;
    private final BarbarShop _shop;
    private final int _totalNumOfCustomer;
    
    private int _numOfCustomerLeftWithoutHairCut = 0;
    private int _totalCustomerCreated;
    
    public CustomerManager(BarbarShop bShop, int totalCustomer)
    {
        _waitingQueue = new ArrayBlockingQueue<Customer>(11);
        _shop = bShop;
        _totalNumOfCustomer = totalCustomer;
    }
    
    private synchronized void IncNumOfCustomerLeftWithoutHairCut()
    {
        _numOfCustomerLeftWithoutHairCut++;
    }
    
    
    public synchronized int GetNumOfCustomerLeftWithoutHairCut()
    {
        return _numOfCustomerLeftWithoutHairCut;
    }
    
    public synchronized int GetTotalNumOfCustomerCreated()
    {
        return _totalCustomerCreated;
    }
    
    private synchronized void SetNumOfCustomerCreated(int i)
    {
        _totalCustomerCreated = i;
    }
    
    
    //@Override
    public void run()
    {
        for(int i=0; i<_totalNumOfCustomer; i++)
        {
            SetNumOfCustomerCreated(i+1);
            Customer c = new Customer(_totalCustomerCreated, this, _shop);
            int num = _shop.GetCurrentTotalCustomerNum();
            if(num < 11)//if total num of customer in shop is less than 11 or not
            {
                try 
                {
                    _shop.IncCustomerNum();
                    _waitingQueue.put(c);
                    
                    c.start();
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(CustomerManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                IncNumOfCustomerLeftWithoutHairCut();
            }
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(CustomerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void RemoveFromWaiting(Customer customer)
    {
        if(_waitingQueue.contains(customer))
            _waitingQueue.remove(customer);
    }
}
