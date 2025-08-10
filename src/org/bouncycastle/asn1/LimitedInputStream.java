package org.bouncycastle.asn1;

import java.io.InputStream;

abstract class LimitedInputStream extends InputStream {
  protected final InputStream _in;
  
  private int _limit;
  
  LimitedInputStream(InputStream paramInputStream, int paramInt) {
    this._in = paramInputStream;
    this._limit = paramInt;
  }
  
  int getLimit() {
    return this._limit;
  }
  
  protected void setParentEofDetect(boolean paramBoolean) {
    if (this._in instanceof IndefiniteLengthInputStream)
      ((IndefiniteLengthInputStream)this._in).setEofOn00(paramBoolean); 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/LimitedInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */