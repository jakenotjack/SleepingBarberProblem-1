/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreading;

/**
 *
 * @author Naima
 */
public class Customer extends Thread
{
    private final int _name;
    private final CustomerManager _cm;
    private final BarbarShop _shop;
    
    public Customer(int name, CustomerManager cm, BarbarShop bShop)
    {
        _name= name;
        _cm = cm;
        _shop = bShop;
    }
    
    public int GetName()
    {
        return _name;
    }
    
    //@Override
    public void run()
    { 
        boolean gotChair;
        while(!(gotChair = _shop.TryGettingChair(this)))
            ;
        if(gotChair)
        {
            _cm.RemoveFromWaiting(this);
        }
    }
}
