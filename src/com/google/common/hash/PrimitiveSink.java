package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@ElementTypesAreNonnullByDefault
@Beta
public interface PrimitiveSink {
  @CanIgnoreReturnValue
  PrimitiveSink putByte(byte paramByte);
  
  @CanIgnoreReturnValue
  PrimitiveSink putBytes(byte[] paramArrayOfbyte);
  
  @CanIgnoreReturnValue
  PrimitiveSink putBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  @CanIgnoreReturnValue
  PrimitiveSink putBytes(ByteBuffer paramByteBuffer);
  
  @CanIgnoreReturnValue
  PrimitiveSink putShort(short paramShort);
  
  @CanIgnoreReturnValue
  PrimitiveSink putInt(int paramInt);
  
  @CanIgnoreReturnValue
  PrimitiveSink putLong(long paramLong);
  
  @CanIgnoreReturnValue
  PrimitiveSink putFloat(float paramFloat);
  
  @CanIgnoreReturnValue
  PrimitiveSink putDouble(double paramDouble);
  
  @CanIgnoreReturnValue
  PrimitiveSink putBoolean(boolean paramBoolean);
  
  @CanIgnoreReturnValue
  PrimitiveSink putChar(char paramChar);
  
  @CanIgnoreReturnValue
  PrimitiveSink putUnencodedChars(CharSequence paramCharSequence);
  
  @CanIgnoreReturnValue
  PrimitiveSink putString(CharSequence paramCharSequence, Charset paramCharset);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/PrimitiveSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */