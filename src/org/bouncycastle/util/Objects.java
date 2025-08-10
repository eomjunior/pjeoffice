package org.bouncycastle.util;

public class Objects {
  public static boolean areEqual(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (null != paramObject1 && null != paramObject2 && paramObject1.equals(paramObject2)));
  }
  
  public static int hashCode(Object paramObject) {
    return (null == paramObject) ? 0 : paramObject.hashCode();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/Objects.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */