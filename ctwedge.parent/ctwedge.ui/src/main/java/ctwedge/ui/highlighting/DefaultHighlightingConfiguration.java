/*******************************************************************************
 * Copyright (c) 2013 University of Bergamo - Italy
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Paolo Vavassori - initial API and implementation
 *   Angelo Gargantini - utils and architecture
 ******************************************************************************/
package ctwedge.ui.highlighting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class DefaultHighlightingConfiguration implements
		IHighlightingConfiguration {

	public static final String KEYWORD_ID = "keyword";
	public static final String COMMENT_ID = "comment";
	public static final String NUMBER_ID = "number";

	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword",
				keywordTextStyle());
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment",
				commentTextStyle());
		acceptor.acceptDefaultHighlighting(NUMBER_ID, "number",
				numberTextStyle());
		 
	}

	private TextStyle numberTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(26, 148, 49));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}


	public TextStyle keywordTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(230, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle commentTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(27, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
}
