package org.bouncycastle.asn1;

import java.io.IOException;

public class DLTaggedObject extends ASN1TaggedObject {
  public DLTaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
    super(paramBoolean, paramInt, paramASN1Encodable);
  }
  
  boolean isConstructed() {
    return (this.explicit || this.obj.toASN1Primitive().toDLObject().isConstructed());
  }
  
  int encodedLength() throws IOException {
    int i = this.obj.toASN1Primitive().toDLObject().encodedLength();
    return this.explicit ? (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(i) + i) : (StreamUtil.calculateTagLength(this.tagNo) + --i);
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDLObject();
    int i = 128;
    if (this.explicit || aSN1Primitive.isConstructed())
      i |= 0x20; 
    paramASN1OutputStream.writeTag(paramBoolean, i, this.tagNo);
    if (this.explicit)
      paramASN1OutputStream.writeLength(aSN1Primitive.encodedLength()); 
    paramASN1OutputStream.getDLSubStream().writePrimitive(aSN1Primitive, this.explicit);
  }
  
  ASN1Primitive toDLObject() {
    return this;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DLTaggedObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */