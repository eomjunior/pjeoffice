package org.bouncycastle.crypto;

public interface Signer {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters);
  
  void update(byte paramByte);
  
  void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  byte[] generateSignature() throws CryptoException, DataLengthException;
  
  boolean verifySignature(byte[] paramArrayOfbyte);
  
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/Signer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */