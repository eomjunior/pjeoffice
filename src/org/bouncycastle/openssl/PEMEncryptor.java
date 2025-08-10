package org.bouncycastle.openssl;

public interface PEMEncryptor {
  String getAlgorithm();
  
  byte[] getIV();
  
  byte[] encrypt(byte[] paramArrayOfbyte) throws PEMException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/PEMEncryptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */