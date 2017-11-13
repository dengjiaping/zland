
package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.Contact;
import  com.beem.project.beem.service.Message;
import  com.beem.project.beem.service.aidl.IChatRoom;
import  com.beem.project.beem.service.aidl.IChatRoomManagerListener;

interface IChatRoomManager {

    IChatRoom getChatRoom(in String roomName);
	void createRoom(in String roomName, in String password);
	IChatRoom joinMultiUserChat(in String roomName, in String userName, in String password, in boolean daemon);
	List<String> getHostRooms();

    void addChatRoomCreationListener(in IChatRoomManagerListener listener);
    void removeChatRoomCreationListener(in IChatRoomManagerListener listener);
}
