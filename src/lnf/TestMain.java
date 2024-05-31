package lnf;

import java.lang.reflect.InvocationTargetException;

import s1geo.S1GeoDataCollector;
import sageo.SAGeoDataCollector;
import sbin.SBinDataCollector;

public class TestMain {

	static int numSimulations = 100;

	public static void main(String args[]) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		double epsilons[] = { 0.1, 0.2, 0.5, 1, 2, 3, 4, 5 };
		double deltas[] = { Math.pow(0.1, 12) };
		String dataNames[] = { "census", "foursquare", "localization", "rfid" };

		for (String dataName : dataNames) {
			for (double epsilon : epsilons) {
				for (double delta : deltas) {
					runTest(epsilon, delta, dataName, SBinDataCollector.class);
					runTest(epsilon, delta, dataName, SAGeoDataCollector.class);
					runTest(epsilon, delta, dataName, S1GeoDataCollector.class);
				}
			}
		}
	}

	public static <T1 extends LNFAbstractDataCollector> void runTest(double epsilon, double delta, String dataName,
			Class<T1> targetDataCollector) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		int orgData[] = Util.getOrgVals(dataName);
		int n = orgData.length;
		int d = Util.getCategoryNum(orgData);

		double sumMSE = 0.0;
		double sumMAE = 0.0;
		double expectedError = 0.0;
		double approximatedExpectedError = 0.0;

		for (int sim = 0; sim < numSimulations; sim++) {
			// Initialize users
			LNFUser users[] = new LNFUser[n];
			for (int i = 0; i < n; i++) {
				users[i] = new LNFUser(orgData[i]);
			}

			// Initialize the data collector
			T1 dataCollector = targetDataCollector
					.getDeclaredConstructor(double.class, double.class, int.class, int.class)
					.newInstance(epsilon, delta, d, n);
			// Initialize the shuffler
			LNFShuffler shuffler = new LNFShuffler(d, dataCollector.getBeta(), dataCollector.getDistribution());

			expectedError = dataCollector.getExpectedError();
			approximatedExpectedError = dataCollector.getExpectedApproximatedError();

			// Send input values
			for (LNFUser user : users) {
				shuffler.receiveValue(user.getOriginalValue());
			}

			// Sampling and adding fake values
			shuffler.sampleAndAddFakeValues();
			shuffler.permutation();

			// Data collector receives the sampled values
			dataCollector.receives(shuffler.getPermutatedValues());
			double frequency[] = dataCollector.getFrequency();
			double frequency_thresholding[] = Util.significance_threshold(frequency, n, expectedError);

			// For evaluation
			double[] originalFrequency = Util.getOrgFrequency(users, d);

			sumMSE += Util.getMSE(originalFrequency, frequency_thresholding);
			sumMAE += Util.getMAE(originalFrequency, frequency_thresholding);
		}

		System.out.println(targetDataCollector.getPackage().getName() + "\t" + dataName + "\t" + epsilon + "\t" + delta
				+ "\t" + (sumMSE / numSimulations) + "\t" + (sumMAE / numSimulations) + "\t" + expectedError + "\t"
				+ approximatedExpectedError);
	}

}
