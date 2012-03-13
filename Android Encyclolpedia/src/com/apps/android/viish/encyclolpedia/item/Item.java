package com.apps.android.viish.encyclolpedia.item;


public class Item 
{
	private String Name;
    private String Price;
    private String Desc;
    private String FullPrice;
    
    public Item()
    {
    	Name = "";
    	Price = "";
    	Desc = "";
    }
    
    public String getName() 
    {
        return Name;
    }
    
    public void setName(String Name) 
    {
        this.Name = Name;
    }
    
    public String getPrice() 
    {
        return Price;
    }
    
    public void setPrice(String Price) 
    {
        this.Price = Price;
    }
    
    public String getFullPrice() 
    {
        return FullPrice;
    }
    
    public void setFullPrice(String FullPrice) 
    {
        this.FullPrice = FullPrice;
    }
    
    public String getDesc() 
    {
        return Desc;
    }
    
    public void setDesc(String Desc) 
    {
        this.Desc = Desc;
    }
    
    @Override
	public String toString()
    {
    	return Name + " (" + Price + ") : " + Desc;
    }
}
