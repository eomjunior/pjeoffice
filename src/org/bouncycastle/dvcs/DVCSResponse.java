package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.dvcs.DVCSObjectIdentifiers;
import org.bouncycastle.cms.CMSSignedData;

public class DVCSResponse extends DVCSMessage {
  private org.bouncycastle.asn1.dvcs.DVCSResponse asn1;
  
  public DVCSResponse(CMSSignedData paramCMSSignedData) throws DVCSConstructionException {
    this(SignedData.getInstance(paramCMSSignedData.toASN1Structure().getContent()).getEncapContentInfo());
  }
  
  public DVCSResponse(ContentInfo paramContentInfo) throws DVCSConstructionException {
    super(paramContentInfo);
    if (!DVCSObjectIdentifiers.id_ct_DVCSResponseData.equals((ASN1Primitive)paramContentInfo.getContentType()))
      throw new DVCSConstructionException("ContentInfo not a DVCS Response"); 
    try {
      if (paramContentInfo.getContent().toASN1Primitive() instanceof org.bouncycastle.asn1.ASN1Sequence) {
        this.asn1 = org.bouncycastle.asn1.dvcs.DVCSResponse.getInstance(paramContentInfo.getContent());
      } else {
        this.asn1 = org.bouncycastle.asn1.dvcs.DVCSResponse.getInstance(ASN1OctetString.getInstance(paramContentInfo.getContent()).getOctets());
      } 
    } catch (Exception exception) {
      throw new DVCSConstructionException("Unable to parse content: " + exception.getMessage(), exception);
    } 
  }
  
  public ASN1Encodable getContent() {
    return (ASN1Encodable)this.asn1;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/dvcs/DVCSResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */