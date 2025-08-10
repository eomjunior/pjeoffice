package org.bouncycastle.jcajce.provider.digest;

import java.security.MessageDigest;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Xof;

public class BCMessageDigest extends MessageDigest {
  protected Digest digest;
  
  protected int digestSize;
  
  protected BCMessageDigest(Digest paramDigest) {
    super(paramDigest.getAlgorithmName());
    this.digest = paramDigest;
    this.digestSize = paramDigest.getDigestSize();
  }
  
  protected BCMessageDigest(Xof paramXof, int paramInt) {
    super(paramXof.getAlgorithmName());
    this.digest = (Digest)paramXof;
    this.digestSize = paramInt / 8;
  }
  
  public void engineReset() {
    this.digest.reset();
  }
  
  public void engineUpdate(byte paramByte) {
    this.digest.update(paramByte);
  }
  
  public void engineUpdate(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.digest.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public int engineGetDigestLength() {
    return this.digestSize;
  }
  
  public byte[] engineDigest() {
    byte[] arrayOfByte = new byte[this.digestSize];
    this.digest.doFinal(arrayOfByte, 0);
    return arrayOfByte;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/digest/BCMessageDigest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */