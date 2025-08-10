package org.bouncycastle.crypto.digests;

import org.bouncycastle.util.Memoable;

public class GOST3411_2012_512Digest extends GOST3411_2012Digest {
  private static final byte[] IV = new byte[] { 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0 };
  
  public GOST3411_2012_512Digest() {
    super(IV);
  }
  
  public GOST3411_2012_512Digest(GOST3411_2012_512Digest paramGOST3411_2012_512Digest) {
    super(IV);
    reset(paramGOST3411_2012_512Digest);
  }
  
  public String getAlgorithmName() {
    return "GOST3411-2012-512";
  }
  
  public int getDigestSize() {
    return 64;
  }
  
  public Memoable copy() {
    return new GOST3411_2012_512Digest(this);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/digests/GOST3411_2012_512Digest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */