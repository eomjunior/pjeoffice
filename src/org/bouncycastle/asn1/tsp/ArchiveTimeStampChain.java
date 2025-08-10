package org.bouncycastle.asn1.tsp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class ArchiveTimeStampChain extends ASN1Object {
  private ASN1Sequence archiveTimestamps;
  
  public static ArchiveTimeStampChain getInstance(Object paramObject) {
    return (paramObject instanceof ArchiveTimeStampChain) ? (ArchiveTimeStampChain)paramObject : ((paramObject != null) ? new ArchiveTimeStampChain(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ArchiveTimeStampChain(ArchiveTimeStamp paramArchiveTimeStamp) {
    this.archiveTimestamps = (ASN1Sequence)new DERSequence((ASN1Encodable)paramArchiveTimeStamp);
  }
  
  public ArchiveTimeStampChain(ArchiveTimeStamp[] paramArrayOfArchiveTimeStamp) {
    this.archiveTimestamps = (ASN1Sequence)new DERSequence((ASN1Encodable[])paramArrayOfArchiveTimeStamp);
  }
  
  private ArchiveTimeStampChain(ASN1Sequence paramASN1Sequence) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(paramASN1Sequence.size());
    Enumeration enumeration = paramASN1Sequence.getObjects();
    while (enumeration.hasMoreElements())
      aSN1EncodableVector.add((ASN1Encodable)ArchiveTimeStamp.getInstance(enumeration.nextElement())); 
    this.archiveTimestamps = (ASN1Sequence)new DERSequence(aSN1EncodableVector);
  }
  
  public ArchiveTimeStamp[] getArchiveTimestamps() {
    ArchiveTimeStamp[] arrayOfArchiveTimeStamp = new ArchiveTimeStamp[this.archiveTimestamps.size()];
    for (byte b = 0; b != arrayOfArchiveTimeStamp.length; b++)
      arrayOfArchiveTimeStamp[b] = ArchiveTimeStamp.getInstance(this.archiveTimestamps.getObjectAt(b)); 
    return arrayOfArchiveTimeStamp;
  }
  
  public ArchiveTimeStampChain append(ArchiveTimeStamp paramArchiveTimeStamp) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(this.archiveTimestamps.size() + 1);
    for (byte b = 0; b != this.archiveTimestamps.size(); b++)
      aSN1EncodableVector.add(this.archiveTimestamps.getObjectAt(b)); 
    aSN1EncodableVector.add((ASN1Encodable)paramArchiveTimeStamp);
    return new ArchiveTimeStampChain((ASN1Sequence)new DERSequence(aSN1EncodableVector));
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.archiveTimestamps;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/tsp/ArchiveTimeStampChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */