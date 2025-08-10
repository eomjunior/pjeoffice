package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import org.bouncycastle.cms.CMSCompressedData;
import org.bouncycastle.cms.CMSException;

public class SMIMECompressed extends CMSCompressedData {
  MimePart message;
  
  private static InputStream getInputStream(Part paramPart) throws MessagingException {
    try {
      return paramPart.getInputStream();
    } catch (IOException iOException) {
      throw new MessagingException("can't extract input stream: " + iOException);
    } 
  }
  
  public SMIMECompressed(MimeBodyPart paramMimeBodyPart) throws MessagingException, CMSException {
    super(getInputStream((Part)paramMimeBodyPart));
    this.message = (MimePart)paramMimeBodyPart;
  }
  
  public SMIMECompressed(MimeMessage paramMimeMessage) throws MessagingException, CMSException {
    super(getInputStream((Part)paramMimeMessage));
    this.message = (MimePart)paramMimeMessage;
  }
  
  public MimePart getCompressedContent() {
    return this.message;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMECompressed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */