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

package com.apps.android.viish.encyclolpedia;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.android.viish.encyclolpedia.champion.Champions;
import com.apps.android.viish.encyclolpedia.database.DatabaseStream;
import com.apps.android.viish.encyclolpedia.tools.ImageManager;
import com.apps.android.viish.encyclolpedia.tools.WebServiceConnector;

public class Encyclolpedia extends Activity
{
	public static final String	VERSION				= "0.1";

	private static final String	TAG					= null;

	private ProgressDialog		mProgressDialog;
	private Handler				mHandler			= new Handler()
													{
														public void handleMessage(
																Message msg)
														{
															if (msg.obj == null)
															{
																mProgressDialog
																		.dismiss();

																if (updateSuccessfull)
																{
																	Toast.makeText(
																			Encyclolpedia.this,
																			"Informations successfully updated!",
																			Toast.LENGTH_SHORT)
																			.show();
																}
																else
																{
																	Toast.makeText(
																			Encyclolpedia.this,
																			"Error, please try again later!",
																			Toast.LENGTH_SHORT)
																			.show();
																}
															}
															else
															{
																mProgressDialog
																		.setMessage("Downloading "
																				+ msg.obj
																						.toString()
																				+ " ...");
															}
														}
													};
	private boolean				updateSuccessfull	= false;
	private List<String>		mChampionsList;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);

		assingActionsToMenuButtons();

		SharedPreferences lastSettings = getSharedPreferences("Preferences",
				Context.MODE_PRIVATE);
		String oldVersion = lastSettings.getString("Version", "0");

		if (!oldVersion.equals(VERSION))
		{
			// Display a message to the user if it's the first time he launches
			// the application
			SharedPreferences.Editor editor = lastSettings.edit();
			editor.clear();
			editor.putString("Version", VERSION);
			editor.commit();

			showAbout();
			updateInformations();
		}
	}

	private void updateInformations()
	{
		if (!isNetworkAvailable())
		{
			// Display a warning message to the user if Internet is not
			// available
			Toast.makeText(
					this,
					"You are not connected to internet, you possibly won't have latest informations !",
					Toast.LENGTH_LONG).show();
			return;
		}

		mProgressDialog = ProgressDialog
				.show(Encyclolpedia.this, "Please wait...",
						"Updating informations, this process can be long the first time.");

		new Thread()
		{
			public void run()
			{
				if (mChampionsList == null)
				{
					mChampionsList = WebServiceConnector
							.getChampionsList(Encyclolpedia.this);
				}
				updateSuccessfull = downloadUpdates();
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	private boolean downloadUpdates()
	{
		boolean ok = false;
		DatabaseStream dbs = new DatabaseStream(this);
		try
		{
			Iterator<String> championIterator = mChampionsList.iterator();
			while (championIterator.hasNext())
			{
				String championRealName = championIterator.next();
				// Download champion picture if needed
				String champion = ImageManager
						.getChampionFileName(championRealName);
				String championFileName = champion + ".png";
				mHandler.sendMessage(Message.obtain(mHandler, 0,
						championRealName));
				if (!ImageManager.isFileExists(this, championFileName))
				{
					Log.e(TAG, "Downloading champion picture "
							+ championFileName);
					ImageManager.downloadAndSaveChampionImage(this,
							championFileName);
					Log.e(TAG, "Downloaded champion picture "
							+ championFileName);
				}

				// Download skill pictures if needed
				String[] skillsLetters = new String[] { "P", "Q", "W", "E", "R" };
				for (String skillLetter : skillsLetters)
				{
					String championSkillFileName = champion + skillLetter
							+ ".png";
					if (!ImageManager.isFileExists(this, championSkillFileName))
					{
						Log.e(TAG, "Downloading skill picture "
								+ championSkillFileName);
						ImageManager.downloadAndSaveSkillImage(this,
								championSkillFileName);
						Log.e(TAG, "Downloaded skill picture "
								+ championSkillFileName);
					}
				}

				// Download champions database if needed
				int rev = WebServiceConnector.getChampionRevision(this,
						championRealName);
				Log.e(TAG, "Champion " + championRealName
						+ " : latest revision = " + rev);
				if (dbs.isEntryUpdatable(true, championRealName, rev))
				{
					Log.e(TAG, "Champion " + championRealName
							+ " : updating...");
					List<String> requests = WebServiceConnector
							.getChampionRequests(this, championRealName);
					dbs.updateEntry(true, champion, requests);
					Log.e(TAG, "Champion " + championRealName + " : updated");
				}

				// Update complete, we remove him from the iterator
				championIterator.remove();
			}
			// TODO : Items
			ok = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dbs.close();

			Button bUpdate = (Button) findViewById(R.id.bUpdate);
			if (ok)
			{
				mChampionsList = null;
				bUpdate.setText("Force Update");
			}
			else
			{
				bUpdate.setText("Finish Update");
			}
		}

		return ok;
	}

	private boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	private void showAbout()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Encyclolpedia version " + VERSION);
		alert.setMessage("This is opensource software (http://code.google.com/p/encyclolpedia/)\nDevelopped by Viish");
		alert.setInverseBackgroundForced(true);

		alert.setNegativeButton("Back", null);

		alert.show();
	}

	private void showFacebookPage()
	{
		String url = "http://www.facebook.com/pages/Android-LoL-Encyclopedia/107534552634639";
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(myIntent);
	}

	private void assingActionsToMenuButtons()
	{
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.setImageResource(R.drawable.logo);
		iv.setAdjustViewBounds(true);
		iv.setPadding(0, 0, 0, 0);

		Button bChamps = (Button) findViewById(R.id.bChamps);
		bChamps.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent i = new Intent(v.getContext(), Champions.class);
				startActivity(i);
			}
		});

		ImageView bFacebook = (ImageView) findViewById(R.id.bFacebook);
		bFacebook.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				showFacebookPage();
			}
		});

		Button bAbout = (Button) findViewById(R.id.bAbout);
		bAbout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				showAbout();
			}
		});

		Button bUpdate = (Button) findViewById(R.id.bUpdate);
		bUpdate.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				updateInformations();
			}
		});
	}
}
