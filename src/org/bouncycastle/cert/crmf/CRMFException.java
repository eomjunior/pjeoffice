package org.bouncycastle.cert.crmf;

public class CRMFException extends Exception {
  private Throwable cause;
  
  public CRMFException(String paramString) {
    this(paramString, null);
  }
  
  public CRMFException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/crmf/CRMFException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */