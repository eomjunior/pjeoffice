package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface DigestAlgorithmIdentifierFinder {
  AlgorithmIdentifier find(AlgorithmIdentifier paramAlgorithmIdentifier);
  
  AlgorithmIdentifier find(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/DigestAlgorithmIdentifierFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */