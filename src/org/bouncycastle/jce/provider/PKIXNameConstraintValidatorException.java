package org.bouncycastle.jce.provider;

public class PKIXNameConstraintValidatorException extends Exception {
  private Throwable cause;
  
  public PKIXNameConstraintValidatorException(String paramString) {
    super(paramString);
  }
  
  public PKIXNameConstraintValidatorException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jce/provider/PKIXNameConstraintValidatorException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */