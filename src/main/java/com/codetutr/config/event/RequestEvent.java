package com.codetutr.config.event;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import org.springframework.stereotype.Component;

import com.codetutr.config.logging.TrackingLogger;
import com.codetutr.utility.UtilityHelper;

@Component
@WebListener
public class RequestEvent implements ServletRequestListener
{
	@Override
    public void requestInitialized (ServletRequestEvent event) {
		TrackingLogger.resetTransactionId();
        //TrackingLogger.setTransactionId(((HttpServletRequest) event.getServletRequest()).getSession().getId() + "  ::  " + UtilityHelper.generateUUID());
        TrackingLogger.setTransactionId(UtilityHelper.generateUUID());
    }
	
	@Override
	public void requestDestroyed (ServletRequestEvent event) {
		
		/**
		 * Some application server might re-use the thread. so it is better to trace it after using
		 */
		TrackingLogger.resetTransactionId();
	}


}
