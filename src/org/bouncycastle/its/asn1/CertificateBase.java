package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class CertificateBase extends ASN1Object {
  private CertificateType type;
  
  private byte[] version;
  
  protected CertificateBase(ASN1Sequence paramASN1Sequence) {}
  
  public static CertificateBase getInstance(Object paramObject) {
    if (paramObject instanceof ImplicitCertificate)
      return (ImplicitCertificate)paramObject; 
    if (paramObject instanceof ExplicitCertificate)
      return (ExplicitCertificate)paramObject; 
    if (paramObject != null) {
      ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramObject);
      if (aSN1Sequence.getObjectAt(1).equals(CertificateType.Implicit))
        return ImplicitCertificate.getInstance(aSN1Sequence); 
      if (aSN1Sequence.getObjectAt(1).equals(CertificateType.Explicit))
        return ExplicitCertificate.getInstance(aSN1Sequence); 
      throw new IllegalArgumentException("unknown certificate type");
    } 
    return null;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/CertificateBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */