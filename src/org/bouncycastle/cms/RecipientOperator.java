package org.bouncycastle.cms;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.operator.InputAEADDecryptor;
import org.bouncycastle.operator.InputDecryptor;
import org.bouncycastle.operator.MacCalculator;
import org.bouncycastle.util.io.TeeInputStream;

public class RecipientOperator {
  private final Object operator;
  
  public RecipientOperator(InputDecryptor paramInputDecryptor) {
    this.operator = paramInputDecryptor;
  }
  
  public RecipientOperator(MacCalculator paramMacCalculator) {
    this.operator = paramMacCalculator;
  }
  
  public InputStream getInputStream(InputStream paramInputStream) {
    return (InputStream)((this.operator instanceof InputDecryptor) ? ((InputDecryptor)this.operator).getInputStream(paramInputStream) : new TeeInputStream(paramInputStream, ((MacCalculator)this.operator).getOutputStream()));
  }
  
  public boolean isAEADBased() {
    return this.operator instanceof InputAEADDecryptor;
  }
  
  public OutputStream getAADStream() {
    return ((InputAEADDecryptor)this.operator).getAADStream();
  }
  
  public boolean isMacBased() {
    return this.operator instanceof MacCalculator;
  }
  
  public byte[] getMac() {
    return ((MacCalculator)this.operator).getMac();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/RecipientOperator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */