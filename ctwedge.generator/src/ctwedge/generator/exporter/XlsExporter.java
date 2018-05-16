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
package ctwedge.generator.exporter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import ctwedge.util.Assignment;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestSuiteExporter;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class XlsExporter extends ICTWedgeTestSuiteExporter {

	@Override
	public void generateOutput(TestSuite input, String fileName) {
		File file = new File(fileName);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file,
					wbSettings);
			workbook.createSheet("Report", 0);
			WritableSheet excelSheet = workbook.getSheet(0);
			createLabel(input, excelSheet);
			createContent(input, excelSheet);

			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO: handle exception
		} catch (WriteException e) {
			// TODO: handle exception
		}

	}

	private void createLabel(TestSuite testsuite, WritableSheet sheet)
			throws WriteException {
		

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		WritableCellFormat timesBoldUnderline = new WritableCellFormat(
				times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		for (Assignment assignment : testsuite.getTests().get(0)
				.getAssignments()) {
			XlsWriter.addCaption(sheet, testsuite.getTests().get(0)
					.getAssignments().indexOf(assignment),0, assignment
					.getParameter().getName(),timesBoldUnderline);
		}

	}

	private void createContent(TestSuite testSuite, WritableSheet sheet)
			throws WriteException, RowsExceededException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		WritableCellFormat times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
			times.setWrap(true);

		for (Test test : testSuite.getTests()) {

			for (Assignment assignment : test.getAssignments())
				XlsWriter.addLabel(sheet,
						test.getAssignments().indexOf(assignment) ,
						testSuite.getTests().indexOf(test)+1,

						assignment.getValue(),times);
		}
	}
}
