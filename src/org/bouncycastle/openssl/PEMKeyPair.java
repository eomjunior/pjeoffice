package org.bouncycastle.openssl;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

public class PEMKeyPair {
  private final SubjectPublicKeyInfo publicKeyInfo;
  
  private final PrivateKeyInfo privateKeyInfo;
  
  public PEMKeyPair(SubjectPublicKeyInfo paramSubjectPublicKeyInfo, PrivateKeyInfo paramPrivateKeyInfo) {
    this.publicKeyInfo = paramSubjectPublicKeyInfo;
    this.privateKeyInfo = paramPrivateKeyInfo;
  }
  
  public PrivateKeyInfo getPrivateKeyInfo() {
    return this.privateKeyInfo;
  }
  
  public SubjectPublicKeyInfo getPublicKeyInfo() {
    return this.publicKeyInfo;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/PEMKeyPair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */