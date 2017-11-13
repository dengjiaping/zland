/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.beem.service;

import android.os.RemoteException;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IXmppConnection;
import com.beem.project.beem.service.aidl.IXmppFacade;

/**
 * This class is a facade for the Beem Service.
 *
 * @author darisk
 */
public class XmppFacade extends IXmppFacade.Stub {

    private static final String TAG = "XmppFacade";
    private XmppConnectionAdapter mConnexion;
    private final BeemService service;

    /**
     * Create an XmppFacade.
     *
     * @param service the service providing the facade
     */
    public XmppFacade(final BeemService service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectAsync() throws RemoteException {
        initConnection();
        mConnexion.connectAsync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectSync() throws RemoteException {
        initConnection();
        mConnexion.connectSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IXmppConnection createConnection() throws RemoteException {
        initConnection();
        return mConnexion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws RemoteException {
        initConnection();
        mConnexion.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChatManager getChatManager() throws RemoteException {
        initConnection();
        return mConnexion.getChatManager();
    }

    /**
     * Initialize the connection.
     */
    private void initConnection() {
        if (mConnexion == null) {
            mConnexion = service.createConnection();
        }
    }

}
