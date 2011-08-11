package view.helper;

import java.math.BigInteger;

public class CheckResult {

	public BigInteger f, s;
	public boolean isPrime;
	public long elapsedTime;
	
	public String getEleapsedTime() {
		String formattedTime = Float.toString(this.elapsedTime / 1000f);
		return formattedTime + " s";
	}
}
