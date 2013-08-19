package org.lightuml.ui.editor;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * <p>
 * A completion proposal for UMLGraph javadoc tags.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class LightUMLCompletionProposal implements IJavaCompletionProposal {
	private String displayString, additionalInfo;

	private int start, replaceLength;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 * @param displayStr
	 *            The proposal string to display.
	 * @param s
	 *            The starting offset in the buffer.
	 * @param l
	 *            The length of the string to replace.
	 * @param info
	 *            Additional information to display.
	 */
	public LightUMLCompletionProposal(String displayStr, int s, int l,
			String info) {
		displayString = displayStr;
		start = s;
		replaceLength = l;
		additionalInfo = info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.java.IJavaCompletionProposal#getRelevance()
	 */
	public int getRelevance() {
		return 20;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#apply(org.eclipse.jface.text.IDocument)
	 */
	public void apply(IDocument document) {
		try {
			document.replace(start, replaceLength + 1, displayString);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getSelection(org.eclipse.jface.text.IDocument)
	 */
	public Point getSelection(IDocument document) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getAdditionalProposalInfo()
	 */
	public String getAdditionalProposalInfo() {
		return additionalInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getDisplayString()
	 */
	public String getDisplayString() {
		return displayString;
	}

	/*
	 * (non-Javadoc) TODO: create an image/logo for LightUML
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposal#getContextInformation()
	 */
	public IContextInformation getContextInformation() {
		return null;
	}

}
