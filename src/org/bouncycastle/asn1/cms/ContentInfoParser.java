package org.bouncycastle.asn1.cms;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;

public class ContentInfoParser {
  private ASN1ObjectIdentifier contentType;
  
  private ASN1TaggedObjectParser content;
  
  public ContentInfoParser(ASN1SequenceParser paramASN1SequenceParser) throws IOException {
    this.contentType = (ASN1ObjectIdentifier)paramASN1SequenceParser.readObject();
    this.content = (ASN1TaggedObjectParser)paramASN1SequenceParser.readObject();
  }
  
  public ASN1ObjectIdentifier getContentType() {
    return this.contentType;
  }
  
  public ASN1Encodable getContent(int paramInt) throws IOException {
    return (this.content != null) ? this.content.getObjectParser(paramInt, true) : null;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/cms/ContentInfoParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */