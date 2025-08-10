package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class BEROctetString extends ASN1OctetString {
  private static final int DEFAULT_CHUNK_SIZE = 1000;
  
  private final int chunkSize;
  
  private final ASN1OctetString[] octs;
  
  private static byte[] toBytes(ASN1OctetString[] paramArrayOfASN1OctetString) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    for (byte b = 0; b != paramArrayOfASN1OctetString.length; b++) {
      try {
        byteArrayOutputStream.write(paramArrayOfASN1OctetString[b].getOctets());
      } catch (IOException iOException) {
        throw new IllegalArgumentException("exception converting octets " + iOException.toString());
      } 
    } 
    return byteArrayOutputStream.toByteArray();
  }
  
  public BEROctetString(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, 1000);
  }
  
  public BEROctetString(ASN1OctetString[] paramArrayOfASN1OctetString) {
    this(paramArrayOfASN1OctetString, 1000);
  }
  
  public BEROctetString(byte[] paramArrayOfbyte, int paramInt) {
    this(paramArrayOfbyte, null, paramInt);
  }
  
  public BEROctetString(ASN1OctetString[] paramArrayOfASN1OctetString, int paramInt) {
    this(toBytes(paramArrayOfASN1OctetString), paramArrayOfASN1OctetString, paramInt);
  }
  
  private BEROctetString(byte[] paramArrayOfbyte, ASN1OctetString[] paramArrayOfASN1OctetString, int paramInt) {
    super(paramArrayOfbyte);
    this.octs = paramArrayOfASN1OctetString;
    this.chunkSize = paramInt;
  }
  
  public Enumeration getObjects() {
    return (this.octs == null) ? new Enumeration() {
        int pos = 0;
        
        public boolean hasMoreElements() {
          return (this.pos < BEROctetString.this.string.length);
        }
        
        public Object nextElement() {
          if (this.pos < BEROctetString.this.string.length) {
            int i = Math.min(BEROctetString.this.string.length - this.pos, BEROctetString.this.chunkSize);
            byte[] arrayOfByte = new byte[i];
            System.arraycopy(BEROctetString.this.string, this.pos, arrayOfByte, 0, i);
            this.pos += i;
            return new DEROctetString(arrayOfByte);
          } 
          throw new NoSuchElementException();
        }
      } : new Enumeration() {
        int counter = 0;
        
        public boolean hasMoreElements() {
          return (this.counter < BEROctetString.this.octs.length);
        }
        
        public Object nextElement() {
          if (this.counter < BEROctetString.this.octs.length)
            return BEROctetString.this.octs[this.counter++]; 
          throw new NoSuchElementException();
        }
      };
  }
  
  boolean isConstructed() {
    return true;
  }
  
  int encodedLength() throws IOException {
    int i = 0;
    Enumeration<ASN1Encodable> enumeration = getObjects();
    while (enumeration.hasMoreElements())
      i += ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().encodedLength(); 
    return 2 + i + 2;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    paramASN1OutputStream.writeEncodedIndef(paramBoolean, 36, getObjects());
  }
  
  static BEROctetString fromSequence(ASN1Sequence paramASN1Sequence) {
    int i = paramASN1Sequence.size();
    ASN1OctetString[] arrayOfASN1OctetString = new ASN1OctetString[i];
    for (byte b = 0; b < i; b++)
      arrayOfASN1OctetString[b] = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(b)); 
    return new BEROctetString(arrayOfASN1OctetString);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/BEROctetString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */