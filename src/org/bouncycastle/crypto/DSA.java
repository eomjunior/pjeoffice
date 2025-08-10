package org.bouncycastle.crypto;

import java.math.BigInteger;

public interface DSA {
  void init(boolean paramBoolean, CipherParameters paramCipherParameters);
  
  BigInteger[] generateSignature(byte[] paramArrayOfbyte);
  
  boolean verifySignature(byte[] paramArrayOfbyte, BigInteger paramBigInteger1, BigInteger paramBigInteger2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/DSA.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */