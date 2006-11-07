/*
 *
 * Copyright  1990-2006 Sun Microsystems, Inc. All Rights Reserved.
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

package com.sun.midp.automation;

/**
 * Represents event in event queue.
 */
public interface AutoEvent {
    /**
     * Waits (blocks) until event reaches specified state.
     *
     * @param state state to wait for
     */
    public void waitFor(AutoEventState state)
        throws IllegalStateException;
        
    /**
     * Gets current event's state.
     *
     * @return AutoEventState representing current
     * event's state
     */
    public AutoEventState getEventState();
    
    /**
     * Gets event's data.
     *
     * @return AutoEventData representing event's data
     */
    public AutoEventData getEventData();
}
