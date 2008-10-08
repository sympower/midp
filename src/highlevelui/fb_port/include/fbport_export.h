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

#ifndef _FB_PORT_EXPORT_H_
#define _FB_PORT_EXPORT_H_

/**
 * @file
 *
 * Porting layer for frame buffer based applications designed to unify
 * various fb application types such as qvfb, directfb and simple fb
 */

#ifdef __cplusplus
extern "C" {
#endif

#include <gxj_screen_buffer.h>

/** Initialize frame buffer video device */
extern void connectFrameBuffer(int hardwareId);

/** Allocate system screen buffer according to the screen geometry */
extern void initScreenBuffer(int width, int height);

extern void initScreenList();

extern  void initSystemScreen(int id, int isFullScreen, int reverse_orientation, int width, int height);

extern void clearScreens();

/** Free allocated resources and restore system state */
extern void finalizeFrameBuffer();

/**
 * Change screen orientation to landscape or portrait,
 * depending on the current screen mode
 */
extern jboolean reverseScreenOrientation(int id);

extern jboolean setFullScreenMode(int id, int mode, int width, int height);

extern int getReverseOrientation(int id);

extern jboolean isFullScreenMode(int id);

/**
 * Resizes system screen buffer to fit the screen dimensions.
 * Call after frame buffer is initialized.
 */
extern void resizeScreenBuffer(int width, int height);

extern void refreshScreen(int id, int x1, int y1, int x2, int y2);

/** Return file descriptor of keyboard device, or -1 in none */
extern int getKeyboardFd();

/** Return file descriptor of mouse device, or -1 in none */
extern int getMouseFd();

/** Clear screen device */
extern void clearScreen(int hardwareId);

/** Get x-coordinate of screen origin */
extern int getScreenX(int hardwareId, int width);

/** Get y-coordinate of screen origin */
extern int getScreenY(int hardwareId, int height);

  /** Get the list of display ids */
extern jint* getDisplayIds(jint* n );

extern void displayStateChanged(int hardwareId, int state);
extern int getNextDisplayId();
extern int getCurrentDisplayId();

#ifdef __cplusplus
}
#endif

#endif /* _FB_PORT_EXPORT_H_ */
