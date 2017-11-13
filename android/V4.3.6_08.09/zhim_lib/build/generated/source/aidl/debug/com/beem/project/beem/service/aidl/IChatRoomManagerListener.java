/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/lipengju/zhisland/android/V4.3.6_08.09/zhim_lib/src/com/beem/project/beem/service/aidl/IChatRoomManagerListener.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IChatRoomManagerListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IChatRoomManagerListener
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IChatRoomManagerListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IChatRoomManagerListener interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IChatRoomManagerListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IChatRoomManagerListener))) {
return ((com.beem.project.beem.service.aidl.IChatRoomManagerListener)iin);
}
return new com.beem.project.beem.service.aidl.IChatRoomManagerListener.Stub.Proxy(obj);
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
case TRANSACTION_chatRoomCreated:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChatRoom _arg0;
_arg0 = com.beem.project.beem.service.aidl.IChatRoom.Stub.asInterface(data.readStrongBinder());
boolean _arg1;
_arg1 = (0!=data.readInt());
this.chatRoomCreated(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IChatRoomManagerListener
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
@Override public void chatRoomCreated(com.beem.project.beem.service.aidl.IChatRoom chatroom, boolean locally) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((chatroom!=null))?(chatroom.asBinder()):(null)));
_data.writeInt(((locally)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_chatRoomCreated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_chatRoomCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void chatRoomCreated(com.beem.project.beem.service.aidl.IChatRoom chatroom, boolean locally) throws android.os.RemoteException;
}
