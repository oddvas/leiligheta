/*
 * Copyright (C) 1999  Jesse E. Peterson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *
 */

package com.jpeterson.x10;

/**
 * Indicates that too many attempts have occurred while trying to send a
 * transmission.
 * 
 * @author Jesse Peterson <jesse@jpeterson.com>
 */
public class TooManyAttemptsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new exception.
	 */
	public TooManyAttemptsException() {
		super();
	}

	/**
	 * Create a new exception with the specified message.
	 * 
	 * @param s
	 *            the detail message
	 */
	public TooManyAttemptsException(String s) {
		super(s);
	}
}