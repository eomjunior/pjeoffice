package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class SRP6GroupParameters {
  private BigInteger N;
  
  private BigInteger g;
  
  public SRP6GroupParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
    this.N = paramBigInteger1;
    this.g = paramBigInteger2;
  }
  
  public BigInteger getG() {
    return this.g;
  }
  
  public BigInteger getN() {
    return this.N;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/SRP6GroupParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */