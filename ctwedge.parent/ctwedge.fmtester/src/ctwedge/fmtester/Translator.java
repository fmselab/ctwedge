package ctwedge.fmtester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.nio.file.Path;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.medici.MediciCITGenerator;
import ctwedge.generator.util.Utility;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;

/**
 * Translator offers static methods for different model conversions:
 * <ul>
 * <li>From FM (.xml) to CTWedge (.ctw)
 * <li>From CTWedge (.ctw) to medici (.txt)
 * </ul>
 * 
 * The main() translates all models in the <i>evolutionModels</i> folder from FM
 * to CTWedge to medici and then executes medici.
 */
public class Translator {

	// import/export path info
	static String inputModelName;
	static String outputModelName;
	// input/output folders are the same
	static String modelFolder;
	final static String modelUpperFolder = "evolutionModels";
	static String inputModelPath;
	static String outputModelPath;

	/**
	 * Translate the input FM model to the corresponding CTWedge model
	 * 
	 * @param inPath  the path to the input Feature Model .xml file
	 * @param outPath the path to the output CTWedge .ctw file
	 * @return a {@link String} with the translated CTWedge model
	 */
	static public String fromFMtoCTWedge(String inPath, String outPath) throws IOException {

		// disabilito temporaneamente system.out
		PrintStream originalStream = System.out;
		Test.consolePrintingOff();

		// importo Feature Model .xml
		XmlFeatureModelImporter importer = new XmlFeatureModelImporter();
		CitModel model = importer.importModel(inPath);
		ModelUtils utils = new ModelUtils(model);

		// converto e scrivo il modello CTWedge in outPath
		// AG messo a false l'append
		FileWriter file = new FileWriter(outPath, false);
		PrintWriter outputfile = new PrintWriter(file);
		final String res = utils.serializeToString();
		outputfile.write(res);
		outputfile.close();

		// riabilito system out
		Test.consolePrintingOn(originalStream);

		return res;
	}

	/**
	 * Translate the input CTWedge model to the corresponding medici model
	 * 
	 * @param inPath  the path to the input CTWedge model .ctw file
	 * @param outPath the path to the output medici .txt file
	 * @return a {@link String} with the translated medici model
	 */
	static public String fromCTWedgeToMedici(String inPath, String outPath) throws IOException {

		// disabilito temporaneamente system.out
		PrintStream originalStream = System.out;
		Test.consolePrintingOff();

		Path file = Paths.get(inPath, "");
		MediciCITGenerator gen = new MediciCITGenerator();
		CitModel loadModel = Utility.loadModelFromPath(file.toString());

		// convert to medici
		File model = new File(outPath);
		FileWriter wf = new FileWriter(model);
		String translateModel = gen.translateModel(loadModel, false);
		wf.write(translateModel);

		wf.close();

		// riabilito system out
		Test.consolePrintingOn(originalStream);

		return translateModel;
	}

	public static void main(String[] args) throws IOException {

		/* TRADUZIONE MODELLI: da FM a CTWedge */
		evolutionModels_FMtoCTWedge();

		/* TRADUZIONE MODELLI: da CTWedge a medici */
		evolutionModels_CTWedgeToMedici();

		/* ESECUZIONE "medici.exe" E GENERAZIONE DATI SULLE CONFIGURAZIONI */
		evolutionModels_MediciEXE();

	}

	private static void evolutionModels_FMtoCTWedge() throws IOException {

		System.out.println("###############################################");
		System.out.println("-- CONVERSION: FROM FEATURE MODEL TO CTWEDGE --");
		System.out.println("###############################################\n");

		// ###########################################################################
		/* EVOLUTION MODEL: AmbientAssistedLiving */
		modelFolder = "AmbientAssistedLiving";

		/* v1 */
		inputModelName = "AmbientAssistedLivingv1.xml";
		outputModelName = "AmbientAssistedLivingv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "AmbientAssistedLivingv2.xml";
		outputModelName = "AmbientAssistedLivingv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: AutomotiveMultimedia */
		modelFolder = "AutomotiveMultimedia";

		/* v1 */
		inputModelName = "AutomotiveMultimediav1.xml";
		outputModelName = "AutomotiveMultimediav1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "AutomotiveMultimediav2.xml";
		outputModelName = "AutomotiveMultimediav2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "AutomotiveMultimediav3.xml";
		outputModelName = "AutomotiveMultimediav3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: Boeing */
		modelFolder = "Boeing";

		/* v1 */
		inputModelName = "Boeingv1.xml";
		outputModelName = "Boeingv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");

		/* v2 */
		inputModelName = "Boeingv2.xml";
		outputModelName = "Boeingv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");

		/* v3 */
		inputModelName = "Boeingv3.xml";
		outputModelName = "Boeingv3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: CarBody */
		modelFolder = "CarBody";

		/* v1 */
		inputModelName = "CarBodyv1.xml";
		outputModelName = "CarBodyv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");

		/* v2 */
		inputModelName = "CarBodyv2.xml";
		outputModelName = "CarBodyv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");

		/* v3 */
		inputModelName = "CarBodyv3.xml";
		outputModelName = "CarBodyv3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");

		/* v4 */
		inputModelName = "CarBodyv4.xml";
		outputModelName = "CarBodyv4_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		System.out.println("\n");
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: LinuxKernel */
		modelFolder = "LinuxKernel";

		/* v1 */
		inputModelName = "LinuxKernelv1.xml";
		outputModelName = "LinuxKernelv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "LinuxKernelv2.xml";
		outputModelName = "LinuxKernelv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "LinuxKernelv3.xml";
		outputModelName = "LinuxKernelv3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: ParkingAssistant */
		modelFolder = "ParkingAssistant";

		/* v1 */
		inputModelName = "ParkingAssistantv1.xml";
		outputModelName = "ParkingAssistantv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "ParkingAssistantv2.xml";
		outputModelName = "ParkingAssistantv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "ParkingAssistantv3.xml";
		outputModelName = "ParkingAssistantv3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v4 */
		inputModelName = "ParkingAssistantv4.xml";
		outputModelName = "ParkingAssistantv4_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v5 */
		inputModelName = "ParkingAssistantv5.xml";
		outputModelName = "ParkingAssistantv5_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: PPU */
		modelFolder = "PPU";

		/* v1 */
		inputModelName = "PPUv1.xml";
		outputModelName = "PPUv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "PPUv2.xml";
		outputModelName = "PPUv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "PPUv3.xml";
		outputModelName = "PPUv3_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v4 */
		inputModelName = "PPUv4.xml";
		outputModelName = "PPUv4_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v5 */
		inputModelName = "PPUv5.xml";
		outputModelName = "PPUv5_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v6 */
		inputModelName = "PPUv6.xml";
		outputModelName = "PPUv6_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v7 */
		inputModelName = "PPUv7.xml";
		outputModelName = "PPUv7_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v8 */
		inputModelName = "PPUv8.xml";
		outputModelName = "PPUv8_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v9 */
		inputModelName = "PPUv9.xml";
		outputModelName = "PPUv9_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartHotel */
		modelFolder = "SmartHotel";

		/* v1 */
		inputModelName = "SmartHotelv1.xml";
		outputModelName = "SmartHotelv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "SmartHotelv2.xml";
		outputModelName = "SmartHotelv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		// ###########################################################################
		/* EVOLUTION MODEL: SmartWatch */
		modelFolder = "SmartWatch";

		/* v1 */
		inputModelName = "SmartWatchv1.xml";
		outputModelName = "SmartWatchv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "SmartWatchv2.xml";
		outputModelName = "SmartWatchv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: WeatherStation */
		modelFolder = "WeatherStation";

		/* v1 */
		inputModelName = "WeatherStationv1.xml";
		outputModelName = "WeatherStationv1_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "WeatherStationv2.xml";
		outputModelName = "WeatherStationv2_ctwedge.ctw";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromFMtoCTWedge(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		System.out.println("\n");
	}

	private static void evolutionModels_CTWedgeToMedici() throws IOException {

		System.out.println("###############################################");
		System.out.println("-- CONVERSION: FROM CTWEDGE TO MEDICI --");
		System.out.println("###############################################\n");

		// ###########################################################################
		/* EVOLUTION MODEL: AmbientAssistedLiving */
		modelFolder = "AmbientAssistedLiving";

		/* v1 */
		inputModelName = "AmbientAssistedLivingv1_ctwedge.ctw";
		outputModelName = "AmbientAssistedLivingv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "AmbientAssistedLivingv2_ctwedge.ctw";
		outputModelName = "AmbientAssistedLivingv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: AutomotiveMultimedia */
		modelFolder = "AutomotiveMultimedia";

		/* v1 */
		inputModelName = "AutomotiveMultimediav1_ctwedge.ctw";
		outputModelName = "AutomotiveMultimediav1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "AutomotiveMultimediav2_ctwedge.ctw";
		outputModelName = "AutomotiveMultimediav2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "AutomotiveMultimediav3_ctwedge.ctw";
		outputModelName = "AutomotiveMultimediav3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: Boeing */
		modelFolder = "Boeing";

		/* v1 */
		inputModelName = "Boeingv1_ctwedge.ctw";
		outputModelName = "Boeingv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "Boeingv2_ctwedge.ctw";
		outputModelName = "Boeingv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "Boeingv3_ctwedge.ctw";
		outputModelName = "Boeingv3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: CarBody */
		modelFolder = "CarBody";

		/* v1 */
		inputModelName = "CarBodyv1_ctwedge.ctw";
		outputModelName = "CarBodyv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "CarBodyv2_ctwedge.ctw";
		outputModelName = "CarBodyv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "CarBodyv3_ctwedge.ctw";
		outputModelName = "CarBodyv3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v4 */
		inputModelName = "CarBodyv4_ctwedge.ctw";
		outputModelName = "CarBodyv4_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: LinuxKernel */
		modelFolder = "LinuxKernel";

		/* v1 */
		inputModelName = "LinuxKernelv1_ctwedge.ctw";
		outputModelName = "LinuxKernelv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "LinuxKernelv2_ctwedge.ctw";
		outputModelName = "LinuxKernelv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "LinuxKernelv3_ctwedge.ctw";
		outputModelName = "LinuxKernelv3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: ParkingAssistant */
		modelFolder = "ParkingAssistant";

		/* v1 */
		inputModelName = "ParkingAssistantv1_ctwedge.ctw";
		outputModelName = "ParkingAssistantv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "ParkingAssistantv2_ctwedge.ctw";
		outputModelName = "ParkingAssistantv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "ParkingAssistantv3_ctwedge.ctw";
		outputModelName = "ParkingAssistantv3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v4 */
		inputModelName = "ParkingAssistantv4_ctwedge.ctw";
		outputModelName = "ParkingAssistantv4_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v5 */
		inputModelName = "ParkingAssistantv5_ctwedge.ctw";
		outputModelName = "ParkingAssistantv5_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: ParkingAssistant */
		modelFolder = "PPU";

		/* v1 */
		inputModelName = "PPUv1_ctwedge.ctw";
		outputModelName = "PPUv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "PPUv2_ctwedge.ctw";
		outputModelName = "PPUv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v3 */
		inputModelName = "PPUv3_ctwedge.ctw";
		outputModelName = "PPUv3_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v4 */
		inputModelName = "PPUv4_ctwedge.ctw";
		outputModelName = "PPUv4_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v5 */
		inputModelName = "PPUv5_ctwedge.ctw";
		outputModelName = "PPUv5_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v6 */
		inputModelName = "PPUv6_ctwedge.ctw";
		outputModelName = "PPUv6_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v7 */
		inputModelName = "PPUv7_ctwedge.ctw";
		outputModelName = "PPUv7_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v8 */
		inputModelName = "PPUv8_ctwedge.ctw";
		outputModelName = "PPUv8_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v9 */
		inputModelName = "PPUv9_ctwedge.ctw";
		outputModelName = "PPUv9_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartHotel */
		modelFolder = "SmartHotel";

		/* v1 */
		inputModelName = "SmartHotelv1_ctwedge.ctw";
		outputModelName = "SmartHotelv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "SmartHotelv2_ctwedge.ctw";
		outputModelName = "SmartHotelv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartHotel */
		modelFolder = "SmartWatch";

		/* v1 */
		inputModelName = "SmartWatchv1_ctwedge.ctw";
		outputModelName = "SmartWatchv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "SmartWatchv2_ctwedge.ctw";
		outputModelName = "SmartWatchv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartHotel */
		modelFolder = "WeatherStation";

		/* v1 */
		inputModelName = "WeatherStationv1_ctwedge.ctw";
		outputModelName = "WeatherStationv1_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);

		/* v2 */
		inputModelName = "WeatherStationv2_ctwedge.ctw";
		outputModelName = "WeatherStationv2_medici.txt";

		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;
		outputModelPath = modelUpperFolder + "/" + modelFolder + "/" + outputModelName;

		System.out.println("----------------------------------------------");
		fromCTWedgeToMedici(inputModelPath, outputModelPath);
		System.out.println("** " + inputModelName + " **\n" + "Output: " + outputModelName);
		// ###########################################################################

		System.out.println("\n");
	}

	private static void evolutionModels_MediciEXE() throws IOException {
		System.out.println("###############################################");
		System.out.println("-- MEDICI EXECUTION AND DATA GENERATION --");
		System.out.println("###############################################\n");

		// ###########################################################################
		/* EVOLUTION MODEL: AmbientAssistedLiving */
		modelFolder = "AmbientAssistedLiving";

		/* v1 */
		inputModelName = "AmbientAssistedLivingv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "AmbientAssistedLivingv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: AutomotiveMultimedia */
		modelFolder = "AutomotiveMultimedia";

		/* v1 */
		inputModelName = "AutomotiveMultimediav1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "AutomotiveMultimediav2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "AutomotiveMultimediav3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: Boeing */
		modelFolder = "Boeing";

		/* v1 */
		inputModelName = "Boeingv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "Boeingv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "Boeingv3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: CarBody */
		modelFolder = "CarBody";

		/* v1 */
		inputModelName = "CarBodyv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "CarBodyv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "CarBodyv3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v4 */
		inputModelName = "CarBodyv4_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: LinuxKernel */
		modelFolder = "LinuxKernel";

		/* v1 */
		inputModelName = "LinuxKernelv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "LinuxKernelv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "LinuxKernelv3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: ParkingAssistant */
		modelFolder = "ParkingAssistant";

		/* v1 */
		inputModelName = "ParkingAssistantv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "ParkingAssistantv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "ParkingAssistantv3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v4 */
		inputModelName = "ParkingAssistantv4_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v5 */
		inputModelName = "ParkingAssistantv5_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: PPU */
		modelFolder = "PPU";

		/* v1 */
		inputModelName = "PPUv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "PPUv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v3 */
		inputModelName = "PPUv3_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v4 */
		inputModelName = "PPUv4_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v5 */
		inputModelName = "PPUv5_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v6 */
		inputModelName = "PPUv6_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v7 */
		inputModelName = "PPUv7_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v8 */
		inputModelName = "PPUv8_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v9 */
		inputModelName = "PPUv9_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartHotel */
		modelFolder = "SmartHotel";

		/* v1 */
		inputModelName = "SmartHotelv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "SmartHotelv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: SmartWatch */
		modelFolder = "SmartWatch";

		/* v1 */
		inputModelName = "SmartWatchv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "SmartWatchv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

		// ###########################################################################
		/* EVOLUTION MODEL: WeatherStation */
		modelFolder = "WeatherStation";

		/* v1 */
		inputModelName = "WeatherStationv1_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();

		/* v2 */
		inputModelName = "WeatherStationv2_medici.txt";
		inputModelPath = modelUpperFolder + "/" + modelFolder + "/" + inputModelName;

		System.out.println("----------------------------------------------");
		System.out.println("** " + inputModelName + " **\n");
		MediciInvoker.printMediciData(inputModelPath);
		System.out.println();
		// ###########################################################################

	}

}