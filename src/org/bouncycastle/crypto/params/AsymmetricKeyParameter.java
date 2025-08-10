package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class AsymmetricKeyParameter implements CipherParameters {
  boolean privateKey;
  
  public AsymmetricKeyParameter(boolean paramBoolean) {
    this.privateKey = paramBoolean;
  }
  
  public boolean isPrivate() {
    return this.privateKey;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/AsymmetricKeyParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */