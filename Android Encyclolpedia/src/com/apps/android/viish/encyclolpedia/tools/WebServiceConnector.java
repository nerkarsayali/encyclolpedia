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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

public class WebServiceConnector
{
	public static List<String> getChampionsList(Context context)
	{
		String SOAP_ACTION = "http://tempuri.org/GetChampionsList";
		String METHOD_NAME = "GetChampionsList";
		String NAMESPACE = "http://tempuri.org/";
		String URL = "http://www.thinkdroid.eu/WebService1.asmx";

		List<String> champions = new ArrayList<String>();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try
		{
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			Log.d("Envelope", result.toString());
			for (int i = 0; i < result.getPropertyCount(); i++)
			{
				SoapPrimitive primitive = (SoapPrimitive) result.getProperty(i);
				String champion = primitive.toString();
				champions.add(champion);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		

		return champions;
	}

	public static List<String> getChampionRequests(Context context,
			String championName) throws IOException, XmlPullParserException
	{
		String SOAP_ACTION = "http://tempuri.org/GetChampionRequests";
		String METHOD_NAME = "GetChampionRequests";
		String NAMESPACE = "http://tempuri.org/";
		String URL = "http://www.thinkdroid.eu/WebService1.asmx";

		List<String> requests = new ArrayList<String>();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("championName", championName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try
		{
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			Log.d("Envelope", result.toString());
			for (int i = 0; i < result.getPropertyCount(); i++)
			{
				SoapPrimitive primitive = (SoapPrimitive) result.getProperty(i);
				String r = primitive.toString();
				requests.add(r);
			}
		}
		catch (Exception e)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			Log.d("Envelope", result.toString());
			for (int i = 0; i < result.getPropertyCount(); i++)
			{
				SoapPrimitive primitive = (SoapPrimitive) result.getProperty(i);
				String r = primitive.toString();
				requests.add(r);
			}
		}

		return requests;
	}

	public static int getChampionRevision(Context context, String championName)
			throws IOException, XmlPullParserException
	{
		String SOAP_ACTION = "http://tempuri.org/GetChampionRevision";
		String METHOD_NAME = "GetChampionRevision";
		String NAMESPACE = "http://tempuri.org/";
		String URL = "http://www.thinkdroid.eu/WebService1.asmx";

		int revision = -1;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("championName", championName);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try
		{
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			Log.d("Envelope", result.toString());
			revision = Integer.parseInt(result.toString());
		}
		catch (Exception e)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
			Log.d("Envelope", result.toString());
			revision = Integer.parseInt(result.toString());
		}

		return revision;
	}
}
