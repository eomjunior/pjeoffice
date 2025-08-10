package org.bouncycastle.mail.smime.handlers;

import java.awt.datatransfer.DataFlavor;
import javax.activation.ActivationDataFlavor;
import javax.mail.internet.MimeBodyPart;

public class pkcs7_signature extends PKCS7ContentHandler {
  private static final ActivationDataFlavor ADF = new ActivationDataFlavor(MimeBodyPart.class, "application/pkcs7-signature", "Signature");
  
  private static final DataFlavor[] DFS = new DataFlavor[] { (DataFlavor)ADF };
  
  public pkcs7_signature() {
    super(ADF, DFS);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/handlers/pkcs7_signature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */