package org.bouncycastle.asn1;

import java.io.IOException;

public class BERApplicationSpecificParser implements ASN1ApplicationSpecificParser {
  private final int tag;
  
  private final ASN1StreamParser parser;
  
  BERApplicationSpecificParser(int paramInt, ASN1StreamParser paramASN1StreamParser) {
    this.tag = paramInt;
    this.parser = paramASN1StreamParser;
  }
  
  public ASN1Encodable readObject() throws IOException {
    return this.parser.readObject();
  }
  
  public ASN1Primitive getLoadedObject() throws IOException {
    return new BERApplicationSpecific(this.tag, this.parser.readVector());
  }
  
  public ASN1Primitive toASN1Primitive() {
    try {
      return getLoadedObject();
    } catch (IOException iOException) {
      throw new ASN1ParsingException(iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/BERApplicationSpecificParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */