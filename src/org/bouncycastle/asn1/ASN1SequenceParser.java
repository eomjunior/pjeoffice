package org.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1SequenceParser extends ASN1Encodable, InMemoryRepresentable {
  ASN1Encodable readObject() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ASN1SequenceParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */