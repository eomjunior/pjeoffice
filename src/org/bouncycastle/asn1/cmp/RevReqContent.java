package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class RevReqContent extends ASN1Object {
  private ASN1Sequence content;
  
  private RevReqContent(ASN1Sequence paramASN1Sequence) {
    this.content = paramASN1Sequence;
  }
  
  public static RevReqContent getInstance(Object paramObject) {
    return (paramObject instanceof RevReqContent) ? (RevReqContent)paramObject : ((paramObject != null) ? new RevReqContent(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public RevReqContent(RevDetails paramRevDetails) {
    this.content = (ASN1Sequence)new DERSequence((ASN1Encodable)paramRevDetails);
  }
  
  public RevReqContent(RevDetails[] paramArrayOfRevDetails) {
    this.content = (ASN1Sequence)new DERSequence((ASN1Encodable[])paramArrayOfRevDetails);
  }
  
  public RevDetails[] toRevDetailsArray() {
    RevDetails[] arrayOfRevDetails = new RevDetails[this.content.size()];
    for (byte b = 0; b != arrayOfRevDetails.length; b++)
      arrayOfRevDetails[b] = RevDetails.getInstance(this.content.getObjectAt(b)); 
    return arrayOfRevDetails;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.content;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/cmp/RevReqContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */