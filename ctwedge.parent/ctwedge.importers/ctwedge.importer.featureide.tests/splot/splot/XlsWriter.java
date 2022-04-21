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
package splot;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class XlsWriter {

	static void addCaption(WritableSheet sheet, int column, int row, String s, WritableCellFormat timesBoldUnderline)
	
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s,timesBoldUnderline );
		sheet.addCell(label);
	}

	static void addNumber(WritableSheet sheet, int column, int row,
			double d, WritableCellFormat times) throws WriteException, RowsExceededException {
		Number number;
		number = new Number (column,row,d,times);
		sheet.addCell(number);
	}
	static void addNumber(WritableSheet sheet, int column, int row,
			int d, WritableCellFormat times) throws WriteException, RowsExceededException {
		Number number;
		number = new Number (column,row,d,times);
		sheet.addCell(number);
	}
	static void addNumber(WritableSheet sheet, int column, int row,
			long d, WritableCellFormat times) throws WriteException, RowsExceededException {
		Number number;
		number = new Number (column,row,d,times);
		sheet.addCell(number);
	}

	 static void addLabel(WritableSheet sheet, int column, int row, String s, WritableCellFormat times)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}



}
