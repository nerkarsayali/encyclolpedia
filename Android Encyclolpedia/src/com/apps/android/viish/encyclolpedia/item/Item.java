/*
    Author Sylvain "Viish" Berfini
    
	Encyclolpedia is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Encyclolpedia is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Encyclolpedia. If not, see <http://www.gnu.org/licenses/>.
*/

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
