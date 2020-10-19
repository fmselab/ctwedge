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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTestSuiteExporter;

public class XlsExporter extends ICTWedgeTestSuiteExporter {

	@Override
	public void generateOutput(TestSuite input, String fileName) {
		File file = new File(fileName);

		try {
			Workbook workbook = new HSSFWorkbook();
			Sheet excelSheet = workbook.createSheet("Report");
			createLabel(input, excelSheet);
			createContent(input, excelSheet);

			workbook.write(new FileOutputStream(file));
			workbook.close();
		} catch (IOException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void createLabel(TestSuite testsuite, Sheet sheet)
			throws Exception {

		// Create create a bold font with unterlines
		Font times10ptBoldUnderline = sheet.getWorkbook().createFont();
		times10ptBoldUnderline.setFontHeightInPoints((short)10);
		times10ptBoldUnderline.setFontName("Times New Roman");
		times10ptBoldUnderline.setBold(true);
		times10ptBoldUnderline.setUnderline((byte)1);
		
		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setFont(times10ptBoldUnderline);

		int i=0;
		for (Entry<String,String> assignment : testsuite.getTests().get(0).entrySet()) {
			XlsWriter.addCaption(sheet, i++,0, assignment.getKey(),style);
		}

	}

	private void createContent(TestSuite testSuite, Sheet sheet)
			throws Exception {
		// Lets create a times font
		Font times = sheet.getWorkbook().createFont();
		times.setFontHeightInPoints((short)10);
		times.setFontName("Times New Roman");
		
		// Define the cell format
		CellStyle style = sheet.getWorkbook().createCellStyle();
		style.setFont(times);

		for (Test test : testSuite.getTests()) {
			int i=0;
			for (Entry<String,String> assignment : test.entrySet())
				XlsWriter.addLabel(sheet,
						i++, //test.getAssignments().indexOf(assignment) ,
						testSuite.getTests().indexOf(test)+1,
						assignment.getValue(),style);
		}
	}
}
