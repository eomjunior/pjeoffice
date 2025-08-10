package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public class PolicyQualifierId extends ASN1ObjectIdentifier {
  private static final String id_qt = "1.3.6.1.5.5.7.2";
  
  public static final PolicyQualifierId id_qt_cps = new PolicyQualifierId("1.3.6.1.5.5.7.2.1");
  
  public static final PolicyQualifierId id_qt_unotice = new PolicyQualifierId("1.3.6.1.5.5.7.2.2");
  
  private PolicyQualifierId(String paramString) {
    super(paramString);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/x509/PolicyQualifierId.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */