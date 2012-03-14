package com.apps.android.viish.encyclolpedia;

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

	private static final String	TAG	= null;

	private ProgressDialog		mProgressDialog;
	private Handler				mHandler			= new Handler()
													{
														public void handleMessage(
																Message msg)
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
													};
	private boolean				updateSuccessfull	= false;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);

		assingActionsToMenuButtons();
		updateInformations();
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
				updateSuccessfull = downloadUpdates();
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	private boolean downloadUpdates()
	{
		boolean ok = false;
		try
		{
			DatabaseStream dbs = new DatabaseStream(this);
			List<String> champions = WebServiceConnector.getChampionsList(this);
			for (String champion : champions)
			{
				String championFileName = champion + ".jpg";
				if (!ImageManager.isFileExists(this, championFileName)) {
					Log.e(TAG, "Downloading picture " + championFileName);
					ImageManager.downloadAndSaveImage(this, championFileName);
				}
				
				int rev = WebServiceConnector.getChampionRevision(this,
						champion);
				Log.e(TAG, "Champion " + champion + " : latest revision = " + rev);
				if (dbs.isEntryUpdatable(true, champion, rev))
				{
					Log.e(TAG, "Champion " + champion + " : updating...");
					List<String> requests = WebServiceConnector.getChampionRequests(this, champion);
					dbs.updateEntry(true, champion, requests);
					Log.e(TAG, "Champion " + champion + " : updated");
				}
			}
			//TODO : Items
			ok = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		}
	}
}
