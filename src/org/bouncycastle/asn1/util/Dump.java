package org.bouncycastle.asn1.util;

import java.io.FileInputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;

public class Dump {
  public static void main(String[] paramArrayOfString) throws Exception {
    FileInputStream fileInputStream = new FileInputStream(paramArrayOfString[0]);
    ASN1InputStream aSN1InputStream = new ASN1InputStream(fileInputStream);
    ASN1Primitive aSN1Primitive = null;
    while ((aSN1Primitive = aSN1InputStream.readObject()) != null)
      System.out.println(ASN1Dump.dumpAsString(aSN1Primitive)); 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/util/Dump.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */