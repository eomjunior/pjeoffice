package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

class LazyEncodedSequence extends ASN1Sequence {
  private byte[] encoded;
  
  LazyEncodedSequence(byte[] paramArrayOfbyte) throws IOException {
    this.encoded = paramArrayOfbyte;
  }
  
  public synchronized ASN1Encodable getObjectAt(int paramInt) {
    force();
    return super.getObjectAt(paramInt);
  }
  
  public synchronized Enumeration getObjects() {
    return (null != this.encoded) ? new LazyConstructionEnumeration(this.encoded) : super.getObjects();
  }
  
  public synchronized int hashCode() {
    force();
    return super.hashCode();
  }
  
  public synchronized Iterator<ASN1Encodable> iterator() {
    force();
    return super.iterator();
  }
  
  public synchronized int size() {
    force();
    return super.size();
  }
  
  public synchronized ASN1Encodable[] toArray() {
    force();
    return super.toArray();
  }
  
  ASN1Encodable[] toArrayInternal() {
    force();
    return super.toArrayInternal();
  }
  
  synchronized int encodedLength() throws IOException {
    return (null != this.encoded) ? (1 + StreamUtil.calculateBodyLength(this.encoded.length) + this.encoded.length) : super.toDLObject().encodedLength();
  }
  
  synchronized void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    if (null != this.encoded) {
      paramASN1OutputStream.writeEncoded(paramBoolean, 48, this.encoded);
    } else {
      super.toDLObject().encode(paramASN1OutputStream, paramBoolean);
    } 
  }
  
  synchronized ASN1Primitive toDERObject() {
    force();
    return super.toDERObject();
  }
  
  synchronized ASN1Primitive toDLObject() {
    force();
    return super.toDLObject();
  }
  
  private void force() {
    if (null != this.encoded) {
      ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
      LazyConstructionEnumeration lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
      while (lazyConstructionEnumeration.hasMoreElements())
        aSN1EncodableVector.add(lazyConstructionEnumeration.nextElement()); 
      this.elements = aSN1EncodableVector.takeElements();
      this.encoded = null;
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/LazyEncodedSequence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */