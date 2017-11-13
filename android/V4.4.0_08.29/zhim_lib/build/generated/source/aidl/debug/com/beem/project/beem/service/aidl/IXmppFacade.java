/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/lipengju/zhisland/android/V4.4.0_08.29/zhim_lib/src/main/aidl/com/beem/project/beem/service/aidl/IXmppFacade.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IXmppFacade extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IXmppFacade
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IXmppFacade";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IXmppFacade interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IXmppFacade asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IXmppFacade))) {
return ((com.beem.project.beem.service.aidl.IXmppFacade)iin);
}
return new com.beem.project.beem.service.aidl.IXmppFacade.Stub.Proxy(obj);
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
case TRANSACTION_createConnection:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IXmppConnection _result = this.createConnection();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_connectSync:
{
data.enforceInterface(DESCRIPTOR);
this.connectSync();
reply.writeNoException();
return true;
}
case TRANSACTION_connectAsync:
{
data.enforceInterface(DESCRIPTOR);
this.connectAsync();
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
this.disconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_getChatManager:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChatManager _result = this.getChatManager();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IXmppFacade
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
/**
     * Get the XmppConnection of the facade.
     */
@Override public com.beem.project.beem.service.aidl.IXmppConnection createConnection() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.aidl.IXmppConnection _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_createConnection, _data, _reply, 0);
_reply.readException();
_result = com.beem.project.beem.service.aidl.IXmppConnection.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Connect and login synchronously on the server.
     */
@Override public void connectSync() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_connectSync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Connect and login asynchronously on the server.
     */
@Override public void connectAsync() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_connectAsync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Disconnect from the server
     */
@Override public void disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Get the chat manager.
     */
@Override public com.beem.project.beem.service.aidl.IChatManager getChatManager() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.aidl.IChatManager _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChatManager, _data, _reply, 0);
_reply.readException();
_result = com.beem.project.beem.service.aidl.IChatManager.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_createConnection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_connectSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_connectAsync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getChatManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
/**
     * Get the XmppConnection of the facade.
     */
public com.beem.project.beem.service.aidl.IXmppConnection createConnection() throws android.os.RemoteException;
/**
     * Connect and login synchronously on the server.
     */
public void connectSync() throws android.os.RemoteException;
/**
     * Connect and login asynchronously on the server.
     */
public void connectAsync() throws android.os.RemoteException;
/**
     * Disconnect from the server
     */
public void disconnect() throws android.os.RemoteException;
/**
     * Get the chat manager.
     */
public com.beem.project.beem.service.aidl.IChatManager getChatManager() throws android.os.RemoteException;
}
