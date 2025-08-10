package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.util.BigIntegers;

public class RandomDSAKCalculator implements DSAKCalculator {
  private static final BigInteger ZERO = BigInteger.valueOf(0L);
  
  private BigInteger q;
  
  private SecureRandom random;
  
  public boolean isDeterministic() {
    return false;
  }
  
  public void init(BigInteger paramBigInteger, SecureRandom paramSecureRandom) {
    this.q = paramBigInteger;
    this.random = paramSecureRandom;
  }
  
  public void init(BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
    throw new IllegalStateException("Operation not supported");
  }
  
  public BigInteger nextK() {
    int i = this.q.bitLength();
    while (true) {
      BigInteger bigInteger = BigIntegers.createRandomBigInteger(i, this.random);
      if (!bigInteger.equals(ZERO) && bigInteger.compareTo(this.q) < 0)
        return bigInteger; 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/signers/RandomDSAKCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */