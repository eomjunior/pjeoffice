package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class OtherName extends ASN1Object {
  private final ASN1ObjectIdentifier typeID;
  
  private final ASN1Encodable value;
  
  public static OtherName getInstance(Object paramObject) {
    return (paramObject instanceof OtherName) ? (OtherName)paramObject : ((paramObject != null) ? new OtherName(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public OtherName(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable) {
    this.typeID = paramASN1ObjectIdentifier;
    this.value = paramASN1Encodable;
  }
  
  private OtherName(ASN1Sequence paramASN1Sequence) {
    this.typeID = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.value = (ASN1Encodable)ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(1)).getObject();
  }
  
  public ASN1ObjectIdentifier getTypeID() {
    return this.typeID;
  }
  
  public ASN1Encodable getValue() {
    return this.value;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)this.typeID);
    aSN1EncodableVector.add((ASN1Encodable)new DERTaggedObject(true, 0, this.value));
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/x509/OtherName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */