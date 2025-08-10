package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@ElementTypesAreNonnullByDefault
@Beta
public interface Hasher extends PrimitiveSink {
  @CanIgnoreReturnValue
  Hasher putByte(byte paramByte);
  
  @CanIgnoreReturnValue
  Hasher putBytes(byte[] paramArrayOfbyte);
  
  @CanIgnoreReturnValue
  Hasher putBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  @CanIgnoreReturnValue
  Hasher putBytes(ByteBuffer paramByteBuffer);
  
  @CanIgnoreReturnValue
  Hasher putShort(short paramShort);
  
  @CanIgnoreReturnValue
  Hasher putInt(int paramInt);
  
  @CanIgnoreReturnValue
  Hasher putLong(long paramLong);
  
  @CanIgnoreReturnValue
  Hasher putFloat(float paramFloat);
  
  @CanIgnoreReturnValue
  Hasher putDouble(double paramDouble);
  
  @CanIgnoreReturnValue
  Hasher putBoolean(boolean paramBoolean);
  
  @CanIgnoreReturnValue
  Hasher putChar(char paramChar);
  
  @CanIgnoreReturnValue
  Hasher putUnencodedChars(CharSequence paramCharSequence);
  
  @CanIgnoreReturnValue
  Hasher putString(CharSequence paramCharSequence, Charset paramCharset);
  
  @CanIgnoreReturnValue
  <T> Hasher putObject(@ParametricNullness T paramT, Funnel<? super T> paramFunnel);
  
  HashCode hash();
  
  @Deprecated
  int hashCode();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Hasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */