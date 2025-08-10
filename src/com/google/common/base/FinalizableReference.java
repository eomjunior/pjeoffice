package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use an instance of one of the Finalizable*Reference classes")
@ElementTypesAreNonnullByDefault
@J2ktIncompatible
@GwtIncompatible
public interface FinalizableReference {
  void finalizeReferent();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/FinalizableReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */