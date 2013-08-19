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

package org.lightuml.ui.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * <p>
 * An editor for .pic files. Requires some work.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class PicEditor extends TextEditor {
    /**
     * <p>
     * Sourceviewerconfiguration.
     * </p>
     * 
     * @hidden
     */
    private class PicFileSourceViewerConfiguration extends
            TextSourceViewerConfiguration {
    }

    /**
     * <p>
     * The constructor.
     * </p>
     * 
     */
    public PicEditor() {
        super();
        setSourceViewerConfiguration(new PicFileSourceViewerConfiguration());
    }

}
