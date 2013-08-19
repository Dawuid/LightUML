package org.lightuml.ui.editor;

import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.lightuml.ui.IUMLGraphConstants;
import org.lightuml.ui.LightUMLUIPlugin;

/**
 * <p>
 * Javadoc completion processor for UMLGraph javadoc tags.
 * </p>
 * 
 * @author Antti Hakala
 * 
 */
public class LightUMLJavadocProcessor implements IJavadocCompletionProcessor,
		IUMLGraphConstants {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#computeContextInformation(org.eclipse.jdt.core.ICompilationUnit,
	 *      int)
	 */
	public IContextInformation[] computeContextInformation(ICompilationUnit cu,
			int offset) {
		return null;
	}

	/**
	 * <p>
	 * a class that holds the text that should be completed and its offset in
	 * the buffer.
	 * </p>
	 * 
	 * @author Antti Hakala
	 * 
	 */
	private class CompletionFor {
		public int offset;

		public String text;

		public CompletionFor(int i, String s) {
			offset = i;
			text = s;
		}
	}

	/**
	 * <p>
	 * Find the actual text to complete from the compilation unit.
	 * </p>
	 * 
	 * @param cu
	 *            compilation unit
	 * @param offset
	 *            offset in the text
	 * @param length
	 *            length of the selection
	 * @return a CompletionFor that holds the text to complete
	 */
	private CompletionFor getTextToComplete(ICompilationUnit cu, int offset,
			int length) {
		// search backwards till @ is found,
		// search forwards till ' ' or '\n' is found.
		int begin, end;
		try {
			String wholeText = cu.getBuffer().getContents();
			for (begin = offset; begin > 0; begin--) {
				if (wholeText.charAt(begin) == '@')
					break;
			}
			for (end = offset; end < wholeText.length(); end++) {
				if (wholeText.charAt(end) == ' '
						|| wholeText.charAt(end) == '\n')
					break;
			}
			// begin +1 to remove the @, substring() omits the last char
			String text = wholeText.substring(begin + 1, end);
			return new CompletionFor(begin, text);

		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>
	 * Get proposals for (at)opt options.
	 * </p>
	 * 
	 * @param cf
	 *            CompletionFor, get completion for this
	 * @return a Vector with the LightUMLCompletionProposals
	 */
	public Vector getOptionProposals(CompletionFor cf) {
		Vector result = new Vector();
        String[][] umlgraphParams = LightUMLUIPlugin.getDefault().getUMLGraphParams();
        String[][] umlgraphParamsNoUI = LightUMLUIPlugin.getDefault().getUMLGraphParamsNoUI();
        
        // TODO: clean this up a bit.
		for (int i = 0; i < umlgraphParams.length; i++)
			if (("opt " + umlgraphParams[i][0]).indexOf(cf.text) == 0)
				result.add(new LightUMLCompletionProposal("@opt "
						+ umlgraphParams[i][0], cf.offset, cf.text.length(),
                        umlgraphParams[i][1]));
		
        for (int i = 0; i < umlgraphParamsNoUI.length; i++)
            if (("opt " + umlgraphParamsNoUI[i][0]).indexOf(cf.text) == 0)
                result.add(new LightUMLCompletionProposal("@opt "
                        + umlgraphParamsNoUI[i][0], cf.offset, cf.text.length(),
                        umlgraphParamsNoUI[i][1]));
        
		for (int i = 0; i < UMLGRAPH_COLOR_PARAMS.length; i++)
			if (("opt " + UMLGRAPH_COLOR_PARAMS[i][0]).indexOf(cf.text) == 0)
				result.add(new LightUMLCompletionProposal("@opt "
						+ UMLGRAPH_COLOR_PARAMS[i][0], cf.offset, cf.text
						.length(), UMLGRAPH_COLOR_PARAMS[i][1]));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#computeCompletionProposals(org.eclipse.jdt.core.ICompilationUnit,
	 *      int, int, int)
	 */
	public IJavaCompletionProposal[] computeCompletionProposals(
			ICompilationUnit cu, int offset, int length, int flags) {
		// length means the length of the selected or highlighted part.
		Vector proposals = new Vector();

		CompletionFor toComplete = getTextToComplete(cu, offset, length);
		if (toComplete == null)
			return null;

		if (toComplete.text.indexOf("opt ") == 0)
			proposals = getOptionProposals(toComplete);
		else {
			for (int i = 0; i < UMLGRAPH_JAVADOC_TAGS.length; i++) {
				if (UMLGRAPH_JAVADOC_TAGS[i][0].indexOf(toComplete.text) == 0)
					proposals.add(new LightUMLCompletionProposal("@"
							+ UMLGRAPH_JAVADOC_TAGS[i][0], toComplete.offset,
							toComplete.text.length(),
							UMLGRAPH_JAVADOC_TAGS[i][1]));
			}
		}
		if (proposals.isEmpty())
			return null;

		return (IJavaCompletionProposal[]) proposals
				.toArray(new IJavaCompletionProposal[proposals.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.text.java.IJavadocCompletionProcessor#getErrorMessage()
	 */
	public String getErrorMessage() {
		return null;
	}

}
