package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class HeaderInfo extends ASN1Object {
  private HeaderInfo(ASN1Sequence paramASN1Sequence) {}
  
  public static HeaderInfo getInstance(Object paramObject) {
    return (paramObject instanceof HeaderInfo) ? (HeaderInfo)paramObject : ((paramObject != null) ? new HeaderInfo(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/HeaderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */