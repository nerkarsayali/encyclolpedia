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

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
		try 
		{
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d("Envelope", envelope.getResponse().toString());
            
            SoapObject result = (SoapObject) envelope.getResponse();
            for(int i = 0; i < result.getPropertyCount(); i++){
                champions.add((String) result.getProperty(i));
            }
        } 
		catch (Exception e) 
		{
			e.printStackTrace();
        }
		
		return champions;
	}
	
	public static List<String> getChampionRequests(Context context, String championName)
	{
		String SOAP_ACTION = "http://tempuri.org/GetChampionRequests";
		String METHOD_NAME = "GetChampionRequests";
	    String NAMESPACE = "http://tempuri.org/";
	    String URL = "http://www.thinkdroid.eu/WebService1.asmx";
	    
		List<String> requests = new ArrayList<String>();
		try 
		{
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d("Envelope", envelope.getResponse().toString());
            
            SoapObject result = (SoapObject) envelope.getResponse();
            for(int i = 0; i < result.getPropertyCount(); i++){
                requests.add((String) result.getProperty(i));
            }
        } 
		catch (Exception e) 
		{
			e.printStackTrace();
        }
		
		return requests;
	}
	
	public static int getChampionRevision(Context context, String championName)
	{
		String SOAP_ACTION = "http://tempuri.org/GetChampionRevision";
		String METHOD_NAME = "GetChampionRevision";
	    String NAMESPACE = "http://tempuri.org/";
	    String URL = "http://www.thinkdroid.eu/WebService1.asmx";
	    
		int revision = -1;
		try 
		{
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d("Envelope", envelope.getResponse().toString());
            
            SoapObject result = (SoapObject) envelope.getResponse();
            revision = Integer.parseInt(result.toString());
        } 
		catch (Exception e) 
		{
			e.printStackTrace();
        }
		
		return revision;
	}
}
