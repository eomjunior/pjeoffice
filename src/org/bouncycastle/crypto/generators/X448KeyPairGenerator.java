package org.bouncycastle.crypto.generators;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.X448PrivateKeyParameters;
import org.bouncycastle.crypto.params.X448PublicKeyParameters;

public class X448KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
  private SecureRandom random;
  
  public void init(KeyGenerationParameters paramKeyGenerationParameters) {
    this.random = paramKeyGenerationParameters.getRandom();
  }
  
  public AsymmetricCipherKeyPair generateKeyPair() {
    X448PrivateKeyParameters x448PrivateKeyParameters = new X448PrivateKeyParameters(this.random);
    X448PublicKeyParameters x448PublicKeyParameters = x448PrivateKeyParameters.generatePublicKey();
    return new AsymmetricCipherKeyPair((AsymmetricKeyParameter)x448PublicKeyParameters, (AsymmetricKeyParameter)x448PrivateKeyParameters);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/generators/X448KeyPairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */