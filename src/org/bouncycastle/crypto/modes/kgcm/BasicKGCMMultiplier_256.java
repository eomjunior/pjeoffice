package org.bouncycastle.crypto.modes.kgcm;

public class BasicKGCMMultiplier_256 implements KGCMMultiplier {
  private final long[] H = new long[4];
  
  public void init(long[] paramArrayOflong) {
    KGCMUtil_256.copy(paramArrayOflong, this.H);
  }
  
  public void multiplyH(long[] paramArrayOflong) {
    KGCMUtil_256.multiply(paramArrayOflong, this.H, paramArrayOflong);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/kgcm/BasicKGCMMultiplier_256.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */