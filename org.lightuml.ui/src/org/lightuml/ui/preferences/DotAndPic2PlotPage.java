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

package org.lightuml.ui.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

/**
 * <p>
 * Preference subpage for dot executable.
 * </p>
 * 
 * @author Antti Hakala
 */
public class DotAndPic2PlotPage extends LightUMLPreferencePage {
    /**
     * <p>
     * Constructor.
     * </p>
     */
    public DotAndPic2PlotPage() {
        setDescription("Preferences for dot and pic2plot:");
    }

    /**
     * <p>
     * Creates the field editors.
     * </p>
     */
    public void createFieldEditors() {
        addField(new StringFieldEditor(P_DOT_EXTRA_PARAM,
                "Dot extra commandline parameters (optional):",
                getFieldEditorParent()));
        addField(new StringFieldEditor(P_PIC2PLOT_EXTRA_PARAM,
                "pic2plot extra commandline parameters (optional):",
                getFieldEditorParent()));

        addField(new DirectoryFieldEditor(P_EXTRA_LOOKUP_PATH,
                "Extra lookup path (optional):", getFieldEditorParent()));
        addField(new RadioGroupFieldEditor(P_GRAPHICS_FORMAT,
                "Graphics format:", 1, OUTPUT_FORMATS, getFieldEditorParent()));
    }
}