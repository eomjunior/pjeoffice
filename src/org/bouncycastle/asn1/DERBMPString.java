package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.Arrays;

public class DERBMPString extends ASN1Primitive implements ASN1String {
  private final char[] string;
  
  public static DERBMPString getInstance(Object paramObject) {
    if (paramObject == null || paramObject instanceof DERBMPString)
      return (DERBMPString)paramObject; 
    if (paramObject instanceof byte[])
      try {
        return (DERBMPString)fromByteArray((byte[])paramObject);
      } catch (Exception exception) {
        throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
      }  
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }
  
  public static DERBMPString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
    return (paramBoolean || aSN1Primitive instanceof DERBMPString) ? getInstance(aSN1Primitive) : new DERBMPString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
  }
  
  DERBMPString(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      throw new NullPointerException("'string' cannot be null"); 
    int i = paramArrayOfbyte.length;
    if (0 != (i & 0x1))
      throw new IllegalArgumentException("malformed BMPString encoding encountered"); 
    int j = i / 2;
    char[] arrayOfChar = new char[j];
    for (int k = 0; k != j; k++)
      arrayOfChar[k] = (char)(paramArrayOfbyte[2 * k] << 8 | paramArrayOfbyte[2 * k + 1] & 0xFF); 
    this.string = arrayOfChar;
  }
  
  DERBMPString(char[] paramArrayOfchar) {
    if (paramArrayOfchar == null)
      throw new NullPointerException("'string' cannot be null"); 
    this.string = paramArrayOfchar;
  }
  
  public DERBMPString(String paramString) {
    if (paramString == null)
      throw new NullPointerException("'string' cannot be null"); 
    this.string = paramString.toCharArray();
  }
  
  public String getString() {
    return new String(this.string);
  }
  
  public String toString() {
    return getString();
  }
  
  public int hashCode() {
    return Arrays.hashCode(this.string);
  }
  
  protected boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
    if (!(paramASN1Primitive instanceof DERBMPString))
      return false; 
    DERBMPString dERBMPString = (DERBMPString)paramASN1Primitive;
    return Arrays.areEqual(this.string, dERBMPString.string);
  }
  
  boolean isConstructed() {
    return false;
  }
  
  int encodedLength() {
    return 1 + StreamUtil.calculateBodyLength(this.string.length * 2) + this.string.length * 2;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    int i = this.string.length;
    if (paramBoolean)
      paramASN1OutputStream.write(30); 
    paramASN1OutputStream.writeLength(i * 2);
    byte[] arrayOfByte = new byte[8];
    byte b = 0;
    int j = i & 0xFFFFFFFC;
    while (b < j) {
      char c1 = this.string[b];
      char c2 = this.string[b + 1];
      char c3 = this.string[b + 2];
      char c4 = this.string[b + 3];
      b += 4;
      arrayOfByte[0] = (byte)(c1 >> 8);
      arrayOfByte[1] = (byte)c1;
      arrayOfByte[2] = (byte)(c2 >> 8);
      arrayOfByte[3] = (byte)c2;
      arrayOfByte[4] = (byte)(c3 >> 8);
      arrayOfByte[5] = (byte)c3;
      arrayOfByte[6] = (byte)(c4 >> 8);
      arrayOfByte[7] = (byte)c4;
      paramASN1OutputStream.write(arrayOfByte, 0, 8);
    } 
    if (b < i) {
      byte b1 = 0;
      while (true) {
        char c = this.string[b];
        b++;
        arrayOfByte[b1++] = (byte)(c >> 8);
        arrayOfByte[b1++] = (byte)c;
        if (b >= i) {
          paramASN1OutputStream.write(arrayOfByte, 0, b1);
          break;
        } 
      } 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DERBMPString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */