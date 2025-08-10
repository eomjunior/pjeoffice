package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.Attribute;

public class CryptoInfos extends ASN1Object {
  private ASN1Sequence attributes;
  
  public static CryptoInfos getInstance(Object paramObject) {
    return (paramObject instanceof CryptoInfos) ? (CryptoInfos)paramObject : ((paramObject != null) ? new CryptoInfos(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public static CryptoInfos getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }
  
  private CryptoInfos(ASN1Sequence paramASN1Sequence) {
    this.attributes = paramASN1Sequence;
  }
  
  public CryptoInfos(Attribute[] paramArrayOfAttribute) {
    this.attributes = (ASN1Sequence)new DERSequence((ASN1Encodable[])paramArrayOfAttribute);
  }
  
  public Attribute[] getAttributes() {
    Attribute[] arrayOfAttribute = new Attribute[this.attributes.size()];
    for (byte b = 0; b != arrayOfAttribute.length; b++)
      arrayOfAttribute[b] = Attribute.getInstance(this.attributes.getObjectAt(b)); 
    return arrayOfAttribute;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.attributes;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/tsp/CryptoInfos.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */