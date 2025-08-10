/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import javax.annotation.CheckForNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class Defaults
/*    */ {
/* 35 */   private static final Double DOUBLE_DEFAULT = Double.valueOf(0.0D);
/* 36 */   private static final Float FLOAT_DEFAULT = Float.valueOf(0.0F);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   public static <T> T defaultValue(Class<T> type) {
/* 46 */     Preconditions.checkNotNull(type);
/* 47 */     if (type.isPrimitive()) {
/* 48 */       if (type == boolean.class)
/* 49 */         return (T)Boolean.FALSE; 
/* 50 */       if (type == char.class)
/* 51 */         return (T)Character.valueOf(false); 
/* 52 */       if (type == byte.class)
/* 53 */         return (T)Byte.valueOf((byte)0); 
/* 54 */       if (type == short.class)
/* 55 */         return (T)Short.valueOf((short)0); 
/* 56 */       if (type == int.class)
/* 57 */         return (T)Integer.valueOf(0); 
/* 58 */       if (type == long.class)
/* 59 */         return (T)Long.valueOf(0L); 
/* 60 */       if (type == float.class)
/* 61 */         return (T)FLOAT_DEFAULT; 
/* 62 */       if (type == double.class) {
/* 63 */         return (T)DOUBLE_DEFAULT;
/*    */       }
/*    */     } 
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Defaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */