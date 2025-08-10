package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public class NonMemoableDigest implements ExtendedDigest {
  private ExtendedDigest baseDigest;
  
  public NonMemoableDigest(ExtendedDigest paramExtendedDigest) {
    if (paramExtendedDigest == null)
      throw new IllegalArgumentException("baseDigest must not be null"); 
    this.baseDigest = paramExtendedDigest;
  }
  
  public String getAlgorithmName() {
    return this.baseDigest.getAlgorithmName();
  }
  
  public int getDigestSize() {
    return this.baseDigest.getDigestSize();
  }
  
  public void update(byte paramByte) {
    this.baseDigest.update(paramByte);
  }
  
  public void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.baseDigest.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
    return this.baseDigest.doFinal(paramArrayOfbyte, paramInt);
  }
  
  public void reset() {
    this.baseDigest.reset();
  }
  
  public int getByteLength() {
    return this.baseDigest.getByteLength();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/digests/NonMemoableDigest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */