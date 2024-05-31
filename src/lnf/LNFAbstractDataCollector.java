package lnf;

public abstract class LNFAbstractDataCollector {

	protected double epsilon;
	protected double delta;
	protected int d;
	protected int n;
	protected double beta;
	protected int[] permutatedValues;
	protected double[] frequency;
	protected LNFAbstractDummyDistribution distribution;
	protected double expectedError;
	protected double expectedApproximatedError;
	protected double mu;

	protected LNFAbstractDataCollector(double epsilon, double delta, int d, int n) {
		this.epsilon = epsilon;
		this.delta = delta;
		this.d = d;
		this.n = n;
		frequency = new double[d];
	}

	public void receives(int[] permutatedValues) {
		this.permutatedValues = permutatedValues;
		calcFreqDist();
		adjustFreqDist();
	}

	public void calcFreqDist() {
		for (int i = 0; i < permutatedValues.length; i++) {
			frequency[permutatedValues[i]]++;
		}
	}

	protected void adjustFreqDist() {
		for (int i = 0; i < d; i++) {
			frequency[i] = 1 / (n * beta) * (frequency[i] - mu);
		}
	}

	public double[] getFrequency() {
		return frequency;
	}

	public LNFAbstractDummyDistribution getDistribution() {
		return distribution;
	}

	public double getBeta() {
		return beta;
	}

	public double getExpectedError() {
		return expectedError;
	}

	public double getExpectedApproximatedError() {
		return expectedError;
	}
}
