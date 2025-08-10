package org.bouncycastle.jcajce.provider.symmetric;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseStreamCipher;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class Salsa20 {
  public static class AlgParams extends IvAlgorithmParameters {
    protected String engineToString() {
      return "Salsa20 IV";
    }
  }
  
  public static class Base extends BaseStreamCipher {
    public Base() {
      super((StreamCipher)new Salsa20Engine(), 8);
    }
  }
  
  public static class KeyGen extends BaseKeyGenerator {
    public KeyGen() {
      super("Salsa20", 128, new CipherKeyGenerator());
    }
  }
  
  public static class Mappings extends AlgorithmProvider {
    private static final String PREFIX = Salsa20.class.getName();
    
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("Cipher.SALSA20", PREFIX + "$Base");
      param1ConfigurableProvider.addAlgorithm("KeyGenerator.SALSA20", PREFIX + "$KeyGen");
      param1ConfigurableProvider.addAlgorithm("AlgorithmParameters.SALSA20", PREFIX + "$AlgParams");
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/symmetric/Salsa20.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */