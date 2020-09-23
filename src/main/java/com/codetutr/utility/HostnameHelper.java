package com.codetutr.utility;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostnameHelper {

	private static HostnameHelper instance;
	private String localHostname;
	
	private HostnameHelper()
	{
		InetAddress localhost = null;
		try {
			//This relies on DNS lookup, and can fail.
			localhost = InetAddress.getLocalHost();
		}
		catch(UnknownHostException unknownHostException){
		}
		
		if(null != localhost) {
			localHostname = localhost.getHostName();
		}
		localHostname = null;
	}
	
	public static HostnameHelper getInstance()
	{
		if(instance == null){
			instance = new HostnameHelper();
		}
		return instance;
	}
	
	public String getLocalHostname(){
		return localHostname;
	}
}
