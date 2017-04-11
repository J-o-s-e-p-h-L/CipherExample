package blockcipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

/**
 * The class containing the Main daemon thread that allows arguments to be
 * parsed into the program. <code>Crypt</code> is used to create an instance of
 * <code>Comp343Cipher</code> to run the cryptographic process with given files,
 * a key and command in Stage 1, or run birthday and pre-image attacks for Stage
 * 2.
 * 
 * @author Joseph Lewis
 *
 */
public class Crypt {
	// Declare variables
	static Comp343Cipher blockCipher;
	static String plainFileName;
	static String cipherFileName;
	static String command;
	// Plaintext byte array
	private static byte[] m;
	// Ciphertext byte array
	private static byte[] c;

	/**
	 * A method used to validate the arguments given by the user and catch any
	 * exception caused due to errors. The plaintext byte array m and ciphertext
	 * byte array c are loaded with the data contained in the text files, while
	 * the command argument is checked for validity.
	 * 
	 * @param plainArg
	 *            contains the plaintext filename
	 * @param cipherArg
	 *            contains the ciphertext filename
	 * @param commandArg
	 *            is the command argument
	 * @return true if all arguments are valid
	 * @throws IOException
	 *             if one of the files cannot be read
	 */
	public static boolean validateArguments(String plainArg, String cipherArg, String commandArg) throws IOException {
		try {
			// Parse the plaintext file and load it into the byte array m
			Path textPath = Paths.get(System.getProperty("user.dir") + File.separator + plainArg);
			byte[] plainClone = Files.readAllBytes(textPath);
			if (plainClone.length % 2 == 1) {
				m = new byte[plainClone.length + 1];
				System.arraycopy(plainClone, 0, m, 0, plainClone.length);
			} else {
				m = plainClone.clone();
			}
			plainFileName = plainArg;
			// Parse the ciphertext file and load it into the byte array c
			textPath = Paths.get(System.getProperty("user.dir") + File.separator + cipherArg);
			c = Files.readAllBytes(textPath);
			cipherFileName = cipherArg;
			// Validate the command argument
			if (!(commandArg.equals("E") || commandArg.equals("D"))) {
				System.out.println("'" + commandArg
						+ "' is not a valid argument.\nUse the command 'E' to encypt or 'D' to decrypt text.");
				return false;
			}
			command = commandArg;
		} catch (FileNotFoundException e) {
			// A FileNotFoundException will be caught if a file is not found
			System.out.println("The file '" + e.getMessage() + "' could not be found.");
			return false;
		} catch (IOException e) {
			// An IO exception will be caught if a file is not found
			System.out.println("The file '" + e.getMessage() + "' could not be read.");
			return false;
		}
		return true;
	}

	/**
	 * This method creates a 2-byte array representation of a given hexadecimal
	 * string.
	 * 
	 * @param str
	 *            16-bit key
	 * @return is the converted byte array
	 */
	static byte[] createByteArray(String str) {
		str = str.replace("0x", "");
		byte B1 = (byte) Integer.parseInt(str.substring(0, 2), 16);
		byte B2 = (byte) Integer.parseInt(str.substring(2, 4), 16);

		return new byte[] { B1, B2 };
	}

	/**
	 * This method is called from the Main method and encrypts or decrypts the
	 * input file contents, depending on the command argument. The cryptography
	 * is run using an instance of Comp343Cipher.
	 * 
	 * @throws IOException
	 *             if a read or write error occurs
	 */
	static void runCryptography() throws IOException {
		// A byte array containing the output dependent on the command argument
		byte[] O;
		if (command.equals("E")) {
			O = new byte[m.length];
			System.out.println("Encrypting text blocks from " + plainFileName + "...");
			for (int i = 0; i < m.length - 1; i += 2) {
				byte[] block = blockCipher.encrypt(new byte[] { m[i], m[i + 1] });
				O[i] = block[0];
				O[i + 1] = block[1];
			}
			writeToFile(O, cipherFileName);
		} else {
			O = new byte[c.length];
			System.out.println("Decrypting text blocks from " + cipherFileName + "...");
			for (int i = c.length - 1; i > 0; i -= 2) {
				byte[] block = blockCipher.decrypt(new byte[] { c[i], c[i - 1] });
				O[i] = block[0];
				O[i - 1] = block[1];
			}
			writeToFile(O, plainFileName);
		}
	}

	/**
	 * The encrypted/decrypted content of the input file is written to the
	 * output file.
	 * 
	 * @param O
	 *            is the output block to be written to the designated file
	 * @param outputFileName
	 *            is the output file parameter
	 * @throws IOException
	 *             if a read or write error occurs
	 */
	public static void writeToFile(byte[] O, String outputFileName) throws IOException {
		File pathOfFile = new File(System.getProperty("user.dir") + File.separator + outputFileName);
		FileOutputStream fos = new FileOutputStream(pathOfFile);
		System.out.println("Writing blocks to " + outputFileName + "...");
		fos.write(O);
		System.out.println(
				"Process complete...\n" + pathOfFile.length() + "B were successfully written to '" + pathOfFile + "'.");
		fos.close();
	}
	
	/*public static void main(String args[]) throws IOException {
	// Dummy arguments used purely for testing purposes in Stage 1
	// args = new String[4];
	// args[0] = "plain.txt";
	// args[1] = "cipher.txt";
	// args[2] = "0xb0b1";
	// args[3] = "E";

	// Assign the file names to variables for verbose feedback
		try {
			// Run the block cipher if validateArguments returns true
			if (validateArguments(args[0], args[1], args[3])) {
				blockCipher = new Comp343Cipher(createByteArray(args[2]));
				runCryptography();
			} else {
				System.out.println("Process failed...");
			}
		} catch (Exception e) {
		System.out.println(
				"One or more arguments are invalid...\nUse the syntax: java -jar Crypt.jar plain.txt cipher.txt 0xabcd E");
		}
	}*/

	/**
	 * The Main method used for Stage 2. Random byte arrays are generated to be
	 * used as parameters for @see Comp343Cipher#compression(byte[] m, byte[] h)
	 * and random digests are then generated for the exploration of separate
	 * birthday pre-image cryptographic attacks.
	 *
	 * @param args[]
	 *            contains the filenames, initial cryptographic key and command
	 *            (Used only in Stage 1)
	 */
	public static void main(String args[]) {
		// Print some information about the program
		String info = "Welcome to Stage 2 of COMP343 Assignment 1, S1 2016.\n";
		info += "Author: Joseph Lewis\n";
		info += "This program will now launch birthay and pre-image attacks on digests created through the compression function of a modified Stage 1 block cipher.";
		System.out.println(info);
		// Create a new instance of Comp343Cipher
		blockCipher = new Comp343Cipher();
		// Generate random m and h values
		byte[] m0 = new byte[2];
		byte[] h0 = new byte[2];
		new Random().nextBytes(m0);
		new Random().nextBytes(h0);
		// Generate a digest for the birthday attack
		byte[] H = blockCipher.compression(m0, h0);
		// Run the birthday attack
		System.out.println("*** Starting the Birthday Attack ***");
		BirthdayAttack ba = new BirthdayAttack();
		System.out.println("Average attempts/collision: " + ba.getAvgAttempts() + ".");
		// Run the pre-image attack
		System.out.println("*** Starting the Pre-Image Attack ***");
		PreImageAttack pia = new PreImageAttack(H);
		System.out.println("Average attempts/collision: " + pia.getAvgAttempts() + ".");
	}
}
