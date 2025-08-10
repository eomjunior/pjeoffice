package org.bouncycastle.tsp;

public class TSPValidationException extends TSPException {
  private int failureCode = -1;
  
  public TSPValidationException(String paramString) {
    super(paramString);
  }
  
  public TSPValidationException(String paramString, int paramInt) {
    super(paramString);
    this.failureCode = paramInt;
  }
  
  public int getFailureCode() {
    return this.failureCode;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/tsp/TSPValidationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */