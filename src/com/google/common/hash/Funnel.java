package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;

@DoNotMock("Implement with a lambda")
@ElementTypesAreNonnullByDefault
@Beta
public interface Funnel<T> extends Serializable {
  void funnel(@ParametricNullness T paramT, PrimitiveSink paramPrimitiveSink);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Funnel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */