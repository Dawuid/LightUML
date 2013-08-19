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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.lightuml.core.LightUMLCorePlugin;

/**
 * <p>
 * Popup menu action for generating a sequence diagram from a .pic file.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class GenerateSequenceDiagram implements IObjectActionDelegate {
	private IFile picFile;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void run(IAction action) {
		LightUMLCorePlugin.getDefault().generateSequenceDiagram(picFile);
	}

	public void selectionChanged(IAction action, ISelection selection) {
		Object target = ((StructuredSelection) selection).getFirstElement();
		picFile = (IFile) target;
	}

}
