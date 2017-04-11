package blockcipher;

/**
 * The implementation of the permutation as a class that provides the required
 * bit manipulation functions for the block cipher.
 *
 * @author Joseph Lewis
 * 
 */

public class Permutation {

	/**
	 * The key scheduling algorithm produces 8 round keys K0,...,K7 from the
	 * 16-bit primary key k; the rest of the round keys are generated according
	 * to a recurrence relation.
	 * 
	 * @param k
	 *            is given 16-bit primary key used to generate the round keys
	 * @return K, the 8 round keys
	 */
	
	public static byte[] keySchedule(byte[] k) {
		// Create 8-byte round keys
		byte[] K = new byte[8];
		// K0 is the lower-order byte of k1; K1 is the high-order byte of k
		K[0] = k[1];
		K[1] = k[0];
		// Round keys are generated, where i=2,...,7
		for (int i = 2; i <= 7; i++) {
			K[i] = (byte) (rotL(K[i - 1], 3) ^ rotL(K[i - 2], 5));
		}
		return K;
	}

	/**
	 * Converts a given byte into two 4-bit nibbles.
	 *
	 * @param B
	 *            is the byte to be converted
	 * @return the byte array containing the nibbles
	 */
	public static byte[] convert(int B) {
		// Declare the nibble
		byte[] nibble = new byte[2];
		// Get the lowest-order byte
		nibble[0] = (byte) (B & 0x0f);
		// Get the highest-order byte
		nibble[1] = (byte) ((B >> 4) & 0x0f);
		// Return the nibble
		return nibble;
	}

	/**
	 * Consolidates two 4-bit nibbles into a single byte.
	 *
	 * @param highB
	 *            is the highest-order byte
	 * @param lowB
	 *            the lowest-order byte
	 * @return the consolidated byte
	 */
	public static byte consolidate(int highB, int lowB) {
		return (byte) ((highB << 4) + lowB);
	}

	/**
	 * This method is used as part of a recurrence relation to generate round
	 * keys. rotL gives a rotation of a byte parameter by a given position
	 * argument.
	 *
	 * @param B
	 *            is the byte to be rotated
	 * @param pos
	 *            is the amount of positions to rotate B by
	 * @return the rotated byte
	 */
	public static byte rotL(int B, int pos) {
		return (byte) (((B & 0xff) << pos) | ((B & 0xff) >>> (8 - pos)));
	}
}
