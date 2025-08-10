package org.bouncycastle.util.test;

import org.bouncycastle.util.encoders.Hex;

public class TestRandomData extends FixedSecureRandom {
  public TestRandomData(String paramString) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.Data(Hex.decode(paramString)) });
  }
  
  public TestRandomData(byte[] paramArrayOfbyte) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.Data(paramArrayOfbyte) });
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/test/TestRandomData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */