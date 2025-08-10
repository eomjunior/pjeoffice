package org.bouncycastle.tsp;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.tsp.PartialHashtree;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.util.Arrays;

public class PartialHashTreeProcessor {
  private final byte[][] values;
  
  public PartialHashTreeProcessor(PartialHashtree paramPartialHashtree) {
    this.values = paramPartialHashtree.getValues();
  }
  
  public byte[] getHash(DigestCalculator paramDigestCalculator) {
    if (this.values.length == 1)
      return this.values[0]; 
    try {
      OutputStream outputStream = paramDigestCalculator.getOutputStream();
      for (byte b = 1; b != this.values.length; b++)
        outputStream.write(this.values[b]); 
      return paramDigestCalculator.getDigest();
    } catch (IOException iOException) {
      throw new IllegalStateException("calculator failed: " + iOException.getMessage());
    } 
  }
  
  public void verifyContainsHash(byte[] paramArrayOfbyte) throws PartialHashTreeVerificationException {
    if (!containsHash(paramArrayOfbyte))
      throw new PartialHashTreeVerificationException("calculated hash is not present in partial hash tree"); 
  }
  
  public boolean containsHash(byte[] paramArrayOfbyte) {
    for (byte b = 1; b != this.values.length; b++) {
      if (Arrays.areEqual(paramArrayOfbyte, this.values[b]))
        return true; 
    } 
    return false;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/tsp/PartialHashTreeProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */