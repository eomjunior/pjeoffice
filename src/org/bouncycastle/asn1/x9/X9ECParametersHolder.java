package org.bouncycastle.asn1.x9;

public abstract class X9ECParametersHolder {
  private X9ECParameters params;
  
  public synchronized X9ECParameters getParameters() {
    if (this.params == null)
      this.params = createParameters(); 
    return this.params;
  }
  
  protected abstract X9ECParameters createParameters();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/x9/X9ECParametersHolder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */