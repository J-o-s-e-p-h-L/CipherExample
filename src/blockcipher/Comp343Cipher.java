package blockcipher;

/**
 * This method implements the Cipher interface with algorithms to encrypt and
 * decrypt blocks of bits.
 *
 * @author Joseph Lewis
 */
public class Comp343Cipher implements Cipher {
	// Declare variables
	private Sbox sbox;
	// Declare the initial key
	private byte[] key;
	// Declare the 8 round keys
	private byte[] K;
	// Declare the left and right blocks
	private byte[] L;
	private byte[] R;

	/**
	 * Constructor used to create an instance of Comp343Cipher.
	 */
	public Comp343Cipher() {
		sbox = new Sbox();
	}

	/**
	 * The constructor for the class, that loads the key and initialises the
	 * S-box.
	 * 
	 * @param k
	 *            is the key to be loaded
	 */
	public Comp343Cipher(byte[] k) {
		loadKey(k);
		sbox = new Sbox();
	}

	/**
	 * Load a key into the Cipher, perform any key setup, etc.
	 * 
	 * @param block
	 * @return true on success
	 */
	public boolean loadKey(byte[] k) {
		if (k == null) {
			// Return false if the key is null
			return false;
		}
		// Load the given key
		key = k;
		K = Permutation.keySchedule(key);
		return true;
	}

	/**
	 * Delete the key from the Cipher
	 * 
	 * @return true on success
	 */

	public boolean deleteKey() {
		// Set the key to null
		key = null;
		return true;
	}

	/**
	 * Encrypt a block of plaintext using a key scheduling algorithm along with
	 * a defined S-box.
	 * 
	 * @param block
	 * @return the block of ciphertext
	 */
	public byte[] encrypt(byte[] block) {
		// Split the block into left and right blocks
		// Initialise and instantiate L with the byte at block[0] and R with the
		// byte at block[1]
		L = new byte[9];
		R = new byte[9];
		L[0] = block[0];
		R[0] = block[1];
		// Encrypt the block using the S-box and permutations
		for (int i = 0; i <= 7; i++) {
			byte y = (byte) (R[i] ^ K[i]);
			// Convert the bytes into nibbles
			@SuppressWarnings("static-access")
			byte highZ = (byte) sbox.getOutput(Permutation.convert(y)[1]);
			@SuppressWarnings("static-access")
			byte lowZ = (byte) sbox.getOutput(Permutation.convert(y)[0]);
			// Consolidate the highest-order and lowest-order bits
			byte z = Permutation.consolidate(highZ, lowZ);
			// The permutation is a rotation of the bits of the argument by 2
			// positions left
			L[i + 1] = R[i];
			R[i + 1] = (byte) (L[i] ^ Permutation.rotL(z, 2));
		}
		return new byte[] { L[8], R[8] };
	}

	/**
	 * Decrypt a block of ciphertext using a key scheduling algorithm along with
	 * a defined S-box.
	 * 
	 * @param block
	 * @return the block of plaintext
	 */
	public byte[] decrypt(byte[] block) {
		// Split the block into left and right blocks
		// Initialise and instantiate L with the byte at block[0] and R with the
		// byte at block[1]
		byte[] L = new byte[9];
		byte[] R = new byte[9];
		L[0] = block[0];
		R[0] = block[1];
		// Encrypt the block using the S-box and permutations
		for (int i = 0; i <= 7; i++) {
			byte y = (byte) (R[i] ^ K[K.length - i - 1]);
			// Convert the bytes into nibbles
			@SuppressWarnings("static-access")
			byte highZ = (byte) sbox.getOutput(Permutation.convert(y)[1]);
			@SuppressWarnings("static-access")
			byte lowZ = (byte) sbox.getOutput(Permutation.convert(y)[0]);
			// Consolidate the highest-order and lowest-order bits
			byte z = Permutation.consolidate(highZ, lowZ);
			// The permutation is a rotation of the bits of the argument by 2
			// positions left
			L[i + 1] = R[i];
			R[i + 1] = (byte) (L[i] ^ Permutation.rotL(z, 2));
		}
		return new byte[] { L[8], R[8] };
	}

	/**
	 * The compression function that turns the block cipher into a hash function
	 * that produces a digest.
	 * 
	 * @param m
	 *            is a 2-byte (16-bit) message block
	 * @param h
	 *            is a 2-byte (16-bit) chaining variable
	 */

	public byte[] compression(byte[] m, byte[] h) {
		// Load the chaining variable
		loadKey(h);
		byte[] H = encrypt(m);
		// Return the digest 'H' that is generated from Eh(m)=H
		return H;
	}
}
