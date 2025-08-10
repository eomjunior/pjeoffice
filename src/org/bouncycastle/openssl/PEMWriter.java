package org.bouncycastle.openssl;

import java.io.IOException;
import java.io.Writer;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;

public class PEMWriter extends PemWriter {
  public PEMWriter(Writer paramWriter) {
    super(paramWriter);
  }
  
  public void writeObject(Object paramObject) throws IOException {
    writeObject(paramObject, null);
  }
  
  public void writeObject(Object paramObject, PEMEncryptor paramPEMEncryptor) throws IOException {
    try {
      super.writeObject((PemObjectGenerator)new JcaMiscPEMGenerator(paramObject, paramPEMEncryptor));
    } catch (PemGenerationException pemGenerationException) {
      if (pemGenerationException.getCause() instanceof IOException)
        throw (IOException)pemGenerationException.getCause(); 
      throw pemGenerationException;
    } 
  }
  
  public void writeObject(PemObjectGenerator paramPemObjectGenerator) throws IOException {
    super.writeObject(paramPemObjectGenerator);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/openssl/PEMWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */