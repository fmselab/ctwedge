package ctwedge.fmtester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.medici.MediciCITGenerator;
import ctwedge.generator.util.Utility;
import ctwedge.importer.featureide.FeatureIdeImporterBoolean;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;

/**
 * Converts between different models:
 * <ul>
 * <li>From FM (.xml) to CTWedge (.ctw)
 * <li>From CTWedge (.ctw) to medici (.txt)
 * </ul>
 * 
 * The main() translates all models in the <i>evolutionModels</i> folder from FM
 * to CTWedge to medici and then executes medici.
 * 
 * @author Luca Parimbelli
 * 
 */
public class Converter {

	/**
	 * Translate the input FM model to the corresponding CTWedge model considering
	 * also the possibility of having ENUM parameters
	 * 
	 * @param inPath  the path to the input Feature Model .xml file
	 * @param outPath the path to the output CTWedge .ctw file
	 * @return a {@link String} with the translated CTWedge model
	 */
	static public String fromFMtoCTWedge_ENUM(String inPath, String outPath) throws IOException {

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		// importo Feature Model .xml con Enum importer
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

		// Enabling console printing
		cm.consolePrintingOn();

		return res;
	}
	
	/**
	 * Translate the input FM model to the corresponding CTWedge model
	 * forcing all the parameters to be only boolean
	 * 
	 * @param inPath  the path to the input Feature Model .xml file
	 * @param outPath the path to the output CTWedge .ctw file
	 * @return a {@link String} with the translated CTWedge model
	 */
	static public String fromFMtoCTWedge_BOOLEAN(String inPath, String outPath) throws IOException {

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		// importo Feature Model .xml con Boolean importer
		FeatureIdeImporterBoolean importer = new FeatureIdeImporterBoolean();
		CitModel model = importer.importModel(inPath);
		ModelUtils utils = new ModelUtils(model);

		// converto e scrivo il modello CTWedge in outPath
		// AG messo a false l'append
		FileWriter file = new FileWriter(outPath, false);
		PrintWriter outputfile = new PrintWriter(file);
		final String res = utils.serializeToString();
		outputfile.write(res);
		outputfile.close();

		// Enabling console printing
		cm.consolePrintingOn();

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

		// Disabling console printing
		ConsoleManager cm = new ConsoleManager(System.out);
		cm.consolePrintingOff();

		Path file = Paths.get(inPath, "");
		MediciCITGenerator gen = new MediciCITGenerator();
		CitModel loadModel = Utility.loadModelFromPath(file.toString());

		// convert to medici
		File model = new File(outPath);
		FileWriter wf = new FileWriter(model);
		String translateModel = gen.translateModel(loadModel, false);
		wf.write(translateModel);

		wf.close();

		// Enabling console printing
		cm.consolePrintingOn();

		return translateModel;
	}
}
