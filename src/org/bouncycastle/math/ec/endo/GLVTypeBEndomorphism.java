package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.ScaleXPointMap;

public class GLVTypeBEndomorphism implements GLVEndomorphism {
  protected final GLVTypeBParameters parameters;
  
  protected final ECPointMap pointMap;
  
  public GLVTypeBEndomorphism(ECCurve paramECCurve, GLVTypeBParameters paramGLVTypeBParameters) {
    this.parameters = paramGLVTypeBParameters;
    this.pointMap = (ECPointMap)new ScaleXPointMap(paramECCurve.fromBigInteger(paramGLVTypeBParameters.getBeta()));
  }
  
  public BigInteger[] decomposeScalar(BigInteger paramBigInteger) {
    return EndoUtil.decomposeScalar(this.parameters.getSplitParams(), paramBigInteger);
  }
  
  public ECPointMap getPointMap() {
    return this.pointMap;
  }
  
  public boolean hasEfficientPointMap() {
    return true;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/endo/GLVTypeBEndomorphism.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */