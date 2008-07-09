/*
 *
 *
 * Copyright  1990-2007 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.midp.rms;

import com.sun.midp.security.SecurityToken;
import com.sun.midp.security.Permissions;

public class RmsEnvironment { 
    static SuiteContainer suiteContainer;
    private static RmsEnvironment rmsEnv;

    private RmsEnvironment() {
    }
    
    public static RmsEnvironment getRmsEnvironment() {
        if (rmsEnv == null) {
            rmsEnv = new RmsEnvironment();
        }
        return rmsEnv;
    }
            
    /* Called by the class running the suite. */
    public static void init(SecurityToken token, SuiteContainer container) {
        token.checkIfPermissionAllowed(Permissions.MIDP);
        suiteContainer = container;
       
    }

    /* Called by java.microedition.rms.RecordStore. */
    public static int getCallersSuiteId() {
        return suiteContainer.getCallersSuiteId();
    }

    /* Called by java.microedition.rms.RecordStore. */
    public static int getSuiteID(String vendorName, String suiteName) {
        return suiteContainer.getSuiteID(vendorName, suiteName);
    }

    /* Called by com.sun.midp.rms.RecordStoreFile. */
    static String getSecureFilenameBase(int suiteId) {
        System.out.println("in getSecureFilenameBase!");
        return suiteContainer.getSecureFilenameBase(suiteId);
    }
}
