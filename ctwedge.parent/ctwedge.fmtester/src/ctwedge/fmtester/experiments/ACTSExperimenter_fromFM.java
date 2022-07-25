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
 * class, the time it is included also the generation from Feature Model to CTWedge
 * model.
 * 
 * @author Luca Parimbelli
 *
 */
public class ACTSExperimenter_fromFM {

	private static final String EVOLUTION_MODELS_INPUT_FOLDER = "evolutionModels/";

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
		String FMName;
		String FMSubFolder;
		String FMInputPath;
		long FMtime;

		/* INITIAL GENERATIONS FOR ENVIRONMENT SET UP */
		FMSubFolder = "AmbientAssistedLiving";
		FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
		FMName = "AmbientAssistedLivingv1.xml";
		System.out.println("------------------- **ENVIRONMENT SET UP GENERATIONS** -------------------");
		for (int i = 0; i < 200; i++) {
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");
		}
		System.out.println();
		System.out.println("------------------- **END OF ENVIRONMENT SET UP GENERATIONS** -------------------");

		System.out.println();
		System.out.println("-- REAL TEST GENERATION --");

		/* REAL EXPERIMENT 1000 TEST GENERATIONS */
		for (int i = 0; i < 1000; i++) {
			System.out.println();
			System.out.println("ITERATION NUMBER : " + (i + 1) + "/1000");
			System.out.println();

			// going to a new row on .xlsx file
			row = sheet.createRow(++rowCount);
			// reset columnCount on the current row
			columnCount = 0;

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: AmbientAssistedLiving */
			FMSubFolder = "AmbientAssistedLiving";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "AmbientAssistedLivingv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			Cell cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "AmbientAssistedLivingv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);
			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: AutomotiveMultimedia */
			FMSubFolder = "AutomotiveMultimedia";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "AutomotiveMultimediav1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "AutomotiveMultimediav2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "AutomotiveMultimediav3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: Boeing */
			FMSubFolder = "Boeing";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "Boeingv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "Boeingv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "Boeingv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: CarBody */
			FMSubFolder = "CarBody";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "CarBodyv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "CarBodyv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "CarBodyv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			FMName = "CarBodyv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: LinuxKernel */
			FMSubFolder = "LinuxKernel";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "LinuxKernelv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "LinuxKernelv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "LinuxKernelv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: ParkingAssistant */
			FMSubFolder = "ParkingAssistant";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "ParkingAssistantv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "ParkingAssistantv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "ParkingAssistantv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			FMName = "ParkingAssistantv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v5 */
			FMName = "ParkingAssistantv5.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: PPU */
			FMSubFolder = "PPU";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "PPUv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "PPUv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v3 */
			FMName = "PPUv3.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v4 */
			FMName = "PPUv4.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v5 */
			FMName = "PPUv5.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v6 */
			FMName = "PPUv6.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v7 */
			FMName = "PPUv7.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v8 */
			FMName = "PPUv8.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v9 */
			FMName = "PPUv9.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: SmartHotel */
			FMSubFolder = "SmartHotel";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "SmartHotelv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "SmartHotelv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: SmartWatch */
			FMSubFolder = "SmartWatch";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "SmartWatchv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "SmartWatchv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			// ------------------------------------------------------------------

			// ------------------------------------------------------------------
			/* EVOLUTION MODEL: WeatherStation */
			FMSubFolder = "WeatherStation";
			FMInputPath = EVOLUTION_MODELS_INPUT_FOLDER + FMSubFolder + "/";
			System.out.println("----------------------------------------------");
			/* v1 */
			FMName = "WeatherStationv1.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
			System.out.println("\n ** " + FMName + "**\n" + "Elapsed time: " + FMtime + " ms");

			cell = row.createCell(++columnCount);
			cell.setCellValue(FMtime);

			/* v2 */
			FMName = "WeatherStationv2.xml";
			FMtime = ACTSGenerator.generateBooleanTestAndExportCSV(FMName, FMInputPath);
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
