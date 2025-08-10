package org.bouncycastle.mime.smime;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.mime.BasicMimeParser;
import org.bouncycastle.mime.Headers;
import org.bouncycastle.mime.MimeParser;
import org.bouncycastle.mime.MimeParserProvider;
import org.bouncycastle.operator.DigestCalculatorProvider;

public class SMimeParserProvider implements MimeParserProvider {
  private final String defaultContentTransferEncoding;
  
  private final DigestCalculatorProvider digestCalculatorProvider;
  
  public SMimeParserProvider(String paramString, DigestCalculatorProvider paramDigestCalculatorProvider) {
    this.defaultContentTransferEncoding = paramString;
    this.digestCalculatorProvider = paramDigestCalculatorProvider;
  }
  
  public MimeParser createParser(InputStream paramInputStream) throws IOException {
    return (MimeParser)new BasicMimeParser(new SMimeParserContext(this.defaultContentTransferEncoding, this.digestCalculatorProvider), SMimeUtils.autoBuffer(paramInputStream));
  }
  
  public MimeParser createParser(Headers paramHeaders, InputStream paramInputStream) throws IOException {
    return (MimeParser)new BasicMimeParser(new SMimeParserContext(this.defaultContentTransferEncoding, this.digestCalculatorProvider), paramHeaders, SMimeUtils.autoBuffer(paramInputStream));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mime/smime/SMimeParserProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */