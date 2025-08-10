package org.bouncycastle.asn1;

import java.io.IOException;

public class DERBitString extends ASN1BitString {
  public static DERBitString getInstance(Object paramObject) {
    if (paramObject == null || paramObject instanceof DERBitString)
      return (DERBitString)paramObject; 
    if (paramObject instanceof DLBitString)
      return new DERBitString(((DLBitString)paramObject).data, ((DLBitString)paramObject).padBits); 
    if (paramObject instanceof byte[])
      try {
        return (DERBitString)fromByteArray((byte[])paramObject);
      } catch (Exception exception) {
        throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
      }  
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }
  
  public static DERBitString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
    return (paramBoolean || aSN1Primitive instanceof DERBitString) ? getInstance(aSN1Primitive) : fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
  }
  
  protected DERBitString(byte paramByte, int paramInt) {
    super(paramByte, paramInt);
  }
  
  public DERBitString(byte[] paramArrayOfbyte, int paramInt) {
    super(paramArrayOfbyte, paramInt);
  }
  
  public DERBitString(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, 0);
  }
  
  public DERBitString(int paramInt) {
    super(getBytes(paramInt), getPadBits(paramInt));
  }
  
  public DERBitString(ASN1Encodable paramASN1Encodable) throws IOException {
    super(paramASN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
  }
  
  boolean isConstructed() {
    return false;
  }
  
  int encodedLength() {
    return 1 + StreamUtil.calculateBodyLength(this.data.length + 1) + this.data.length + 1;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    int i = this.data.length;
    if (0 == i || 0 == this.padBits || this.data[i - 1] == (byte)(this.data[i - 1] & 255 << this.padBits)) {
      paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data);
    } else {
      byte b = (byte)(this.data[i - 1] & 255 << this.padBits);
      paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data, 0, i - 1, b);
    } 
  }
  
  ASN1Primitive toDERObject() {
    return this;
  }
  
  ASN1Primitive toDLObject() {
    return this;
  }
  
  static DERBitString fromOctetString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 1)
      throw new IllegalArgumentException("truncated BIT STRING detected"); 
    byte b = paramArrayOfbyte[0];
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length - 1];
    if (arrayOfByte.length != 0)
      System.arraycopy(paramArrayOfbyte, 1, arrayOfByte, 0, paramArrayOfbyte.length - 1); 
    return new DERBitString(arrayOfByte, b);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DERBitString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */