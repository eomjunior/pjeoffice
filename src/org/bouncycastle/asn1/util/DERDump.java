package org.bouncycastle.asn1.util;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;

public class DERDump extends ASN1Dump {
  public static String dumpAsString(ASN1Primitive paramASN1Primitive) {
    StringBuffer stringBuffer = new StringBuffer();
    _dumpAsString("", false, paramASN1Primitive, stringBuffer);
    return stringBuffer.toString();
  }
  
  public static String dumpAsString(ASN1Encodable paramASN1Encodable) {
    StringBuffer stringBuffer = new StringBuffer();
    _dumpAsString("", false, paramASN1Encodable.toASN1Primitive(), stringBuffer);
    return stringBuffer.toString();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/util/DERDump.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */