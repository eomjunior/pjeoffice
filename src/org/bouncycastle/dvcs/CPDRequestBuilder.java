package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.DVCSRequestInformationBuilder;
import org.bouncycastle.asn1.dvcs.Data;
import org.bouncycastle.asn1.dvcs.ServiceType;

public class CPDRequestBuilder extends DVCSRequestBuilder {
  public CPDRequestBuilder() {
    super(new DVCSRequestInformationBuilder(ServiceType.CPD));
  }
  
  public DVCSRequest build(byte[] paramArrayOfbyte) throws DVCSException {
    Data data = new Data(paramArrayOfbyte);
    return createDVCRequest(data);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/dvcs/CPDRequestBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */