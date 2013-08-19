package org.lightuml.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lightuml.core.IGraphBuildProperties;
import org.lightuml.ui.IUMLGraphConstants;
import org.lightuml.ui.LightUMLUIPlugin;

abstract public class LightUMLPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage, IGraphBuildProperties, IUMLGraphConstants {

    protected static LightUMLUIPlugin uiPlugin;

    public LightUMLPreferencePage() {
        super(GRID);
        uiPlugin = LightUMLUIPlugin.getDefault();
        setPreferenceStore(uiPlugin.getPreferenceStore());
    }

    // field editors created in subclasses
    // protected void createFieldEditors() {
    // }

    public void init(IWorkbench workbench) {
    }
}
