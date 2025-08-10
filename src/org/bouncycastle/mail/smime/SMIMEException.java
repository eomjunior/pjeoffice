package org.bouncycastle.mail.smime;

public class SMIMEException extends Exception {
  Exception e;
  
  public SMIMEException(String paramString) {
    super(paramString);
  }
  
  public SMIMEException(String paramString, Exception paramException) {
    super(paramString);
    this.e = paramException;
  }
  
  public Exception getUnderlyingException() {
    return this.e;
  }
  
  public Throwable getCause() {
    return this.e;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMEException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */