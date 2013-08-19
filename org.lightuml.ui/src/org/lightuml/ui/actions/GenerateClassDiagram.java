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


package org.lightuml.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.lightuml.core.LightUMLCorePlugin;

/**
 * 
 * <p>
 * The popup menu action that is used to trigger generation of a class diagram.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class GenerateClassDiagram implements IObjectActionDelegate {
    /**
     * <p>
     * The project to generate a class diagram for.
     * </p>
     */
    private IProject project = null;

    /**
     * <p>
     * The Java package path (if a package is selected).
     * </p>
     */
    private IPath packagePath = null;

    /**
     * <p>
     * Name of the Java package (if a package is selected).
     * </p>
     */
    private String packageName = null;

    /**
     * <p>
     * Constructor for GenerateClassDiagram.
     * </p>
     */
    public GenerateClassDiagram() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
     *      org.eclipse.ui.IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    /**
     * @see IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        LightUMLCorePlugin.getDefault().generateClassDiagram(project, packagePath,
                packageName);
    }

    /**
     * @see IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {

        Object target = ((StructuredSelection) selection).getFirstElement();

        // target selection is a java project
        // TODO: subject for refactoring (null assignment smells)
        if (target instanceof IJavaProject) {
            project = ((IJavaProject) target).getProject();
            packagePath = null;
            packageName = null;
        }
        // target selection is a java package
        else if (target instanceof IPackageFragment) {
            IPackageFragment pf = (IPackageFragment) target;
            IResource resource = pf.getResource();

            project = resource.getProject();
            packagePath = resource.getProjectRelativePath();
            packageName = pf.getElementName();
        }
    }
}
