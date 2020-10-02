package ctwedge.util.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.jna.Pointer;
import ctwedge.ctWedge.*;
import org.apache.log4j.Logger;

public class ConstraintChecker {

	private static final Logger logger = Logger.getLogger(ConstraintChecker.class);

	/**
	 * return true if consistent
	 * 
	 * @param model
	 * @return
	 */
	YicesLibrary yices;

	public ConstraintChecker(YicesLibrary yices) {
		this.yices = yices;

	}

	public static void createCtxFromModel(CitModel model, List<Constraint> list, YicesLibrary yices, Pointer ctx,
			Map<String, String> declaredElements, Map<Parameter, Pointer> variables) {
		addParamters(model, ctx, declaredElements, variables);
		// traduce tutti i constraint e li aggiunge al contesto
		for (Constraint r : list) {
			YicesConstraintTranslator translator = new YicesConstraintTranslator(ctx, variables, declaredElements,
					yices);
			Pointer constraint = translator.doSwitch(r);
			// add this constraint
			yices.yices_assert(ctx, constraint);

		}
	}

	public static void addParamters(CitModel model, Pointer ctx, Map<String, String> declaredElements,
			Map<Parameter, Pointer> variables) {
		ParameterAdder pa = new ParameterAdder(ctx, declaredElements);
		for (Parameter nt : model.getParameters()) {
			Pointer variable = pa.doSwitch(nt);
			variables.put(nt, variable);
		}
	}

	public static void addParamters(CitModel model, CitModel model2, Pointer ctx, Map<String, String> declaredElements,
			Map<Parameter, Pointer> variables) {
		ParameterAdder pa = new ParameterAdder(ctx, declaredElements);
		for (Parameter nt : model.getParameters()) {
			Pointer variable = pa.doSwitch(nt);
			variables.put(nt, variable);
			for (Parameter nt2 : model2.getParameters()) {
				if (nt2.getName().equals(nt.getName())) {
					variables.put(nt2, variable);
					break;
				}
			}
		}
	}

	boolean checkConsistency(CitModel model, Boolean deleteCtx) {

		Pointer ctx = yices.yices_mk_context();
		// crea contesto

		// aggiunge tutti i parametri con i loro tipi
		Map<String, String> declaredElements = new HashMap<>();

		Map<Parameter, Pointer> variables = new HashMap<Parameter, Pointer>();
		createCtxFromModel(model, model.getConstraints(), yices, ctx, declaredElements, variables);
		int res = yices.yices_check(ctx);
		// debug??
		if (logger.isDebugEnabled()) {
			yices.yices_dump_context(ctx);
			if (res == YicesLibrary.lbool.l_true) {
				Pointer m = yices.yices_get_model(ctx);
				yices.yices_display_model(m);
			}
		}
		if (deleteCtx)
			yices.yices_del_context(ctx);
		if (res == YicesLibrary.lbool.l_undef)
			throw new RuntimeException();
		if (res == YicesLibrary.lbool.l_true)
			return true;
		assert (res == YicesLibrary.lbool.l_false);
		return false;
	}

	ArrayList<Constraint> findMaxConstraintsSet(CitModel model, Boolean deleteCtx) {

		Pointer ctx = yices.yices_mk_context();

		if (checkConsistency(model, deleteCtx))
			return new ArrayList<Constraint>(model.getConstraints());

		// yices.yices_del_context(ctx);
		ArrayList<ArrayList<Constraint>> subSets = SubSetMaker
				.getSubsets(new ArrayList<Constraint>(model.getConstraints()));

		Collections.sort(subSets, new Comparator<ArrayList<Constraint>>() {

			@Override
			public int compare(ArrayList<Constraint> o1, ArrayList<Constraint> o2) {
				Integer size1 = o1.size();
				Integer size2 = o2.size();
				return size2.compareTo(size1);
			}

		});
		for (ArrayList<Constraint> constraints : subSets) {
			if (constraints.size() == 0)
				return null;

			ctx = yices.yices_mk_context();
			HashMap<String, String> declaredElements = new HashMap<>();
			HashMap<Parameter, Pointer> variables = new HashMap<Parameter, Pointer>();
			createCtxFromModel(model, constraints, yices, ctx, declaredElements, variables);

			if (yices.yices_check(ctx) == YicesLibrary.lbool.l_true) {
				logger.debug("soddisfa - constraints: " + constraints.size() + " = " + constraints);
				yices.yices_del_context(ctx);
				return constraints;

			} else {
				System.out.println(" non soddisfa ");
				yices.yices_del_context(ctx);
			}
		}
		return null;
	}

}
