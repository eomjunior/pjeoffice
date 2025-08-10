package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class OriginatorPublicKey extends ASN1Object {
  private AlgorithmIdentifier algorithm;
  
  private DERBitString publicKey;
  
  public OriginatorPublicKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
    this.algorithm = paramAlgorithmIdentifier;
    this.publicKey = new DERBitString(paramArrayOfbyte);
  }
  
  public OriginatorPublicKey(ASN1Sequence paramASN1Sequence) {
    this.algorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.publicKey = (DERBitString)paramASN1Sequence.getObjectAt(1);
  }
  
  public static OriginatorPublicKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }
  
  public static OriginatorPublicKey getInstance(Object paramObject) {
    return (paramObject instanceof OriginatorPublicKey) ? (OriginatorPublicKey)paramObject : ((paramObject != null) ? new OriginatorPublicKey(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public AlgorithmIdentifier getAlgorithm() {
    return this.algorithm;
  }
  
  public DERBitString getPublicKey() {
    return this.publicKey;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)this.algorithm);
    aSN1EncodableVector.add((ASN1Encodable)this.publicKey);
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/cms/OriginatorPublicKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */