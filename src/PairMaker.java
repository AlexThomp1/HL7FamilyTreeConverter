//package hl7.testing;

public class PairMaker 
{
    private int item1;
    private int item2;
    
    //default constructor
    public PairMaker() 
    {
        item1 = 0;
        item2 = 0;
    }

    public PairMaker(int newItem1, int newItem2) 
    {
        item1 = newItem1;
        item2 = newItem2;
    }

    public int getItem1() 
    {
        return item1;
    }

    public int getItem2() 
    {
        return item2;
    }

    public void setItem1(int item1) 
    {
        this.item1 = item1;
    }

    public void setItem2(int item2) 
    {
        this.item2 = item2;
    }
}
