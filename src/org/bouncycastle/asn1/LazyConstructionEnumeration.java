package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

class LazyConstructionEnumeration implements Enumeration {
  private ASN1InputStream aIn;
  
  private Object nextObj;
  
  public LazyConstructionEnumeration(byte[] paramArrayOfbyte) {
    this.aIn = new ASN1InputStream(paramArrayOfbyte, true);
    this.nextObj = readObject();
  }
  
  public boolean hasMoreElements() {
    return (this.nextObj != null);
  }
  
  public Object nextElement() {
    if (this.nextObj != null) {
      Object object = this.nextObj;
      this.nextObj = readObject();
      return object;
    } 
    throw new NoSuchElementException();
  }
  
  private Object readObject() {
    try {
      return this.aIn.readObject();
    } catch (IOException iOException) {
      throw new ASN1ParsingException("malformed DER construction: " + iOException, iOException);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/LazyConstructionEnumeration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */