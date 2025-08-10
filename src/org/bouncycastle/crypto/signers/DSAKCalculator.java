package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;

public interface DSAKCalculator {
  boolean isDeterministic();
  
  void init(BigInteger paramBigInteger, SecureRandom paramSecureRandom);
  
  void init(BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte);
  
  BigInteger nextK();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/signers/DSAKCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */