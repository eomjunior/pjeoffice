package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Primitive;

public class CertificateType {
  public static final CertificateType Explicit = new CertificateType(0);
  
  public static final CertificateType Implicit = new CertificateType(1);
  
  private final ASN1Enumerated enumerated;
  
  protected CertificateType(int paramInt) {
    this.enumerated = new ASN1Enumerated(paramInt);
  }
  
  private CertificateType(ASN1Enumerated paramASN1Enumerated) {
    this.enumerated = paramASN1Enumerated;
  }
  
  public CertificateType getInstance(Object paramObject) {
    return (paramObject == null) ? null : ((paramObject instanceof CertificateType) ? (CertificateType)paramObject : new CertificateType(ASN1Enumerated.getInstance(paramObject)));
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.enumerated;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/CertificateType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */