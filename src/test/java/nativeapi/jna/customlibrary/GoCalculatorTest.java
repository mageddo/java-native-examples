package nativeapi.jna.customlibrary;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoCalculatorTest {
	@Test
	public void sum() throws Exception {
		Assert.assertEquals(3, GoCalculator.INSTANCE.Add(1,2));
		Assert.assertEquals(5, GoCalculator.INSTANCE.Add(-2,7));

	}

}
