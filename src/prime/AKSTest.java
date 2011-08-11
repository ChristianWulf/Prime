package prime;

import java.math.BigInteger;

import nuim.cs.crypto.polynomial.PolynomialException;
import nuim.cs.crypto.polynomial.big.BigPolynomial;
import nuim.cs.crypto.polynomial.big.field.BigFieldPolynomial;

public class AKSTest implements Runnable {

	private BigInteger		n;
	private static boolean	runFlag;
	private static boolean	toWait	= false;

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public boolean getRunFlag() {
		return this.runFlag;
	}

	public void setToWait(boolean toWait) {
		this.toWait = toWait;
	}

	public boolean getToWait() {
		return this.toWait;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	// **********************************************************************
	// Hilfsfunktionen zum AKS Test
	// **********************************************************************
	// Schritt 1: n=a^b fuer irgendein b>1?
	// **********************************************************************

	public static BigInteger pow(BigInteger base, BigInteger exp) {

		BigInteger p = BigInteger.ONE;
		while (exp.compareTo(new BigInteger("0")) > 0) { // inv: b^e * p = b0 ^ e0
			if (exp.remainder(new BigInteger("2")).compareTo(BigInteger.ONE) == 0) {
				p = p.multiply(base);
			}
			base = base.multiply(base);
			exp = exp.divide(new BigInteger("2")); // abgerundet
		}
		return p;
	}

	public static boolean isPow(BigInteger n) {

		boolean zusammengesetzt = false;
		BigInteger obereSchranke = n;
		BigInteger untereSchranke = BigInteger.ONE;
		BigInteger t;

		for (BigInteger i = BigInteger.ONE; i.compareTo(new BigInteger(new Integer(n.bitLength())
				.toString())) < 0; i = i.add(BigInteger.ONE)) {
			while (obereSchranke.subtract(untereSchranke).compareTo(BigInteger.ONE) > 0) {
				t = obereSchranke.add(untereSchranke).divide(new BigInteger("2"));
				if (pow(t, i.add(BigInteger.ONE)).compareTo(n) == 0) {
					return zusammengesetzt = true;
				}
				if (pow(t, i.add(BigInteger.ONE)).compareTo(n) > 0) {
					obereSchranke = t;
				}
				if (pow(t, i.add(BigInteger.ONE)).compareTo(n) < 0) {
					untereSchranke = t;
				}
			}
		}
		return zusammengesetzt;
	}

	// ***********************************************************************
	/*
	 * Schritt2,3: Wahl des AKS-Zeugen r, (Schritt 2) und ob !(a|n) fuer alle a <= r (Schritt 3)
	 */// **********************************************************************

	public static boolean isRPrime(BigInteger r) {
		BigInteger a = new BigInteger("2");
		while (a.multiply(a).compareTo(r) <= 0) {
			if (r.remainder(a).compareTo(BigInteger.ZERO) == 0) {
				return false;
			}
			a = a.add(BigInteger.ONE);
		}
		return true;
	}

	// ************************************************************************
	// Hier wird nach dem r gesucht
	// ************************************************************************

	public static BigInteger findAppropriateR(BigInteger n) {
		BigInteger r = new BigInteger("2");
		BigInteger lgn = new BigInteger(
				new Integer(n.subtract(BigInteger.ONE).bitLength()).toString());
		BigInteger s1 = lgn.multiply(new BigInteger("4")).multiply(lgn);

		loopToFindR: while (r.compareTo(n) < 0) {
			if (n.remainder(r).compareTo(BigInteger.ZERO) == 0) {
				return BigInteger.ZERO; // r|n
			}
			if (isRPrime(r)) {
				for (BigInteger i = BigInteger.ONE; i.compareTo(s1) <= 0; i = i.add(BigInteger.ONE)) {
					if (n.modPow(i, r).compareTo(BigInteger.ONE) == 0) {
						break;
					}
					if (i.add(BigInteger.ONE).compareTo(s1) == 0) {
						break loopToFindR;
					}
				}
			}
			r = r.add(BigInteger.ONE);
		}

		return r;
	}

	@Override
	public void run() {

		runFlag = true;

		aks: while (runFlag) {

			// ************************************************************************
			// eigentlicher AKS Test
			// ************************************************************************

			// Spezialfall n=1
			if (n.compareTo(BigInteger.ONE) == 0) {
				runFlag = false;
				break aks;
			}

			// Schritt 1: n=a^b fuer irgendein b>1?
			if (isPow(n)) {
				runFlag = false;
				break aks;
			}

			// Schritt 2: Waehle AKS Zeuge
			BigInteger r = findAppropriateR(n);

			if (r.compareTo(BigInteger.ZERO) == 0) {
				runFlag = false;
				break aks;
			}

			// ************************************************************************
			// Schritt 4: n<=r ? prim : Schritt 5
			// ************************************************************************

			if (r.compareTo(n) == 0) {
				runFlag = false;
				break aks;
			} else {

				// ************************************************************************
				// Schritt 5: Teste Polynomkongruenz
				// ************************************************************************
				// Eigentlicher Vergleich der Polynom Kongruenzen
				// hierzu wird die das Packet nium.cs.crypto verwendet
				// siehe http://www.crypto.cs.nuim.ie/software/theodor/download/
				// **************************************************************************
				BigInteger lgn = new BigInteger(
						new Integer(n.subtract(BigInteger.ONE).bitLength()).toString());
				BigInteger sL = new BigInteger(new Integer(
						new Double(Math.sqrt(r.doubleValue())).intValue()).toString())
						.multiply(lgn).multiply(new BigInteger("2"));
				BigFieldPolynomial modulus = new BigFieldPolynomial(new BigPolynomial("x^"
						+ r.toString() + "-1"), n);
				for (BigInteger a = new BigInteger("1"); a.compareTo(sL) < 0; a = a
						.add(BigInteger.ONE)) {
					// x+a mod n
					BigFieldPolynomial u = new BigFieldPolynomial(new BigPolynomial("x+"
							+ a.toString()), n);
					// x^n+a mod n
					BigFieldPolynomial v = new BigFieldPolynomial(new BigPolynomial("x^"
							+ n.toString() + "+" + a.toString()), n);
					try {
						// (x+a)^n mod(x^r-1,n) <-- zeitkritischer Schritt
						BigFieldPolynomial lhs = new BigFieldPolynomial(u.modPow(n, modulus), n);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
						if (runFlag == false) {
							toWait = false;
							break aks;
						}
						// (x^n+a) mod(x^r-1,n)
						BigFieldPolynomial rhs = new BigFieldPolynomial(v.mod(modulus)
								.modCoefficient(n), n);
						if (!lhs.equals(rhs)) {

							runFlag = false;
							break aks;
						}
					} catch (PolynomialException e) {
						System.out.println(e.getMessage());
					}
				}
				runFlag = false;
			}
		}
	}

	public static boolean isPrime(BigInteger n) {
		// Spezialfall n=1
		if (n.compareTo(BigInteger.ONE) == 0) {
			return true;
		}

		// Schritt 1: n=a^b fuer irgendein b>1?
		if (isPow(n)) {
			return false;
		}

		// Schritt 2: Waehle AKS Zeuge
		BigInteger r = findAppropriateR(n);

		if (r.compareTo(BigInteger.ZERO) == 0) {
			return false;
		}

		// ************************************************************************
		// Schritt 4: n<=r ? prim : Schritt 5
		// ************************************************************************

		if (r.compareTo(n) == 0) {
			return true;
		} else {

			// ************************************************************************
			// Schritt 5: Teste Polynomkongruenz
			// ************************************************************************
			// Eigentlicher Vergleich der Polynom Kongruenzen
			// hierzu wird die das Packet nium.cs.crypto verwendet
			// siehe http://www.crypto.cs.nuim.ie/software/theodor/download/
			// **************************************************************************
			BigInteger lgn = new BigInteger(
					new Integer(n.subtract(BigInteger.ONE).bitLength()).toString());
			BigInteger sL = new BigInteger(new Integer(
					new Double(Math.sqrt(r.doubleValue())).intValue()).toString()).multiply(lgn)
					.multiply(new BigInteger("2"));
			BigFieldPolynomial modulus = new BigFieldPolynomial(new BigPolynomial("x^"
					+ r.toString() + "-1"), n);
			for (BigInteger a = new BigInteger("1"); a.compareTo(sL) < 0; a = a.add(BigInteger.ONE)) {
				// x+a mod n
				BigFieldPolynomial u = new BigFieldPolynomial(
						new BigPolynomial("x+" + a.toString()), n);
				// x^n+a mod n
				BigFieldPolynomial v = new BigFieldPolynomial(new BigPolynomial("x^" + n.toString()
						+ "+" + a.toString()), n);
				try {
					// (x+a)^n mod(x^r-1,n) <-- zeitkritischer Schritt
					BigFieldPolynomial lhs = new BigFieldPolynomial(u.modPow(n, modulus), n);
					// (x^n+a) mod(x^r-1,n)
					BigFieldPolynomial rhs = new BigFieldPolynomial(v.mod(modulus)
							.modCoefficient(n), n);
					if (!lhs.equals(rhs)) {
						return false;
					}
				} catch (PolynomialException e) {
					System.out.println(e.getMessage());
				}
			}
			return true;
		}
	}

}