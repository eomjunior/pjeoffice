package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class KeyGenerationParameters {
  private SecureRandom random;
  
  private int strength;
  
  public KeyGenerationParameters(SecureRandom paramSecureRandom, int paramInt) {
    this.random = CryptoServicesRegistrar.getSecureRandom(paramSecureRandom);
    this.strength = paramInt;
  }
  
  public SecureRandom getRandom() {
    return this.random;
  }
  
  public int getStrength() {
    return this.strength;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/KeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */