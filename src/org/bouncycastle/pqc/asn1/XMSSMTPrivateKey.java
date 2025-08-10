package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class XMSSMTPrivateKey extends ASN1Object {
  private final int version;
  
  private final long index;
  
  private final long maxIndex;
  
  private final byte[] secretKeySeed;
  
  private final byte[] secretKeyPRF;
  
  private final byte[] publicSeed;
  
  private final byte[] root;
  
  private final byte[] bdsState;
  
  public XMSSMTPrivateKey(long paramLong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte[] paramArrayOfbyte5) {
    this.version = 0;
    this.index = paramLong;
    this.secretKeySeed = Arrays.clone(paramArrayOfbyte1);
    this.secretKeyPRF = Arrays.clone(paramArrayOfbyte2);
    this.publicSeed = Arrays.clone(paramArrayOfbyte3);
    this.root = Arrays.clone(paramArrayOfbyte4);
    this.bdsState = Arrays.clone(paramArrayOfbyte5);
    this.maxIndex = -1L;
  }
  
  public XMSSMTPrivateKey(long paramLong1, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte[] paramArrayOfbyte5, long paramLong2) {
    this.version = 1;
    this.index = paramLong1;
    this.secretKeySeed = Arrays.clone(paramArrayOfbyte1);
    this.secretKeyPRF = Arrays.clone(paramArrayOfbyte2);
    this.publicSeed = Arrays.clone(paramArrayOfbyte3);
    this.root = Arrays.clone(paramArrayOfbyte4);
    this.bdsState = Arrays.clone(paramArrayOfbyte5);
    this.maxIndex = paramLong2;
  }
  
  private XMSSMTPrivateKey(ASN1Sequence paramASN1Sequence) {
    ASN1Integer aSN1Integer = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
    if (!aSN1Integer.hasValue(BigIntegers.ZERO) && !aSN1Integer.hasValue(BigIntegers.ONE))
      throw new IllegalArgumentException("unknown version of sequence"); 
    this.version = aSN1Integer.intValueExact();
    if (paramASN1Sequence.size() != 2 && paramASN1Sequence.size() != 3)
      throw new IllegalArgumentException("key sequence wrong size"); 
    ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
    this.index = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).longValueExact();
    this.secretKeySeed = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(1)).getOctets());
    this.secretKeyPRF = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(2)).getOctets());
    this.publicSeed = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(3)).getOctets());
    this.root = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(4)).getOctets());
    if (aSN1Sequence.size() == 6) {
      ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(5));
      if (aSN1TaggedObject.getTagNo() != 0)
        throw new IllegalArgumentException("unknown tag in XMSSPrivateKey"); 
      this.maxIndex = ASN1Integer.getInstance(aSN1TaggedObject, false).longValueExact();
    } else if (aSN1Sequence.size() == 5) {
      this.maxIndex = -1L;
    } else {
      throw new IllegalArgumentException("keySeq should be 5 or 6 in length");
    } 
    if (paramASN1Sequence.size() == 3) {
      this.bdsState = Arrays.clone(DEROctetString.getInstance(ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(2)), true).getOctets());
    } else {
      this.bdsState = null;
    } 
  }
  
  public static XMSSMTPrivateKey getInstance(Object paramObject) {
    return (paramObject instanceof XMSSMTPrivateKey) ? (XMSSMTPrivateKey)paramObject : ((paramObject != null) ? new XMSSMTPrivateKey(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public int getVersion() {
    return this.version;
  }
  
  public long getIndex() {
    return this.index;
  }
  
  public long getMaxIndex() {
    return this.maxIndex;
  }
  
  public byte[] getSecretKeySeed() {
    return Arrays.clone(this.secretKeySeed);
  }
  
  public byte[] getSecretKeyPRF() {
    return Arrays.clone(this.secretKeyPRF);
  }
  
  public byte[] getPublicSeed() {
    return Arrays.clone(this.publicSeed);
  }
  
  public byte[] getRoot() {
    return Arrays.clone(this.root);
  }
  
  public byte[] getBdsState() {
    return Arrays.clone(this.bdsState);
  }
  
  public ASN1Primitive toASN1Primitive() {
    ASN1EncodableVector aSN1EncodableVector1 = new ASN1EncodableVector();
    if (this.maxIndex >= 0L) {
      aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(1L));
    } else {
      aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(0L));
    } 
    ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
    aSN1EncodableVector2.add((ASN1Encodable)new ASN1Integer(this.index));
    aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.secretKeySeed));
    aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.secretKeyPRF));
    aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.publicSeed));
    aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.root));
    if (this.maxIndex >= 0L)
      aSN1EncodableVector2.add((ASN1Encodable)new DERTaggedObject(false, 0, (ASN1Encodable)new ASN1Integer(this.maxIndex))); 
    aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector2));
    aSN1EncodableVector1.add((ASN1Encodable)new DERTaggedObject(true, 0, (ASN1Encodable)new DEROctetString(this.bdsState)));
    return (ASN1Primitive)new DERSequence(aSN1EncodableVector1);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/asn1/XMSSMTPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */