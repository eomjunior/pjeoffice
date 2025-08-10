package org.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1TaggedObjectParser extends ASN1Encodable, InMemoryRepresentable {
  int getTagNo();
  
  ASN1Encodable getObjectParser(int paramInt, boolean paramBoolean) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ASN1TaggedObjectParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */