package com.codetutr.validationHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import com.codetutr.config.exception.MyExceptionUtils;

public final class ValidationHelper {
	
	public static final String INTERNET_MESSAGE_ADDRESS_REGULAR_EXPRESSION = "^(.+)@(.+)$"; //"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	

	private static final Pattern INTERNET_MESSAGE_ADDRESS_PATTERN = Pattern.compile(INTERNET_MESSAGE_ADDRESS_REGULAR_EXPRESSION, (Pattern.CASE_INSENSITIVE));
    public static final int INTERNET_MESSAGE_ADDRESS_MAX_LENGTH = 255;
    
    
    
    // case-sensetive. Convert input to uppercase before using
     public static final String STATE_REGULAR_EXPRESSION = "^ (AL|AK|AZ|AR|CA|CO|CT|DE|DC|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MO|MT|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY|AS|FM|GU|MH|MP|PW|PR|VI)$";
     private static final Pattern STATE_PATTERN = Pattern.compile(STATE_REGULAR_EXPRESSION, (Pattern.CASE_INSENSITIVE));
     
     
     public static final String URL_REGULAR_EXPRESSION = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
     private static final Pattern URL_PATTERN = Pattern.compile(URL_REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);
     
     
     public static final String TELEPHONE_NUMBER_REGULAR_EXPRESSION = "\\d{10}";
     private static final Pattern TELEPHONE_NUMER_PATTERN = Pattern.compile(TELEPHONE_NUMBER_REGULAR_EXPRESSION);
     
     
     //mfa phone number - not currently used by ui. used by validator after cleaning it
     public static final String TELEPHONE_MFA_NUMBER_REGULAR_EXPRESSION = "\\d{10}";
     private static final Pattern TELEPHONE_MFA_NUMBER_PATTERN = Pattern.compile(TELEPHONE_MFA_NUMBER_REGULAR_EXPRESSION);
     
     
     public static final String ZIP_CODE_REGULAR_EXPRESSION = "(^[0-9]{5}$)|(^[0-9]{5}-?[0-9]{4}$)";
     private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(ZIP_CODE_REGULAR_EXPRESSION);
     
     public static final String PERSON_NAME_REGULAR_EXPRESSION = "^[a-zA-Z\\s]+";
     private static final Pattern PERSON_NAME_PATTERN = Pattern.compile(PERSON_NAME_REGULAR_EXPRESSION);
     public static final int PERSON_NAME_MIN_LENGTH = 0;
     public static final int PERSON_NAME_MAX_LENGTH = 45;
     
     
     public static final String MONTH_REGULAR_EXPRESSION = " ";
     private static final Pattern MONTH_PATTERN = Pattern.compile(MONTH_REGULAR_EXPRESSION);
     
     
     public static final String DAY_REGULAR_EXPRESSION = " ";
     private static final Pattern DAY_PATTERN = Pattern.compile(DAY_REGULAR_EXPRESSION);
     
     
     
     public static final String YEAR_REGULAR_EXPRESSION = " ";
     private static final Pattern YEAR_PATTERN = Pattern.compile(YEAR_REGULAR_EXPRESSION);
     
     
     public static final String SSN_LAST_FOUR_DIGITS_REGULAR_EXPRESSION = " ";
     private static final Pattern SSN_LAST_FOUR_DIGITS_PATTERN = Pattern.compile(SSN_LAST_FOUR_DIGITS_REGULAR_EXPRESSION);
     
    
     public static final String TEXT_REGULAR_EXPRESSION = "^[a-zA-Z0-9\\s]+";
     private static final Pattern TEXT_PATTERN = Pattern.compile(TEXT_REGULAR_EXPRESSION);
     
     
     
     public static final String PASSWORD_REGULAR_EXPRESSION = "^[a-zA-Z0-9\\s]+";
     private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGULAR_EXPRESSION);
     public static final int PASSWORD_MIN_LENGTH = 8;
     public static final int PASSWORD_MAX_LENGTH = 16;
     
     
     
     
     public static final int ADDRESS_MIN_LENGTH = 5;
     public static final int ADDRESS_MAX_LENGTH = 20;
     
     
     private ValidationHelper()
     {
     	
     }
     
     //generic validations
     public static boolean isValidLength(String data, int min, int max)
     {
    	 boolean valid = false;
    	 
    	 if(data !=null)
    	 {
    		 int dataLength = data.length();
    		 
    		 if(dataLength >= min && dataLength <= max)
    		 {
    			 valid = true;
    		 }
    	 }
    	 
    	 return valid;
     }
     
     
     
     
    public static boolean isValidRequired(String data)
    {
    	return !StringUtils.isBlank(data);
    }
    
    
    
    // field specific validations
    
    public static boolean isValidEmailAddress(String emailAddress)
    {
    	boolean valid = false;
    	
    	if((emailAddress = StringUtils.trimToNull(emailAddress)) !=null && emailAddress.length() < INTERNET_MESSAGE_ADDRESS_MAX_LENGTH)
    	{
    		Matcher matcher = INTERNET_MESSAGE_ADDRESS_PATTERN.matcher(emailAddress);
    		valid = matcher.matches();
    	}
    	
    	return valid;
    }
    
    
    
    public static boolean isValidUrl(String url)
    {
    	boolean valid = false;
    	
    	if((url = StringUtils.trimToNull(url)) != null)
    	{
    		Matcher matcher = URL_PATTERN.matcher(url);
    		valid = matcher.matches();
    	}
    	
    	return valid;
    }
    
     
    public static boolean isValidTelephoneNumber(String telephoneNumber)
    {
    	boolean valid = false;
    	
    	if((telephoneNumber = StringUtils.trimToNull(telephoneNumber)) != null)
    	{
    		Matcher matcher = TELEPHONE_NUMER_PATTERN.matcher(telephoneNumber);
    		valid = matcher.matches();
    	}
    	
    	return valid;
    }
     
     
     
   public static boolean isValidTelephoneNumberMFA(String telephoneNumber)
   {
   	boolean valid = false;
	
   	if((telephoneNumber = StringUtils.trimToNull(telephoneNumber)) != null)
   	{
   		Matcher matcher = TELEPHONE_MFA_NUMBER_PATTERN.matcher(telephoneNumber);
   		valid = matcher.matches();
   	}
   	
   	return valid;
   }
     
     
     
   public static boolean isValidZipcode(String zipCode)
   {
   	boolean valid = false;
	
   	if((zipCode = StringUtils.trimToNull(zipCode)) != null)
   	{
   		Matcher matcher = ZIP_CODE_PATTERN.matcher(zipCode);
   		valid = matcher.matches();
   	}
   	
   	return valid;
   }   
     
     
   public static boolean isValidPersonName(String personName)
   {
   	boolean valid = false;
	
   	if((personName = StringUtils.trimToNull(personName)) != null)
   	{
   		Matcher matcher = PERSON_NAME_PATTERN.matcher(personName);
   		valid = matcher.matches();
   		
   		if(valid)
   		{
   			valid = isValidLength(personName, PERSON_NAME_MIN_LENGTH,PERSON_NAME_MAX_LENGTH );
   		}
   	}
   	
   	return valid;
   }      
     
   
   
 public static boolean isValidLastFourDigits(String LastFourDigits)
 {
 	boolean valid = false;
	
 	if((LastFourDigits = StringUtils.trimToNull(LastFourDigits)) != null)
 	{
 		Matcher matcher = SSN_LAST_FOUR_DIGITS_PATTERN.matcher(LastFourDigits);
 		valid = matcher.matches();
 	}
 	
 	return valid;
 }  
     
 
    
     

     
 public static boolean isValidPassword(String password)
 {
 	boolean valid = false;
	
 	if((password = StringUtils.trimToNull(password)) != null)
 	{
 		Matcher matcher = PASSWORD_PATTERN.matcher(password);
 		valid = matcher.matches();
 		
 		if(valid)
 		{
 			valid = isValidLength(password, PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH );
 		}
 	}
 	
 	return valid;
 }      
 
     
     
 public static boolean isValidMonth(String month)
 {
 	boolean valid = false;
	
 	if((month = StringUtils.trimToNull(month)) != null)
 	{
 		Matcher matcher = MONTH_PATTERN.matcher(month);
 		valid = matcher.matches();
 	}
 	
 	return valid;
 }     
 
 
 
public static boolean isValidDay(String day)
{
	boolean valid = false;

	if((day = StringUtils.trimToNull(day)) != null)
	{
		Matcher matcher = DAY_PATTERN.matcher(day);
		valid = matcher.matches();
	}
	
	return valid;
} 
     
     
public static boolean isValidYear(String year)
{
	boolean valid = false;

	if((year = StringUtils.trimToNull(year)) != null)
	{
		Matcher matcher = YEAR_PATTERN.matcher(year);
		valid = matcher.matches();
	}
	
	return valid;
}  
  

public static boolean isValidState(String state)
{
	boolean valid = false;

	if((state = StringUtils.trimToNull(state)) != null)
	{
		Matcher matcher = STATE_PATTERN.matcher(state);
		valid = matcher.matches();
	}
	
	return valid;
}  
  

public static boolean isSuspisciousChar(String text)
{
	boolean valid = false;
	
	String badChars = "<>/;=~^|";
	
	if(!StringUtils.contains(text, badChars))
	{
		valid = true;
	}
	
	return valid;
}



public static boolean isNonPrintable(String text)
{
	boolean valid = false;
	
	char[] chars = text.toCharArray();
	for(int i=0; i<chars.length; i++)
	{
		int ascii = (int) chars[i];
		
		if(ascii <32)
		{
			valid = true;
			break;
		}
	}
	
	return valid;
}


public static boolean isValidText(String text)
{
	boolean valid = false;

	if((text = StringUtils.trimToNull(text)) != null)
	{
		Matcher matcher = TEXT_PATTERN.matcher(text);
		valid = matcher.matches();
	}
	
	return valid;
}


public static boolean isUserIdInPassword(String userId, String password)
{
	boolean found = false;

	if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(password))
	{
		if(StringUtils.containsIgnoreCase(password, userId))
				
		{
			found = true;
		}
	}
	return found;
}

	
/**
 * ***************************************************************
 *  Newly added methods starts from here
 * ****************************************************************
 */
public static void ensureAuthority(boolean authorized, String messageKey) 
{
	if (!authorized)
		throw new AccessDeniedException(MyExceptionUtils.getMessage(messageKey));
}

public static void ensureCredentials(boolean valid, String messageKey) 
{
	if (!valid)
		throw new BadCredentialsException(MyExceptionUtils.getMessage(messageKey));
}

}
