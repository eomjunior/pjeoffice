package org.bouncycastle.pqc.crypto.lms;

import java.io.IOException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Encodable;

public abstract class LMSKeyParameters extends AsymmetricKeyParameter implements Encodable {
  protected LMSKeyParameters(boolean paramBoolean) {
    super(paramBoolean);
  }
  
  public abstract byte[] getEncoded() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */