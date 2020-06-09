package com.codetutr.config.logging;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

import com.codetutr.utility.UtilityHelper;


public class TrackingLogger
{
	//Getting Logger Object
	public static Logger getLogger(final Class<?> clazz){
		return new TrackingLoggerWrapper(Logger.getLogger(clazz));
	}
	
	public static Logger getLogger(String name) {
		return new TrackingLoggerWrapper(Logger.getLogger(name));
	}
	
	//Setting trackingId
	public static void setTransactionId(String trackingId){
		TrackingLoggerWrapper.setTransactionId(trackingId);
	}
	
	//Resetting transactionId
	public static void resetTransactionId(){
		TrackingLoggerWrapper.setTransactionId(null);
	}
	
	//Getting TransactionId for CurrentRequest
	public static String getTransactionIdforCurrentRequest(){
		return TrackingLoggerWrapper.getTransactionIdForCurrentRequest();
	}
 
	
	public static class TrackingLoggerWrapper extends Logger
	{
		private final Logger logger;
		private static ThreadLocal<String> transactionId = new ThreadLocal<>();

		
		public TrackingLoggerWrapper(final Logger logger){
			super(logger.getName());
			this.logger = logger;
		}
		
		private static void setTransactionId(String id){
			transactionId.set(id);
		}

		
		private String getTrackingId() {
			if (getTransactionIdForCurrentRequest() == null) {
				transactionId.set(UtilityHelper.generateFullUUIDWithOutDashes());
			}
			return transactionId.get();
		}
		
		
		private static String getTransactionIdForCurrentRequest(){
			return transactionId.get();
		}
		
		
		private Object addTrackingId(Object message) {
			message = "[transactionID : " + getTrackingId() + "] " + message;
			return message;
		}
		
		
		
		
		// Overriding parent Logger class method where replace 'message' by 'addTrackingId(message)' to all. Beyond that nothing

	    @Override
		public int hashCode()
		{
			return logger.hashCode();
		}

	    @Override
		public boolean equals(Object obj)
		{
			return logger.equals(obj);
		}

	    @Override
		public void addAppender(Appender newAppender)
		{
			logger.addAppender(newAppender);
		}

	    @Override
		public void assertLog(boolean assertion, String msg)
		{
			logger.assertLog(assertion, msg);
		}

	    @Override
		public void callAppenders(LoggingEvent event)
		{
			logger.callAppenders(event);
		}

	    @Override
		public void debug(Object message)
		{
			logger.debug(addTrackingId(message));
		}

	    @Override
		public void debug(Object message, Throwable t)
		{
			logger.debug(addTrackingId(message), t);
		}

	    @Override
		public void error(Object message)
		{
			logger.error(addTrackingId(message));
		}

	    @Override
		public void error(Object message, Throwable t)
		{
			logger.error(addTrackingId(message), t);
		}

	    @Override
		public String toString()
		{
			return logger.toString();
		}

	    @Override
		public void fatal(Object message)
		{
			logger.fatal(addTrackingId(message));
		}

	    @Override
		public void fatal(Object message, Throwable t)
		{
			logger.fatal(addTrackingId(message), t);
		}

	    @Override
		public boolean getAdditivity()
		{
			return logger.getAdditivity();
		}

	    @Override
		public Enumeration getAllAppenders()
		{
			return logger.getAllAppenders();
		}

	    @Override
		public Appender getAppender(String name)
		{
			return logger.getAppender(name);
		}

	    @Override
		public Level getEffectiveLevel()
		{
			return logger.getEffectiveLevel();
		}

	    @Override
		public Priority getChainedPriority()
		{
			return logger.getChainedPriority();
		}

	    @Override
		public LoggerRepository getHierarchy()
		{
			return logger.getHierarchy();
		}

	    @Override
		public LoggerRepository getLoggerRepository()
		{
			return logger.getLoggerRepository();
		}

	    @Override
		public ResourceBundle getResourceBundle()
		{
			return logger.getResourceBundle();
		}

	    @Override
		public void info(Object message)
		{
			logger.info(addTrackingId(message));
		}

	    @Override
		public void info(Object message, Throwable t)
		{
			logger.info(addTrackingId(message), t);
		}

	    @Override
		public boolean isAttached(Appender appender)
		{
			return logger.isAttached(appender);
		}

	    @Override
		public boolean isDebugEnabled()
		{
			return logger.isDebugEnabled();
		}

	    @Override
		public boolean isEnabledFor(Priority level)
		{
			return logger.isEnabledFor(level);
		}

	    @Override
		public boolean isInfoEnabled()
		{
			return logger.isInfoEnabled();
		}

	    @Override
		public void l7dlog(Priority priority, String key, Throwable t)
		{
			logger.l7dlog(priority, key, t);
		}

	    @Override
		public void l7dlog(Priority priority, String key, Object[] params, Throwable t)
		{
			logger.l7dlog(priority, key, params, t);
		}

	    @Override
		public void log(Priority priority, Object message, Throwable t)
		{
			logger.log(priority, addTrackingId(message), t);
		}

	    @Override
		public void log(Priority priority, Object message)
		{
			logger.log(priority, addTrackingId(message));
		}

	    @Override
		public void log(String callerFQCN, Priority level, Object message, Throwable t)
		{
			logger.log(callerFQCN, level, addTrackingId(message), t);
		}

	    @Override
		public void removeAllAppenders()
		{
			logger.removeAllAppenders();
		}

	    @Override
		public void removeAppender(Appender appender)
		{
			logger.removeAppender(appender);
		}

	    @Override
		public void removeAppender(String name)
		{
			logger.removeAppender(name);
		}

	    @Override
		public void setAdditivity(boolean additive)
		{
			logger.setAdditivity(additive);
		}

	    @Override
		public void setLevel(Level level)
		{
			logger.setLevel(level);
		}

	    @Override
		public void setPriority(Priority priority)
		{
			logger.setPriority(priority);
		}

	    @Override
		public void setResourceBundle(ResourceBundle bundle)
		{
			logger.setResourceBundle(bundle);
		}

	    @Override
		public void warn(Object message)
		{
			logger.warn(addTrackingId(message));
		}

	    @Override
		public void warn(Object message, Throwable t)
		{
			logger.warn(addTrackingId(message), t);
		}
	}


}
