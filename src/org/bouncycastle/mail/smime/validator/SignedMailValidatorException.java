package org.bouncycastle.mail.smime.validator;

import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.i18n.LocalizedException;

public class SignedMailValidatorException extends LocalizedException {
  public SignedMailValidatorException(ErrorBundle paramErrorBundle, Throwable paramThrowable) {
    super(paramErrorBundle, paramThrowable);
  }
  
  public SignedMailValidatorException(ErrorBundle paramErrorBundle) {
    super(paramErrorBundle);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/validator/SignedMailValidatorException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */