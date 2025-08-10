package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.util.encoders.Hex;

public abstract class X509NameEntryConverter {
  protected ASN1Primitive convertHexEncoded(String paramString, int paramInt) throws IOException {
    return ASN1Primitive.fromByteArray(Hex.decodeStrict(paramString, paramInt, paramString.length() - paramInt));
  }
  
  protected boolean canBePrintable(String paramString) {
    return DERPrintableString.isPrintableString(paramString);
  }
  
  public abstract ASN1Primitive getConvertedValue(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/x509/X509NameEntryConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */