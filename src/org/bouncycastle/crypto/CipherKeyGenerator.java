package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class CipherKeyGenerator {
  protected SecureRandom random;
  
  protected int strength;
  
  public void init(KeyGenerationParameters paramKeyGenerationParameters) {
    this.random = paramKeyGenerationParameters.getRandom();
    this.strength = (paramKeyGenerationParameters.getStrength() + 7) / 8;
  }
  
  public byte[] generateKey() {
    byte[] arrayOfByte = new byte[this.strength];
    this.random.nextBytes(arrayOfByte);
    return arrayOfByte;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/CipherKeyGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */