package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.GeneralName;

public class SinglePubInfo extends ASN1Object {
  public static final ASN1Integer dontCare = new ASN1Integer(0L);
  
  public static final ASN1Integer x500 = new ASN1Integer(1L);
  
  public static final ASN1Integer web = new ASN1Integer(2L);
  
  public static final ASN1Integer ldap = new ASN1Integer(3L);
  
  private ASN1Integer pubMethod;
  
  private GeneralName pubLocation;
  
  private SinglePubInfo(ASN1Sequence paramASN1Sequence) {
    this.pubMethod = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
      this.pubLocation = GeneralName.getInstance(paramASN1Sequence.getObjectAt(1)); 
  }
  
  public static SinglePubInfo getInstance(Object paramObject) {
    return (paramObject instanceof SinglePubInfo) ? (SinglePubInfo)paramObject : ((paramObject != null) ? new SinglePubInfo(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public SinglePubInfo(ASN1Integer paramASN1Integer, GeneralName paramGeneralName) {
    this.pubMethod = paramASN1Integer;
    this.pubLocation = paramGeneralName;
  }
  
  public ASN1Integer getPubMethod() {
    return this.pubMethod;
  }
  
  public GeneralName getPubLocation() {
    return this.pubLocation;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)this.pubMethod);
    if (this.pubLocation != null)
      aSN1EncodableVector.add((ASN1Encodable)this.pubLocation); 
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/crmf/SinglePubInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */