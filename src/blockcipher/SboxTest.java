package blockcipher;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * A JUnit test class to test the constructor and @see {@link Sbox#getOutput(int)} method of the SBox
 * class.
 * 
 * @author Joseph Lewis
 *
 */
public class SboxTest {
	@SuppressWarnings("unused")
	// Create an instance for Sbox for testing
	private Sbox sbox = new Sbox();

	/**
	 * Test method for @see {@link Sbox#getOutput(int)}.
	 */
	@Test
	public void getOutputTest() {
		// Test that selected inputs give the right output value
		assertEquals((int) Sbox.getOutput(0b0000), 0);
		assertEquals((int) Sbox.getOutput(0b0110), 6);
		assertEquals((int) Sbox.getOutput(0b0100), 9);
		assertEquals((int) Sbox.getOutput(0b1111), 10);
	}

}
