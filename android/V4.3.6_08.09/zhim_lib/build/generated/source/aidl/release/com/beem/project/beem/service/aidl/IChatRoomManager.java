/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/lipengju/zhisland/android/V4.3.6_08.09/zhim_lib/src/com/beem/project/beem/service/aidl/IChatRoomManager.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IChatRoomManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IChatRoomManager
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IChatRoomManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IChatRoomManager interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IChatRoomManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IChatRoomManager))) {
return ((com.beem.project.beem.service.aidl.IChatRoomManager)iin);
}
return new com.beem.project.beem.service.aidl.IChatRoomManager.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getChatRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.beem.project.beem.service.aidl.IChatRoom _result = this.getChatRoom(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_createRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.createRoom(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_joinMultiUserChat:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
boolean _arg3;
_arg3 = (0!=data.readInt());
com.beem.project.beem.service.aidl.IChatRoom _result = this.joinMultiUserChat(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getHostRooms:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getHostRooms();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_addChatRoomCreationListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChatRoomManagerListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IChatRoomManagerListener.Stub.asInterface(data.readStrongBinder());
this.addChatRoomCreationListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeChatRoomCreationListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChatRoomManagerListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IChatRoomManagerListener.Stub.asInterface(data.readStrongBinder());
this.removeChatRoomCreationListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IChatRoomManager
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public com.beem.project.beem.service.aidl.IChatRoom getChatRoom(java.lang.String roomName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.aidl.IChatRoom _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(roomName);
mRemote.transact(Stub.TRANSACTION_getChatRoom, _data, _reply, 0);
_reply.readException();
_result = com.beem.project.beem.service.aidl.IChatRoom.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void createRoom(java.lang.String roomName, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(roomName);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_createRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.beem.project.beem.service.aidl.IChatRoom joinMultiUserChat(java.lang.String roomName, java.lang.String userName, java.lang.String password, boolean daemon) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.aidl.IChatRoom _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(roomName);
_data.writeString(userName);
_data.writeString(password);
_data.writeInt(((daemon)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_joinMultiUserChat, _data, _reply, 0);
_reply.readException();
_result = com.beem.project.beem.service.aidl.IChatRoom.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getHostRooms() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getHostRooms, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addChatRoomCreationListener(com.beem.project.beem.service.aidl.IChatRoomManagerListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addChatRoomCreationListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeChatRoomCreationListener(com.beem.project.beem.service.aidl.IChatRoomManagerListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeChatRoomCreationListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getChatRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_createRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_joinMultiUserChat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getHostRooms = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_addChatRoomCreationListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_removeChatRoomCreationListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public com.beem.project.beem.service.aidl.IChatRoom getChatRoom(java.lang.String roomName) throws android.os.RemoteException;
public void createRoom(java.lang.String roomName, java.lang.String password) throws android.os.RemoteException;
public com.beem.project.beem.service.aidl.IChatRoom joinMultiUserChat(java.lang.String roomName, java.lang.String userName, java.lang.String password, boolean daemon) throws android.os.RemoteException;
public java.util.List<java.lang.String> getHostRooms() throws android.os.RemoteException;
public void addChatRoomCreationListener(com.beem.project.beem.service.aidl.IChatRoomManagerListener listener) throws android.os.RemoteException;
public void removeChatRoomCreationListener(com.beem.project.beem.service.aidl.IChatRoomManagerListener listener) throws android.os.RemoteException;
}
