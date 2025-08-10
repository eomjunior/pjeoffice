package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.mail.smime.util.CRLFOutputStream;

public class CMSProcessableBodyPartOutbound implements CMSProcessable {
  private BodyPart bodyPart;
  
  private String defaultContentTransferEncoding;
  
  public CMSProcessableBodyPartOutbound(BodyPart paramBodyPart) {
    this.bodyPart = paramBodyPart;
  }
  
  public CMSProcessableBodyPartOutbound(BodyPart paramBodyPart, String paramString) {
    this.bodyPart = paramBodyPart;
    this.defaultContentTransferEncoding = paramString;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException, CMSException {
    try {
      CRLFOutputStream cRLFOutputStream;
      if (SMIMEUtil.isCanonicalisationRequired((MimeBodyPart)this.bodyPart, this.defaultContentTransferEncoding))
        cRLFOutputStream = new CRLFOutputStream(paramOutputStream); 
      this.bodyPart.writeTo((OutputStream)cRLFOutputStream);
    } catch (MessagingException messagingException) {
      throw new CMSException("can't write BodyPart to stream.", messagingException);
    } 
  }
  
  public Object getContent() {
    return this.bodyPart;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/CMSProcessableBodyPartOutbound.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */