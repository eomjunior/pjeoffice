package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;

public class BitmapSspRange extends ASN1Object {
  private final byte[] sspValue;
  
  private final byte[] sspBitmask;
  
  private BitmapSspRange(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("expected sequence with sspValue and sspBitmask"); 
    this.sspValue = Utils.octetStringFixed(ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)).getOctets());
    this.sspBitmask = Utils.octetStringFixed(ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets());
  }
  
  public static BitmapSspRange getInstance(Object paramObject) {
    return (paramObject instanceof BitmapSspRange) ? (BitmapSspRange)paramObject : ((paramObject != null) ? new BitmapSspRange(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public byte[] getSspValue() {
    return Arrays.clone(this.sspValue);
  }
  
  public byte[] getSspBitmask() {
    return Arrays.clone(this.sspBitmask);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.sspValue));
    aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.sspBitmask));
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/BitmapSspRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */