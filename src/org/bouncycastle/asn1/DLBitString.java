package org.bouncycastle.asn1;

import java.io.IOException;

public class DLBitString extends ASN1BitString {
  public static ASN1BitString getInstance(Object paramObject) {
    if (paramObject == null || paramObject instanceof DLBitString)
      return (DLBitString)paramObject; 
    if (paramObject instanceof DERBitString)
      return (DERBitString)paramObject; 
    if (paramObject instanceof byte[])
      try {
        return (ASN1BitString)fromByteArray((byte[])paramObject);
      } catch (Exception exception) {
        throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
      }  
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }
  
  public static ASN1BitString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
    return (paramBoolean || aSN1Primitive instanceof DLBitString) ? getInstance(aSN1Primitive) : fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
  }
  
  protected DLBitString(byte paramByte, int paramInt) {
    super(paramByte, paramInt);
  }
  
  public DLBitString(byte[] paramArrayOfbyte, int paramInt) {
    super(paramArrayOfbyte, paramInt);
  }
  
  public DLBitString(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, 0);
  }
  
  public DLBitString(int paramInt) {
    super(getBytes(paramInt), getPadBits(paramInt));
  }
  
  public DLBitString(ASN1Encodable paramASN1Encodable) throws IOException {
    super(paramASN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
  }
  
  boolean isConstructed() {
    return false;
  }
  
  int encodedLength() {
    return 1 + StreamUtil.calculateBodyLength(this.data.length + 1) + this.data.length + 1;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    paramASN1OutputStream.writeEncoded(paramBoolean, 3, (byte)this.padBits, this.data);
  }
  
  ASN1Primitive toDLObject() {
    return this;
  }
  
  static DLBitString fromOctetString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 1)
      throw new IllegalArgumentException("truncated BIT STRING detected"); 
    byte b = paramArrayOfbyte[0];
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length - 1];
    if (arrayOfByte.length != 0)
      System.arraycopy(paramArrayOfbyte, 1, arrayOfByte, 0, paramArrayOfbyte.length - 1); 
    return new DLBitString(arrayOfByte, b);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DLBitString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */