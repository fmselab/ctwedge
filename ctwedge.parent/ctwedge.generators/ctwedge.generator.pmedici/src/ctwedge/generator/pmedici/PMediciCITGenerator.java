package ctwedge.generator.pmedici;

import ctwedge.ctWedge.CitModel;
import ctwedge.util.NotConvertableModel;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeTranslTestGenerator;
import pMedici.main.PMedici;

public class PMediciCITGenerator extends ICTWedgeTranslTestGenerator {

	private static final boolean READ_STD_OUT = true;

	public static boolean OUTPUT_ON_STD_OUT_DURING_TRANSLATION = true;

	private String path;

	public PMediciCITGenerator() {
		super("PMedici");
	}

	@Override
	public TestSuite getTestSuite(CitModel loadModel, int t, boolean ignoreC) throws Exception {
		PMedici pMedici = new PMedici();
		return pMedici.generateTests(loadModel, t, 1);
	}

	@Override
	public TestSuite getTestSuite(CitModel model, int strength, boolean ignoreConstraints, TestSuite ts)
			throws Exception {
		// TODO Auto-generated method stub
		return getTestSuite(model, strength, ignoreConstraints, ts);
	}

	@Override
	public String translateModel(CitModel citModel, boolean ignoreConstraints) throws NotConvertableModel {
		// TODO Auto-generated method stub
		return null;
	}
}
