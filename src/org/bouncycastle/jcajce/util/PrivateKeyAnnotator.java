package org.bouncycastle.jcajce.util;

import java.security.PrivateKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PrivateKeyAnnotator {
  public static AnnotatedPrivateKey annotate(PrivateKey paramPrivateKey, String paramString) {
    return new AnnotatedPrivateKey(paramPrivateKey, paramString);
  }
  
  public static AnnotatedPrivateKey annotate(PrivateKey paramPrivateKey, Map<String, Object> paramMap) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>(paramMap);
    return new AnnotatedPrivateKey(paramPrivateKey, Collections.unmodifiableMap(hashMap));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/util/PrivateKeyAnnotator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */