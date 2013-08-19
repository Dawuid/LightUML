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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

/**
 * <p>
 * Note: most comments from Eclipse template for preference page extension
 * point.
 * </p>
 * <p>
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * </p>
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 * </p>
 * <p>
 * General preferences for LightUML.
 * </p>
 * 
 * @has - - - org.lightuml.ui.preferences.DotAndPic2PlotPage
 * @has - - - org.lightuml.ui.preferences.UMLGraphPage
 * @has - - - org.lightuml.ui.preferences.ClassDiagramsPage
 * 
 * @author Antti Hakala
 */
public class LightUMLPage extends LightUMLPreferencePage {
    /**
     * <p>
     * Constructor. Sets the preference store to preference store of
     * LightUMLUIPlugin.
     * </p>
     * 
     */
    public LightUMLPage() {
        setDescription("General preferences for LightUML:");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */

    public void createFieldEditors() {
        addField(new StringFieldEditor(P_GRAPH_FILE_NAME, "Graph file name:",
                getFieldEditorParent()));
        addField(new StringFieldEditor(P_PROJECT_OUTPUT_DIR,
                "Output directory (relative to project root, optional):",
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_USE_PACKAGE_NAME,
                "Use package or project name as the graph file name",
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_RECURSE_PACKAGES,
                "Recurse into subpackages", getFieldEditorParent()));
        addField(new FileFieldEditor(P_JAVADOC_EXECUTABLE_PATH,
                "Javadoc executable path (optional):", true,
                getFieldEditorParent()));
    }
}