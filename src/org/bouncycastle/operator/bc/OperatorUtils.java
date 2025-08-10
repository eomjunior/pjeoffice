package org.bouncycastle.operator.bc;

import java.security.Key;
import org.bouncycastle.operator.GenericKey;

class OperatorUtils {
  static byte[] getKeyBytes(GenericKey paramGenericKey) {
    if (paramGenericKey.getRepresentation() instanceof Key)
      return ((Key)paramGenericKey.getRepresentation()).getEncoded(); 
    if (paramGenericKey.getRepresentation() instanceof byte[])
      return (byte[])paramGenericKey.getRepresentation(); 
    throw new IllegalArgumentException("unknown generic key type");
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/bc/OperatorUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */