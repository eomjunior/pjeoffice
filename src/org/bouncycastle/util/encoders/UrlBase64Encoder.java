package org.bouncycastle.util.encoders;

public class UrlBase64Encoder extends Base64Encoder {
  public UrlBase64Encoder() {
    this.encodingTable[this.encodingTable.length - 2] = 45;
    this.encodingTable[this.encodingTable.length - 1] = 95;
    this.padding = 46;
    initialiseDecodingTable();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/encoders/UrlBase64Encoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */