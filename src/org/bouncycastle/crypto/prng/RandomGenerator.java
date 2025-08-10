package org.bouncycastle.crypto.prng;

public interface RandomGenerator {
  void addSeedMaterial(byte[] paramArrayOfbyte);
  
  void addSeedMaterial(long paramLong);
  
  void nextBytes(byte[] paramArrayOfbyte);
  
  void nextBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/prng/RandomGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */