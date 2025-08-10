package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class SequenceOfRectangularRegion extends ASN1Object {
  private final RectangularRegion[] sequence;
  
  private SequenceOfRectangularRegion(ASN1Sequence paramASN1Sequence) {
    this.sequence = new RectangularRegion[paramASN1Sequence.size()];
    for (byte b = 0; b != paramASN1Sequence.size(); b++)
      this.sequence[b] = RectangularRegion.getInstance(paramASN1Sequence.getObjectAt(b)); 
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)new DERSequence((ASN1Encodable[])this.sequence);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/SequenceOfRectangularRegion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */