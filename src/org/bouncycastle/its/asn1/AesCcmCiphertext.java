package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class AesCcmCiphertext extends ASN1Object {
  private final byte[] nonce;
  
  private final SequenceOfOctetString opaque;
  
  private AesCcmCiphertext(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("sequence not length 2"); 
    this.nonce = Utils.octetStringFixed(ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)).getOctets(), 12);
    this.opaque = SequenceOfOctetString.getInstance(paramASN1Sequence.getObjectAt(1));
  }
  
  public static AesCcmCiphertext getInstance(Object paramObject) {
    return (paramObject instanceof AesCcmCiphertext) ? (AesCcmCiphertext)paramObject : ((paramObject != null) ? new AesCcmCiphertext(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.nonce));
    aSN1EncodableVector.add((ASN1Encodable)this.opaque);
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/AesCcmCiphertext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */