package com.apps.android.viish.encyclolpedia.champion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.android.viish.encyclolpedia.R;
import com.apps.android.viish.encyclolpedia.database.DatabaseStream;

public class Champions extends Activity implements OnClickListener
{
	public static int	PICTURE_SIZE	= 80;

	private ArrayList<String>	champs, tags;
	private CheckBox			cbAll, cbAssassin, cbFighter, cbMage, cbTank,
			cbSupport;
	private int					checked;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.champions);

		LinearLayout mainll = (LinearLayout) findViewById(R.id.mainll);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			mainll.setBackgroundResource(R.drawable.background_landscape);
		}
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			mainll.setBackgroundResource(R.drawable.background_portrait);
		}

		cbAll = (CheckBox) findViewById(R.id.cbAll);
		cbAll.setChecked(true);
		cbAll.setEnabled(false);
		cbAll.setTextColor(Color.BLACK);
		cbAssassin = (CheckBox) findViewById(R.id.cbAssassin);
		cbAssassin.setTag("Assassin");
		cbAssassin.setTextColor(Color.BLACK);
		cbFighter = (CheckBox) findViewById(R.id.cbFighter);
		cbFighter.setTag("Fighter");
		cbFighter.setTextColor(Color.BLACK);
		cbMage = (CheckBox) findViewById(R.id.cbMage);
		cbMage.setTag("Mage");
		cbMage.setTextColor(Color.BLACK);
		cbTank = (CheckBox) findViewById(R.id.cbTank);
		cbTank.setTag("Tank");
		cbTank.setTextColor(Color.BLACK);
		cbSupport = (CheckBox) findViewById(R.id.cbSupport);
		cbSupport.setTag("Support");
		cbSupport.setTextColor(Color.BLACK);

		tags = new ArrayList<String>();
		MaJ(tags);

		cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					cbAll.setEnabled(false);
					cbAssassin.setChecked(false);
					cbFighter.setChecked(false);
					cbMage.setChecked(false);
					cbTank.setChecked(false);
					cbSupport.setChecked(false);

					tags = new ArrayList<String>();
					checked = 0;
					MaJ(tags);
				}
			}
		});

		OnCheckedChangeListener occl = new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					cbAll.setChecked(false);
					cbAll.setEnabled(true);
					checked += 1;
					tags.add((String) buttonView.getTag());
					MaJ(tags);
				}
				else
				{
					checked -= 1;
					tags.remove(buttonView.getTag());

					if (checked == 0)
					{
						cbAll.setChecked(true);
						cbAll.setEnabled(false);
					}
					else
					{
						MaJ(tags);
					}
				}
			}
		};

		cbAssassin.setOnCheckedChangeListener(occl);
		cbFighter.setOnCheckedChangeListener(occl);
		cbMage.setOnCheckedChangeListener(occl);
		cbTank.setOnCheckedChangeListener(occl);
		cbSupport.setOnCheckedChangeListener(occl);
	}

	private void MaJ(ArrayList<String> tags)
	{
		GridView gridview = (GridView) findViewById(R.id.champions);
		champs = new ArrayList<String>();

		DatabaseStream dbs = new DatabaseStream(this);

		if (tags.size() == 0 && checked == 0)// All champions
		{
			Cursor request = dbs.getChampions("ASC");
			if (request != null && request.move(1))
			{
				int nameColumn = request.getColumnIndex("Name");
				do
				{
					String name = request.getString(nameColumn);
					champs.add(name);
				}
				while (request.move(1));
			}

			request.deactivate();
			request.close();
		}
		else
		// Champions filtered by tags
		{
			int counter = 0;
			for (String tag : tags)
			{
				String[] champions = dbs.getChampionbyTag(tag);
				ArrayList<String> temp = new ArrayList<String>();
				for (String champion : champions)
				{
					if (counter == 0)
					{
						temp.add(champion);
					}
					else
					{
						if (champs.contains(champion))
							temp.add(champion);
					}
				}
				champs = temp;
				counter += 1;
			}
		}

		dbs.close();

		int width = getWindowManager().getDefaultDisplay().getWidth();
		gridview.setNumColumns(width / (PICTURE_SIZE + 10));
		gridview.setAdapter(new ImageAdapter(this, champs));
	}

	public void onClick(View v)
	{
		Intent i = new Intent(this, ShowChampion.class);
		i.putExtra("Name", v.getTag().toString());
		Log.i("ChampionName", v.getTag().toString());
		startActivity(i);
	}
}

class ImageAdapter extends BaseAdapter
{
	public static int			PICTURE_SIZE	= 80;

	private Context				mContext;
	private ArrayList<String>	mThumbIds;
	private OnClickListener		ock;

	public ImageAdapter(Champions c, ArrayList<String> champs)
	{
		mContext = c;
		ock = c;
		mThumbIds = champs;
	}

	public int getCount()
	{
		return mThumbIds.size();
	}

	public Object getItem(int position)
	{
		return null;
	}

	public long getItemId(int position)
	{
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v;
		TextView tv;
		ImageView iv;
		if (convertView == null)
		{
			LayoutInflater li = ((Activity) mContext).getLayoutInflater();
			v = li.inflate(R.layout.icon, null);
			iv = (ImageView) v.findViewById(R.id.icon_image);
			iv.setTag(mThumbIds.get(position));
			iv.setOnClickListener(ock);
			iv.setAdjustViewBounds(true);
			iv.setMaxHeight(PICTURE_SIZE);
			iv.setMaxWidth(PICTURE_SIZE);
		}
		else
		{
			v = convertView;
			iv = (ImageView) v.findViewById(R.id.icon_image);
			iv.setTag(mThumbIds.get(position));
		}
		v.setPadding(5, 5, 5, 5);

		tv = (TextView) v.findViewById(R.id.icon_text);
		tv.setText(mThumbIds.get(position));
		tv.setTextColor(Color.BLACK);
		try
		{
			iv = (ImageView) v.findViewById(R.id.icon_image);
			Bitmap bm = BitmapFactory.decodeStream(mContext.getResources()
					.getAssets().open(mThumbIds.get(position) + ".jpg"));
			iv.setImageBitmap(bm);
		}
		catch (Exception e)
		{
			Log.i("Error", e.toString());
		}

		return v;
	}
}
