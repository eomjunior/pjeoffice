/*    */ package com.github.utils4j.echo.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Networks
/*    */ {
/*    */   public static boolean isLoopbackEndpoint(String host) {
/* 35 */     host = Strings.trim(host).toLowerCase();
/* 36 */     return (host
/* 37 */       .startsWith("127.0.0.1") || host
/* 38 */       .startsWith("localhost") || host
/* 39 */       .startsWith("0:0:0:0:0:0:0:1"));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/echo/imp/Networks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */