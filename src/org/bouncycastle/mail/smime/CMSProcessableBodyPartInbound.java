package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;

public class CMSProcessableBodyPartInbound implements CMSProcessable {
  private final BodyPart bodyPart;
  
  private final String defaultContentTransferEncoding;
  
  public CMSProcessableBodyPartInbound(BodyPart paramBodyPart) {
    this(paramBodyPart, "7bit");
  }
  
  public CMSProcessableBodyPartInbound(BodyPart paramBodyPart, String paramString) {
    this.bodyPart = paramBodyPart;
    this.defaultContentTransferEncoding = paramString;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException, CMSException {
    try {
      SMIMEUtil.outputBodyPart(paramOutputStream, true, this.bodyPart, this.defaultContentTransferEncoding);
    } catch (MessagingException messagingException) {
      throw new CMSException("can't write BodyPart to stream: " + messagingException, messagingException);
    } 
  }
  
  public Object getContent() {
    return this.bodyPart;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/CMSProcessableBodyPartInbound.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */