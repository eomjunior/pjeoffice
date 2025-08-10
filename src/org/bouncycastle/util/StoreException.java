package org.bouncycastle.util;

public class StoreException extends RuntimeException {
  private Throwable _e;
  
  public StoreException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._e = paramThrowable;
  }
  
  public Throwable getCause() {
    return this._e;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/StoreException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */