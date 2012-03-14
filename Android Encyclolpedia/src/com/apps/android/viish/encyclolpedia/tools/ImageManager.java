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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageManager
{
	public static final String	BASE_URL	= "http://thinkdroid.eu/encyclolpedia/images/";

	public static boolean isFileExists(Context context, String fileName)
	{
		try
		{
			FileInputStream fis = getFileInputStream(context, fileName);
			fis.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static Bitmap getImage(Context context, String fileName)
			throws IOException
	{
		FileInputStream fis = getFileInputStream(context, fileName);
		Bitmap bm = BitmapFactory.decodeStream(fis);
		fis.close();

		return bm;
	}

	public static void deleteImage(Context context, String fileName)
	{
		context.deleteFile(fileName);
	}

	public static void downloadAndSaveChampionImage(Context context,
			String fileName) throws IOException
	{
		try
		{
			downloadAndSaveImageWithPrefix(context, "", fileName);
		}
		catch (IOException ioe)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			downloadAndSaveImageWithPrefix(context, "", fileName);
		}
	}

	public static void downloadAndSaveSkillImage(Context context,
			String fileName) throws IOException
	{
		try
		{
			downloadAndSaveImageWithPrefix(context, "skills/", fileName);
		}
		catch (IOException ioe)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			downloadAndSaveImageWithPrefix(context, "skills/", fileName);
		}
	}

	private static void downloadAndSaveImageWithPrefix(Context context,
			String prefix, String fileName) throws IOException
	{
		String stringUrl = BASE_URL + prefix + fileName;
		URL url = new URL(stringUrl);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		InputStream is = urlConnection.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		FileOutputStream fos = getFileOutputStream(context, fileName);

		ByteArrayBuffer baf = new ByteArrayBuffer(65535);
		int current = 0;
		while ((current = bis.read()) != -1)
		{
			baf.append((byte) current);
		}
		fos.write(baf.toByteArray());
		fos.close();
		bis.close();
	}

	private static FileOutputStream getFileOutputStream(Context context,
			String fileName) throws FileNotFoundException
	{
		File path = context.getExternalFilesDir(null);
		File image = new File(path, fileName);
		return new FileOutputStream(image);
	}

	private static FileInputStream getFileInputStream(Context context,
			String fileName) throws FileNotFoundException
	{
		File path = context.getExternalFilesDir(null);
		File image = new File(path, fileName);
		return new FileInputStream(image);
	}

	public static String getChampionFileName(String championRealName)
	{
		return championRealName.replace("'", "").replace(" ", "")
				.replace(".", "");
	}
}
