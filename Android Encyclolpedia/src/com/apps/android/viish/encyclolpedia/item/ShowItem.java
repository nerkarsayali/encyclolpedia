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

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.android.viish.encyclolpedia.R;
import com.apps.android.viish.encyclolpedia.database.DatabaseStream;
import com.apps.android.viish.encyclolpedia.tools.ImageManager;


public class ShowItem extends Activity implements OnClickListener
{
	private String name;
	private String price;
	private String fullprice;
	private String desc;
	private String[] parents;
	private ArrayList<String> childs;
	private ArrayList<String> childsPrices;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showitem);
        
        name = this.getIntent().getStringExtra("Name");
        price = this.getIntent().getStringExtra("Price");
        fullprice = this.getIntent().getStringExtra("FullPrice");
        desc = this.getIntent().getStringExtra("Desc");
        
       Button bAB = (Button) findViewById(R.id.bAddBuild);
    	final Activity activity = this;
    	bAB.setOnClickListener(new OnClickListener()
    	{
    		@Override
			public void onClick(View v) 
    		{
				Intent i = new Intent();
				i.putExtra("ItemName", name);
				activity.setResult(RESULT_OK, i);
				activity.finish();
			}
    	});
        
        if (price == null || price.equals("") || desc == null || desc.equals("") || fullprice == null || fullprice.equals(""))
        {
        	DatabaseStream dbs = new DatabaseStream(this);
    		Cursor request = dbs.getItemsByName(name, "Name", "ASC");
    		
    		if (request != null && request.move(1)) 
            {
            	 int priceColumn = request.getColumnIndex("Price");
            	 int fullpriceColumn = request.getColumnIndex("FullPrice");
            	 int descColumn = request.getColumnIndex("Desc");
            	 
                 do 
                 {
                	 if (price == null || price.equals("")) price = request.getString(priceColumn);
                	 if (fullprice == null || fullprice.equals("")) fullprice = request.getString(fullpriceColumn);
                	 if (desc == null || desc.equals("")) desc = request.getString(descColumn);
                 } 
                 while (request.move(1));
            }
    		
            request.deactivate();
            request.close();
            dbs.close();
        }
        
        getParents();
        getChilds();
        
        show();
    }
	
	private void show() // Affiche la page
	{
		LinearLayout topll = (LinearLayout) findViewById(R.id.topll);
		LinearLayout botll = (LinearLayout) findViewById(R.id.botll);
		TextView tvfullprice = (TextView) findViewById(R.id.fullprice);
		TextView tvprice = (TextView) findViewById(R.id.price);
		TextView tvdesc = (TextView) findViewById(R.id.desc);
		TextView tvname = (TextView) findViewById(R.id.name);
    	ImageView ivi = (ImageView) findViewById(R.id.icon);
    	ImageView ivi2 = (ImageView) findViewById(R.id.icon2);
    	
    	for(String parent : parents) // Affiche les icones des parents
    	{    		
    		ImageView iv = new ImageView(this);
    		iv.setOnClickListener(this);
    		
    		String resourceName = parent.replace(" ", "_").replace("'", "").replace("-", "").toLowerCase();
    		try 
    		{
    			Bitmap bm = ImageManager.getImage(this, resourceName + ".gif");
    			iv.setImageBitmap(bm);
    			iv.setAdjustViewBounds(true);
    			iv.setMaxWidth(getWindowManager().getDefaultDisplay().getWidth() / parents.length);
    			iv.setTag(parent);
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    			Log.i("Error", e.toString());
    		}
    		
    		topll.addView(iv);
    	}
    	
    	tvname.setText(name); // Nom de l'item courant
    	tvname.setTextColor(Color.GREEN);
    		
    	String resourceName = name.replace(" ", "_").replace("'", "").replace("-", "").toLowerCase(); // Image de l'item courant
		try 
		{
			Bitmap bm = ImageManager.getImage(this, resourceName + ".gif");
			ivi.setImageBitmap(bm);
			if (childs.size() > 0) // Si item compos�
			{
				ivi2.setImageBitmap(bm); // Meme icone
				tvprice.setText(price); // Prix de l'am�lioration seulement
				tvprice.setTextColor(Color.YELLOW);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			Log.i("Error", e.toString());
		}
		
		tvfullprice.setText(fullprice); // Prix complet de l'item courant
		tvfullprice.setTextColor(Color.YELLOW);
    	
    	tvdesc.setText(desc); // Description de l'item courant
		
		int indexChild = 0;
		for(String child : childs) // Affiche les icones et les prix des fils
    	{			
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);

			TextView tv = new TextView (this);
			tv.setText(" ");
			botll.addView(tv);
			
    		ImageView iv = new ImageView(this);
    		iv.setOnClickListener(this);
    		
    		resourceName = child.replace(" ", "_").replace("'", "").replace("-", "").toLowerCase();
    		try 
    		{
    			Bitmap bm = ImageManager.getImage(this, resourceName + ".gif");
    			iv.setImageBitmap(bm);
    			iv.setTag(child);
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    			Log.i("Error", e.toString());
    		}
    		
    		ll.addView(iv);
    		
    		tv = new TextView(this);
    		tv.setText(childsPrices.get(indexChild));
    		indexChild++;
    		tv.setTextColor(Color.YELLOW);
    		tv.setGravity(Gravity.CENTER);
    		ll.addView(tv);
    		
    		botll.addView(ll);
    		
    		tv = new TextView (this);
			tv.setText(" ");
    		botll.addView(tv);
    	}
	}
	
	private void getParents() // R�cup�re les noms des parents
	{
		DatabaseStream dbs = new DatabaseStream(this);
		Cursor request = dbs.getParents(name);
		
		if (request == null) return ;
		
		parents = new String[request.getCount()];
		int indexParents = 0;
		
		if (request.move(1)) 
        {
        	 int itemNameColumn = request.getColumnIndex("Name");
             
             do 
             {
            	 String itemName = request.getString(itemNameColumn);
            	 parents[indexParents] = itemName;
            	 indexParents++;
             } 
             while (request.move(1));
        }
		
        request.deactivate();
        request.close();
        dbs.close();
	}
	
	private void getChilds() // R�cup�re les noms et prix des fils
	{
		DatabaseStream dbs = new DatabaseStream(this);
		Cursor request = dbs.getChilds(name);
		
		if (request == null) return ;
		
		childs = new ArrayList<String>();
		
		if (request.move(1)) 
        {
        	 int itemNameColumn = request.getColumnIndex("Needed");
        	 int nbitemColumn = request.getColumnIndex("Amount");
     
             do 
             {
            	 int amount = Integer.parseInt(request.getString(nbitemColumn));
            	 
            	 for (int i = 0; i < amount; i++)
            	 {
	            	 String itemName = request.getString(itemNameColumn);
	            	 childs.add(itemName);
            	 }
             } 
             while (request.move(1));
        }
		
        request.deactivate();
        request.close();   
        
        childsPrices = new ArrayList<String>();
        for(String child : childs)
        {
        	request = dbs.getItemsByName(child, "Name", "ASC");
        	request.move(1);
        	int fullpriceColumn = request.getColumnIndex("FullPrice");
        	childsPrices.add(request.getString(fullpriceColumn));
        	
        	request.deactivate();
            request.close();   
        }
        
        dbs.close();
	}

	public void onClick(View v) // Lors d'un click sur une icone parent ou enfant
	{
		Intent i = new Intent(this, ShowItem.class);
		i.putExtra("Name", v.getTag().toString());
		startActivity(i);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	if (resultCode == RESULT_OK && requestCode == 0x1515)
    	{
    		setResult(RESULT_OK, data);
    		finish();
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
