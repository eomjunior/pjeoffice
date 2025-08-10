package org.bouncycastle.pqc.crypto.gmss;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class GMSSKeyParameters extends AsymmetricKeyParameter {
  private GMSSParameters params;
  
  public GMSSKeyParameters(boolean paramBoolean, GMSSParameters paramGMSSParameters) {
    super(paramBoolean);
    this.params = paramGMSSParameters;
  }
  
  public GMSSParameters getParameters() {
    return this.params;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/gmss/GMSSKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */