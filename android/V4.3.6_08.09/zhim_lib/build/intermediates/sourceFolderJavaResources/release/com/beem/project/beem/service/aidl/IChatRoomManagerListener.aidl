package com.beem.project.beem.service.aidl;

import  com.beem.project.beem.service.aidl.IChatRoom;

interface IChatRoomManagerListener {
    void chatRoomCreated(IChatRoom chatroom, boolean locally);
}
