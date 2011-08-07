package prime.db.entity;

import java.math.BigInteger;

import org.hibernate.annotations.Entity;

@Entity
public class FSi {
	private BigInteger	fi;
	private BigInteger	si;

	public BigInteger getFi() {
		return fi;
	}

	public void setFi(BigInteger fmax) {
		this.fi = fmax;
	}

	public BigInteger getSi() {
		return si;
	}

	public void setSi(BigInteger si) {
		this.si = si;
	}
}
