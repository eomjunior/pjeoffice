package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;

public class CMacWithIV extends CMac {
  public CMacWithIV(BlockCipher paramBlockCipher) {
    super(paramBlockCipher);
  }
  
  public CMacWithIV(BlockCipher paramBlockCipher, int paramInt) {
    super(paramBlockCipher, paramInt);
  }
  
  void validate(CipherParameters paramCipherParameters) {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/macs/CMacWithIV.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */