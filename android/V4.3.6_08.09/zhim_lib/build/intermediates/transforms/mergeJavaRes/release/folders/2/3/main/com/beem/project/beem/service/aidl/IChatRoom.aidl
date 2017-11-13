
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.Contact;
import  com.beem.project.beem.service.Message;
import  com.beem.project.beem.service.aidl.IMessageListener;

interface IChatRoom {
    List<com.beem.project.beem.service.Message> getMessages(in long time, in long limit);

    /**
    * Send a message.
    * @param message	the message to send
    */
	void sendMessage(in com.beem.project.beem.service.Message message);
	/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
	void addMessageListener(in IMessageListener listener);

	/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
	void removeMessageListener(in IMessageListener listener);
    void setOpen(in boolean isOpen);
    boolean isOpen();
    int getUnreadMessageCount();
    boolean isOwner();
    void inviteOccupants(in List<Contact> contacts, in String message);
    void kickOccupant(in String nickname, in String message);
    List<Contact> getOccupants();
    void leaveRoom();
}
