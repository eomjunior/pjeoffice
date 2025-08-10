package org.bouncycastle.asn1;

import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Properties;

public class ASN1Integer extends ASN1Primitive {
  static final int SIGN_EXT_SIGNED = -1;
  
  static final int SIGN_EXT_UNSIGNED = 255;
  
  private final byte[] bytes;
  
  private final int start;
  
  public static ASN1Integer getInstance(Object paramObject) {
    if (paramObject == null || paramObject instanceof ASN1Integer)
      return (ASN1Integer)paramObject; 
    if (paramObject instanceof byte[])
      try {
        return (ASN1Integer)fromByteArray((byte[])paramObject);
      } catch (Exception exception) {
        throw new IllegalArgumentException("encoding error in getInstance: " + exception.toString());
      }  
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }
  
  public static ASN1Integer getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
    ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
    return (paramBoolean || aSN1Primitive instanceof ASN1Integer) ? getInstance(aSN1Primitive) : new ASN1Integer(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
  }
  
  public ASN1Integer(long paramLong) {
    this.bytes = BigInteger.valueOf(paramLong).toByteArray();
    this.start = 0;
  }
  
  public ASN1Integer(BigInteger paramBigInteger) {
    this.bytes = paramBigInteger.toByteArray();
    this.start = 0;
  }
  
  public ASN1Integer(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, true);
  }
  
  ASN1Integer(byte[] paramArrayOfbyte, boolean paramBoolean) {
    if (isMalformed(paramArrayOfbyte))
      throw new IllegalArgumentException("malformed integer"); 
    this.bytes = paramBoolean ? Arrays.clone(paramArrayOfbyte) : paramArrayOfbyte;
    this.start = signBytesToSkip(paramArrayOfbyte);
  }
  
  public BigInteger getPositiveValue() {
    return new BigInteger(1, this.bytes);
  }
  
  public BigInteger getValue() {
    return new BigInteger(this.bytes);
  }
  
  public boolean hasValue(BigInteger paramBigInteger) {
    return (null != paramBigInteger && intValue(this.bytes, this.start, -1) == paramBigInteger.intValue() && getValue().equals(paramBigInteger));
  }
  
  public int intPositiveValueExact() {
    int i = this.bytes.length - this.start;
    if (i > 4 || (i == 4 && 0 != (this.bytes[this.start] & 0x80)))
      throw new ArithmeticException("ASN.1 Integer out of positive int range"); 
    return intValue(this.bytes, this.start, 255);
  }
  
  public int intValueExact() {
    int i = this.bytes.length - this.start;
    if (i > 4)
      throw new ArithmeticException("ASN.1 Integer out of int range"); 
    return intValue(this.bytes, this.start, -1);
  }
  
  public long longValueExact() {
    int i = this.bytes.length - this.start;
    if (i > 8)
      throw new ArithmeticException("ASN.1 Integer out of long range"); 
    return longValue(this.bytes, this.start, -1);
  }
  
  boolean isConstructed() {
    return false;
  }
  
  int encodedLength() {
    return 1 + StreamUtil.calculateBodyLength(this.bytes.length) + this.bytes.length;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
    paramASN1OutputStream.writeEncoded(paramBoolean, 2, this.bytes);
  }
  
  public int hashCode() {
    return Arrays.hashCode(this.bytes);
  }
  
  boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
    if (!(paramASN1Primitive instanceof ASN1Integer))
      return false; 
    ASN1Integer aSN1Integer = (ASN1Integer)paramASN1Primitive;
    return Arrays.areEqual(this.bytes, aSN1Integer.bytes);
  }
  
  public String toString() {
    return getValue().toString();
  }
  
  static int intValue(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramArrayOfbyte.length;
    int j = Math.max(paramInt1, i - 4);
    int k;
    for (k = paramArrayOfbyte[j] & paramInt2; ++j < i; k = k << 8 | paramArrayOfbyte[j] & 0xFF);
    return k;
  }
  
  static long longValue(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramArrayOfbyte.length;
    int j = Math.max(paramInt1, i - 8);
    long l;
    for (l = (paramArrayOfbyte[j] & paramInt2); ++j < i; l = l << 8L | (paramArrayOfbyte[j] & 0xFF));
    return l;
  }
  
  static boolean isMalformed(byte[] paramArrayOfbyte) {
    switch (paramArrayOfbyte.length) {
      case 0:
        return true;
      case 1:
        return false;
    } 
    return (paramArrayOfbyte[0] == paramArrayOfbyte[1] >> 7 && !Properties.isOverrideSet("org.bouncycastle.asn1.allow_unsafe_integer"));
  }
  
  static int signBytesToSkip(byte[] paramArrayOfbyte) {
    byte b = 0;
    int i = paramArrayOfbyte.length - 1;
    while (b < i && paramArrayOfbyte[b] == paramArrayOfbyte[b + 1] >> 7)
      b++; 
    return b;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ASN1Integer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */