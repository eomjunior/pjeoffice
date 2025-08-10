package org.bouncycastle.crypto;

public interface RawAgreement {
  void init(CipherParameters paramCipherParameters);
  
  int getAgreementSize();
  
  void calculateAgreement(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte, int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/RawAgreement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */