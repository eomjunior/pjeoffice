package META-INF.versions.9.org.bouncycastle.math.ec.endo;

import org.bouncycastle.math.ec.ECPointMap;

public interface ECEndomorphism {
  ECPointMap getPointMap();
  
  boolean hasEfficientPointMap();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/ECEndomorphism.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */