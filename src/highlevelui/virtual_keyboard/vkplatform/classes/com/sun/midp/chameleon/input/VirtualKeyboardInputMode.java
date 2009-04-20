/*
 *
 * Copyright  1990-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.midp.chameleon.input;

import com.sun.midp.i18n.Resource;
import com.sun.midp.i18n.ResourceConstants;

import com.sun.midp.lcdui.EventConstants;

import com.sun.midp.events.Event;
import com.sun.midp.events.EventTypes;
import com.sun.midp.events.EventQueue;
import com.sun.midp.events.EventListener;
import com.sun.midp.events.NativeEvent;

/**
 * An InputMode instance which allows to use native Windows Mobile
 * input methods.
 */
public class VirtualKeyboardInputMode extends KeyboardInputMode {

    private static native void showNativeKeyboard();
    private static native void hideNativeKeyboard();

    private static String modeName;
    static {
        modeName = Resource.getString(ResourceConstants.LCDUI_TF_NATIVE_VKBD);
    }

	static private class KeyboardDataEventListener implements EventListener {
		VirtualKeyboardInputMode curInputMode;

		KeyboardDataEventListener() {
			EventQueue eq = EventQueue.getEventQueue();
			eq.registerEventListener(EventTypes.VIRTUAL_KEYBOARD_RETURN_DATA_EVENT, this);
		}

		public boolean preprocess(Event event, Event waitingEvent)
		{
			return true;
		}

		public void process(Event event)
		{
			System.out.println("VirtualKeyboardInputMode.process");

			if (curInputMode == null)
			{
				return;
			}

			NativeEvent nativeEvent = (NativeEvent)event;

			if (event.getType() == EventTypes.VIRTUAL_KEYBOARD_RETURN_DATA_EVENT)
			{
				if (nativeEvent.intParam1 == EventConstants.IME2)
				{
					System.out.println("nativeEvent.intParam1 == EventConstants.IME2");
					System.out.println("nativeEvent.stringParam1=" + nativeEvent.stringParam1);

					int curAvailableSize = curInputMode.mediator.getAvailableSize();
					for (; ; )
					{
						System.out.println("curAvailableSize=" + curAvailableSize);
						try
						{
							curInputMode.mediator.clear(1);
						}
						catch (Exception e)
						{
							e.printStackTrace();
							break;
						}
						if (curAvailableSize == curInputMode.mediator.getAvailableSize())
						{
							break;
						}

						curAvailableSize = curInputMode.mediator.getAvailableSize();
					}

					System.out.println("VirtualKeyboardInputMode.process: curInputMode.mediator.commit");
					curInputMode.mediator.commit(nativeEvent.stringParam1);
					System.out.println("VirtualKeyboardInputMode.process: done");
				}
			}
			curInputMode = null;
		}
	}

	private static KeyboardDataEventListener listener = new KeyboardDataEventListener();

	VirtualKeyboardInputMode() {
		listener.curInputMode = this;
	}	

    /**
     * This method will be called before any input keys are passed
     * to this InputMode to allow the InputMode to perform any needed
     * initialization. A reference to the InputModeMediator which is
     * currently managing the relationship between this InputMode and
     * the input session is passed in. This reference can be used
     * by this InputMode to commit text input as well as end the input
     * session with this InputMode. The reference is only valid until
     * this InputMode's endInput() method is called.
     *
     * @param constraints text input constraints. The semantics of the
     * constraints value are defined in the TextField API.
     *
     * @param mediator the InputModeMediator which is negotiating the
     *        relationship between this InputMode and the input session
     *
     * @param inputSubset current input subset
     */
    public void beginInput(InputModeMediator mediator, String inputSubset, int constraints) {
		showNativeKeyboard();
        super.beginInput(mediator, inputSubset, constraints);
    }

    /**
     * Mark the end of this InputMode's processing. The only possible call
     * to this InputMode after a call to endInput() is a call to beginInput()
     * to begin a new input session.
     */
    public void endInput() throws IllegalStateException {
        super.endInput();
        hideNativeKeyboard();
    }

    /**
     * Returns the display name which will represent this InputMode to
     * the user, such as in a selection list or the softbutton bar.
     *
     * @return the locale-appropriate name to represent this InputMode
     *         to the user
     */
    public String getName() {
        return modeName;
    }
}