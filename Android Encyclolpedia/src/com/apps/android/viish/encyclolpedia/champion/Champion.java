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

import com.apps.android.viish.encyclolpedia.database.DatabaseStream;

import android.content.Context;
import android.database.Cursor;

public class Champion 
{
	private String Name;
	private String[] SkillsName, SkillsDesc;
	
	public Champion(Context c, String name)
	{
		Name = name;
		
		DatabaseStream dbs = new DatabaseStream(c);		
		Cursor request = dbs.getChampionSkills(name);
		
		if (request != null && request.move(1)) 
        {
			SkillsName = new String[request.getCount()];
			SkillsDesc = new String[request.getCount()];
			int index = 0;
			
			do
			{
				int skillnameColum = request.getColumnIndex("SkillName");
				int skilldescColumn = request.getColumnIndex("SkillDesc");
				SkillsName[index] = request.getString(skillnameColum);
				SkillsDesc[index] = request.getString(skilldescColumn);
				index++;
			}
			while(request.move(1));
			
       	 	request.deactivate();
       	 	request.close();
        }
		
		dbs.close();
	}
	
	public String[] getSkillsName()
	{
		return SkillsName;
	}
	
	public String[] getSkillsDesc()
	{
		return SkillsDesc;
	}
	
	public String getName()
	{
		return Name;
	}
}
