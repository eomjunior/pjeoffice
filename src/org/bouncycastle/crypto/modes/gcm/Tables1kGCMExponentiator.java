package org.bouncycastle.crypto.modes.gcm;

import java.util.Vector;
import org.bouncycastle.util.Arrays;

public class Tables1kGCMExponentiator implements GCMExponentiator {
  private Vector lookupPowX2;
  
  public void init(byte[] paramArrayOfbyte) {
    long[] arrayOfLong = GCMUtil.asLongs(paramArrayOfbyte);
    if (this.lookupPowX2 != null && Arrays.areEqual(arrayOfLong, this.lookupPowX2.elementAt(0)))
      return; 
    this.lookupPowX2 = new Vector(8);
    this.lookupPowX2.addElement(arrayOfLong);
  }
  
  public void exponentiateX(long paramLong, byte[] paramArrayOfbyte) {
    long[] arrayOfLong = GCMUtil.oneAsLongs();
    byte b = 0;
    while (paramLong > 0L) {
      if ((paramLong & 0x1L) != 0L) {
        ensureAvailable(b);
        GCMUtil.multiply(arrayOfLong, this.lookupPowX2.elementAt(b));
      } 
      b++;
      paramLong >>>= 1L;
    } 
    GCMUtil.asBytes(arrayOfLong, paramArrayOfbyte);
  }
  
  private void ensureAvailable(int paramInt) {
    int i = this.lookupPowX2.size();
    if (i <= paramInt) {
      long[] arrayOfLong = this.lookupPowX2.elementAt(i - 1);
      do {
        arrayOfLong = Arrays.clone(arrayOfLong);
        GCMUtil.square(arrayOfLong, arrayOfLong);
        this.lookupPowX2.addElement(arrayOfLong);
      } while (++i <= paramInt);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/modes/gcm/Tables1kGCMExponentiator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */