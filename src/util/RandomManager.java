package util;

public final class RandomManager {
	private static MT19937 random;

	static {
		random = new MT19937();
	}

	private RandomManager() {
	}

	public static void setSeed(long seed) {
		random = new MT19937(seed);
	}

	public static void setSeed() {
		random = new MT19937(System.currentTimeMillis());
	}

	public static double getGaussian(double average, double dev) {
		return random.Gaussian(average, dev);
	}

	public static double getRandom() {
		return random.nextDouble();
	}

	public static int getRandom(int size) {
		return (int) (random.nextDouble() * size);
	}

	public static int getRandom(int min, int max) {
		return (int) (random.nextDouble() * (max - min) + min);
	}

	public static double getRandom(double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}

	public static void initSeed() {
		random = new MT19937();
	}
}
