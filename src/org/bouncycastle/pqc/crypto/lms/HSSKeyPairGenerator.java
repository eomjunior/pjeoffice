package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class HSSKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
  HSSKeyGenerationParameters param;
  
  public void init(KeyGenerationParameters paramKeyGenerationParameters) {
    this.param = (HSSKeyGenerationParameters)paramKeyGenerationParameters;
  }
  
  public AsymmetricCipherKeyPair generateKeyPair() {
    HSSPrivateKeyParameters hSSPrivateKeyParameters = HSS.generateHSSKeyPair(this.param);
    return new AsymmetricCipherKeyPair(hSSPrivateKeyParameters.getPublicKey(), hSSPrivateKeyParameters);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/HSSKeyPairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */