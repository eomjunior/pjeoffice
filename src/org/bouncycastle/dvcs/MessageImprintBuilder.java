package org.bouncycastle.dvcs;

import java.io.OutputStream;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.operator.DigestCalculator;

public class MessageImprintBuilder {
  private final DigestCalculator digestCalculator;
  
  public MessageImprintBuilder(DigestCalculator paramDigestCalculator) {
    this.digestCalculator = paramDigestCalculator;
  }
  
  public MessageImprint build(byte[] paramArrayOfbyte) throws DVCSException {
    try {
      OutputStream outputStream = this.digestCalculator.getOutputStream();
      outputStream.write(paramArrayOfbyte);
      outputStream.close();
      return new MessageImprint(new DigestInfo(this.digestCalculator.getAlgorithmIdentifier(), this.digestCalculator.getDigest()));
    } catch (Exception exception) {
      throw new DVCSException("unable to build MessageImprint: " + exception.getMessage(), exception);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/dvcs/MessageImprintBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */