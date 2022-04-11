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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;


import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class XlsExporter {

	public void generateOutput(ArrayList<ArrayList<?>> input, String fileName) {
		File file = new File(fileName);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file,
					wbSettings);
			workbook.createSheet("Report", 0);
			WritableSheet excelSheet = workbook.getSheet(0);
			createLabel(excelSheet);
			createContent(input, excelSheet);

			workbook.write();
			workbook.close();
		} catch (IOException e) {
			// TODO: handle exception
		} catch (WriteException e) {
			// TODO: handle exception
		}

	}

	private void createContent(ArrayList<ArrayList<?>> input,
			WritableSheet sheet) throws WriteException, RowsExceededException {
		// Lets create a times font
		WritableFont times12pt = new WritableFont(WritableFont.TIMES, 12);
		// Define the cell format
		WritableCellFormat times = new WritableCellFormat(times12pt);
		WritableCellFormat numberd = new WritableCellFormat(NumberFormats.EXPONENTIAL);
		WritableCellFormat numberi = new WritableCellFormat(NumberFormats.INTEGER);
		// Lets automatically wrap the cells
		times.setWrap(true);
		for (ArrayList<?> metrica : input) {
			for (Object value : metrica) {
				if (value instanceof String) {
					XlsWriter.addLabel(sheet, metrica.indexOf(value),
							input.indexOf(metrica) + 1,

							(String) value, times);
				} else if (value instanceof Double) {
					XlsWriter.addNumber(sheet, metrica.indexOf(value),
							input.indexOf(metrica) + 1,

							(double) value, times);
				}else if (value instanceof Integer ) {
					XlsWriter.addNumber(sheet, metrica.indexOf(value),
							input.indexOf(metrica) + 1, (int) value, times);
				}else if (value instanceof Long ) {
					XlsWriter.addNumber(sheet, metrica.indexOf(value),
							input.indexOf(metrica) + 1, (long) value, times);
				}else 
				{
					XlsWriter.addCaption(sheet, metrica.indexOf(value),
							input.indexOf(metrica) + 1,"ERRR",times);
				}

			}
		}

	}

	private void createLabel(WritableSheet sheet) throws WriteException {

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 14, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		WritableCellFormat timesBoldUnderline = new WritableCellFormat(
				times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		// nome del modello
		XlsWriter.addCaption(sheet, 0, 0, "NOME", timesBoldUnderline);
		// numero di Feature
		XlsWriter.addCaption(sheet, 1, 0, "FEATURES", timesBoldUnderline);
		// numero di prodotti (coincide tra citlab e splot)
		XlsWriter.addCaption(sheet, 2, 0, "PRODOTTI", timesBoldUnderline);
		// numero di paramteri per citlab
		XlsWriter.addCaption(sheet, 3, 0, "PARAMETRI", timesBoldUnderline);
		// numero di paramteri per citlab dopo semplificazione
		XlsWriter
				.addCaption(sheet, 4, 0, "P. SEMPLIFICATI", timesBoldUnderline);
		// numero nodi nei BDD
		XlsWriter.addCaption(sheet, 5, 0, "NUMERO NODI", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 6, 0, "RATE CITLAB", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 7, 0, "RATE SPLOT", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 8, 0, "VARIABILITY", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 9, 0, "CONSTRAINT", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 10, 0, "C SIMPLIFIED", timesBoldUnderline);
		XlsWriter.addCaption(sheet, 11, 0, "CNF C", timesBoldUnderline);
		// da aggiungere
		// numero di prodotti senza semplificazione in citlab
	}

}
