package blockcipher;

import java.util.HashMap;

/**
 * The implementation of a prescribed S-box functionality as a class. A HashMap
 * data structure is used to store 16 4-bit nibbles with corresponding keys.
 * 
 * @author Joseph Lewis
 * 
 */

public class Sbox {

	// Declare the S-box
	private static HashMap<Integer, Integer> sbox;

	/**
	 * The constructor for the S-box that is used to create an <code>Sbox</code> instance
	 * that initialises and populates an instance of a HashMap with a defined
	 * S-box function. Values are put in sbox, such that sbox.put(I,O) where I
	 * is the input and O is the respective output.
	 */
	public Sbox() {
		// Instantiate and initialise the instance of the S-box
		sbox = new HashMap<Integer, Integer>();

		sbox.put(0b0000, 0b0000);
		sbox.put(0b0001, 0b0001);
		sbox.put(0b0010, 0b1011);
		sbox.put(0b0011, 0b1101);
		sbox.put(0b0100, 0b1001);
		sbox.put(0b0101, 0b1110);
		sbox.put(0b0110, 0b0110);
		sbox.put(0b0111, 0b0111);
		sbox.put(0b1000, 0b1100);
		sbox.put(0b1001, 0b0101);
		sbox.put(0b1010, 0b1000);
		sbox.put(0b1011, 0b0011);
		sbox.put(0b1100, 0b1111);
		sbox.put(0b1101, 0b0010);
		sbox.put(0b1110, 0b0100);
		sbox.put(0b1111, 0b1010);
	}

	/**
	 * Getter method that returns the output bits corresponding to the given
	 * input bits.
	 *
	 * @param I
	 *            is a key, with a value from decimal 0-15
	 * @return the S-box defined output corresponding to the input I
	 */
	public static byte getOutput(int I) {
		return sbox.get(I).byteValue();
	}
}