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

package com.apps.android.viish.encyclolpedia.champion;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.apps.android.viish.encyclolpedia.R;
import com.apps.android.viish.encyclolpedia.database.DatabaseStream;
import com.apps.android.viish.encyclolpedia.item.ShowItem;
import com.apps.android.viish.encyclolpedia.tools.ImageManager;

public class ShowChampion extends Activity implements OnClickListener
{
	private String		name, title;
	private Champion	champion;
	private String		skills[], skillsDesc[], tags[], items[], stats[];
	private int[]		bars;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showchampion);

		ScrollView mainll = (ScrollView) findViewById(R.id.mainll);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			mainll.setBackgroundResource(R.drawable.background_landscape);
			int paddind = getWindowManager().getDefaultDisplay().getWidth() * 5 / 100;
			mainll.setPadding(paddind, paddind, paddind, paddind);
		}
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			mainll.setBackgroundResource(R.drawable.background_portrait);
			int paddind = getWindowManager().getDefaultDisplay().getWidth() * 5 / 100;
			mainll.setPadding(paddind, paddind, paddind, paddind);
		}

		name = this.getIntent().getStringExtra("Name");
		champion = new Champion(this, name);
		skills = champion.getSkillsName();
		skillsDesc = new String[5];

		DatabaseStream dbs = new DatabaseStream(this);
		title = dbs.getChampionTitle(name);
		tags = dbs.getChampionTags(name);
		items = dbs.getChampionItems(name);
		stats = dbs.getChampionStats(name);
		bars = dbs.getChampionBars(name);

		int PI = dbs.getChampionPI(name);
		int RP = dbs.getChampionRP(name);

		for (int i = 0; i < 5; i++)
		{
			try
			{
				skillsDesc[i] = dbs.getChampionSkill(name, skills[i]);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		dbs.close();

		LinearLayout llbars = (LinearLayout) findViewById(R.id.bars);
		if (bars == null || bars[0] == 0 || bars[1] == 0 || bars[2] == 0
				|| bars[3] == 0)
		{
			llbars.setVisibility(View.GONE);
		}
		else
		{
			for (int i = 0; i < 4; i++)
			{
				LinearLayout lltemp = new LinearLayout(this);
				lltemp.setOrientation(LinearLayout.HORIZONTAL);

				TextView tv = new TextView(this);
				lltemp.addView(tv);

				for (int j = 0; j < bars[i]; j++)
				{
					ImageView iv = new ImageView(this);

					if (i == 0)
					{
						if (j == 0)
						{
							iv.setImageResource(R.drawable.attackstart);
							tv.setText("Attack :     ");
							tv.setTextColor(Color.BLACK);
						}
						else if (j == bars[i] - 1)
						{
							iv.setImageResource(R.drawable.attackend);
						}
						else
						{
							iv.setImageResource(R.drawable.attack);
						}
					}
					else if (i == 1)
					{

						if (j == 0)
						{
							iv.setImageResource(R.drawable.healthstart);
							tv.setText("Health :     ");
							tv.setTextColor(Color.BLACK);
						}
						else if (j == bars[i] - 1)
						{
							iv.setImageResource(R.drawable.healthend);
						}
						else
						{
							iv.setImageResource(R.drawable.health);
						}
					}
					else if (i == 2)
					{

						if (j == 0)
						{
							iv.setImageResource(R.drawable.difficultystart);
							tv.setText("Difficulty : ");
							tv.setTextColor(Color.BLACK);
						}
						else if (j == bars[i] - 1)
						{
							iv.setImageResource(R.drawable.difficultyend);
						}
						else
						{
							iv.setImageResource(R.drawable.difficulty);
						}
					}
					else if (i == 3)
					{
						if (j == 0)
						{
							iv.setImageResource(R.drawable.spellsstart);
							tv.setText("Spells :       ");
							tv.setTextColor(Color.BLACK);
						}
						else if (j == bars[i] - 1)
						{
							iv.setImageResource(R.drawable.spellsend);
						}
						else
						{
							iv.setImageResource(R.drawable.spells);
						}
					}

					lltemp.addView(iv);
				}
				llbars.addView(lltemp);
			}
		}

		TextView tvname = (TextView) findViewById(R.id.championName);
		TextView tvtitle = (TextView) findViewById(R.id.championTitle);
		TextView tvtags = (TextView) findViewById(R.id.tags);
		TextView RPvalue = (TextView) findViewById(R.id.RPvalue);
		TextView PIvalue = (TextView) findViewById(R.id.PIvalue);

		ImageView ivicon = (ImageView) findViewById(R.id.champicon);
		ImageView RPicon = (ImageView) findViewById(R.id.RPicon);
		ImageView PIicon = (ImageView) findViewById(R.id.PIicon);

		ImageView skillicon0 = (ImageView) findViewById(R.id.skillicon0);
		ImageView skillicon1 = (ImageView) findViewById(R.id.skillicon1);
		ImageView skillicon2 = (ImageView) findViewById(R.id.skillicon2);
		ImageView skillicon3 = (ImageView) findViewById(R.id.skillicon3);
		ImageView skillicon4 = (ImageView) findViewById(R.id.skillicon4);

		final TextView skillname0 = (TextView) findViewById(R.id.skillname0);
		final TextView skillname1 = (TextView) findViewById(R.id.skillname1);
		final TextView skillname2 = (TextView) findViewById(R.id.skillname2);
		final TextView skillname3 = (TextView) findViewById(R.id.skillname3);
		final TextView skillname4 = (TextView) findViewById(R.id.skillname4);

		TextView skilldesc0 = (TextView) findViewById(R.id.skilldesc0);
		TextView skilldesc1 = (TextView) findViewById(R.id.skilldesc1);
		TextView skilldesc2 = (TextView) findViewById(R.id.skilldesc2);
		TextView skilldesc3 = (TextView) findViewById(R.id.skilldesc3);
		TextView skilldesc4 = (TextView) findViewById(R.id.skilldesc4);

		tvname.setText(name);
		tvname.setTextSize(23);
		tvname.setTextColor(Color.RED);
		tvtitle.setText(title);
		tvtitle.setTextColor(Color.BLACK);
		tvtitle.setTextSize(16);

		// Prices
		RPvalue.setText(RP + "");
		RPvalue.setTextColor(Color.BLUE);
		RPvalue.setTextSize(14);
		PIvalue.setText(PI + "");
		PIvalue.setTextColor(Color.BLUE);
		PIvalue.setTextSize(14);
		RPicon.setImageDrawable(getResources().getDrawable(R.drawable.rp));
		PIicon.setImageDrawable(getResources().getDrawable(R.drawable.pi));

		// Tags
		String taglist = "";
		for (int i = 0; i < tags.length; i++)
		{
			taglist += tags[i];
			if (i < tags.length - 1)
				taglist += ", ";
		}
		if (tags.length > 0)
			tvtags.setText(taglist);
		tvtags.setTextColor(Color.BLACK);

		// Champion
		try
		{
			Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + ".png");
			ivicon.setImageBitmap(bm);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}
		
		// Skills
		try
		{
			final Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + "P.png");
			skillicon0.setImageBitmap(bm);
			skillicon0.setMaxWidth((getWindowManager().getDefaultDisplay()
					.getWidth() - 30) / 5);
			skillicon0.setAdjustViewBounds(true);

			skillname0.setText(skills[0]);
			skillname0.setTextSize(18);
			skillname0.setTextColor(Color.BLUE);

			skilldesc0.setText(skillsDesc[0]);
			skilldesc0.setTextSize(15);
			skilldesc0.setTextColor(Color.BLACK);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}

		try
		{
			final Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + "Q.png");
			skillicon1.setImageBitmap(bm);
			skillicon1.setMaxWidth((getWindowManager().getDefaultDisplay()
					.getWidth() - 30) / 5);
			skillicon1.setAdjustViewBounds(true);

			skillname1.setText(skills[1]);
			skillname1.setTextSize(18);
			skillname1.setTextColor(Color.BLUE);

			skilldesc1.setText(skillsDesc[1]);
			skilldesc1.setTextSize(15);
			skilldesc1.setTextColor(Color.BLACK);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}

		try
		{
			final Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + "W.png");
			skillicon2.setImageBitmap(bm);
			skillicon2.setMaxWidth((getWindowManager().getDefaultDisplay()
					.getWidth() - 30) / 5);
			skillicon2.setAdjustViewBounds(true);

			skillname2.setText(skills[2]);
			skillname2.setTextSize(18);
			skillname2.setTextColor(Color.BLUE);

			skilldesc2.setText(skillsDesc[2]);
			skilldesc2.setTextSize(15);
			skilldesc2.setTextColor(Color.BLACK);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}

		try
		{
			final Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + "E.png");
			skillicon3.setImageBitmap(bm);
			skillicon3.setMaxWidth((getWindowManager().getDefaultDisplay()
					.getWidth() - 30) / 5);
			skillicon3.setAdjustViewBounds(true);

			skillname3.setText(skills[3]);
			skillname3.setTextSize(18);
			skillname3.setTextColor(Color.BLUE);

			skilldesc3.setText(skillsDesc[3]);
			skilldesc3.setTextSize(15);
			skilldesc3.setTextColor(Color.BLACK);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}

		try
		{
			final Bitmap bm = ImageManager.getImage(this, ImageManager.getChampionFileName(name) + "R.png");
			skillicon4.setImageBitmap(bm);
			skillicon4.setMaxWidth((getWindowManager().getDefaultDisplay()
					.getWidth() - 30) / 5);
			skillicon4.setAdjustViewBounds(true);

			skillname4.setText(skills[4]);
			skillname4.setTextSize(18);
			skillname4.setTextColor(Color.BLUE);

			skilldesc4.setText(skillsDesc[4]);
			skilldesc4.setTextSize(15);
			skilldesc4.setTextColor(Color.BLACK);
		}
		catch (IOException e)
		{
			Log.i("Error", e.toString());
		}

		// Recommended Items
		LinearLayout itemsLL = (LinearLayout) findViewById(R.id.itemsLL);
		TextView itemsTV = (TextView) findViewById(R.id.itemsTV);

		itemsTV.setTextSize(16);
		itemsTV.setTextColor(Color.BLUE);
		itemsTV.setText("Recommended Items :");

		for (String item : items)
		{
			ImageView iv = new ImageView(this);
			iv.setOnClickListener(this);

			String itemName = item.replace(" ", "_").replace("'", "")
					.replace("-", "").toLowerCase();
			try
			{
				Bitmap bm = ImageManager.getImage(this, itemName + ".png");
				iv.setImageBitmap(bm);
				iv.setAdjustViewBounds(true);
				iv.setMaxWidth((getWindowManager().getDefaultDisplay()
						.getWidth() - 30) / items.length);
				iv.setTag(item);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Log.i("Error", e.toString());
			}

			itemsLL.addView(iv);
		}

		// Stats
		TextView range, statsTV, damage, damagePL, health, healthPL, mana, manaPL, speed, armor, armorPL, spellblock, spellblockPL, healthregen, healthregenPL, manaregen, manaregenPL;
		statsTV = (TextView) findViewById(R.id.StatsTV);
		statsTV.setText("Stats :");
		statsTV.setTextColor(Color.BLUE);
		statsTV.setTextSize(16);
		damage = (TextView) findViewById(R.id.Damage);
		damage.setTextColor(Color.BLACK);
		damage.setText("Damage : " + stats[0]);
		damagePL = (TextView) findViewById(R.id.DamagePL);
		damagePL.setTextColor(Color.RED);
		damagePL.setText(" (+" + stats[1] + " per level)");
		health = (TextView) findViewById(R.id.Health);
		health.setTextColor(Color.BLACK);
		health.setText("Health : " + stats[2]);
		healthPL = (TextView) findViewById(R.id.HealthPL);
		healthPL.setTextColor(Color.RED);
		healthPL.setText(" (+" + stats[3] + " per level)");
		mana = (TextView) findViewById(R.id.Mana);
		mana.setTextColor(Color.BLACK);
		mana.setText("Mana/Energy/Furor : " + stats[4]);
		manaPL = (TextView) findViewById(R.id.ManaPL);
		manaPL.setTextColor(Color.RED);
		manaPL.setText(" (+" + stats[5] + " per level)");
		speed = (TextView) findViewById(R.id.Speed);
		speed.setTextColor(Color.BLACK);
		speed.setText("Move Speed : " + stats[6]);
		armor = (TextView) findViewById(R.id.Armor);
		armor.setTextColor(Color.BLACK);
		armor.setText("Armor : " + stats[7]);
		armorPL = (TextView) findViewById(R.id.ArmorPL);
		armorPL.setTextColor(Color.RED);
		armorPL.setText(" (+" + stats[8] + " per level)");
		spellblock = (TextView) findViewById(R.id.SpellBlock);
		spellblock.setTextColor(Color.BLACK);
		spellblock.setText("Spell Block : " + stats[9]);
		spellblockPL = (TextView) findViewById(R.id.SpellBlockPL);
		spellblockPL.setTextColor(Color.RED);
		spellblockPL.setText(" (+" + stats[10] + " per level)");
		healthregen = (TextView) findViewById(R.id.HealthRegen);
		healthregen.setTextColor(Color.BLACK);
		healthregen.setText("Health Regen : " + stats[11]);
		healthregenPL = (TextView) findViewById(R.id.HealthRegenPL);
		healthregenPL.setTextColor(Color.RED);
		healthregenPL.setText(" (+" + stats[12] + " per level)");
		manaregen = (TextView) findViewById(R.id.ManaRegen);
		manaregen.setTextColor(Color.BLACK);
		manaregen.setText("Mana/Energy Regen : " + stats[13]);
		manaregenPL = (TextView) findViewById(R.id.ManaRegenPL);
		manaregenPL.setTextColor(Color.RED);
		manaregenPL.setText(" (+" + stats[14] + " per level)");
		range = (TextView) findViewById(R.id.Range);
		range.setTextColor(Color.BLACK);
		range.setText("Range : " + stats[15]);
	}

	public void onClick(View v) // Lors d'un click sur une icone item
	{
		Intent i = new Intent(this, ShowItem.class);
		i.putExtra("Name", v.getTag().toString());
		startActivity(i);
	}
}
