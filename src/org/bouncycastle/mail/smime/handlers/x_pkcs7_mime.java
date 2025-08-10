package org.bouncycastle.mail.smime.handlers;

import java.awt.datatransfer.DataFlavor;
import javax.activation.ActivationDataFlavor;
import javax.mail.internet.MimeBodyPart;

public class x_pkcs7_mime extends PKCS7ContentHandler {
  private static final ActivationDataFlavor ADF = new ActivationDataFlavor(MimeBodyPart.class, "application/x-pkcs7-mime", "Encrypted Data");
  
  private static final DataFlavor[] DFS = new DataFlavor[] { (DataFlavor)ADF };
  
  public x_pkcs7_mime() {
    super(ADF, DFS);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/handlers/x_pkcs7_mime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */