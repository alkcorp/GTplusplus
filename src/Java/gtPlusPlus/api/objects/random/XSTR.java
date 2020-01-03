package gtPlusPlus.api.objects.random;
/**
 * A subclass of java.util.random that implements the Xorshift random number
 * generator
 *
 * - it is 30% faster than the generator from Java's library - it produces
 * random sequences of higher quality than java.util.Random - this class also
 * provides a clone() function
 *
 * Usage: XSRandom rand = new XSRandom(); //Instantiation x = rand.nextInt();
 * //pull a random number
 *
 * To use the class in legacy code, you may also instantiate an XSRandom object
 * and assign it to a java.util.Random object: java.util.Random rand = new
 * XSRandom();
 *
 * for an explanation of the algorithm, see
 * http://demesos.blogspot.com/2011/09/pseudo-random-number-generators.html
 *
 * @author Wilfried Elmenreich University of Klagenfurt/Lakeside Labs
 * http://www.elmenreich.tk
 *
 * This code is released under the GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * XSTR - Xorshift ThermiteRandom
 * Modified by Bogdan-G
 * 03.06.2016
 * version 0.0.4
 */
public class XSTR extends Random implements Cloneable {

	private static final long serialVersionUID = 6208727693524452904L;
	private long seed;
	private long last;
	private static final long GAMMA = 0x9e3779b97f4a7c15L;
	private static final int PROBE_INCREMENT = 0x9e3779b9;
	private static final long SEEDER_INCREMENT = 0xbb67ae8584caa73bL;
	private static final double DOUBLE_UNIT = 0x1.0p-53;  // 1.0  / (1L << 53)
	private static final float  FLOAT_UNIT  = 0x1.0p-24f; // 1.0f / (1 << 24)

	/*
     MODIFIED BY: Robotia
     Modification: Implemented Random class seed generator
	 */
	/**
	 * Creates a new pseudo random number generator. The seed is initialized to
	 * the current time, as if by
	 * <code>setSeed(System.currentTimeMillis());</code>.
	 */
	public XSTR() {
		this(seedUniquifier() ^ System.nanoTime());
	}
	private static final AtomicLong seedUniquifier
	= new AtomicLong(8682522807148012L);

	private static long seedUniquifier() {
		// L'Ecuyer, "Tables of Linear Congruential Generators of
		// Different Sizes and Good Lattice Structure", 1999
		for (;;) {
			final long current = seedUniquifier.get();
			final long next = current * 181783497276652981L;
			if (seedUniquifier.compareAndSet(current, next)) {
				return next;
			}
		}
	}

	/**
	 * Creates a new pseudo random number generator, starting with the specified
	 * seed, using <code>setSeed(seed);</code>.
	 *
	 * @param seed the initial seed
	 */
	public XSTR(final long seed) {
		this.seed = seed;
	}
	@Override
	public boolean nextBoolean() {
		return this.next(1) != 0;
	}

	@Override
	public double nextDouble() {
		return (((long)(this.next(26)) << 27) + this.next(27)) * DOUBLE_UNIT;
	}
	/**
	 * Returns the current state of the seed, can be used to clone the object
	 *
	 * @return the current seed
	 */
	public synchronized long getSeed() {
		return this.seed;
	}

	/**
	 * Sets the seed for this pseudo random number generator. As described
	 * above, two instances of the same random class, starting with the same
	 * seed, produce the same results, if the same methods are called.
	 *
	 * @param seed the new seed
	 */
	@Override
	public synchronized void setSeed(final long seed) {
		this.seed = seed;
	}

	/**
	 * @return Returns an XSRandom object with the same state as the original
	 */
	@Override
	public XSTR clone() {
		try {
			super.clone();
		}
		catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new XSTR(this.getSeed());
	}

	/**
	 * Implementation of George Marsaglia's elegant Xorshift random generator
	 * 30% faster and better quality than the built-in java.util.random see also
	 * see http://www.javamex.com/tutorials/random_numbers/xorshift.shtml
	 *
	 * @param nbits
	 * @return
	 */
	@Override
	public int next(final int nbits) {
		long x = this.seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		this.seed = x;
		x &= ((1L << nbits) - 1);
		return (int) x;
	}
	boolean haveNextNextGaussian = false;
	double nextNextGaussian = 0;
	@Override
	synchronized public double nextGaussian() {
		// See Knuth, ACP, Section 3.4.1 Algorithm C.
		if (this.haveNextNextGaussian) {
			this.haveNextNextGaussian = false;
			return this.nextNextGaussian;
		}
		double v1, v2, s;
		do {
			v1 = (2 * this.nextDouble()) - 1; // between -1 and 1
			v2 = (2 * this.nextDouble()) - 1; // between -1 and 1
			s = (v1 * v1) + (v2 * v2);
		} while ((s >= 1) || (s == 0));
		final double multiplier = StrictMath.sqrt((-2 * StrictMath.log(s))/s);
		this.nextNextGaussian = v2 * multiplier;
		this.haveNextNextGaussian = true;
		return v1 * multiplier;
	}
	/**
	 * Returns a pseudorandom, uniformly distributed {@code int} value between 0
	 * (inclusive) and the specified value (exclusive), drawn from this random
	 * number generator's sequence. The general contract of {@code nextInt} is
	 * that one {@code int} value in the specified range is pseudorandomly
	 * generated and returned. All {@code bound} possible {@code int} values are
	 * produced with (approximately) equal probability. The method
	 * {@code nextInt(int bound)} is implemented by class {@code Random} as if
	 * by:
	 * <pre> {@code
	 * public int nextInt(int bound) {
	 *   if (bound <= 0)
	 *     throw new IllegalArgumentException("bound must be positive");
	 *
	 *   if ((bound & -bound) == bound)  // i.e., bound is a power of 2
	 *     return (int)((bound * (long)next(31)) >> 31);
	 *
	 *   int bits, val;
	 *   do {
	 *       bits = next(31);
	 *       val = bits % bound;
	 *   } while (bits - val + (bound-1) < 0);
	 *   return val;
	 * }}</pre>
	 *
	 * <p>The hedge "approx
	 * imately" is used in the foregoing description only because the next
	 * method is only approximately an unbiased source of independently chosen
	 * bits. If it were a perfect source of randomly chosen bits, then the
	 * algorithm shown would choose {@code int} values from the stated range
	 * with perfect uniformity.
	 * <p>
	 * The algorithm is slightly tricky. It rejects values that would result in
	 * an uneven distribution (due to the fact that 2^31 is not divisible by n).
	 * The probability of a value being rejected depends on n. The worst case is
	 * n=2^30+1, for which the probability of a reject is 1/2, and the expected
	 * number of iterations before the loop terminates is 2.
	 * <p>
	 * The algorithm treats the case where n is a power of two specially: it
	 * returns the correct number of high-order bits from the underlying
	 * pseudo-random number generator. In the absence of special treatment, the
	 * correct number of <i>low-order</i> bits would be returned. Linear
	 * congruential pseudo-random number generators such as the one implemented
	 * by this class are known to have short periods in the sequence of values
	 * of their low-order bits. Thus, this special case greatly increases the
	 * length of the sequence of values returned by successive calls to this
	 * method if n is a small power of two.
	 *
	 * @param bound the upper bound (exclusive). Must be positive.
	 * @return the next pseudorandom, uniformly distributed {@code int} value
	 * between zero (inclusive) and {@code bound} (exclusive) from this random
	 * number generator's sequence
	 * @throws IllegalArgumentException if bound is not positive
	 * @since 1.2
	 */
	@Override
	public int nextInt(final int bound) {
		final int newBound;
		if (bound <= 0) {
			newBound = 1;
		//throw new RuntimeException("BadBound");
		}
		else {
			newBound = bound;
		}

		/*int r = next(31);
        int m = bound - 1;
        if ((bound & m) == 0) // i.e., bound is a power of 2
        {
            r = (int) ((bound * (long) r) >> 31);
        } else {
            for (int u = r;
                    u - (r = u % bound) + m < 0;
                    u = next(31))
                ;
        }
        return r;*/
		//speedup, new nextInt ~+40%
		this.last = this.seed ^ (this.seed << 21);
		this.last ^= (this.last >>> 35);
		this.last ^= (this.last << 4);
		this.seed = this.last;
		final int out = (int) this.last % newBound;
		return (out < 0) ? -out : out;
	}
	@Override
	public int nextInt() {
		return this.next(32);
	}

	@Override
	public float nextFloat() {
		return this.next(24) * FLOAT_UNIT;
	}

	@Override
	public long nextLong() {
		// it's okay that the bottom word remains signed.
		return ((long)(this.next(32)) << 32) + this.next(32);
	}

	@Override
	public void nextBytes(final byte[] bytes_arr) {
		for (int iba = 0, lenba = bytes_arr.length; iba < lenba; ) {
			for (int rndba = this.nextInt(),
					nba = Math.min(lenba - iba, Integer.SIZE/Byte.SIZE);
					nba-- > 0; rndba >>= Byte.SIZE) {
				bytes_arr[iba++] = (byte)rndba;
			}
		}
	}
}