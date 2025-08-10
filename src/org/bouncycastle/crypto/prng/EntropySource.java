package org.bouncycastle.crypto.prng;

public interface EntropySource {
  boolean isPredictionResistant();
  
  byte[] getEntropy();
  
  int entropySize();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/prng/EntropySource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */