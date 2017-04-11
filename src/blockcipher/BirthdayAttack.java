package blockcipher;

import java.util.ArrayList;
import java.util.Random;

/**
 * The BirthdayAttack class uses @see Comp343Cipher#compression(byte[] m, byte[]
 * h), taking 32-bit inputs (m, h) and translating it to a shorter 16-bit
 * digest. There must be colliding inputs, such that Eh1(m1) = Eh2(m2), and this
 * is the mathematical basis for the attack.
 * 
 * @author Joseph Lewis
 *
 */
public class BirthdayAttack {
	// Decleare and initialise some variables
	private Comp343Cipher bc;
	private byte[] message1 = new byte[2];
	private byte[] chaining_value1 = new byte[2];
	private byte[] digest1 = new byte[2];

	private ArrayList<String[]> pool;
	private int passAvg;

	/**
	 * Constructor method used for starting the birthday attack.
	 */
	public BirthdayAttack() {
		// Create a new cipher instance
		bc = new Comp343Cipher();
		// Generate the first random m and h
		pool = new ArrayList<String[]>();
		new Random().nextBytes(message1);
		new Random().nextBytes(chaining_value1);
		passAvg = 0;
		// Start looking for collisions
		find100Collisions();
	}

	/**
	 * This method is parsed a byte array as a parameter and returns a hex
	 * string for console display.
	 * 
	 * @param block
	 *            the byte array to be converted
	 * @return the hex string of the block
	 */
	public static String toHex(byte[] block) {
		String hexString = "";
		for (int i = 0; i < block.length; i++) {
			hexString += Integer.toString((block[i] & 0xff) + 0x100, 16).substring(1);
		}
		return "0x" + hexString;
	}

	/**
	 * Used to generate digests and store them in a pool to search for 100
	 * collisions created using the compression function.
	 */
	public void find100Collisions() {
		ArrayList<Integer> attempts = new ArrayList<Integer>();
		int passes = 0;
		int most = -1;
		int least = -1;
		System.out.println("Printing the next 100 collisions...");
		while (attempts.size() < 100) {
			if (pool.isEmpty()) {
				String[] tmp1 = { toHex(message1), toHex(chaining_value1), toHex(digest1) };
				pool.add(tmp1);
			}
			new Random().nextBytes(message1);
			new Random().nextBytes(chaining_value1);
			digest1 = bc.compression(message1, chaining_value1);
			String[] tmp1 = { toHex(message1), toHex(chaining_value1), toHex(digest1) };
			pool.add(tmp1);
			// A pool of digests is used to find collisions, and a successful
			// collision is printed
			for (int i = 0; i < pool.size() - 1; i++) {
				if (pool.get(pool.size() - 1)[2].equals(pool.get(i)[2])) {
					System.out.println(pool.get(attempts.size())[0] + " " + pool.get(attempts.size())[1] + "\n"
							+ pool.get(i)[0] + " " + pool.get(i)[1] + "\ncollision");
					// Keep track of the most and lest passes for collisions
					if (most == -1 || passes > most) {
						most = passes;
					}
					if (least == -1 || passes < least) {
						least = passes;
					}
					attempts.add(passes);
					passes = 0;
				}
			}
			passes++;
		}
		for (int i = 0; i < attempts.size(); i++) {
			passAvg += attempts.get(i);
		}
		passAvg /= attempts.size();
		// Print out some stats on the birthday attack
		// Print the statistics of the attack to the console or command line /
		// terminal
		Double BA_TC = Math.pow(2, (16 / 2));
		String BA_Stats = "--- Birthday Attack Statistics ---\n";
		BA_Stats += "Theoretical complexity of the attack is: O(2^(n/2)) = " + BA_TC.intValue() + "\n";
		BA_Stats += "First Collision: " + attempts.get(0) + " | Last Collision: " + attempts.get(attempts.size() - 1)
				+ "\n";
		BA_Stats += "Most attempts until collision: " + most + " | Least: " + least;
		System.out.println(BA_Stats);
	}

	/**
	 * Getter method that returns the average number of attempts for each
	 * collision in the attack.
	 * 
	 * @return the average number of attempts
	 */
	public int getAvgAttempts() {
		return passAvg;
	}
}
