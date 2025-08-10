package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

class DLOutputStream extends ASN1OutputStream {
  DLOutputStream(OutputStream paramOutputStream) {
    super(paramOutputStream);
  }
  
  void writePrimitive(ASN1Primitive paramASN1Primitive, boolean paramBoolean) throws IOException {
    paramASN1Primitive.toDLObject().encode(this, paramBoolean);
  }
  
  ASN1OutputStream getDLSubStream() {
    return this;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DLOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */