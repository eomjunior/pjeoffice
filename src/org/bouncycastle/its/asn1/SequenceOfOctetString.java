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

public class SequenceOfOctetString extends ASN1Object {
  private byte[][] octetStrings;
  
  private SequenceOfOctetString(ASN1Sequence paramASN1Sequence) {
    this.octetStrings = toByteArrays(paramASN1Sequence);
  }
  
  public static SequenceOfOctetString getInstance(Object paramObject) {
    return (paramObject instanceof SequenceOfOctetString) ? (SequenceOfOctetString)paramObject : ((paramObject != null) ? new SequenceOfOctetString(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public int size() {
    return this.octetStrings.length;
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    for (byte b = 0; b != this.octetStrings.length; b++)
      aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(Arrays.clone(this.octetStrings[b]))); 
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
  }
  
  static byte[][] toByteArrays(ASN1Sequence paramASN1Sequence) {
    byte[][] arrayOfByte = new byte[paramASN1Sequence.size()][];
    for (byte b = 0; b != paramASN1Sequence.size(); b++)
      arrayOfByte[b] = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(b)).getOctets(); 
    return arrayOfByte;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/SequenceOfOctetString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */