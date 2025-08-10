package org.bouncycastle.cert.cmp;

public class CMPException extends Exception {
  private Throwable cause;
  
  public CMPException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this.cause = paramThrowable;
  }
  
  public CMPException(String paramString) {
    super(paramString);
  }
  
  public Throwable getCause() {
    return this.cause;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/cmp/CMPException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */