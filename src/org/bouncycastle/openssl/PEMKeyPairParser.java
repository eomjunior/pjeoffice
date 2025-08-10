package org.bouncycastle.openssl;

import java.io.IOException;

interface PEMKeyPairParser {
  PEMKeyPair parse(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/PEMKeyPairParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */