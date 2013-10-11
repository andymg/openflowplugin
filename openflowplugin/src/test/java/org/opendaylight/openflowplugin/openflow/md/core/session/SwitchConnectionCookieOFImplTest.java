/**
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.openflow.md.core.session;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.openflowplugin.openflow.md.core.SwitchConnectionDestinguisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mirehak
 */
public class SwitchConnectionCookieOFImplTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(SwitchConnectionCookieOFImplTest.class);

    private SwitchConnectionCookieOFImpl switchConnectionKey;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        switchConnectionKey = createSwitchSessionKey("a1a2a3a4a5a6", (short) 42);
    }

    /**
     * @param datapathId
     * @return
     */
    private static SwitchConnectionCookieOFImpl createSwitchSessionKey(
            String datapathId, short auxiliary) {
        SwitchConnectionCookieOFImpl key = new SwitchConnectionCookieOFImpl();
        key.setDatapathId(new BigInteger(datapathId, 16));
        key.setAuxiliaryId(auxiliary);
        return key;
    }

    /**
     * Test method for
     * {@link org.opendaylight.openflowplugin.openflow.md.core.session.SwitchConnectionCookieOFImpl#getId()}
     * .
     */
    @Test
    public void testGetId() {
        switchConnectionKey.initId();
        LOG.debug("testKey.id: " + Arrays.toString(switchConnectionKey.getId()));
        byte[] expected = new byte[] { 32, 9, -69, 43, -68, -53, 49, 105, -109,
                -15, -20, 50, -121, -34, -22, 3, 27, 42, 90, -59 };
        Assert.assertArrayEquals(expected, switchConnectionKey.getId());
    }

    /**
     * Test method for
     * {@link org.opendaylight.openflowplugin.openflow.md.core.session.SwitchConnectionCookieOFImpl#initUUID()}
     * .
     */
    @Test
    public void testInitId1() {
        try {
            switchConnectionKey.setDatapathId(null);
            switchConnectionKey.initId();
            Assert.fail("init should fail with no datapathId");
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.opendaylight.openflowplugin.openflow.md.core.session.SwitchConnectionCookieOFImpl#equals(Object)}
     * ,
     * {@link org.opendaylight.openflowplugin.openflow.md.core.session.SwitchConnectionCookieOFImpl#hashCode()}
     * .
     */
    @Test
    public void testHashAndEquals() {
        // insert equal keys
        SwitchConnectionCookieOFImpl key1 = createSwitchSessionKey("1234567890",
                (short) 42);
        key1.initId();

        SwitchConnectionCookieOFImpl key2 = createSwitchSessionKey("1234567890",
                (short) 42);
        key2.initId();

        SwitchConnectionCookieOFImpl key3 = createSwitchSessionKey("123456789",
                (short) 42);
        key3.initId();
        SwitchConnectionCookieOFImpl key4 = createSwitchSessionKey("123456789",
                (short) 21);
        key4.initId();

        Map<SwitchConnectionDestinguisher, Integer> keyLot = new HashMap<>();
        keyLot.put(key1, System.identityHashCode(key1));
        Assert.assertEquals(1, keyLot.size());
        keyLot.put(key2, System.identityHashCode(key2));
        Assert.assertEquals(1, keyLot.size());
        keyLot.put(key3, System.identityHashCode(key3));
        Assert.assertEquals(2, keyLot.size());
        keyLot.put(key4, System.identityHashCode(key4));
        Assert.assertEquals(3, keyLot.size());

        // lookup using inited key
        Assert.assertEquals(System.identityHashCode(key2), keyLot.get(key1)
                .intValue());
        Assert.assertEquals(System.identityHashCode(key2), keyLot.get(key2)
                .intValue());
        Assert.assertEquals(System.identityHashCode(key3), keyLot.get(key3)
                .intValue());
        Assert.assertEquals(System.identityHashCode(key4), keyLot.get(key4)
                .intValue());

        // lookup using not inited key
        SwitchConnectionCookieOFImpl keyWithoutInit = createSwitchSessionKey(
                "123456789", (short) 42);
        Assert.assertNull(keyLot.get(keyWithoutInit));

        // creating brand new key and lookup
        SwitchConnectionCookieOFImpl keyWithInit = createSwitchSessionKey(
                "123456789", (short) 42);
        keyWithInit.initId();
        Assert.assertEquals(System.identityHashCode(key3),
                keyLot.get(keyWithInit).intValue());

        // lookup with key containing encoded part only
        LOG.debug("key3.id: " + Arrays.toString(key3.getId()));
        SwitchConnectionCookieOFImpl keyWithoutDPID = new SwitchConnectionCookieOFImpl(
                new byte[] { -12, -121, -45, -16, 98, 33, -19, -66, -93, -46, -52, 79, -13, -116, -97, 0, 121, 78, -104, 29 });
        Assert.assertEquals(System.identityHashCode(key3),
                keyLot.get(keyWithoutDPID).intValue());
    }

}
