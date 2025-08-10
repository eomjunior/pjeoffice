package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.DataInput;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@J2ktIncompatible
@GwtIncompatible
public interface ByteArrayDataInput extends DataInput {
  void readFully(byte[] paramArrayOfbyte);
  
  void readFully(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int skipBytes(int paramInt);
  
  @CanIgnoreReturnValue
  boolean readBoolean();
  
  @CanIgnoreReturnValue
  byte readByte();
  
  @CanIgnoreReturnValue
  int readUnsignedByte();
  
  @CanIgnoreReturnValue
  short readShort();
  
  @CanIgnoreReturnValue
  int readUnsignedShort();
  
  @CanIgnoreReturnValue
  char readChar();
  
  @CanIgnoreReturnValue
  int readInt();
  
  @CanIgnoreReturnValue
  long readLong();
  
  @CanIgnoreReturnValue
  float readFloat();
  
  @CanIgnoreReturnValue
  double readDouble();
  
  @CheckForNull
  @CanIgnoreReturnValue
  String readLine();
  
  @CanIgnoreReturnValue
  String readUTF();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/ByteArrayDataInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */