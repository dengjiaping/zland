/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/lipengju/zhisland/android/V4.3.6_08.09/zhim_lib/src/com/beem/project/beem/service/aidl/IChatRoom.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IChatRoom extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IChatRoom
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IChatRoom";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IChatRoom interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IChatRoom asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IChatRoom))) {
return ((com.beem.project.beem.service.aidl.IChatRoom)iin);
}
return new com.beem.project.beem.service.aidl.IChatRoom.Stub.Proxy(obj);
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
case TRANSACTION_getMessages:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
long _arg1;
_arg1 = data.readLong();
java.util.List<com.beem.project.beem.service.Message> _result = this.getMessages(_arg0, _arg1);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.Message _arg0;
if ((0!=data.readInt())) {
_arg0 = com.beem.project.beem.service.Message.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.sendMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_addMessageListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IMessageListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IMessageListener.Stub.asInterface(data.readStrongBinder());
this.addMessageListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeMessageListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IMessageListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IMessageListener.Stub.asInterface(data.readStrongBinder());
this.removeMessageListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setOpen(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isOpen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getUnreadMessageCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getUnreadMessageCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isOwner:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isOwner();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_inviteOccupants:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.beem.project.beem.service.Contact> _arg0;
_arg0 = data.createTypedArrayList(com.beem.project.beem.service.Contact.CREATOR);
java.lang.String _arg1;
_arg1 = data.readString();
this.inviteOccupants(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_kickOccupant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.kickOccupant(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getOccupants:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.beem.project.beem.service.Contact> _result = this.getOccupants();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_leaveRoom:
{
data.enforceInterface(DESCRIPTOR);
this.leaveRoom();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IChatRoom
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
@Override public java.util.List<com.beem.project.beem.service.Message> getMessages(long time, long limit) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.beem.project.beem.service.Message> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(time);
_data.writeLong(limit);
mRemote.transact(Stub.TRANSACTION_getMessages, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.beem.project.beem.service.Message.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
    * Send a message.
    * @param message	the message to send
    */
@Override public void sendMessage(com.beem.project.beem.service.Message message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((message!=null)) {
_data.writeInt(1);
message.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
@Override public void addMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addMessageListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
@Override public void removeMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeMessageListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setOpen(boolean isOpen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isOpen)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setOpen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isOpen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isOpen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getUnreadMessageCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUnreadMessageCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isOwner() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isOwner, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void inviteOccupants(java.util.List<com.beem.project.beem.service.Contact> contacts, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(contacts);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_inviteOccupants, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void kickOccupant(java.lang.String nickname, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(nickname);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_kickOccupant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.beem.project.beem.service.Contact> getOccupants() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.beem.project.beem.service.Contact> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOccupants, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.beem.project.beem.service.Contact.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void leaveRoom() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_leaveRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getMessages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_addMessageListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_removeMessageListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_setOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_isOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getUnreadMessageCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_isOwner = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_inviteOccupants = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_kickOccupant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getOccupants = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_leaveRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public java.util.List<com.beem.project.beem.service.Message> getMessages(long time, long limit) throws android.os.RemoteException;
/**
    * Send a message.
    * @param message	the message to send
    */
public void sendMessage(com.beem.project.beem.service.Message message) throws android.os.RemoteException;
/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
public void addMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException;
/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
public void removeMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException;
public void setOpen(boolean isOpen) throws android.os.RemoteException;
public boolean isOpen() throws android.os.RemoteException;
public int getUnreadMessageCount() throws android.os.RemoteException;
public boolean isOwner() throws android.os.RemoteException;
public void inviteOccupants(java.util.List<com.beem.project.beem.service.Contact> contacts, java.lang.String message) throws android.os.RemoteException;
public void kickOccupant(java.lang.String nickname, java.lang.String message) throws android.os.RemoteException;
public java.util.List<com.beem.project.beem.service.Contact> getOccupants() throws android.os.RemoteException;
public void leaveRoom() throws android.os.RemoteException;
}
