package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.CMSCompressedDataStreamGenerator;
import org.bouncycastle.operator.OutputCompressor;

public class SMIMECompressedGenerator extends SMIMEGenerator {
  public static final String ZLIB = "1.2.840.113549.1.9.16.3.8";
  
  private static final String COMPRESSED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7z\"; smime-type=compressed-data";
  
  private MimeBodyPart make(MimeBodyPart paramMimeBodyPart, OutputCompressor paramOutputCompressor) throws SMIMEException {
    try {
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(new ContentCompressor(paramMimeBodyPart, paramOutputCompressor), "application/pkcs7-mime; name=\"smime.p7z\"; smime-type=compressed-data");
      mimeBodyPart.addHeader("Content-Type", "application/pkcs7-mime; name=\"smime.p7z\"; smime-type=compressed-data");
      mimeBodyPart.addHeader("Content-Disposition", "attachment; filename=\"smime.p7z\"");
      mimeBodyPart.addHeader("Content-Description", "S/MIME Compressed Message");
      mimeBodyPart.addHeader("Content-Transfer-Encoding", this.encoding);
      return mimeBodyPart;
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception putting multi-part together.", messagingException);
    } 
  }
  
  public MimeBodyPart generate(MimeBodyPart paramMimeBodyPart, OutputCompressor paramOutputCompressor) throws SMIMEException {
    return make(makeContentBodyPart(paramMimeBodyPart), paramOutputCompressor);
  }
  
  public MimeBodyPart generate(MimeMessage paramMimeMessage, OutputCompressor paramOutputCompressor) throws SMIMEException {
    try {
      paramMimeMessage.saveChanges();
    } catch (MessagingException messagingException) {
      throw new SMIMEException("unable to save message", messagingException);
    } 
    return make(makeContentBodyPart(paramMimeMessage), paramOutputCompressor);
  }
  
  static {
    CommandMap commandMap = CommandMap.getDefaultCommandMap();
    if (commandMap instanceof MailcapCommandMap) {
      final MailcapCommandMap mc = (MailcapCommandMap)commandMap;
      mailcapCommandMap.addMailcap("application/pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
      mailcapCommandMap.addMailcap("application/x-pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
      AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
              CommandMap.setDefaultCommandMap((CommandMap)mc);
              return null;
            }
          });
    } 
  }
  
  private class ContentCompressor implements SMIMEStreamingProcessor {
    private final MimeBodyPart content;
    
    private final OutputCompressor compressor;
    
    ContentCompressor(MimeBodyPart param1MimeBodyPart, OutputCompressor param1OutputCompressor) {
      this.content = param1MimeBodyPart;
      this.compressor = param1OutputCompressor;
    }
    
    public void write(OutputStream param1OutputStream) throws IOException {
      CMSCompressedDataStreamGenerator cMSCompressedDataStreamGenerator = new CMSCompressedDataStreamGenerator();
      OutputStream outputStream = cMSCompressedDataStreamGenerator.open(param1OutputStream, this.compressor);
      try {
        this.content.writeTo(outputStream);
        outputStream.close();
      } catch (MessagingException messagingException) {
        throw new IOException(messagingException.toString());
      } 
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMECompressedGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */