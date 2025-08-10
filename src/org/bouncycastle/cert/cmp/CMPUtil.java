package org.bouncycastle.cert.cmp;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Object;

class CMPUtil {
  static void derEncodeToStream(ASN1Object paramASN1Object, OutputStream paramOutputStream) {
    try {
      paramASN1Object.encodeTo(paramOutputStream, "DER");
      paramOutputStream.close();
    } catch (IOException iOException) {
      throw new CMPRuntimeException("unable to DER encode object: " + iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/cmp/CMPUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */