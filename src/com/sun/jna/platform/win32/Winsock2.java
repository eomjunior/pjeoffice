/*    */ package com.sun.jna.platform.win32;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
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
/*    */ public interface Winsock2
/*    */   extends Library
/*    */ {
/* 32 */   public static final Winsock2 INSTANCE = (Winsock2)Native.load("ws2_32", Winsock2.class, W32APIOptions.ASCII_OPTIONS);
/*    */   
/*    */   int gethostname(byte[] paramArrayOfbyte, int paramInt);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/Winsock2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */