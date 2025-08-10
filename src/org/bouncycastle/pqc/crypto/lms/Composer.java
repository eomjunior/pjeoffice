package org.bouncycastle.pqc.crypto.lms;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.util.Encodable;

public class Composer {
  private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
  
  public static Composer compose() {
    return new Composer();
  }
  
  public Composer u64str(long paramLong) {
    u32str((int)(paramLong >>> 32L));
    u32str((int)paramLong);
    return this;
  }
  
  public Composer u32str(int paramInt) {
    this.bos.write((byte)(paramInt >>> 24));
    this.bos.write((byte)(paramInt >>> 16));
    this.bos.write((byte)(paramInt >>> 8));
    this.bos.write((byte)paramInt);
    return this;
  }
  
  public Composer u16str(int paramInt) {
    paramInt &= 0xFFFF;
    this.bos.write((byte)(paramInt >>> 8));
    this.bos.write((byte)paramInt);
    return this;
  }
  
  public Composer bytes(Encodable[] paramArrayOfEncodable) {
    try {
      for (Encodable encodable : paramArrayOfEncodable)
        this.bos.write(encodable.getEncoded()); 
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public Composer bytes(Encodable paramEncodable) {
    try {
      this.bos.write(paramEncodable.getEncoded());
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public Composer pad(int paramInt1, int paramInt2) {
    while (paramInt2 >= 0) {
      try {
        this.bos.write(paramInt1);
      } catch (Exception exception) {
        throw new RuntimeException(exception.getMessage(), exception);
      } 
      paramInt2--;
    } 
    return this;
  }
  
  public Composer bytes(byte[][] paramArrayOfbyte) {
    try {
      for (byte[] arrayOfByte : paramArrayOfbyte)
        this.bos.write(arrayOfByte); 
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public Composer bytes(byte[][] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      for (int i = paramInt1; i != paramInt2; i++)
        this.bos.write(paramArrayOfbyte[i]); 
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public Composer bytes(byte[] paramArrayOfbyte) {
    try {
      this.bos.write(paramArrayOfbyte);
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public Composer bytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      this.bos.write(paramArrayOfbyte, paramInt1, paramInt2);
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage(), exception);
    } 
    return this;
  }
  
  public byte[] build() {
    return this.bos.toByteArray();
  }
  
  public Composer padUntil(int paramInt1, int paramInt2) {
    while (this.bos.size() < paramInt2)
      this.bos.write(paramInt1); 
    return this;
  }
  
  public Composer bool(boolean paramBoolean) {
    this.bos.write(paramBoolean ? 1 : 0);
    return this;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/Composer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */