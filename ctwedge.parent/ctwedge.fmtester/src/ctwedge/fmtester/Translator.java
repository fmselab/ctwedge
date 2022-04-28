package ctwedge.fmtester;

import ctwedge.ctWedge.CitModel;
import ctwedge.generator.util.Utility;
import ctwedge.importer.featureide.FeatureIdeImporterMultipleLevels;
import ctwedge.importer.featureide.XmlFeatureModelImporter;
import ctwedge.util.ModelUtils;

public class Translator {
	// traduce un 
	public static void main(String[] args) {
		// converte da FM a ctwdege
		XmlFeatureModelImporter importer = new XmlFeatureModelImporter();
		CitModel model = importer.importModel("evolutionModels\\AmbientAssistedLiving\\AmbientAssistedLivingv1.xml");
		ModelUtils utils = new ModelUtils(model);
		System.out.println(utils.serializeToString());
	}

}
