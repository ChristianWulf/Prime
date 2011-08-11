package view.helper;

import view.GUI;


public class GUIShouldChecker {

	public static boolean shouldFilterNonFermats(boolean fermat) {
		return GUI.cbxFilterNonFermats.isSelected() && !fermat;
	}

	public static boolean shouldFilterPrimes(boolean isPrime) {
		return GUI.cbxFilterPrimes.isSelected() && isPrime;
	}
}
