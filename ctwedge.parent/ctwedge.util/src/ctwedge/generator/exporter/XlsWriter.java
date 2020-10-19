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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class XlsWriter {

	@SuppressWarnings("deprecation")
	static void addCaption(Sheet sheet, int column, int row, String s, CellStyle timesBoldUnderline)
			throws Exception {
		Row r = sheet.getRow(row);
		Cell cell = r.getCell(column);
		if (cell == null) {
			cell = r.createCell(column);
		}
		
		cell.setCellType(CellType.STRING);
		cell.setCellValue(s);
		cell.setCellStyle(timesBoldUnderline);
	}

	@SuppressWarnings("deprecation")
	static void addNumber(Sheet sheet, int column, int row,
			Integer integer, CellStyle times) throws Exception {		
		Row r = sheet.getRow(row);
		Cell cell = r.getCell(column);
		if (cell == null) {
			cell = r.createCell(column);
		}
		
		cell.setCellType(CellType.NUMERIC);
		cell.setCellValue(integer);
		cell.setCellStyle(times);
	}

	 static void addLabel(Sheet sheet, int column, int row, String s, CellStyle times)
			throws Exception {
		addCaption(sheet, column, row, s, times);
	}



}
