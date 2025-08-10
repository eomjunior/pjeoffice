package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class Ieee1609Dot2Content extends ASN1Object implements ASN1Choice {
  public static Ieee1609Dot2Content getInstance(Object paramObject) {
    return (paramObject instanceof Ieee1609Dot2Content) ? (Ieee1609Dot2Content)paramObject : ((paramObject != null) ? getInstance(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/Ieee1609Dot2Content.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */