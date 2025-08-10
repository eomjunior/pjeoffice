package org.bouncycastle.crypto.modes.kgcm;

public interface KGCMMultiplier {
  void init(long[] paramArrayOflong);
  
  void multiplyH(long[] paramArrayOflong);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/kgcm/KGCMMultiplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */