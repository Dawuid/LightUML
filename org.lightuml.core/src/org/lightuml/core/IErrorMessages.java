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

package org.lightuml.core;

/**
 * <p>
 * Error messages.
 * </p>
 * 
 * @author anthakal
 * @hidden
 */
public interface IErrorMessages {
    public String ERRMSG_NO_UMLGRAPH_JAR = "No UmlGraph.jar found. See that you have set the UmlGraph.jar path in Preferences > Java > LightUML > UMLGraph.";

    public String ERRMSG_NO_PIC_MACROS = "No sequence.pic found! See that you have set the sequence.pic path in Preferences > Java > LightUML > UMLGraph.";
}
