package prime.db.entity;

import java.math.BigInteger;

import org.hibernate.annotations.Entity;

@Entity
public class FSmax {

	private BigInteger	fmax;
	private BigInteger	smax;

	public BigInteger getFmax() {
		return fmax;
	}

	public void setFmax(BigInteger fmax) {
		this.fmax = fmax;
	}

	public BigInteger getSmax() {
		return smax;
	}

	public void setSmax(BigInteger smax) {
		this.smax = smax;
	}
}
