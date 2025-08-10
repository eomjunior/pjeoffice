package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

public class BasicGCMExponentiator implements GCMExponentiator {
  private long[] x;
  
  public void init(byte[] paramArrayOfbyte) {
    this.x = GCMUtil.asLongs(paramArrayOfbyte);
  }
  
  public void exponentiateX(long paramLong, byte[] paramArrayOfbyte) {
    long[] arrayOfLong = GCMUtil.oneAsLongs();
    if (paramLong > 0L) {
      long[] arrayOfLong1 = Arrays.clone(this.x);
      do {
        if ((paramLong & 0x1L) != 0L)
          GCMUtil.multiply(arrayOfLong, arrayOfLong1); 
        GCMUtil.square(arrayOfLong1, arrayOfLong1);
        paramLong >>>= 1L;
      } while (paramLong > 0L);
    } 
    GCMUtil.asBytes(arrayOfLong, paramArrayOfbyte);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/gcm/BasicGCMExponentiator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */