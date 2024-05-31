package lnf;

import java.util.ArrayList;
import java.util.Collections;

public class LNFShuffler {

	private int d;
	private double beta;
	private ArrayList<Integer> allValues;
	private ArrayList<Integer> sampledValues;
	private int permutatedValues[];
	protected LNFAbstractDummyDistribution distribution;

	public LNFShuffler(int d, double beta, LNFAbstractDummyDistribution distribution) {
		this.d = d;
		this.beta = beta;
		this.distribution = distribution;
		allValues = new ArrayList<Integer>();
		sampledValues = new ArrayList<Integer>();
	}

	public void receiveValue(int value) {
		allValues.add(value);
	}

	public void sampleAndAddFakeValues() {

		for (int value : allValues) {
			if (Math.random() < beta) {
				sampledValues.add(value);
			}
		}

		for (int i = 0; i < d; i++) {
			int zi = distribution.sample();
			for (int j = 0; j < zi; j++) {
				sampledValues.add(i);
			}
		}
	}

	public void permutation() {
		Collections.shuffle(sampledValues);
		permutatedValues = new int[sampledValues.size()];
		int count = 0;
		for (int value : sampledValues) {
			permutatedValues[count] = value;
			count++;
		}
	}

	public int[] getPermutatedValues() {
		return permutatedValues;
	}

}
