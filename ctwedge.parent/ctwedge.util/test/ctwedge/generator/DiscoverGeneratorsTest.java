package ctwedge.generator;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.junit.Test;

public class DiscoverGeneratorsTest {

	@Test
	public void testGetGeneratorCASA() throws ClassNotFoundException, InvalidRegistryObjectException, CoreException {
		assert(DiscoverGenerators.getGenerator("CASAGenerator") != null);
	}
	
	@Test
	public void testGetGeneratorPICT() throws ClassNotFoundException, InvalidRegistryObjectException, CoreException {
		assert(DiscoverGenerators.getGenerator("PICTGenerator") != null);
	}
	
	@Test
	public void testGetGeneratorACTS() throws ClassNotFoundException, InvalidRegistryObjectException, CoreException {
		assert(DiscoverGenerators.getGenerator("ACTSGenerator") != null);
	}
	
	@Test
	public void testGetGeneratorNotExisting() throws ClassNotFoundException, InvalidRegistryObjectException, CoreException {
		assert(DiscoverGenerators.getGenerator("ABCD") == null);
	}

}
