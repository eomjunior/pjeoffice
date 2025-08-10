package org.bouncycastle.crypto.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.util.io.Streams;

public class DHIESPublicKeyParser implements KeyParser {
  private DHParameters dhParams;
  
  public DHIESPublicKeyParser(DHParameters paramDHParameters) {
    this.dhParams = paramDHParameters;
  }
  
  public AsymmetricKeyParameter readKey(InputStream paramInputStream) throws IOException {
    byte[] arrayOfByte = new byte[(this.dhParams.getP().bitLength() + 7) / 8];
    Streams.readFully(paramInputStream, arrayOfByte, 0, arrayOfByte.length);
    return (AsymmetricKeyParameter)new DHPublicKeyParameters(new BigInteger(1, arrayOfByte), this.dhParams);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/parsers/DHIESPublicKeyParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */