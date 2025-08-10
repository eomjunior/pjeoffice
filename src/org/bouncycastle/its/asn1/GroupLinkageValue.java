package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class GroupLinkageValue extends ASN1Object {
  private byte[] jValue;
  
  private byte[] value;
  
  private GroupLinkageValue(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("sequence not length 2"); 
    this.jValue = Utils.octetStringFixed(ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)).getOctets(), 4);
    this.value = Utils.octetStringFixed(ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets(), 9);
  }
  
  public static GroupLinkageValue getInstance(Object paramObject) {
    return (paramObject instanceof GroupLinkageValue) ? (GroupLinkageValue)paramObject : ((paramObject != null) ? new GroupLinkageValue(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public byte[] getJValue() {
    return this.jValue;
  }
  
  public byte[] getValue() {
    return this.value;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.jValue));
    aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.value));
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/GroupLinkageValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */