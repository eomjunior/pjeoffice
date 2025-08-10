package org.bouncycastle.math.ec;

public abstract class AbstractECLookupTable implements ECLookupTable {
  public ECPoint lookupVar(int paramInt) {
    return lookup(paramInt);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/AbstractECLookupTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */