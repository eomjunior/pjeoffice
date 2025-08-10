package org.bouncycastle.asn1;

import java.util.Date;

public class DERUTCTime extends ASN1UTCTime {
  DERUTCTime(byte[] paramArrayOfbyte) {
    super(paramArrayOfbyte);
  }
  
  public DERUTCTime(Date paramDate) {
    super(paramDate);
  }
  
  public DERUTCTime(String paramString) {
    super(paramString);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DERUTCTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */