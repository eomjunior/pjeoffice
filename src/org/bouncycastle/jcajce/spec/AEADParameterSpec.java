package org.bouncycastle.jcajce.spec;

import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.util.Arrays;

public class AEADParameterSpec extends IvParameterSpec {
  private final byte[] associatedData;
  
  private final int macSizeInBits;
  
  public AEADParameterSpec(byte[] paramArrayOfbyte, int paramInt) {
    this(paramArrayOfbyte, paramInt, null);
  }
  
  public AEADParameterSpec(byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
    super(paramArrayOfbyte1);
    this.macSizeInBits = paramInt;
    this.associatedData = Arrays.clone(paramArrayOfbyte2);
  }
  
  public int getMacSizeInBits() {
    return this.macSizeInBits;
  }
  
  public byte[] getAssociatedData() {
    return Arrays.clone(this.associatedData);
  }
  
  public byte[] getNonce() {
    return getIV();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/spec/AEADParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */