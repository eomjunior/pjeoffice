package org.bouncycastle.its.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;

public class SspRange extends ASN1Object {
  private final boolean isAll = true;
  
  private final SequenceOfOctetString opaque;
  
  private final BitmapSspRange bitmapSspRange;
  
  private SspRange() {
    this.opaque = null;
    this.bitmapSspRange = null;
  }
  
  private SspRange(SequenceOfOctetString paramSequenceOfOctetString) {
    if (paramSequenceOfOctetString.size() != 2) {
      this.opaque = paramSequenceOfOctetString;
      this.bitmapSspRange = null;
    } else {
      BitmapSspRange bitmapSspRange;
      this.opaque = SequenceOfOctetString.getInstance(paramSequenceOfOctetString);
      try {
        bitmapSspRange = BitmapSspRange.getInstance(paramSequenceOfOctetString);
      } catch (IllegalArgumentException illegalArgumentException) {
        bitmapSspRange = null;
      } 
      this.bitmapSspRange = bitmapSspRange;
    } 
  }
  
  public SspRange(BitmapSspRange paramBitmapSspRange) {
    this.bitmapSspRange = paramBitmapSspRange;
    this.opaque = null;
  }
  
  public static SspRange getInstance(Object paramObject) {
    if (paramObject == null)
      return null; 
    if (paramObject instanceof SspRange)
      return (SspRange)paramObject; 
    if (paramObject instanceof org.bouncycastle.asn1.ASN1Null)
      return new SspRange(); 
    if (paramObject instanceof org.bouncycastle.asn1.ASN1Sequence)
      return new SspRange(SequenceOfOctetString.getInstance(paramObject)); 
    if (paramObject instanceof byte[])
      try {
        return getInstance(ASN1Primitive.fromByteArray((byte[])paramObject));
      } catch (IOException iOException) {
        throw new IllegalArgumentException("unable to parse encoded general name");
      }  
    throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
  }
  
  public boolean isAll() {
    return this.isAll;
  }
  
  public boolean maybeOpaque() {
    return (this.opaque != null);
  }
  
  public BitmapSspRange getBitmapSspRange() {
    return this.bitmapSspRange;
  }
  
  public SequenceOfOctetString getOpaque() {
    return this.opaque;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)(this.isAll ? DERNull.INSTANCE : ((this.bitmapSspRange != null) ? this.bitmapSspRange.toASN1Primitive() : this.opaque.toASN1Primitive()));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/SspRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */