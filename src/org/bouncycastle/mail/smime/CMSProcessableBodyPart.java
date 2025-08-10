package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;

public class CMSProcessableBodyPart implements CMSProcessable {
  private BodyPart bodyPart;
  
  public CMSProcessableBodyPart(BodyPart paramBodyPart) {
    this.bodyPart = paramBodyPart;
  }
  
  public void write(OutputStream paramOutputStream) throws IOException, CMSException {
    try {
      this.bodyPart.writeTo(paramOutputStream);
    } catch (MessagingException messagingException) {
      throw new CMSException("can't write BodyPart to stream.", messagingException);
    } 
  }
  
  public Object getContent() {
    return this.bodyPart;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/CMSProcessableBodyPart.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */