/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naima
 */
public class BarbarShop 
{
    private final BlockingQueue<Customer> _chairQueue = new ArrayBlockingQueue<Customer>(3);
    private final Queue<Customer> _checkingOutQueue = new LinkedList<Customer>();
    private int _totalCustomerCurrentlyAtShop = 0; 
    private int _totalCustomerInChair = 0;
    private boolean _cashRegAccess = true;

    public BarbarShop()
    {

    }
    
    //returns number of customers who are waiting for cashing out
    public int GetNumOfCustomerWaitingForCashOut()
    {
        synchronized(_checkingOutQueue)
        {
            return  _checkingOutQueue.size();
        }
    }
    
    //if cash register is free or not
    public synchronized boolean GetCashRegAccess()
    {
        return _cashRegAccess;
    }
    
    //cash register is unavailable for other barbar
    public synchronized void MakeCashRegUnavailable() 
    {
        _cashRegAccess = false;
    }
    
    //cash register is available for other barbar
    public synchronized void MakeCashRegAvailable() 
    {
        _cashRegAccess = true;
    }
    
    //Increaments the number of customer who are accessing the chair
    private synchronized void IncCustInChair()
    {
        _totalCustomerInChair++;
    }
    
    //Decreaments the number of customer who are accessing the chair
    private synchronized void DecCustInChair()
    {
        _totalCustomerInChair--;
    }
    
    //returns the number of customer who are accessing the chair
    public synchronized int GetCustCountInChair()
    {
        return _totalCustomerInChair;
    }

    //returns the next available customer from chair to get the hair cut
    public Customer GetNextCustomer()
    {
        //checks if there is any customer in the chair
        if(GetCustCountInChair() > 0)
        {
            Customer customer = null;
            try 
            {
                customer = _chairQueue.take();
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(BarbarShop.class.getName()).log(Level.SEVERE, null, ex);
            }
            return customer; 
        }
        return null;
    }

    //Afetr hair cut is done, customer leaves the chair and waits for being cashed out
    public void HairCutDone(Customer customer)
    {
        synchronized(_checkingOutQueue)
        {
            _checkingOutQueue.add(customer);
        }
        synchronized(_chairQueue)
        {
            _chairQueue.notifyAll();
        }
        DecCustInChair();
    }
    
    //returns a customer if there is any customer waiting for cashed out. otherwise returns null
    public Customer GetCustomerForCheckingOut()
    {
        synchronized(_checkingOutQueue)
        {
            if(_checkingOutQueue.isEmpty())
            {
                return null;
            }
            
            return _checkingOutQueue.remove();
        }
    }

    //when cashed out is done, customer leaves. So, number of customer in shop decreases
    public synchronized void CheckoutDone(Customer customer)
    {
        _totalCustomerCurrentlyAtShop--;
    }

    //returns currently available number of customer in shop 
    public synchronized int GetCurrentTotalCustomerNum() 
    {
        return _totalCustomerCurrentlyAtShop;
    }
    
    // increaments number of customer available in shop
    public synchronized void IncCustomerNum()
    {
        _totalCustomerCurrentlyAtShop++;
    }
    
    //customer tries to get chair access
    public synchronized boolean TryGettingChair(Customer customer)
    {
        if(GetCustCountInChair()>=3)
            return false;
        try 
        {
            _chairQueue.put(customer);
            IncCustInChair();

            synchronized(_chairQueue)
            {
                _chairQueue.notifyAll();
            }
            return true;
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(BarbarShop.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
