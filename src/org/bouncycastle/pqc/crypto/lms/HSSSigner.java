package org.bouncycastle.pqc.crypto.lms;

import java.io.IOException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.pqc.crypto.MessageSigner;

public class HSSSigner implements MessageSigner {
  private HSSPrivateKeyParameters privKey;
  
  private HSSPublicKeyParameters pubKey;
  
  public void init(boolean paramBoolean, CipherParameters paramCipherParameters) {
    if (paramBoolean) {
      this.privKey = (HSSPrivateKeyParameters)paramCipherParameters;
    } else {
      this.pubKey = (HSSPublicKeyParameters)paramCipherParameters;
    } 
  }
  
  public byte[] generateSignature(byte[] paramArrayOfbyte) {
    try {
      return HSS.generateSignature(this.privKey, paramArrayOfbyte).getEncoded();
    } catch (IOException iOException) {
      throw new IllegalStateException("unable to encode signature: " + iOException.getMessage());
    } 
  }
  
  public boolean verifySignature(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    try {
      return HSS.verifySignature(this.pubKey, HSSSignature.getInstance(paramArrayOfbyte2, this.pubKey.getL()), paramArrayOfbyte1);
    } catch (IOException iOException) {
      throw new IllegalStateException("unable to decode signature: " + iOException.getMessage());
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/HSSSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */