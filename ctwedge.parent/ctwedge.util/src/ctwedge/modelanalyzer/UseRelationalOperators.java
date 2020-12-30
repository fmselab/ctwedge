package ctwedge.modelanalyzer;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.EObject;

import ctwedge.ctWedge.CitModel;
import ctwedge.ctWedge.RelationalExpression;

// use relational operators
public class UseRelationalOperators extends CTWedgeModelAnalyzer{
	
	
	static public UseRelationalOperators eInstance = new  UseRelationalOperators();
	
	@Override
	public boolean process(CitModel model) {
		Iterator<EObject> sourceIterator = model.eAllContents();
		Stream<EObject> targetStream = StreamSupport.stream(
		          Spliterators.spliteratorUnknownSize(sourceIterator, Spliterator.ORDERED),
		          false);
		return targetStream.anyMatch( x -> isRelational(x));
	}

	private boolean isRelational(EObject x) {
		return (x instanceof RelationalExpression);
	} 
}
