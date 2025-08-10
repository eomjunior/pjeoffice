package org.bouncycastle.asn1;

import java.io.InputStream;

public interface ASN1OctetStringParser extends ASN1Encodable, InMemoryRepresentable {
  InputStream getOctetStream();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ASN1OctetStringParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */