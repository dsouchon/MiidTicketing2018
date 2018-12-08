package com.example.dsouchon.myapplication;


import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MySOAPCallActivity
{
    public final String SOAP_ACTION = "http://tempuri.org/GetUserForTag";

    public final String SOAP_ACTIONLoginEventOrganiser = "http://tempuri.org/LoginEventOrganiser";
    public final String SOAP_ACTIONAllowDenyAccess = "http://tempuri.org/AllowDenyAccess";
    public final String SOAP_ACTIONTicketValidForEvent = "http://tempuri.org/TicketValidForEvent";
    public final String SOAP_ACTIONGetCurrentEvents = "http://tempuri.org/GetCurrentEvents";
    public final String SOAP_ACTIONGetEventImage = "http://tempuri.org/GetEventImage";
    public final String SOAP_ACTIONGetTicketHoldersForEvent = "http://tempuri.org/GetTicketHoldersForEvent";




    public  final String GetUserForTag = "GetUserForTag";

    public  final String LoginEventOrganiser = "LoginEventOrganiser";
    public  final String AllowDenyAccess = "AllowDenyAccess";
    public  final String TicketValidForEvent = "TicketValidForEvent";
    public  final String GetCurrentEvents = "GetCurrentEvents";
    public  final String GetEventImage = "GetEventImage";
    public  final String GetTicketHoldersForEvent = "GetTicketHoldersForEvent";




    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

   //public  final String SOAP_ADDRESS = "http://www.miid.co.zw/miidwebservice.asmx";
   // public  final String SOAP_ADDRESS = "https://www.miid.co.za/miidwebservice.asmx";
   // public  final String SOAP_ADDRESS = "http://demo.miid.co.za/miidwebservice.asmx";
    public  final String SOAP_ADDRESS = "http://MiidService.miid.co.za/miidwebservice.asmx";

    public MySOAPCallActivity()
    {
    }
    public String CallUser(String TagNumber)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,GetUserForTag);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("TagNumber");
        pi.setValue(TagNumber);
        pi.setType(String.class);
        request.addProperty(pi);
        //pi=new PropertyInfo();
        //pi.setName("b");
        //pi.setValue(b);
        //pi.setType(Integer.class);
        //request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {

            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String Login(String UserName, String Password)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,LoginEventOrganiser);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("UserName");
        pi.setValue(UserName);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo pi2 =new PropertyInfo();
        pi2.setName("Password");
        pi2.setValue(Password);
        pi2.setType(String.class);
        request.addProperty(pi2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONLoginEventOrganiser, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }






    public String GetTicketHoldersForEvent(String UserName, String Password, String EventName)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,GetTicketHoldersForEvent);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("UserName");
        pi.setValue(UserName);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo pi2 =new PropertyInfo();
        pi2.setName("Password");
        pi2.setValue(Password);
        pi2.setType(String.class);
        request.addProperty(pi2);

        PropertyInfo pi3 =new PropertyInfo();
        pi3.setName("EventName");
        pi3.setValue(EventName);
        pi3.setType(String.class);
        request.addProperty(pi3);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONGetTicketHoldersForEvent, envelope);


            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }





    public String GetCurrentEvents(String UserName, String Password)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,GetCurrentEvents);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("UserName");
        pi.setValue(UserName);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo pi2 =new PropertyInfo();
        pi2.setName("Password");
        pi2.setValue(Password);
        pi2.setType(String.class);
        request.addProperty(pi2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONGetCurrentEvents, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    public String AllowDenyAccess(String TagNumber, String ReasonForBlocking, boolean Block, String EventName)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,AllowDenyAccess);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("TagNumber");
        pi.setValue(TagNumber);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo pi2=new PropertyInfo();
        pi2.setName("ReasonForBlocking");
        pi2.setValue(ReasonForBlocking);
        pi2.setType(String.class);
        request.addProperty(pi2);

        PropertyInfo pi3=new PropertyInfo();
        pi3.setName("Block");
        pi3.setValue(Block);
        pi3.setType(Boolean.class);
        request.addProperty(pi3);

        PropertyInfo pi4=new PropertyInfo();
        pi4.setName("EventName");
        pi4.setValue(EventName);
        pi4.setType(String.class);
        request.addProperty(pi4);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONAllowDenyAccess, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
    public String TicketValidForEvent(String TagNumber, String EventName)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,TicketValidForEvent);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("TagNumber");
        pi.setValue(TagNumber);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo pi2=new PropertyInfo();
        pi2.setName("EventName");
        pi2.setValue(EventName);
        pi2.setType(String.class);
        request.addProperty(pi2);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS, 14000);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONTicketValidForEvent, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }


    public String GetEventImage( String EventName)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,GetEventImage);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("EventName");
        pi.setValue(EventName);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS, 14000);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTIONGetEventImage, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

}


