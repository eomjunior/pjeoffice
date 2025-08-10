package org.bouncycastle.util.io.pem;

import java.io.IOException;

public interface PemObjectParser {
  Object parseObject(PemObject paramPemObject) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/io/pem/PemObjectParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */