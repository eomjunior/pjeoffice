package org.bouncycastle.operator;

import java.io.InputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface InputDecryptor {
  AlgorithmIdentifier getAlgorithmIdentifier();
  
  InputStream getInputStream(InputStream paramInputStream);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/InputDecryptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */