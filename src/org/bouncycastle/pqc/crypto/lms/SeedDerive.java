package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.Digest;

class SeedDerive {
  private final byte[] I;
  
  private final byte[] masterSeed;
  
  private final Digest digest;
  
  private int q;
  
  private int j;
  
  public SeedDerive(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, Digest paramDigest) {
    this.I = paramArrayOfbyte1;
    this.masterSeed = paramArrayOfbyte2;
    this.digest = paramDigest;
  }
  
  public int getQ() {
    return this.q;
  }
  
  public void setQ(int paramInt) {
    this.q = paramInt;
  }
  
  public int getJ() {
    return this.j;
  }
  
  public void setJ(int paramInt) {
    this.j = paramInt;
  }
  
  public byte[] getI() {
    return this.I;
  }
  
  public byte[] getMasterSeed() {
    return this.masterSeed;
  }
  
  public byte[] deriveSeed(byte[] paramArrayOfbyte, int paramInt) {
    if (paramArrayOfbyte.length < this.digest.getDigestSize())
      throw new IllegalArgumentException("target length is less than digest size."); 
    this.digest.update(this.I, 0, this.I.length);
    this.digest.update((byte)(this.q >>> 24));
    this.digest.update((byte)(this.q >>> 16));
    this.digest.update((byte)(this.q >>> 8));
    this.digest.update((byte)this.q);
    this.digest.update((byte)(this.j >>> 8));
    this.digest.update((byte)this.j);
    this.digest.update((byte)-1);
    this.digest.update(this.masterSeed, 0, this.masterSeed.length);
    this.digest.doFinal(paramArrayOfbyte, paramInt);
    return paramArrayOfbyte;
  }
  
  public void deriveSeed(byte[] paramArrayOfbyte, boolean paramBoolean) {
    deriveSeed(paramArrayOfbyte, paramBoolean, 0);
  }
  
  public void deriveSeed(byte[] paramArrayOfbyte, boolean paramBoolean, int paramInt) {
    deriveSeed(paramArrayOfbyte, paramInt);
    if (paramBoolean)
      this.j++; 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/SeedDerive.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */