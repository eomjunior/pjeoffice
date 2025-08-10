package org.bouncycastle.crypto.util;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public abstract class PBKDFConfig {
  private final ASN1ObjectIdentifier algorithm;
  
  protected PBKDFConfig(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    this.algorithm = paramASN1ObjectIdentifier;
  }
  
  public ASN1ObjectIdentifier getAlgorithm() {
    return this.algorithm;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/util/PBKDFConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */