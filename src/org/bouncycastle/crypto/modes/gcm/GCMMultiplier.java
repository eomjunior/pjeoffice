package org.bouncycastle.crypto.modes.gcm;

public interface GCMMultiplier {
  void init(byte[] paramArrayOfbyte);
  
  void multiplyH(byte[] paramArrayOfbyte);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/gcm/GCMMultiplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */