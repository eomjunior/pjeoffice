package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class ASN1OutputStream {
  private OutputStream os;
  
  public static ASN1OutputStream create(OutputStream paramOutputStream) {
    return new ASN1OutputStream(paramOutputStream);
  }
  
  public static ASN1OutputStream create(OutputStream paramOutputStream, String paramString) {
    return paramString.equals("DER") ? new DEROutputStream(paramOutputStream) : (paramString.equals("DL") ? new DLOutputStream(paramOutputStream) : new ASN1OutputStream(paramOutputStream));
  }
  
  public ASN1OutputStream(OutputStream paramOutputStream) {
    this.os = paramOutputStream;
  }
  
  final void writeLength(int paramInt) throws IOException {
    if (paramInt > 127) {
      byte b = 1;
      int i = paramInt;
      while ((i >>>= 8) != 0)
        b++; 
      write((byte)(b | 0x80));
      for (int j = (b - 1) * 8; j >= 0; j -= 8)
        write((byte)(paramInt >> j)); 
    } else {
      write((byte)paramInt);
    } 
  }
  
  final void write(int paramInt) throws IOException {
    this.os.write(paramInt);
  }
  
  final void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.os.write(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  final void writeElements(ASN1Encodable[] paramArrayOfASN1Encodable) throws IOException {
    int i = paramArrayOfASN1Encodable.length;
    for (byte b = 0; b < i; b++) {
      ASN1Primitive aSN1Primitive = paramArrayOfASN1Encodable[b].toASN1Primitive();
      writePrimitive(aSN1Primitive, true);
    } 
  }
  
  final void writeElements(Enumeration<ASN1Encodable> paramEnumeration) throws IOException {
    while (paramEnumeration.hasMoreElements()) {
      ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramEnumeration.nextElement()).toASN1Primitive();
      writePrimitive(aSN1Primitive, true);
    } 
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt, byte paramByte) throws IOException {
    if (paramBoolean)
      write(paramInt); 
    writeLength(1);
    write(paramByte);
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte) throws IOException {
    if (paramBoolean)
      write(paramInt); 
    writeLength(paramArrayOfbyte.length);
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) throws IOException {
    if (paramBoolean)
      write(paramInt1); 
    writeLength(paramInt3);
    write(paramArrayOfbyte, paramInt2, paramInt3);
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt, byte paramByte, byte[] paramArrayOfbyte) throws IOException {
    if (paramBoolean)
      write(paramInt); 
    writeLength(1 + paramArrayOfbyte.length);
    write(paramByte);
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt1, byte paramByte1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, byte paramByte2) throws IOException {
    if (paramBoolean)
      write(paramInt1); 
    writeLength(2 + paramInt3);
    write(paramByte1);
    write(paramArrayOfbyte, paramInt2, paramInt3);
    write(paramByte2);
  }
  
  final void writeEncoded(boolean paramBoolean, int paramInt1, int paramInt2, byte[] paramArrayOfbyte) throws IOException {
    writeTag(paramBoolean, paramInt1, paramInt2);
    writeLength(paramArrayOfbyte.length);
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  final void writeEncodedIndef(boolean paramBoolean, int paramInt1, int paramInt2, byte[] paramArrayOfbyte) throws IOException {
    writeTag(paramBoolean, paramInt1, paramInt2);
    write(128);
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
    write(0);
    write(0);
  }
  
  final void writeEncodedIndef(boolean paramBoolean, int paramInt, ASN1Encodable[] paramArrayOfASN1Encodable) throws IOException {
    if (paramBoolean)
      write(paramInt); 
    write(128);
    writeElements(paramArrayOfASN1Encodable);
    write(0);
    write(0);
  }
  
  final void writeEncodedIndef(boolean paramBoolean, int paramInt, Enumeration paramEnumeration) throws IOException {
    if (paramBoolean)
      write(paramInt); 
    write(128);
    writeElements(paramEnumeration);
    write(0);
    write(0);
  }
  
  final void writeTag(boolean paramBoolean, int paramInt1, int paramInt2) throws IOException {
    if (!paramBoolean)
      return; 
    if (paramInt2 < 31) {
      write(paramInt1 | paramInt2);
    } else {
      write(paramInt1 | 0x1F);
      if (paramInt2 < 128) {
        write(paramInt2);
      } else {
        byte[] arrayOfByte = new byte[5];
        int i = arrayOfByte.length;
        arrayOfByte[--i] = (byte)(paramInt2 & 0x7F);
        while (true) {
          paramInt2 >>= 7;
          arrayOfByte[--i] = (byte)(paramInt2 & 0x7F | 0x80);
          if (paramInt2 <= 127) {
            write(arrayOfByte, i, arrayOfByte.length - i);
            return;
          } 
        } 
      } 
    } 
  }
  
  public void writeObject(ASN1Encodable paramASN1Encodable) throws IOException {
    if (null == paramASN1Encodable)
      throw new IOException("null object detected"); 
    writePrimitive(paramASN1Encodable.toASN1Primitive(), true);
    flushInternal();
  }
  
  public void writeObject(ASN1Primitive paramASN1Primitive) throws IOException {
    if (null == paramASN1Primitive)
      throw new IOException("null object detected"); 
    writePrimitive(paramASN1Primitive, true);
    flushInternal();
  }
  
  void writePrimitive(ASN1Primitive paramASN1Primitive, boolean paramBoolean) throws IOException {
    paramASN1Primitive.encode(this, paramBoolean);
  }
  
  public void close() throws IOException {
    this.os.close();
  }
  
  public void flush() throws IOException {
    this.os.flush();
  }
  
  void flushInternal() throws IOException {}
  
  DEROutputStream getDERSubStream() {
    return new DEROutputStream(this.os);
  }
  
  ASN1OutputStream getDLSubStream() {
    return new DLOutputStream(this.os);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ASN1OutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */