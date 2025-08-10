package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.errorprone.annotations.RestrictedApi;

@ElementTypesAreNonnullByDefault
@J2ktIncompatible
@GwtIncompatible
interface PatternCompiler {
  @RestrictedApi(explanation = "PatternCompiler is an implementation detail of com.google.common.base", allowedOnPath = ".*/com/google/common/base/.*")
  CommonPattern compile(String paramString);
  
  @RestrictedApi(explanation = "PatternCompiler is an implementation detail of com.google.common.base", allowedOnPath = ".*/com/google/common/base/.*")
  boolean isPcreLike();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/PatternCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */