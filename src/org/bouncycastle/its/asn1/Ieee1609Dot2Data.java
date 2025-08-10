package org.bouncycastle.its.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class Ieee1609Dot2Data extends ASN1Object {
  private final BigInteger protcolVersion;
  
  private final Ieee1609Dot2Content content;
  
  private Ieee1609Dot2Data(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("sequence not length 2"); 
    this.protcolVersion = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0)).getValue();
    this.content = Ieee1609Dot2Content.getInstance(paramASN1Sequence.getObjectAt(1));
  }
  
  public static Ieee1609Dot2Data getInstance(Object paramObject) {
    return (paramObject instanceof Ieee1609Dot2Data) ? (Ieee1609Dot2Data)paramObject : ((paramObject != null) ? new Ieee1609Dot2Data(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/Ieee1609Dot2Data.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */