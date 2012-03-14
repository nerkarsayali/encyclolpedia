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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConnector extends SQLiteOpenHelper 
{
	
	public SQLiteConnector(Context context, String databaseName, int databaseVersion)
	{
		super(context, databaseName, null, databaseVersion);
	}
	
	public void onCreate(SQLiteDatabase db) 
	{
		createDBs_EN(db, true);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
	
	private void createDBs_EN (SQLiteDatabase db, boolean b)
	{
		db.execSQL("CREATE TABLE Items (Name TEXT PRIMARY KEY NOT NULL, FullPrice INTEGER NOT NULL, Price INTEGER NOT NULL, Tag TEXT NOT NULL, Desc TEXT NOT NULL, Version NUMBER NOT NULL)");	
		
		db.execSQL("CREATE TABLE ItemDependencies (Name TEXT NOT NULL, Needed TEXT NOT NULL, Amount NUMBER NOT NULL, PRIMARY KEY (Name, Needed))");
		
		db.execSQL("CREATE TABLE Champions (Name TEXT PRIMARY KEY NOT NULL, Title TEXT, RP NUMBER, PI NUMBER, AttackBar NUMBER, HealthBar NUMBER, DifficultyBar NUMBER, SpellBar NUMBER, Version NUMBER NOT NULL)");
		
		db.execSQL("CREATE TABLE ChampionsTags (Name TEXT NOT NULL, Tag TEXT NOT NULL, PRIMARY KEY (Name, Tag))");
		
		db.execSQL("CREATE TABLE ChampionsSkills (Name TEXT NOT NULL, SkillId NUMBER NOT NULL, SkillName TEXT NOT NULL, SkillDesc TEXT NOT NULL, PRIMARY KEY(Name, SkillId))");

		db.execSQL("CREATE TABLE ChampionsRecommendedItems (Name TEXT, ItemName TEXT, PRIMARY KEY(Name, ItemName))");
	}
	
	public void close()
	{
		super.close();
	}
}