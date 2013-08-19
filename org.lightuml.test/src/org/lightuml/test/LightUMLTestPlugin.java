/*
 * (C) Copyright 2005 Antti Hakala
 * 
 *  Permission to use, copy, and distribute this software and its documentation 
 *  for any purpose and without fee is hereby granted, provided that the above 
 *  copyright notice appear in all copies and that both that copyright notice and 
 *  this permission notice appear in supporting documentation.
 *  
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES,
 *  INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE. 
 */

package org.lightuml.test;

import org.eclipse.core.runtime.Plugin;

public class LightUMLTestPlugin extends Plugin {
	private static LightUMLTestPlugin plugin;

	public LightUMLTestPlugin() {
		plugin = this;
	}
	
	public static LightUMLTestPlugin getDefault() {
		if(plugin == null)
			plugin = new LightUMLTestPlugin();
		return plugin;
	}
}
