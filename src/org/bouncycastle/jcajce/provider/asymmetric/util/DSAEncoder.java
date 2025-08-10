package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.io.IOException;
import java.math.BigInteger;

public interface DSAEncoder {
  byte[] encode(BigInteger paramBigInteger1, BigInteger paramBigInteger2) throws IOException;
  
  BigInteger[] decode(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/util/DSAEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */