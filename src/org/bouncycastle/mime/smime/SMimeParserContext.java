package org.bouncycastle.mime.smime;

import org.bouncycastle.mime.MimeParserContext;
import org.bouncycastle.operator.DigestCalculatorProvider;

public class SMimeParserContext implements MimeParserContext {
  private final String defaultContentTransferEncoding;
  
  private final DigestCalculatorProvider digestCalculatorProvider;
  
  public SMimeParserContext(String paramString, DigestCalculatorProvider paramDigestCalculatorProvider) {
    this.defaultContentTransferEncoding = paramString;
    this.digestCalculatorProvider = paramDigestCalculatorProvider;
  }
  
  public String getDefaultContentTransferEncoding() {
    return this.defaultContentTransferEncoding;
  }
  
  public DigestCalculatorProvider getDigestCalculatorProvider() {
    return this.digestCalculatorProvider;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mime/smime/SMimeParserContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */