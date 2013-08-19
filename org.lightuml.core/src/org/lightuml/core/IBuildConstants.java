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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * <p>
 * Constant filenames and paths.
 * </p>
 * 
 * @author Antti Hakala
 * @hidden
 */
public interface IBuildConstants {
    public IPath BUILD_FILE_NAME = new Path("build")
            .addFileExtension("xml");

    // this is defined in build.xml too..
    public IPath GRAPH_BUILD_PROPERTIES_FILE_NAME = new Path(
            "graph").addFileExtension("ini");

    // files to be copied to statelocation are stored here
    public IPath TO_STATELOCATION_DIR = new Path("to_statelocation");
    
    // statelocation related stuff    
    public IPath STATELOC_OUTPUT_DIR = new Path("graph");
    public IPath STATELOC_RUNSETTINGS = new Path("runsettings")
            .addFileExtension("ini");

}
