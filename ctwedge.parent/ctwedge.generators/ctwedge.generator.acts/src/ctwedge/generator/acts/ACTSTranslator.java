package ctwedge.generator.acts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.Constraint;
import ctwedge.ctWedge.Parameter;
import ctwedge.util.NotConvertableModel;
import ctwedge.util.Test;
import ctwedge.util.TestSuite;
import ctwedge.util.ext.ICTWedgeModelProcessor;
import ctwedge.util.ext.ICTWedgeTestGenerator;
//import edu.uta.cse.fireeye.common.Constraint;
//import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.common.SUT;
import edu.uta.cse.fireeye.common.TestGenProfile.Algorithm;
import edu.uta.cse.fireeye.common.TestSet;
import edu.uta.cse.fireeye.common.TestSetWrapper;
import edu.uta.cse.fireeye.service.engine.IpoEngine;

/**
 * Exports to ACTS a citlab model, has a method to call ACTS, and returns its
 * output into String
 **/
public class ACTSTranslator extends ICTWedgeTestGenerator {

	static Logger LOG = Logger.getLogger(ACTSTranslator.class);

	public ACTSTranslator() {
		super("ACTS");
	}

	public void convertModel(CitModel citModel, Boolean constraintUse, int nWise, String path) {

		try {
			saveActsTXTonlyModel(citModel, path);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// accept only paramters with ascii names
	static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

	public static boolean isPureAscii(String v) {
		return asciiEncoder.canEncode(v);
	}

	/**
	 * translate a citModel from citlab to ACTS SUT
	 * 
	 * @param ignoreConstraints
	 * @throws NotConvertableModel
	 * 
	 */
	public SUT buildSUT(CitModel citModel, boolean ignoreConstraints, int nWise) {
		SUT sut = new SUT(citModel.getName());
		try {
			Log.debug("Building SUT...");
			// build a system configuration
			// it is recommended to create a new parameter from the SUT object
			// doing so will assign the parameter with an ID automatically
			ACTSParameterAdder paramBuilder = new ACTSParameterAdder(sut);
			tanslateParameter(citModel, paramBuilder);
			Log.debug("adding relations ... ");
			//
			// create relation
			Relation r = new Relation(nWise);
			for (edu.uta.cse.fireeye.common.Parameter p : sut.getParameters()) {
				r.addParam(p);
			}
			// add this relation into the sut
			sut.addRelation(r);

			// add the default relation
			// sut.addDefaultRelation(nWise);
			// create constraints
			if (!ignoreConstraints) {
				Log.debug("adding constraints ... ");
				translateConstraints(citModel, sut);
			}
			// ConstraintManager cm = new ConstraintManager(sut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sut;
	}

	private void translateConstraints(CitModel citModel, SUT sut) {
		ACTSConstraintTranslator trans = new ACTSConstraintTranslator(citModel);
		Log.debug(ACTSConstraintTranslator.dump(citModel, ""));
		for (Constraint c : citModel.getConstraints()) {
			// to String
			String casString = trans.doSwitch(c);
			Log.debug("casString: " + casString);
			assert casString != null;
			// to list of parameters
			// TODO it works if if null, not clear what method to use to get the parameters
			ArrayList<edu.uta.cse.fireeye.common.Parameter> pInC = sut.getParams();
			Log.debug("paramters in constraints" + pInC);
			edu.uta.cse.fireeye.common.Constraint constraint = new edu.uta.cse.fireeye.common.Constraint(casString,
					pInC);
			assert constraint != null : "Constraint null";
			sut.addConstraint(constraint);
		}
	}

	private void tanslateParameter(CitModel citModel, ACTSParameterAdder paramBuilder) {
		for (ctwedge.ctWedge.Parameter p : citModel.getParameters()) {
			String name = p.getName();
			if (!isPureAscii(name)) {
				// "parameter " + name + " is not US-ASCII . choco has problems");
			}
			// "adding parameter " + name);
			paramBuilder.doSwitch(p);
		}
	}

	// IMPORTER METHODS

	@Override
	public TestSuite getTestSuite(CitModel model, int strength, boolean ignoreConstraints) {
		try {
			SUT sut = buildSUT(model, ignoreConstraints, strength);
			return getTestSuite(model, strength, ignoreConstraints, sut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TestSuite getTestSuite(CitModel model, int strength, boolean ignoreConstraints, TestSuite ts) {
		try {
			SUT sut = buildSUT(model, ignoreConstraints, strength);
			if (ts != null) {
				File f = saveSeedsToFile(model, ts, ",");
				TestSetWrapper wrapper = new TestSetWrapper(sut);
				TestSet tsOld = wrapper.readTestSet(f.getAbsolutePath(), "CSV Format");
				sut.setExistingTestSet(tsOld);
				f.delete();
			}
			return getTestSuite(model, strength, ignoreConstraints, sut);
		} catch (Exception e) {
			Log.debug("adding relations ... ");
			e.printStackTrace();
		}
		return null;
	}

	private File saveSeedsToFile(CitModel model, TestSuite ts, String separator) throws IOException {
		// First, translate the test suite into a format readable by PICT
		File tempModel = File.createTempFile("seedrows_" + model.getName(), ".txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(tempModel));
		// Write param names
		int counter = 0;
		for (Parameter p : model.getParameters()) {
			if (counter > 0)
				out.append(separator);
			out.append(p.getName());
			counter++;
		}
		out.append("\n");
		// Write values
		for (Test t : ts.getTests()) {
			counter = 0;
			for (Parameter p : model.getParameters()) {
				if (counter > 0)
					out.append(separator);
				out.append(t.get(p.getName()));
				counter++;
			}
			out.append("\n");
		}
		out.close();
		return tempModel;
	}

	public TestSuite getTestSuite(CitModel model, int strength, boolean ignoreConstraints, SUT sut) {
		String res = "";
		long t_end = 0;
		long t_start = 0;
		Log.debug("ACTS sto chiamando ACTS... on " + model.getName() + " " + strength + " " + ignoreConstraints);
		try {
			Log.debug("1. ACTS sto chiamando ACTS... - building engine");
			// Create an IPO engine object
			t_start = System.currentTimeMillis();
			IpoEngine engine = new IpoEngine(sut);
			Log.debug("2. ACTS sto chiamando ACTS... - printing sut");
			Log.debug(sut.toString());
			Log.debug("3. ACTS sto chiamando ACTS... . calling build");
			engine.buildOnlyPT(Algorithm.ipog);
			// get the resulting test set
			Log.debug("4. ACTS sto chiamando ACTS... - get the TS");
			TestSet ts = engine.getTestSet();
			Log.debug("5. ACTS sto chiamando ACTS... - saving tests");
			t_end = System.currentTimeMillis();
			if (ts != null)
				res = serializeTestSet(model, ts);

		} catch (Exception e) {
			Log.debug("Error: " + e.getMessage());
			e.printStackTrace();
		}

		Log.debug("Obtained res: " + res);

		/*
		 * CitModel m = ModelsFromStringsTester.loadModel(model); try { TestPolicy tp =
		 * IgnoringConstraints.getInstance(); ICitLabTestGenerator gen = new
		 * RandomGenerator(); if (generator!=null) switch (generator) { case "random":
		 * break; case "acts": gen = new BaseChoiceGenerator(); break; case "ipof": gen
		 * = new IpoFGenerator(); break; case "ipogd": gen = new IpoGDGenerator();
		 * break; case "ipog": gen = new IpoGGenerator(); break; case "casa": gen = new
		 * TSGeneratorByCasa(); break; case "medici": gen = new TSGeneratorByMedici();
		 * break; case "smt": gen = new GeneratorBySMT(); break; case "ipo": gen = new
		 * IPOgenerator(); break; default: gen = new RandomGenerator(); break; }
		 * TestSuite ts = tp.produceTestSuite(m, gen, 2); res = new
		 * TestSuiteWriter().caseTestSuite(ts).toString(); } catch (Exception e) {
		 * StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);
		 * e.printStackTrace(pw); res = sw.toString(); // stack trace as a string }
		 */

		if (res.isEmpty())
			return null;

		TestSuite ts = new TestSuite(res, model);
		ts.setStrength(strength);
		ts.setGeneratorTime(t_end - t_start);

		return ts;
	}

	/** returns the test set as a CSV string, with heading */
	public static String serializeTestSet(CitModel model, TestSet ts) {
		StringBuilder sb = new StringBuilder();
		assert ts != null : "TestSet null";
		String[][] matrix = getTestSuiteMatrix(model, ts);
		for (int i = 0; i < matrix.length; i++) {
			sb.append(matrix[i][0]);
			for (int j = 1; j < matrix[i].length; j++)
				sb.append(";" + matrix[i][j]);
			sb.append("\n");
		}
		return sb.toString();
	}

	/** get actual values of parameters in the test set (not only 0 1 2...) */
	public static String[][] getTestSuiteMatrix(CitModel model, TestSet ts) {
		assert ts != null : "TestSet null";
		// sb.append(ts+"\n ");
		ArrayList<int[]> matrix = ts.getMatrix();
		String[][] m = new String[matrix.size() + 1][model.getParameters().size()];

		for (int i = 0; i < model.getParameters().size(); i++) {
			Parameter p = model.getParameters().get(i);
			m[0][i] = p.getName();
			int j = 0;
			while (j < model.getParameters().size() && !ts.getParam(j).getName().equals(p.getName()))
				j++;
			for (int r = 0; r < matrix.size(); r++)
				m[r + 1][i] = "" + ts.getParam(j).getValue(matrix.get(r)[j]);
		}
		return m;
	}

	public static TestSuite getTestSuite(CitModel model, TestSet ts) {
		return new TestSuite(serializeTestSet(model, ts), model);
	}

	/** Added to create the actual test suite */
//	public static TestSuite getTestSuite(String serializedTestSuite) {
//		TestSuite ts = new TestSuite(serializedTestSuite);
//		
//		return ts;
//	}
//	

	// OTHER UTILS
	public File saveActsTXTonlyModel(CitModel citModel, String dirPath) throws IOException {
		SUT sut = new SUT(citModel.getName());
		// it is recommended to create a new parameter from the SUT object
		// doing so will assign the parameter with an ID automatically
		ACTSParameterAdder paramBuilder = new ACTSParameterAdder(sut);
		tanslateParameter(citModel, paramBuilder);

		if (citModel.getConstraints().size() > 0) {
			translateConstraints(citModel, sut);
		}
		String pathToFile = citModel.getName() + ".txt";
		File txtFile = new File(dirPath + File.separator + pathToFile);

		try {

			PrintWriter p = new PrintWriter(txtFile);

			p.println("[System]");
			p.println("-- specify system name");
			p.println("Name: " + sut.getName());
			p.println();

			p.println("[Parameter]");
			p.println("-- general syntax is parameter_name : value1, value2, ...");
			int ptype;
			String origStr;
			for (edu.uta.cse.fireeye.common.Parameter parameter : sut.getParams()) {
				ptype = parameter.getParamType();
				String ptypeStr = "";
				switch (ptype) {
				case 2:
					ptypeStr = "boolean";
					break;
				case 1:
					ptypeStr = "enum";
					break;
				case 0:
					ptypeStr = "int";
					break;
				case -1:
					ptypeStr = "invalid";
				}

				String basechoiceValuePrefix = "[";
				String basechoiceValueSuffix = "]";
				ArrayList<String> validValues = new ArrayList<String>();
				for (int i = 0; i < parameter.getValues().size(); i++) {
					if (parameter.isBaseChoice(i)) {
						validValues.add(basechoiceValuePrefix + parameter.getValue(i) + basechoiceValueSuffix);
					} else
						validValues.add(parameter.getValue(i));
				}
				origStr = validValues.toString();
				String valueStr = origStr.substring(1, origStr.length() - 1);

				if (!parameter.getInvalidValues().isEmpty()) {
					String invalidValuesMarker = " ; ";
					ArrayList<String> invalidValues = new ArrayList<String>();

					for (String value : parameter.getInvalidValues()) {
						invalidValues.add(value);
					}
					origStr = invalidValues.toString();
					valueStr = valueStr + invalidValuesMarker + origStr.substring(1, origStr.length() - 1);
				}

				p.println(parameter.getName() + " (" + ptypeStr + ") : " + valueStr);
			}
			p.println();

			if (sut.getRelations() != null && sut.getRelations().size() > 0) {
				p.println("[Relation]");
				p.println("-- this section is optional");
				p.println("-- general format Rx : (p1, p2, ..., pk, Strength)");
				int x = 0;
				for (Relation relation : sut.getRelations()) {
					x++;
					p.println("R" + x + " : (" + relation.getParamNames() + ", " + relation.getStrength() + ")");
				}
				p.println();
			}

			if (sut.getConstraintManager().getConstraints().size() > 0) {
				p.println("[Constraint]");
				p.println("-- this section is also optional");
				for (edu.uta.cse.fireeye.common.Constraint constraint : sut.getConstraintManager().getConstraints()) {
					String text = constraint.getText();
					p.println(text);
// we wanted to split long lines - but ACTS does not know how to temrinate a constraint
//					// split long lines in multiple lines
//					StringTokenizer tok = new StringTokenizer(text, " ");
//					int lineLen = 0;
//					while (tok.hasMoreTokens()) {
//						String word = tok.nextToken();
//						if (lineLen + word.length() > maxLineLength) {
//							p.println();
//							lineLen = 0;
//						}
//						p.print(word);
//						p.print(" ");
//						lineLen += word.length();
//					}
				}
			}
			p.println();
			Log.debug("IGNORING TSs");
			p.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return txtFile;
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ACTSTranslator <CTWedgeModelPath>");
			System.exit(1);
		}
		String modelPath = args[0];
		CitModel model = ICTWedgeModelProcessor.getModel(modelPath);
		ACTSTranslator trans = new ACTSTranslator();
		trans.convertModel(model, true, 2, ".");
	}
}
