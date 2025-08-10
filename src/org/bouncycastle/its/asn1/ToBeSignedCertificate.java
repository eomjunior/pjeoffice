package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

public class ToBeSignedCertificate extends ASN1Object {
  private ToBeSignedCertificate(ASN1Sequence paramASN1Sequence) {}
  
  public static ToBeSignedCertificate getInstance(Object paramObject) {
    return (paramObject instanceof ToBeSignedCertificate) ? (ToBeSignedCertificate)paramObject : ((paramObject != null) ? new ToBeSignedCertificate(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    return null;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/ToBeSignedCertificate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */