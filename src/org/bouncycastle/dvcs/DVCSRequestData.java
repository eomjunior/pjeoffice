package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.Data;

public abstract class DVCSRequestData {
  protected Data data;
  
  protected DVCSRequestData(Data paramData) {
    this.data = paramData;
  }
  
  public Data toASN1Structure() {
    return this.data;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/dvcs/DVCSRequestData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */