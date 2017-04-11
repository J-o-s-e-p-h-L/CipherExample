package blockcipher;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * A JUnit test class for Comp343Cipher, which specifically tests the four
 * methods implemented in the Cipher interface; loadKey(), deleteKey(),
 * encrypt() and decrypt(). The testing of the encrypt() and decrypt() methods
 * uses both the Sbox and Permutation classes, meaning their functionality is
 * also indirectly tested in this class.
 * 
 * @author Joseph Lewis
 * 
 */
public class Comp343CipherTest {
	// Create an instance of Comp343Cipher with a given key
	private String k = "0xb0b1";
	private Comp343Cipher blockCipherTest = new Comp343Cipher(Crypt.createByteArray(k));

	/**
	 * Test method for @see {@link Comp343Cipher#loadKey(byte[])}.
	 */
	@Test
	public final void loadKeyTest1() {
		// Assert a 'true' return as expected from a successful key load, using
		// the createByteArray method
		assertTrue("Returns true when key load is successful", blockCipherTest.loadKey(Crypt.createByteArray(k)));
	}

	/**
	 * Test method for @see {@link Comp343Cipher#loadKey(byte[])}.
	 */
	@Test
	public final void loadKeyTest2() {
		// Assert a 'true' return as expected from the loadkey method after a
		// key is loaded successfully
		byte key[] = { (byte) 0xab, (byte) 0xcd };
		assertTrue("Returns true when key load is successful", blockCipherTest.loadKey(key));
	}

	/**
	 * Test method for @see {@link Comp343Cipher#deleteKey()}.
	 */
	@Test
	public final void deleteKeyTest1() {
		assertTrue("Returns true when key load is successful", blockCipherTest.loadKey(Crypt.createByteArray(k)));
		// Assert a 'true' return as expected from the a successful key deletion
		assertTrue("Returns true when the key is deleted", blockCipherTest.deleteKey());
	}

	/**
	 * Test method for @see {@link Comp343Cipher#deleteKey()}.
	 */
	@Test
	public final void deleteKeyTest2() {
		byte key[] = { (byte) 0xb0, (byte) 0xb1 };
		blockCipherTest.loadKey(key);
		// Assert a successful key deleted from an alternative key load method
		assertTrue("Returns true when the key is deleted", blockCipherTest.deleteKey());
	}

	/**
	 * Test method for @see {@link Comp343Cipher#encrypt(byte[])}.
	 */
	@Test
	public final void encryptTest1() {
		byte key[] = { (byte) 0xb0, (byte) 0xb1 };
		blockCipherTest.loadKey(key);
		// Create some plaintext
		byte[] plainText = { 'a', 'b' };
		// Create some ciphertext
		byte[] cipherText = new byte[plainText.length];
		for (int i = 0; i < cipherText.length; i++) {
			cipherText[i] = blockCipherTest.encrypt(plainText)[i];
		}
		// Test that the ciphertext is not equivalent to the plaintext
		assertFalse("The ciphertext is not equilivlant to the plaintext", cipherText.equals(plainText));
	}

	/**
	 * Test method for @see {@link Comp343Cipher#encrypt(byte[])}.
	 */
	@Test
	public final void encryptTest2() {
		byte key[] = { (byte) 0xb0, (byte) 0xb1 };
		blockCipherTest.loadKey(key);
		// Create some plaintext
		byte[] plainText = { 'a', 'b' };
		// Create some ciphertext
		byte[] cipherText = new byte[plainText.length];
		for (int i = 0; i < plainText.length - 1; i += 2) {
			byte[] block = blockCipherTest.encrypt(new byte[] { plainText[i], plainText[i + 1] });
			cipherText[i] = block[0];
			cipherText[i + 1] = block[1];
		}
		// Test that the encrypted plaintext is equivalent to the ciphertext
		assertArrayEquals("The encrypted plaintext is equal to the ciphertext", blockCipherTest.encrypt(plainText),
				cipherText);
	}

	/**
	 * Test method for @see {@link Comp343Cipher#encrypt(byte[])}.
	 */
	@Test
	public final void encryptTest3() {
		// An alternative method of testing a successful encryption
		byte key[] = { (byte) 0xfc, (byte) 0x39 };
		blockCipherTest.loadKey(key);
		byte[] plain = { 'a', 'b' };
		byte[] cipherText = new byte[2];
		cipherText[0] = blockCipherTest.encrypt(plain)[0];
		cipherText[1] = blockCipherTest.encrypt(plain)[1];
		assertArrayEquals("The encrypted plaintext is equal to the ciphertext", blockCipherTest.encrypt(plain),
				cipherText);
	}

	/**
	 * Test method for @see {@link Comp343Cipher#decrypt(byte[])}.
	 */
	@Test
	public final void decryptTest() {
		blockCipherTest.loadKey(Crypt.createByteArray(k));
		// Create the plaintext
		byte[] plainText = { 'a', 'b' };
		// Create the ciphertext of the plaintext
		byte[] cipherText = new byte[plainText.length];
		for (int i = 0; i < plainText.length - 1; i += 2) {
			byte[] block = blockCipherTest.encrypt(new byte[] { plainText[i], plainText[i + 1] });
			cipherText[i] = block[0];
			cipherText[i + 1] = block[1];
		}
		// Create a byte array with decrypted ciphertext
		byte[] decrypted = new byte[cipherText.length];
		for (int i = cipherText.length - 1; i > 0; i -= 2) {
			byte[] block = blockCipherTest.decrypt(new byte[] { cipherText[i], cipherText[i - 1] });
			decrypted[i] = block[0];
			decrypted[i - 1] = block[1];
		}
		// Test that the decrypted ciphertext is equivalent to the plain
		assertArrayEquals("The decrypted ciphertext is  equilivlant to the plaintext", decrypted, plainText);
	}
}
