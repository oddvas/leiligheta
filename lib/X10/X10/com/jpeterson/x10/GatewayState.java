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
 * Contains the value of the gateway state.
 * 
 * @author Jesse Peterson <6/29/99>
 * @version 1.0
 */
public class GatewayState extends Object {
	private long state;

	/**
	 * Create a new GatewayState object. Initial state is set to zero.
	 */
	public GatewayState() {
		state = 0;
	}

	/**
	 * Set the gateway state.
	 * 
	 * @param state
	 *            the new state
	 */
	public void setState(long state) {
		this.state = state;
	}

	/**
	 * Get the gateway state.
	 * 
	 * @return The current state
	 */
	public long getState() {
		return state;
	}
}
