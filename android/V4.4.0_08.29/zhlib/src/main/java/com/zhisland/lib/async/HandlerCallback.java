package com.zhisland.lib.async;

import android.os.Message;

/**
 * CallBack of handler, implement the interface and register it to handler,
 * handler will call back through this.
 * 
 * @author arthur
 * 
 */
public interface HandlerCallback {

	/**
	 * the implementation should return true only as it really handle the
	 * message, or return false instead
	 * 
	 * @param mes
	 * @return
	 */
	boolean handleMessage(Message mes);

}
