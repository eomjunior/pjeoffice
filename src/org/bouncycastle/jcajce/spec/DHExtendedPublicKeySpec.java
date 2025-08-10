package org.bouncycastle.jcajce.spec;

import java.math.BigInteger;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class DHExtendedPublicKeySpec extends DHPublicKeySpec {
  private final DHParameterSpec params;
  
  public DHExtendedPublicKeySpec(BigInteger paramBigInteger, DHParameterSpec paramDHParameterSpec) {
    super(paramBigInteger, paramDHParameterSpec.getP(), paramDHParameterSpec.getG());
    this.params = paramDHParameterSpec;
  }
  
  public DHParameterSpec getParams() {
    return this.params;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/spec/DHExtendedPublicKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */