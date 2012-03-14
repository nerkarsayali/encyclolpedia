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

package com.apps.android.viish.encyclolpedia.database;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseStream
{
	public static final int	MAX_ITEMS	= 6;

	private SQLiteConnector	connector;
	private SQLiteDatabase	stream;
	private Context			context;

	public DatabaseStream(Context c)
	{
		context = c;
		connector = new SQLiteConnector(context, "Encyclolpedia", 1);
		stream = connector.getWritableDatabase();
	}

	public boolean isEntryUpdatable(Boolean champion, String primaryKeyValue,
			int newVersion)
	{
		String[] colonnes = { "Version" };
		String table;
		if (champion)
		{
			table = "Champions";
		}
		else
		{
			table = "Items";
		}

		Cursor c = stream.query(table, colonnes, "Name LIKE \""
				+ primaryKeyValue + "\"", null, null, null, null);
		int version = -1;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("Version");
			version = c.getInt(column);
		}
		else
		{
			return true;
		}
		c.deactivate();
		c.close();

		return (version == -1 || version < newVersion);
	}

	public boolean updateEntry(Boolean champion, String primaryKeyValue,
			List<String> updateRequests)
	{
		String[] tables;
		if (champion)
		{
			tables = new String[] { "Champions", "ChampionsTags",
					"ChampionsSkills", "ChampionsRecommendedItems" };
		}
		else
		{
			tables = new String[] { "Items", "ItemDependencies" };
		}

		try
		{
			for (String t : tables)
			{
				stream.delete(t, "Name LIKE \"" + primaryKeyValue + "\"", null);
			}
			for (String request : updateRequests)
			{
				stream.execSQL(request);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public Cursor getItemsByTag(String tag, String sortBy, String order)
	{
		String[] colonnes = { "Name", "FullPrice", "Price", "Desc" };

		Cursor c = stream.query("Items", colonnes,
				"Tag LIKE \"%" + tag + "%\"", null, null, null, sortBy + " "
						+ order);

		return c;
	}

	public Cursor getItemsByName(String name, String sortBy, String order)
	{
		String[] colonnes = { "Name", "FullPrice", "Price", "Desc" };

		Cursor c = stream.query("Items", colonnes, "Name LIKE \"%" + name
				+ "%\"", null, null, null, sortBy + " " + order);

		return c;
	}

	public Cursor getAllItems(String sortBy, String order)
	{
		String[] colonnes = { "Name", "FullPrice", "Price", "Desc" };

		Cursor c = stream.query("Items", colonnes, null, null, null, null,
				sortBy + " " + order);

		return c;
	}

	public Cursor getParents(String name)
	{
		String[] colonnes = { "Name" };

		Cursor c = stream.query("ItemDependencies", colonnes, "Needed LIKE \""
				+ name + "\"", null, null, null, "Name ASC");

		return c;
	}

	public Cursor getChilds(String name)
	{
		String[] colonnes = { "Needed", "Amount" };

		Cursor c = stream.query("ItemDependencies", colonnes, "Name LIKE \""
				+ name + "\"", null, null, null, "Name ASC");

		return c;
	}

	public Cursor getChampionSkills(String name)
	{
		String[] colonnes = { "SkillName", "SkillDesc" };

		Cursor c = stream.query("ChampionsSkills", colonnes, "Name LIKE \""
				+ name + "\"", null, null, null, "SkillId ASC");

		return c;
	}

	public String getChampionSkill(String championName, String skillName)
	{
		String[] colonnes = { "SkillDesc" };

		Cursor c = stream.query("ChampionsSkills", colonnes, "Name LIKE \""
				+ championName + "\" AND SkillName LIKE \"" + skillName + "\"",
				null, null, null, null);
		String s = null;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("SkillDesc");
			s = c.getString(column);
		}
		c.deactivate();
		c.close();

		return s;
	}

	public Cursor getChampions(String order)
	{
		String[] colonnes = { "Name" };

		Cursor c = stream.query("Champions", colonnes, null, null, null, null,
				"Name " + order);

		return c;
	}

	public String getChampionTitle(String championName)
	{
		String[] colonnes = { "Title" };

		Cursor c = stream.query("Champions", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		String s = null;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("Title");
			s = c.getString(column);
		}
		c.deactivate();
		c.close();

		return s;
	}

	public String getChampionBackground(String championName)
	{
		String[] colonnes = { "Background" };

		Cursor c = stream.query("Champions", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		String s = null;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("Background");
			s = c.getString(column);
		}
		c.deactivate();
		c.close();

		return s;
	}

	public String[] getChampionTags(String championName)
	{
		String[] colonnes = { "Tag" };

		Cursor c = stream.query("ChampionsTags", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		String[] s = null;

		if (c != null)
		{
			s = new String[c.getCount()];

			int column = c.getColumnIndex("Tag");

			for (int i = 0; i < c.getCount(); i++)
			{
				c.move(1);
				s[i] = c.getString(column);
			}
		}
		c.deactivate();
		c.close();

		return s;
	}

	public int[] getChampionBars(String championName)
	{
		String[] colonnes = { "AttackBar", "HealthBar", "DifficultyBar",
				"SpellBar" };

		Cursor c = stream.query("Champions", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		int[] v = null;

		if (c != null && c.move(1))
		{
			v = new int[4];

			int columnA = c.getColumnIndex("AttackBar");
			int columnH = c.getColumnIndex("HealthBar");
			int columnD = c.getColumnIndex("DifficultyBar");
			int columnS = c.getColumnIndex("SpellBar");

			v[0] = c.getInt(columnA);
			v[1] = c.getInt(columnH);
			v[2] = c.getInt(columnD);
			v[3] = c.getInt(columnS);

		}
		c.deactivate();
		c.close();

		return v;
	}

	public String[] getChampionbyTag(String tag)
	{
		String[] colonnes = { "Name", "Tag" };

		Cursor c = stream.query("ChampionsTags", colonnes, "Tag LIKE \"" + tag
				+ "\"", null, null, null, "Name ASC");
		String[] s = null;

		if (c != null)
		{
			s = new String[c.getCount()];

			int column = c.getColumnIndex("Name");

			for (int i = 0; i < c.getCount(); i++)
			{
				c.move(1);
				s[i] = c.getString(column);
			}
		}
		c.deactivate();
		c.close();

		return s;
	}

	public String[] getMasteriesName(String subtree)
	{
		String[] colonnes = { "Name" };

		Cursor c = stream.query("Masteries", colonnes, "Type LIKE \"" + subtree
				+ "\"", null, null, null, "Place ASC");
		String[] s = null;

		if (c != null)
		{
			s = new String[c.getCount()];

			int column = c.getColumnIndex("Name");

			for (int i = 0; i < c.getCount(); i++)
			{
				c.move(1);
				s[i] = c.getString(column);
			}
		}
		c.deactivate();
		c.close();

		return s;
	}

	public String[] getChampionItems(String name)
	{
		Cursor c = stream.query("ChampionsRecommendedItems", null,
				"Name LIKE \"" + name + "\"", null, null, null, null);
		String[] items = null;

		if (c != null)
		{
			items = new String[c.getCount()];

			int column = c.getColumnIndex("ItemName");
			for (int i = 0; i < c.getCount(); i++)
			{
				c.move(1);
				items[i] = c.getString(column);
			}
		}

		c.deactivate();
		c.close();

		return items;
	}

	public String[] getChampionStats(String name)
	{
		Cursor c = stream.query("ChampionsStats", null, "Name LIKE \"" + name
				+ "\"", null, null, null, null);
		String[] stats = new String[16];

		if (c != null && c.move(1))
		{
			int columnDamage = c.getColumnIndex("Damage");
			stats[0] = c.getString(columnDamage);
			int columnDamagePL = c.getColumnIndex("DamagePL");
			stats[1] = c.getString(columnDamagePL);
			int columnHealth = c.getColumnIndex("Health");
			stats[2] = c.getInt(columnHealth) + "";
			int columnHealthPL = c.getColumnIndex("HealthPL");
			stats[3] = c.getInt(columnHealthPL) + "";
			int columnMana = c.getColumnIndex("Mana");
			stats[4] = c.getInt(columnMana) + "";
			int columnManaPL = c.getColumnIndex("ManaPL");
			stats[5] = c.getInt(columnManaPL) + "";
			int columnSpeed = c.getColumnIndex("Speed");
			stats[6] = c.getInt(columnSpeed) + "";
			int columnArmor = c.getColumnIndex("Armor");
			stats[7] = c.getString(columnArmor);
			int columnArmorPL = c.getColumnIndex("ArmorPL");
			stats[8] = c.getString(columnArmorPL);
			int columnSpellBlock = c.getColumnIndex("SpellBlock");
			stats[9] = c.getString(columnSpellBlock);
			int columnSpellBlockPL = c.getColumnIndex("SpellBlockPL");
			stats[10] = c.getString(columnSpellBlockPL);
			int columnHealthRegen = c.getColumnIndex("HealthRegen");
			stats[11] = c.getString(columnHealthRegen);
			int columnHealthRegenPL = c.getColumnIndex("HealthRegenPL");
			stats[12] = c.getString(columnHealthRegenPL);
			int columnManaRegen = c.getColumnIndex("ManaRegen");
			stats[13] = c.getString(columnManaRegen);
			int columnManaRegenPL = c.getColumnIndex("ManaRegenPL");
			stats[14] = c.getString(columnManaRegenPL);
			int columnRange = c.getColumnIndex("Range");
			stats[15] = c.getString(columnRange);
		}

		c.deactivate();
		c.close();

		return stats;
	}

	public int getChampionRP(String championName)
	{
		String[] colonnes = { "RP" };

		Cursor c = stream.query("Champions", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		int v = 0;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("RP");
			v = c.getInt(column);
		}
		c.deactivate();
		c.close();

		return v;
	}

	public int getChampionPI(String championName)
	{
		String[] colonnes = { "PI" };

		Cursor c = stream.query("Champions", colonnes, "Name LIKE \""
				+ championName + "\"", null, null, null, "Name ASC");
		int v = 0;

		if (c != null && c.move(1))
		{
			int column = c.getColumnIndex("PI");
			v = c.getInt(column);
		}
		c.deactivate();
		c.close();

		return v;
	}

	public boolean asChild(String name)
	{
		String[] colonnes = { "Needed" };

		Cursor c = stream.query("Dependencies", colonnes, "Name LIKE \"" + name
				+ "\"", null, null, null, "Name ASC");

		return c.getCount() > 0;
	}

	public void close()
	{
		connector.close();
		stream.close();
	}
}
