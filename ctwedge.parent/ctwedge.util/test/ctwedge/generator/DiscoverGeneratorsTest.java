package ctwedge.generator;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.junit.Test;

public class DiscoverGeneratorsTest {

	@Test
	public void testGetGenerator() throws ClassNotFoundException, InvalidRegistryObjectException, CoreException {
		assert(DiscoverGenerators.getGenerator("CASATranslator") != null);
	}

}
