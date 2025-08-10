package org.bouncycastle.crypto.params;

import org.bouncycastle.util.Arrays;

public class DSAValidationParameters {
  private int usageIndex;
  
  private byte[] seed;
  
  private int counter;
  
  public DSAValidationParameters(byte[] paramArrayOfbyte, int paramInt) {
    this(paramArrayOfbyte, paramInt, -1);
  }
  
  public DSAValidationParameters(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.seed = Arrays.clone(paramArrayOfbyte);
    this.counter = paramInt1;
    this.usageIndex = paramInt2;
  }
  
  public int getCounter() {
    return this.counter;
  }
  
  public byte[] getSeed() {
    return Arrays.clone(this.seed);
  }
  
  public int getUsageIndex() {
    return this.usageIndex;
  }
  
  public int hashCode() {
    return this.counter ^ Arrays.hashCode(this.seed);
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof DSAValidationParameters))
      return false; 
    DSAValidationParameters dSAValidationParameters = (DSAValidationParameters)paramObject;
    return (dSAValidationParameters.counter != this.counter) ? false : Arrays.areEqual(this.seed, dSAValidationParameters.seed);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/DSAValidationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */