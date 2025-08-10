package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.ScaleYNegateXPointMap;

public class GLVTypeAEndomorphism implements GLVEndomorphism {
  protected final GLVTypeAParameters parameters;
  
  protected final ECPointMap pointMap;
  
  public GLVTypeAEndomorphism(ECCurve paramECCurve, GLVTypeAParameters paramGLVTypeAParameters) {
    this.parameters = paramGLVTypeAParameters;
    this.pointMap = (ECPointMap)new ScaleYNegateXPointMap(paramECCurve.fromBigInteger(paramGLVTypeAParameters.getI()));
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/endo/GLVTypeAEndomorphism.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */