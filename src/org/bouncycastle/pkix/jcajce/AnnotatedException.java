package org.bouncycastle.pkix.jcajce;

class AnnotatedException extends Exception {
  private Throwable _underlyingException;
  
  public AnnotatedException(String paramString, Throwable paramThrowable) {
    super(paramString);
    this._underlyingException = paramThrowable;
  }
  
  public AnnotatedException(String paramString) {
    this(paramString, null);
  }
  
  public Throwable getCause() {
    return this._underlyingException;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pkix/jcajce/AnnotatedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */