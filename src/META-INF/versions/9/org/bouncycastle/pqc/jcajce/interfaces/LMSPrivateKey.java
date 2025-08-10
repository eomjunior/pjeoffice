package META-INF.versions.9.org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PrivateKey;
import org.bouncycastle.pqc.jcajce.interfaces.LMSKey;

public interface LMSPrivateKey extends LMSKey, PrivateKey {
  long getIndex();
  
  long getUsagesRemaining();
  
  org.bouncycastle.pqc.jcajce.interfaces.LMSPrivateKey extractKeyShard(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/interfaces/LMSPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */