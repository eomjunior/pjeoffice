package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DLSequence;

public class EncryptionInfo extends ASN1Object {
  private ASN1ObjectIdentifier encryptionInfoType;
  
  private ASN1Encodable encryptionInfoValue;
  
  public static EncryptionInfo getInstance(ASN1Object paramASN1Object) {
    return (paramASN1Object instanceof EncryptionInfo) ? (EncryptionInfo)paramASN1Object : ((paramASN1Object != null) ? new EncryptionInfo(ASN1Sequence.getInstance(paramASN1Object)) : null);
  }
  
  public static EncryptionInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    return getInstance((ASN1Object)ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }
  
  private EncryptionInfo(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("wrong sequence size in constructor: " + paramASN1Sequence.size()); 
    this.encryptionInfoType = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.encryptionInfoValue = paramASN1Sequence.getObjectAt(1);
  }
  
  public EncryptionInfo(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable) {
    this.encryptionInfoType = paramASN1ObjectIdentifier;
    this.encryptionInfoValue = paramASN1Encodable;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)this.encryptionInfoType);
    aSN1EncodableVector.add(this.encryptionInfoValue);
    return (ASN1Primitive)new DLSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/tsp/EncryptionInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */