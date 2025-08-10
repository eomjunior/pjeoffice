package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;

public class PartialHashtree extends ASN1Object {
  private ASN1Sequence values;
  
  public static PartialHashtree getInstance(Object paramObject) {
    return (paramObject instanceof PartialHashtree) ? (PartialHashtree)paramObject : ((paramObject != null) ? new PartialHashtree(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  private PartialHashtree(ASN1Sequence paramASN1Sequence) {
    for (byte b = 0; b != paramASN1Sequence.size(); b++) {
      if (!(paramASN1Sequence.getObjectAt(b) instanceof DEROctetString))
        throw new IllegalArgumentException("unknown object in constructor: " + paramASN1Sequence.getObjectAt(b).getClass().getName()); 
    } 
    this.values = paramASN1Sequence;
  }
  
  public PartialHashtree(byte[] paramArrayOfbyte) {
    this(new byte[][] { paramArrayOfbyte });
  }
  
  public PartialHashtree(byte[][] paramArrayOfbyte) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(paramArrayOfbyte.length);
    for (byte b = 0; b != paramArrayOfbyte.length; b++)
      aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(Arrays.clone(paramArrayOfbyte[b]))); 
    this.values = (ASN1Sequence)new DERSequence(aSN1EncodableVector);
  }
  
  public byte[][] getValues() {
    byte[][] arrayOfByte = new byte[this.values.size()][];
    for (byte b = 0; b != arrayOfByte.length; b++)
      arrayOfByte[b] = Arrays.clone(ASN1OctetString.getInstance(this.values.getObjectAt(b)).getOctets()); 
    return arrayOfByte;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.values;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/tsp/PartialHashtree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */