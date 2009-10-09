/*
 *
 *
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved.
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

package com.sun.midp.appmanager;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.*;

import com.sun.midp.i18n.Resource;
import com.sun.midp.i18n.ResourceConstants;
import com.sun.midp.lcdui.Notice;
import com.sun.midp.main.MIDletProxy;
import com.sun.midp.main.NoticeManager;
import com.sun.midp.main.NoticeManagerListener;


/**
 * Displays a small informational note unobtrusive way.
 * <p>
 * A notice is used by MIDlet post small portion of information
 * to the user without taking screen control.
 *
 */
class NoticeManagerUIImpl extends Form implements CommandListener, ItemCommandListener, NoticeManagerListener  {
    Alert alert;
    Command dismiss;
    Command select;
    Vector notices;
    Display display;
    NoticeManager manager;
    RunningMIDletSuiteInfo rmi;
    Command exit;
    Command show;
    Displayable parent;
    static Hashtable actionCenter;

    private NoticeManagerUIImpl(Display d, Displayable p, RunningMIDletSuiteInfo s) {
        super(Resource.getString(ResourceConstants.NOTICE_POPUP_TITLE)
                  + s.displayName);
        manager = NoticeManager.getInstance();
        manager.addListener(this);
        display = d;
        parent = p;
        setupForm();
    }

    static Form getNoticeManagerFor(RunningMIDletSuiteInfo s, Display d, Displayable p) {
        if (null == actionCenter) {
            actionCenter = new Hashtable();
        }
        Form form = (Form)actionCenter.get(s);
        if (null == form) {
            form  = new NoticeManagerUIImpl(d, p, s);
            actionCenter.put(s, form);
        }

        return form;
    }

    private void setupForm() {
        Notice[] n = manager.getNotices();
        if (null != n) {
            notices = new Vector();
            MIDletProxy[] proxies = rmi.getProxies();
            for (int i = 0; i < n.length; i++) {
                for (int j = 0; j < proxies.length; j++) {
                    if (n[i].getOriginatorID() == proxies[j].getIsolateId()) {
                        notices.addElement(n[i]);
                    }
                }
            }
        }
        exit = new Command(Resource.getString(ResourceConstants.EXIT), Command.EXIT, 1);
        addCommand(exit);
        setCommandListener(this);
        Enumeration enm = notices.elements();
        while (enm.hasMoreElements()) {
            Notice note = (Notice)enm.nextElement();
            addItem(note);
        }
    }

    private void addItem(Notice n) {
        ImageItem item = new ImageItem(n.getLabel(), n.getImage(), 
                                       ImageItem.LAYOUT_LEFT, 
                                       null, ImageItem.PLAIN);
        if (null == show) {
            show = new Command(Resource.getString(ResourceConstants.SHOW_MSG), Command.ITEM, 1);
        }
        item.addCommand(show);
        item.setItemCommandListener(this);
        append(item);
    }

    /**
     * Informs about new information note.
     *
     * @param notice new information note
     */
    public void notifyNotice(Notice note) {
        MIDletProxy[] proxies = rmi.getProxies();
        for (int j = 0; j < proxies.length; j++) {
            if (note.getOriginatorID() == proxies[j].getIsolateId()) {
                notices.addElement(note);
                addItem(note);
            }
        }
    }

    /**
     * Informs about given information note need to be discarded
     *
     * @param notice information note
     */
    public void removeNotice(Notice note) {
        if (notices.contains(note)) {
            int i = notices.indexOf(note);
            delete(i);
            notices.removeElement(note);
            if (0 == notices.size() && display.getCurrent() != this) {
                // ready for GC
                manager.removeListener(this);
                actionCenter.remove(rmi);
            }
        }
    }

    /**
     * Informs that the notice was updated
     *
     * @param notice the notice was updated
     */
    public void updateNotice(Notice note) {
        if (notices.contains(note)) {
            int i = notices.indexOf(note);
            ImageItem item = (ImageItem)get(i);
            item.setImage(note.getImage());
            item.setLabel(note.getLabel());
        }
    }

    /**
     * Indicates that a command event has occurred on
     * <code>Displayable d</code>.
     *
     * @param c a <code>Command</code> object identifying the
     * command. This is either one of the
     * applications have been added to <code>Displayable</code> with
     * {@link Displayable#addCommand(Command)
     * addCommand(Command)} or is the implicit
     * {@link List#SELECT_COMMAND SELECT_COMMAND} of
     * <code>List</code>.
     * @param d the <code>Displayable</code> on which this event
     *  has occurred
     */
    public void commandAction(Command c, Displayable d) {
        if (c == exit) {
            display.setCurrent(parent);
        }
    }

    /**
     * Called by the system to indicate that a command has been invoked on a 
     * particular item.
     * 
     * @param c the <code>Command</code> that was invoked
     * @param item the <code>Item</code> on which the command was invoked
     */
    public void commandAction(Command c, Item item) {
        if (show == c) {
            Alert alert = new Alert("Message:", item.getLabel(), 
                                    ((ImageItem)item).getImage(),
                                    null);
            display.setCurrent(alert, this);
        }
    }
}
