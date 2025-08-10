package org.bouncycastle.est;

import java.io.IOException;

public interface ESTClient {
  ESTResponse doRequest(ESTRequest paramESTRequest) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/est/ESTClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */