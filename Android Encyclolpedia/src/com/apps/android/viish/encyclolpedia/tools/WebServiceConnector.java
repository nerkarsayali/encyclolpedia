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
