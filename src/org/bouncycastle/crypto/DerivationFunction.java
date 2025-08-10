package org.bouncycastle.crypto;

public interface DerivationFunction {
  void init(DerivationParameters paramDerivationParameters);
  
  int generateBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws DataLengthException, IllegalArgumentException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/DerivationFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */