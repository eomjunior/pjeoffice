package org.bouncycastle.crypto.params;

import org.bouncycastle.util.Arrays;

public class DSTU4145Parameters extends ECDomainParameters {
  private final byte[] dke;
  
  public DSTU4145Parameters(ECDomainParameters paramECDomainParameters, byte[] paramArrayOfbyte) {
    super(paramECDomainParameters.getCurve(), paramECDomainParameters.getG(), paramECDomainParameters.getN(), paramECDomainParameters.getH(), paramECDomainParameters.getSeed());
    this.dke = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getDKE() {
    return Arrays.clone(this.dke);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/DSTU4145Parameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */