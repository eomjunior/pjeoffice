package org.bouncycastle.pqc.crypto.lms;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class LMSKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
  LMSKeyGenerationParameters param;
  
  public void init(KeyGenerationParameters paramKeyGenerationParameters) {
    this.param = (LMSKeyGenerationParameters)paramKeyGenerationParameters;
  }
  
  public AsymmetricCipherKeyPair generateKeyPair() {
    SecureRandom secureRandom = this.param.getRandom();
    byte[] arrayOfByte1 = new byte[16];
    secureRandom.nextBytes(arrayOfByte1);
    byte[] arrayOfByte2 = new byte[32];
    secureRandom.nextBytes(arrayOfByte2);
    LMSPrivateKeyParameters lMSPrivateKeyParameters = LMS.generateKeys(this.param.getParameters().getLMSigParam(), this.param.getParameters().getLMOTSParam(), 0, arrayOfByte1, arrayOfByte2);
    return new AsymmetricCipherKeyPair(lMSPrivateKeyParameters.getPublicKey(), lMSPrivateKeyParameters);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSKeyPairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */