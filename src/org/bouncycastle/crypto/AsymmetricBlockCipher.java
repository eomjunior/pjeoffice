package org.bouncycastle.crypto;

public interface AsymmetricBlockCipher {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters);
  
  int getInputBlockSize();
  
  int getOutputBlockSize();
  
  byte[] processBlock(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws InvalidCipherTextException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/AsymmetricBlockCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */