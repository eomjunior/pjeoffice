package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

public class SMIMECapabilityVector {
  private ASN1EncodableVector capabilities = new ASN1EncodableVector();
  
  public void addCapability(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    this.capabilities.add((ASN1Encodable)new DERSequence((ASN1Encodable)paramASN1ObjectIdentifier));
  }
  
  public void addCapability(ASN1ObjectIdentifier paramASN1ObjectIdentifier, int paramInt) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)paramASN1ObjectIdentifier);
    aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(paramInt));
    this.capabilities.add((ASN1Encodable)new DERSequence(aSN1EncodableVector));
  }
  
  public void addCapability(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
    aSN1EncodableVector.add((ASN1Encodable)paramASN1ObjectIdentifier);
    aSN1EncodableVector.add(paramASN1Encodable);
    this.capabilities.add((ASN1Encodable)new DERSequence(aSN1EncodableVector));
  }
  
  public ASN1EncodableVector toASN1EncodableVector() {
    return this.capabilities;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/smime/SMIMECapabilityVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */