package ctwedge.fmtester.experiments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ctwedge.fmtester.ACTSGenerator;

/**
 * Class used to get experimental data from the ACTS generation. With this
 * class, test are generated importing the CTWedge model directly, without doing
 * also the conversion from Feature Model to CTWedge model. This allows to have
 * a fairer comparision with the pMEDICI generation.
 * 
 * @author Luca Parimbelli
 *
 */
public class ACTSExperimenter_fromCTWedge {

	public static void main(String[] args) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("TIME (ms) for generating TS");

		// Creating arrays with the name of all the models
		String[] AmbientAssistedLiving = new String[] { "AmbientAssistedLivingv1", "AmbientAssistedLivingv2" };
		String[] AutomotiveMultimedia = new String[] { "AutomotiveMultimediav1", "AutomotiveMultimediav2",
				"AutomotiveMultimediav3" };
		String[] Boeing = new String[] { "Boeingv1", "Boeingv2", "Boeingv3" };
		String[] CarBody = new String[] { "CarBodyv1", "CarBodyv2", "CarBodyv3", "CarBodyv4" };
		String[] LinuxKernel = new String[] { "LinuxKernelv1", "LinuxKernelv2", "LinuxKernelv3" };
		String[] ParkingAssistant = new String[] { "ParkingAssistantv1", "ParkingAssistantv2", "ParkingAssistantv3",
				"ParkingAssistantv4", "ParkingAssistantv5" };
		String[] PPU = new String[] { "PPUv1", "PPUv2", "PPUv3", "PPUv4", "PPUv5", "PPUv6", "PPUv7", "PPUv8", "PPUv9" };
		String[] SmartHotel = new String[] { "SmartHotelv1", "SmartHotelv2" };
		String[] Smartwatch = new String[] { "Smartwatchv1", "Smartwatchv2" };
		String[] WeatherStation = new String[] { "WeatherStationv1", "WeatherStationv2" };

		// Adding all names of all the models to the same array
		ArrayList<String> evolutionModels = new ArrayList<String>();
		evolutionModels.addAll(Arrays.asList(AmbientAssistedLiving));
		evolutionModels.addAll(Arrays.asList(AutomotiveMultimedia));
		evolutionModels.addAll(Arrays.asList(Boeing));
		evolutionModels.addAll(Arrays.asList(CarBody));
		evolutionModels.addAll(Arrays.asList(LinuxKernel));
		evolutionModels.addAll(Arrays.asList(ParkingAssistant));
		evolutionModels.addAll(Arrays.asList(PPU));
		evolutionModels.addAll(Arrays.asList(SmartHotel));
		evolutionModels.addAll(Arrays.asList(Smartwatch));
		evolutionModels.addAll(Arrays.asList(WeatherStation));

		int rowCount = 0;
		int columnCount = 0;
		Row row = sheet.createRow(++rowCount);

		// First line .xlsx -> model name
		for (String name : evolutionModels) {
			Cell cell = row.createCell(++columnCount);
			cell.setCellValue(name);
		}

		// Other lines -> time required by TS generations
		// I'm running 1000 generations for each model and, for each generation,
		// the time is set in the corresponding cell of the current sheet

		// The first generations require more time for setting up the environment.
		// For this reason, I will execute 100 generations at the beginning without
		// writing anything in the sheet. The real experiment starts after this 100
		// generations.

		// Input data variables
		String CTWedgeModelPath;
		String outPath;
		String FMName;

		long FMtime;

		/* INITIAL GENERATIONS FOR ENVIRONMENT SET UP */
		FMName = "AmbientAssistedLivingv1.xml";
		CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AmbientAssistedLiving/AmbientAssistedLivingv1_ctwedge.ctw";
		outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AmbientAssistedLivingv1.csv";

		System.out.println("------------------- **ENVIRONMENT SET UP GENERATIONS** -------------------");
		for (int i = 0; i < 2; i++) {
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		}
		System.out.println();
		System.out.println("------------------- **END OF ENVIRONMENT SET UP GENERATIONS** -------------------");

		System.out.println();
		System.out.println("-- REAL TEST GENERATION --");

		/* REAL EXPERIMENT 1000 TEST GENERATIONS */
		for (int i = 0; i < 2; i++) {
			System.out.println();
			System.out.println("ITERATION NUMBER : " + (i + 1) + "/1000");
			System.out.println();

			// going to a new row on .xlsx file
			row = sheet.createRow(++rowCount);
			// reset columnCount on the current row
			columnCount = 0;

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: AmbientAssistedLiving */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AmbientAssistedLiving/AmbientAssistedLivingv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AmbientAssistedLivingv1.csv";
			FMName = "AmbientAssistedLivingv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			Cell cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AmbientAssistedLiving/AmbientAssistedLivingv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AmbientAssistedLivingv2.csv";
			FMName = "AmbientAssistedLivingv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);
			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: AutomotiveMultimedia */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AutomotiveMultimedia/AutomotiveMultimediav1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AutomotiveMultimediav1.csv";
			FMName = "AutomotiveMultimediav1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AutomotiveMultimedia/AutomotiveMultimediav2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AutomotiveMultimediav2.csv";
			FMName = "AutomotiveMultimediav2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/AutomotiveMultimedia/AutomotiveMultimediav3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_AutomotiveMultimediav3.csv";
			FMName = "AutomotiveMultimediav3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: Boeing */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/Boeing/Boeingv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_Boeingv1.csv";
			FMName = "Boeingv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/Boeing/Boeingv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_Boeingv2.csv";
			FMName = "Boeingv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/Boeing/Boeingv3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_Boeingv3.csv";
			FMName = "Boeingv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: CarBody */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/CarBody/CarBodyv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_CarBodyv1.csv";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "CarBodyv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/CarBody/CarBodyv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_CarBodyv2.csv";
			FMName = "CarBodyv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/CarBody/CarBodyv3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_CarBodyv3.csv";
			FMName = "CarBodyv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/CarBody/CarBodyv4_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_CarBodyv4.csv";
			FMName = "CarBodyv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: LinuxKernel */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/LinuxKernel/LinuxKernelv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_LinuxKernelv1.csv";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "LinuxKernelv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/LinuxKernel/LinuxKernelv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_LinuxKernelv2.csv";
			FMName = "LinuxKernelv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/LinuxKernel/LinuxKernelv3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_LinuxKernelv3.csv";
			FMName = "LinuxKernelv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: ParkingAssistant */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv1.csv";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "ParkingAssistantv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv2.csv";
			FMName = "ParkingAssistantv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv3.csv";
			FMName = "ParkingAssistantv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv4_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv4.csv";
			FMName = "ParkingAssistantv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v5 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/ParkingAssistant/ParkingAssistantv5_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_ParkingAssistantv5.csv";
			FMName = "ParkingAssistantv5.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: PPU */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv1.csv";
			FMName = "PPUv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv2.csv";
			FMName = "PPUv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv3_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv3.csv";
			FMName = "PPUv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv4_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv4.csv";
			FMName = "PPUv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v5 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv5_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv5.csv";
			FMName = "PPUv5.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v6 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv6_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv6.csv";
			FMName = "PPUv6.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v7 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv7_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv7.csv";
			FMName = "PPUv7.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v8 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv8_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv8.csv";
			FMName = "PPUv8.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v9 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/PPU/PPUv9_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_PPUv9.csv";
			FMName = "PPUv9.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: SmartHotel */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/SmartHotel/SmartHotelv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_SmartHotelv1.csv";
			FMName = "SmartHotelv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/SmartHotel/SmartHotelv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_SmartHotelv2.csv";
			FMName = "SmartHotelv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: SmartWatch */
			System.out.println("----------------------------------------------");
			/* v1 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/SmartWatch/SmartWatchv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_SmartWatchv1.csv";
			FMName = "SmartWatchv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/SmartWatch/SmartWatchv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_SmartWatchv2.csv";
			FMName = "SmartWatchv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: WeatherStation */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/WeatherStation/WeatherStationv1_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_WeatherStationv1.csv";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "WeatherStationv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			CTWedgeModelPath = "../ctwedge.fmtester/evolutionModels/WeatherStation/WeatherStationv2_ctwedge.ctw";
			outPath = "../ctwedge.fmtester/evolutionModels_TestsCSV/CSVTest_WeatherStationv2.csv";
			FMName = "WeatherStationv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV_FromCTWedgeModel(CTWedgeModelPath, outPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

		}
		// ------------------------------------------------------------------

		// Exporting the data to the .xlsx file
		FileOutputStream outputStream = new FileOutputStream("experimentData/ACTS_Generation_from_scratch_new.xlsx");
		workbook.write(outputStream);

		workbook.close();
	}
}
