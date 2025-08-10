package org.bouncycastle.asn1;

import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject {
  public DERTaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
    super(paramBoolean, paramInt, paramASN1Encodable);
  }
  
  public DERTaggedObject(int paramInt, ASN1Encodable paramASN1Encodable) {
    super(true, paramInt, paramASN1Encodable);
  }
  
  boolean isConstructed() {
    return (this.explicit || this.obj.toASN1Primitive().toDERObject().isConstructed());
  }
  
  int encodedLength() throws IOException {
    ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDERObject();
    int i = aSN1Primitive.encodedLength();
    return this.explicit ? (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(i) + i) : (StreamUtil.calculateTagLength(this.tagNo) + --i);
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDERObject();
    int i = 128;
    if (this.explicit || aSN1Primitive.isConstructed())
      i |= 0x20; 
    paramASN1OutputStream.writeTag(paramBoolean, i, this.tagNo);
    if (this.explicit)
      paramASN1OutputStream.writeLength(aSN1Primitive.encodedLength()); 
    aSN1Primitive.encode(paramASN1OutputStream.getDERSubStream(), this.explicit);
  }
  
  ASN1Primitive toDERObject() {
    return this;
  }
  
  ASN1Primitive toDLObject() {
    return this;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DERTaggedObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */