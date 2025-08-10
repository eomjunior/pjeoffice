package org.bouncycastle.crypto.prng.drbg;

public interface SP80090DRBG {
  int getBlockSize();
  
  int generate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, boolean paramBoolean);
  
  void reseed(byte[] paramArrayOfbyte);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/prng/drbg/SP80090DRBG.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */