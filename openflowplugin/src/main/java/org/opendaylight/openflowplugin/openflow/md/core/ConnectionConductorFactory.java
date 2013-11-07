/**
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.openflow.md.core;

import org.opendaylight.openflowjava.protocol.api.connection.ConnectionAdapter;
import org.opendaylight.openflowplugin.openflow.md.queue.QueueKeeper;

/**
 * @author mirehak
 *
 */
public abstract class ConnectionConductorFactory {

    /**
     * @param connectionAdapter
     * @param queueKeeper 
     * @return conductor for given connection
     */
    public static ConnectionConductor createConductor(ConnectionAdapter connectionAdapter, 
            QueueKeeper<Object> queueKeeper) {
        ConnectionConductor connectionConductor = new ConnectionConductorImpl(connectionAdapter);
        connectionConductor.setQueueKeeper(queueKeeper);
        connectionConductor.init();
        return connectionConductor;
    }

}