package org.bouncycastle.i18n.filter;

public class TrustedInput {
  protected Object input;
  
  public TrustedInput(Object paramObject) {
    this.input = paramObject;
  }
  
  public Object getInput() {
    return this.input;
  }
  
  public String toString() {
    return this.input.toString();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/i18n/filter/TrustedInput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */