package org.bouncycastle.pqc.crypto.gmss;

public class GMSSPublicKeyParameters extends GMSSKeyParameters {
  private byte[] gmssPublicKey;
  
  public GMSSPublicKeyParameters(byte[] paramArrayOfbyte, GMSSParameters paramGMSSParameters) {
    super(false, paramGMSSParameters);
    this.gmssPublicKey = paramArrayOfbyte;
  }
  
  public byte[] getPublicKey() {
    return this.gmssPublicKey;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/gmss/GMSSPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */