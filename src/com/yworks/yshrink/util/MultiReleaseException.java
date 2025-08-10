/*   */ package com.yworks.yshrink.util;
/*   */ 
/*   */ public class MultiReleaseException
/*   */   extends RuntimeException {
/*   */   public String getMessage() {
/* 6 */     return "Multi-release archives containing classes in META-INF are incompatible with yGuard.";
/*   */   }
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/util/MultiReleaseException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */