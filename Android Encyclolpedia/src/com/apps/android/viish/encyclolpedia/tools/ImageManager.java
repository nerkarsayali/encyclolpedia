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

package com.apps.android.viish.encyclolpedia.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageManager
{
	public static final String BASE_URL = "http://thinkdroid.eu/encyclolpedia/images/";
	
	public static boolean isFileExists(Context context, String fileName)
	{
		try
		{
			FileInputStream fis = context.openFileInput(fileName);
			fis.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static Bitmap getImage(Context context, String fileName) throws IOException {
		FileInputStream fis = context.openFileInput(fileName);
		Bitmap bm = BitmapFactory.decodeStream(fis);
		fis.close();
		
		return bm;
	}
	
	public static void downloadAndSaveImage(Context context, String fileName)
			throws IOException
	{
		String stringUrl = BASE_URL + fileName;
		URL url = new URL(stringUrl);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.connect();

		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		InputStream is = urlConnection.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] buf = new byte[1024];
		int n = 0;
		int o = 0;
		while ((n = bis.read(buf, o, buf.length)) != 0)
		{
			fos.write(buf, 0, n);
		}
		bis.close();
		fos.close();
	}
}
