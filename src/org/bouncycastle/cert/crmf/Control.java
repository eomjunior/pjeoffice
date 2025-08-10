package org.bouncycastle.cert.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface Control {
  ASN1ObjectIdentifier getType();
  
  ASN1Encodable getValue();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/crmf/Control.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */