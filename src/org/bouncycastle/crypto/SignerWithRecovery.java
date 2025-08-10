package org.bouncycastle.crypto;

public interface SignerWithRecovery extends Signer {
  boolean hasFullMessage();
  
  byte[] getRecoveredMessage();
  
  void updateWithRecoveredMessage(byte[] paramArrayOfbyte) throws InvalidCipherTextException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/SignerWithRecovery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */