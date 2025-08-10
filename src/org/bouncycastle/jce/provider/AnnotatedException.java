package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

public class AnnotatedException extends Exception implements ExtException {
  private Throwable _underlyingException;
  
  public AnnotatedException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._underlyingException = paramThrowable;
  }
  
  public AnnotatedException(String paramString) {
    this(paramString, null);
  }
  
  Throwable getUnderlyingException() {
    return this._underlyingException;
  }
  
  public Throwable getCause() {
    return this._underlyingException;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jce/provider/AnnotatedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */