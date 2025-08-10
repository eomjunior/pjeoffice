package META-INF.versions.9.org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;

public interface ECLookupTable {
  int getSize();
  
  ECPoint lookup(int paramInt);
  
  ECPoint lookupVar(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ECLookupTable.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */