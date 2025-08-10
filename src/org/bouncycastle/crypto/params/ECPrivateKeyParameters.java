package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ECPrivateKeyParameters extends ECKeyParameters {
  private final BigInteger d;
  
  public ECPrivateKeyParameters(BigInteger paramBigInteger, ECDomainParameters paramECDomainParameters) {
    super(true, paramECDomainParameters);
    this.d = paramECDomainParameters.validatePrivateScalar(paramBigInteger);
  }
  
  public BigInteger getD() {
    return this.d;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/ECPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */