package com.codetutr.config.logging;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**

 * The Log class maps into the Log4J logging facility. It has various

 * static log classes and will use the admin/log4j.config file to configure the

 * various attributes. For full documentation see the www.apache.org (Jakarta Project)

 * (Log4J subproject).

 * The assumption is that the calls to the various logXxxx methods will be done with

 * object being "this" e.g. Log.logDebug(this,"error message"); This will allow for

 * fine-grained manipulation of the messages based on class name, package or global.

 * The Log class automatically initializes based on the first access. It looks for the CSXControllerServlet for the config path and will

 * look for log4j.config first and then log4j.{WAS_ENVIRONMENT} where {WAS_ENVIROMENT} is the System property set in the Application server 

 * to dev,uat,prod.  this is so that you can have a specific config file for each environment.

 * If the Log class needs to be configured outside of CSXControllerServlet or reiniatilized then create a new Log class and call

 * configure with the configuration file name and the path to it. 

 * Creation date: (10/05/2017 11:15:24 PM)

 * @author: ysharma

 */

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.codetutr.properties.LogProperties;




public class Log
{
   public static final Properties LOG_PROPERTIES = new Properties();

  public Log(){
    super();
  }

 
  public void configure(String webInfDirectoryFilePath)      
  {
    String fName = LogProperties.getInstance().getDefaultConfigFileNameForLogging();
    String loggingPath = webInfDirectoryFilePath + LogProperties.getInstance().getDefaultConfigDirectoryForLogging();
    FindFiles findFiles = new FindFiles(fName, loggingPath);
    File file = findFiles.nextFile();
    
    // Find first instance of config file in path
    if (file == null)
    {
      String runtimeEvironment = LogProperties.getInstance().getRuntimeEnvironment();
      if (!StringUtils.isBlank(runtimeEvironment))
      {
        StringBuffer fileName = new StringBuffer();
        fileName.append(LogProperties.getInstance().getCustomConfigFileNameForBase()).append(runtimeEvironment);
        fName = fileName.toString();
      }
    }
    configure(fName, loggingPath);
  }

  /**
   * 
   * This is called to configure the various pieces. If a log4j.config file
   * cannot be found the standard configuration parms will be used. This logs
   * everything to the standard out.
   *  
   *  ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
   *  
   */
  public void configure(String fileName, String configPath)
  {
    boolean fileLoaded = false;
    StringBuffer errorMessage = null;
    FindFiles findFiles = new FindFiles(fileName, configPath);
    File file = findFiles.nextFile();
    // Find first instance of config file in path
    if (file != null)
    {
      try
      {
        LOG_PROPERTIES.load(new FileInputStream(file));
        if (LOG_PROPERTIES.size() > 0)
        {
          fileLoaded = true;
        }
      } catch (Exception ioe)
      {
        errorMessage = new StringBuffer();
        errorMessage.append("Error opening property file for log4j: ").append(ioe.getMessage());
      }
    } else
    {
      errorMessage = new StringBuffer();
      errorMessage.append("Unable to locate config file, ").append(fileName).append(
          ", in config path - ").append(configPath);
    }
    if (fileLoaded)
    {
      PropertyConfigurator.configure(LOG_PROPERTIES);
    } else
    {
      BasicConfigurator.configure();
    }
    if (errorMessage != null)
    {
      Log.logFatal(this, "configure(filename, configPath)", errorMessage.toString());
    }
  }


  
  
  //Entering into a method
  public static void entering(Object className, String methodName)
  {
	  Logger logger = getLoggerObject(className);
	  String output = writeLog(methodName, "Entering");
	  logger.debug(output);
  }

  public static void entering(String className, String methodName)
  {
	  Logger logger = getLoggerObject(className);
	  String output = writeLog(methodName, "Entering");
	  logger.debug(output);
  }



  //Exiting from a method
  public static void exiting(Object className, String methodName)
  {
	  Logger logger = getLoggerObject(className);
	  String output = writeLog(methodName, "Exiting");
	  logger.debug(output);
  }

  public static void exiting(String className, String methodName)
  {
	  Logger logger = getLoggerObject(className);
	  String output = writeLog(methodName, "Exiting");
	  logger.debug(output);
  }
  
  
  //Level Debug
  public static void logDebug(Object className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.debug(output);
  }
  
public static void logDebug(String className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.debug(output);
  }
  
  public static void logDebug(Object className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.debug(output, throwable);
  }

  public static void logDebug(String className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.debug(output, throwable);
  }
  
  
  
  
  //Level Info
  public static void logInfo(Object className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.info(output);
  }

  public static void logInfo(String className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.info(output);
  }

  public static void logInfo(Object className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.info(output, throwable);
  }

  public static void logInfo(String className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.info(output, throwable);
  }
  
  
  
  
  //Level Warn
  public static void logWarn(Object className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.warn(output);
  }

  public static void logWarn(String className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.warn(output);
  }
  
  public static void logWarn(Object className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.warn(output, throwable);
  }

  public static void logWarn(String className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.warn(output, throwable);
  }
  
  
 
  
  
  //Level Error
  public static void logError(Object className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.error(output);
  }

  public static void logError(String className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.error(output);
  }
  
  public static void logError(Object className, String methodName, String message, Throwable throwable)
  {
    Logger logger =  getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.error(output, throwable);
  }

  public static void logError(String className, String methodName, String message, Throwable throwable)
  {
    Logger cat = getLoggerObject(className);
    String output = writeLog(methodName, message);
    cat.error(output, throwable);
  }

  
  
  
 
  //Level Fatal
  public static void logFatal(Object className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.fatal(output);
  }
  
  public static void logFatal(String className, String methodName, String message)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.fatal(output);
  }

  public static void logFatal(Object className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.fatal(output, throwable);
  }

  public static void logFatal(String className, String methodName, String message, Throwable throwable)
  {
    Logger logger = getLoggerObject(className);
    String output = writeLog(methodName, message);
    logger.fatal(output, throwable);
  }



  
  
  //Getting Logger object
  private static Logger getLoggerObject(Object className)
  {
		Logger logger = Logger.getLogger(className.getClass().getName());
		return logger;
	}
  
  private static Logger getLoggerObject(String className)
  {
		Logger logger = Logger.getLogger(className);
		return logger;
   }

  private static String writeLog(String methodName, String msg) 
  {
	  String value;
	  String message = null != TrackingLogger.getTransactionIdforCurrentRequest() ? "[ " + TrackingLogger.getTransactionIdforCurrentRequest() + " ] - " + msg : msg;
	  
	  if(methodName == null)
	  {
		value = message;  
	  }
	  else if(StringUtils.isEmpty(StringUtils.trimToEmpty(methodName)))
	  {
		  value = " " + " - " + message;  
	  }
	  else
	  {
		  value = methodName + "() -  " + message;  
	  }
	  
	 return value;
 }
  
}
