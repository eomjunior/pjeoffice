package org.bouncycastle.jcajce.io;

import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.Signature;
import javax.crypto.Mac;

public class OutputStreamFactory {
  public static OutputStream createStream(Signature paramSignature) {
    return new SignatureUpdatingOutputStream(paramSignature);
  }
  
  public static OutputStream createStream(MessageDigest paramMessageDigest) {
    return new DigestUpdatingOutputStream(paramMessageDigest);
  }
  
  public static OutputStream createStream(Mac paramMac) {
    return new MacUpdatingOutputStream(paramMac);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/io/OutputStreamFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */