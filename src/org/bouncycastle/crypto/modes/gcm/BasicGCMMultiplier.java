package org.bouncycastle.crypto.modes.gcm;

public class BasicGCMMultiplier implements GCMMultiplier {
  private long[] H;
  
  public void init(byte[] paramArrayOfbyte) {
    this.H = GCMUtil.asLongs(paramArrayOfbyte);
  }
  
  public void multiplyH(byte[] paramArrayOfbyte) {
    long[] arrayOfLong = GCMUtil.asLongs(paramArrayOfbyte);
    GCMUtil.multiply(arrayOfLong, this.H);
    GCMUtil.asBytes(arrayOfLong, paramArrayOfbyte);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/gcm/BasicGCMMultiplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */