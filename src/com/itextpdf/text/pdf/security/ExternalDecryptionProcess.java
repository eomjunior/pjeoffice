package com.itextpdf.text.pdf.security;

import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientId;

public interface ExternalDecryptionProcess {
  RecipientId getCmsRecipientId();
  
  Recipient getCmsRecipient();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/ExternalDecryptionProcess.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */