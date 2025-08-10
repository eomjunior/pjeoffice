package org.bouncycastle.asn1.cmc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class TaggedCertificationRequest extends ASN1Object {
  private final BodyPartID bodyPartID;
  
  private final CertificationRequest certificationRequest;
  
  public TaggedCertificationRequest(BodyPartID paramBodyPartID, CertificationRequest paramCertificationRequest) {
    this.bodyPartID = paramBodyPartID;
    this.certificationRequest = paramCertificationRequest;
  }
  
  private TaggedCertificationRequest(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("incorrect sequence size"); 
    this.bodyPartID = BodyPartID.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certificationRequest = CertificationRequest.getInstance(paramASN1Sequence.getObjectAt(1));
  }
  
  public static TaggedCertificationRequest getInstance(Object paramObject) {
    return (paramObject instanceof TaggedCertificationRequest) ? (TaggedCertificationRequest)paramObject : ((paramObject != null) ? new TaggedCertificationRequest(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public static TaggedCertificationRequest getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)this.bodyPartID);
    aSN1EncodableVector.add((ASN1Encodable)this.certificationRequest);
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/cmc/TaggedCertificationRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */