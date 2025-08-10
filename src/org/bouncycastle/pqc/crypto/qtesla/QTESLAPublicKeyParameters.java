package org.bouncycastle.pqc.crypto.qtesla;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;

public final class QTESLAPublicKeyParameters extends AsymmetricKeyParameter {
  private int securityCategory;
  
  private byte[] publicKey;
  
  public QTESLAPublicKeyParameters(int paramInt, byte[] paramArrayOfbyte) {
    super(false);
    if (paramArrayOfbyte.length != QTESLASecurityCategory.getPublicSize(paramInt))
      throw new IllegalArgumentException("invalid key size for security category"); 
    this.securityCategory = paramInt;
    this.publicKey = Arrays.clone(paramArrayOfbyte);
  }
  
  public int getSecurityCategory() {
    return this.securityCategory;
  }
  
  public byte[] getPublicData() {
    return Arrays.clone(this.publicKey);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/qtesla/QTESLAPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */