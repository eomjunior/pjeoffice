package org.bouncycastle.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class AsymmetricCipherKeyPair {
  private AsymmetricKeyParameter publicParam;
  
  private AsymmetricKeyParameter privateParam;
  
  public AsymmetricCipherKeyPair(AsymmetricKeyParameter paramAsymmetricKeyParameter1, AsymmetricKeyParameter paramAsymmetricKeyParameter2) {
    this.publicParam = paramAsymmetricKeyParameter1;
    this.privateParam = paramAsymmetricKeyParameter2;
  }
  
  public AsymmetricCipherKeyPair(CipherParameters paramCipherParameters1, CipherParameters paramCipherParameters2) {
    this.publicParam = (AsymmetricKeyParameter)paramCipherParameters1;
    this.privateParam = (AsymmetricKeyParameter)paramCipherParameters2;
  }
  
  public AsymmetricKeyParameter getPublic() {
    return this.publicParam;
  }
  
  public AsymmetricKeyParameter getPrivate() {
    return this.privateParam;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/AsymmetricCipherKeyPair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */