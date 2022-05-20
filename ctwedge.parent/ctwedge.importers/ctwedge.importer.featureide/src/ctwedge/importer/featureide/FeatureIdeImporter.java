package ctwedge.importer.featureide;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.CtWedgeFactory;
import ctwedge.ctWedge.Expression;
import ctwedge.ctWedge.impl.CtWedgePackageImpl;
import ctwedge.util.ext.NotImportableException;
import de.ovgu.featureide.fm.core.ConstraintAttribute;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

abstract public class FeatureIdeImporter extends ctwedge.util.ext.ICTWedgeImporter {

	// at every feature maps its constraint that means that that feature is chosen
	// it depends how the feature are represented
	protected Map<IFeature, Expression> choosenExpr = new HashMap<>();
	protected Map<IFeature, String> choosenString = new HashMap<>();

	@Override
	public CitModel importModel(String path) throws NotImportableException {
			FeatureModel fm = null;
			try {
				fm = readModel(Paths.get(path));
				//TODO Analyze the model
	//			FeatureModelAnalyzer featureModelAnalyzer = fm.getAnalyser();
	//			if (!featureModelAnalyzer.isValid())
	//				throw new NotImportableException(path + "\n NOT VALID MODEL");
	//			// analyze the model in order to find problems, useless features and
	//			// so on
	//			featureModelAnalyzer.analyzeFeatureModel(new NullProgressMonitor());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new NotImportableException(path + "\n FILE NOT FOUND");
			} catch (UnsupportedModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new NotImportableException(path + "\n UNSUPPORTED EXCEPTION");
	//		} catch (TimeoutException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//			throw new NotImportableException(path + "\n TIMEOUT");
			}
			// transform to citlab
			File file = new File(path);
			String newname = normalize(file.getName().substring(0, file.getName().indexOf(".")));
			CitModel transformed = transform(fm);
			if (startsWithANumber(newname))
				transformed.setName("M" + newname);
			else
				transformed.setName("M" + newname);
			return transformed;
		}

	protected String normalize(String x) {
		return Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll(",", "y")
				.replaceAll("-", "_");
	}
	// subclasses can introduce their own reader for feature models (FeatureModel)
	// changed the way models are supported
	// abstract protected FeatureModelManager getFeatureModelReader(FeatureModel fm);
	FeatureModel readModel(Path path) throws FileNotFoundException, UnsupportedModelException {
			if (path.toString().toLowerCase().endsWith(".xml")) {
				//FeatureModelManager reader = getFeatureModelReader(fm);
				IFeatureModel root = FeatureModelManager.load(path);
				assert root != null : "errors reading " + path;
				System.out.println(root);
				System.out.println(root.getFeatureOrderList());
				return (FeatureModel) root;
			} else {
	//			GuidslReader guidslReader = new GuidslReader(fm);
	//			FeatureModelReaderIFileWrapper reader = new FeatureModelReaderIFileWrapper(guidslReader);
	//			reader.readFromFile(new File(path));
				throw new RuntimeException("only .xml files are supported by this importer");
			}
		}
	/**
	 *
	 * @param fm
	 * @return an equivalent CitModel
	 */
	private CitModel transform(IFeatureModel fm) {
		CtWedgePackageImpl.init();
		CitModel result = CtWedgeFactory.eINSTANCE.createCitModel();
		IFeature root = fm.getStructure().getRoot().getFeature();
		result.setName(normalize(normalize(root.getName())));
		// add parameters and set choose
		addParameterFor(root, result);
		// add the constraints (due to the translation)
		addConstraints(root, result);
		// add the extra constraints contained in the original model
		ConstraintConverter converter = new ConstraintConverter(choosenExpr);
		for (IConstraint c : fm.getConstraints()) {
			// if the constraint is useless, skip it
			// TODO questo potrebbe non funzionare perchè adesso è deprecato
			ConstraintAttribute attribute = FeatureUtils.getConstraintAttribute(c);
			if ((attribute == ConstraintAttribute.REDUNDANT) || (attribute == ConstraintAttribute.DEAD)
					|| (attribute == ConstraintAttribute.TAUTOLOGY))
				continue;
			Expression expr = converter.visit(c.getNode());
			result.getConstraints().add(expr);
		}
		// System.out.println(constraints);
		return result;
	}

	protected abstract void addConstraints(IFeature root, CitModel result);

	protected abstract void addParameterFor(IFeature root, CitModel result);

	private static boolean startsWithANumber(String s) {
		return !(s.charAt(0) >= 'a' || s.charAt(0) >= 'A');
	}
}
