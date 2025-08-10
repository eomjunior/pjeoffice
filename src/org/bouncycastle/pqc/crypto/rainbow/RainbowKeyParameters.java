package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class RainbowKeyParameters extends AsymmetricKeyParameter {
  private int docLength;
  
  public RainbowKeyParameters(boolean paramBoolean, int paramInt) {
    super(paramBoolean);
    this.docLength = paramInt;
  }
  
  public int getDocLength() {
    return this.docLength;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/rainbow/RainbowKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */