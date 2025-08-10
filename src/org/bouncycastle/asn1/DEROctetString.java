package org.bouncycastle.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString {
  public DEROctetString(byte[] paramArrayOfbyte) {
    super(paramArrayOfbyte);
  }
  
  public DEROctetString(ASN1Encodable paramASN1Encodable) throws IOException {
    super(paramASN1Encodable.toASN1Primitive().getEncoded("DER"));
  }
  
  boolean isConstructed() {
    return false;
  }
  
  int encodedLength() {
    return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    paramASN1OutputStream.writeEncoded(paramBoolean, 4, this.string);
  }
  
  ASN1Primitive toDERObject() {
    return this;
  }
  
  ASN1Primitive toDLObject() {
    return this;
  }
  
  static void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    paramASN1OutputStream.writeEncoded(paramBoolean, 4, paramArrayOfbyte, paramInt1, paramInt2);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DEROctetString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */