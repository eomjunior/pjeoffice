package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class NaccacheSternKeyParameters extends AsymmetricKeyParameter {
  private BigInteger g;
  
  private BigInteger n;
  
  int lowerSigmaBound;
  
  public NaccacheSternKeyParameters(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt) {
    super(paramBoolean);
    this.g = paramBigInteger1;
    this.n = paramBigInteger2;
    this.lowerSigmaBound = paramInt;
  }
  
  public BigInteger getG() {
    return this.g;
  }
  
  public int getLowerSigmaBound() {
    return this.lowerSigmaBound;
  }
  
  public BigInteger getModulus() {
    return this.n;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/NaccacheSternKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */