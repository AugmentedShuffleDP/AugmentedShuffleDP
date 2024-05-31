package s1geo;

import lnf.LNFAbstractDataCollector;

public class S1GeoDataCollector extends LNFAbstractDataCollector {

	public S1GeoDataCollector(double epsilon, double delta, int d, int n) {
		super(epsilon, delta, d, n);

		beta = 1 - Math.exp(-epsilon / 2);
		double qr = 1 / (1 + Math.exp(epsilon / 2));
		mu = qr / (1 - qr);

		distribution = new S1GeoDummyDistribution(qr);
		expectedError = S1GeoUtil.getExpectedError(n, d, 0, qr, beta);
		expectedApproximatedError = S1GeoUtil.getApproximatedExpectedError(epsilon, n, d);
	}

}
