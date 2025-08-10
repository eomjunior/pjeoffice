package org.bouncycastle.crypto;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface KeyParser {
  AsymmetricKeyParameter readKey(InputStream paramInputStream) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/KeyParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */