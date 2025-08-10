package org.bouncycastle.crypto.signers;

import java.io.IOException;
import java.math.BigInteger;

public interface DSAEncoding {
  BigInteger[] decode(BigInteger paramBigInteger, byte[] paramArrayOfbyte) throws IOException;
  
  byte[] encode(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/signers/DSAEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */