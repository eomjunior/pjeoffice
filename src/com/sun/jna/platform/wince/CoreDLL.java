/*    */ package com.sun.jna.platform.wince;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.platform.win32.WinNT;
/*    */ import com.sun.jna.win32.W32APIOptions;
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
/*    */ public interface CoreDLL
/*    */   extends WinNT, Library
/*    */ {
/* 36 */   public static final CoreDLL INSTANCE = (CoreDLL)Native.load("coredll", CoreDLL.class, W32APIOptions.UNICODE_OPTIONS);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/wince/CoreDLL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */