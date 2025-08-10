package org.bouncycastle.crypto.modes.gcm;

public interface GCMExponentiator {
  void init(byte[] paramArrayOfbyte);
  
  void exponentiateX(long paramLong, byte[] paramArrayOfbyte);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/gcm/GCMExponentiator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */