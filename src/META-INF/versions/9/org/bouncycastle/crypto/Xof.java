package META-INF.versions.9.org.bouncycastle.crypto;

import org.bouncycastle.crypto.ExtendedDigest;

public interface Xof extends ExtendedDigest {
  int doFinal(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int doOutput(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/Xof.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */