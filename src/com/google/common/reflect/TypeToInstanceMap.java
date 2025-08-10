package com.google.common.reflect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableTypeToInstanceMap or MutableTypeToInstanceMap")
@ElementTypesAreNonnullByDefault
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
  @CheckForNull
  <T extends B> T getInstance(Class<T> paramClass);
  
  @CheckForNull
  <T extends B> T getInstance(TypeToken<T> paramTypeToken);
  
  @CheckForNull
  @CanIgnoreReturnValue
  <T extends B> T putInstance(Class<T> paramClass, @ParametricNullness T paramT);
  
  @CheckForNull
  @CanIgnoreReturnValue
  <T extends B> T putInstance(TypeToken<T> paramTypeToken, @ParametricNullness T paramT);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/TypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */