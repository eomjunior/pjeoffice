/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.sun.jna.platform.win32.Kernel32;
/*    */ import com.sun.jna.platform.win32.Shell32;
/*    */ import com.sun.jna.platform.win32.ShellAPI;
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandRunner
/*    */ {
/*    */   public static void runAsAdmin(File script, String... args) throws Exception {
/* 43 */     String scriptPath = Directory.stringPath(script, true);
/* 44 */     String arguments = Strings.toString(args, ' ');
/* 45 */     throwIf((script == null || !script.exists()), scriptPath, arguments);
/* 46 */     ShellAPI.SHELLEXECUTEINFO info = new ShellAPI.SHELLEXECUTEINFO();
/* 47 */     info.lpFile = scriptPath;
/* 48 */     info.lpParameters = arguments;
/* 49 */     info.nShow = 10;
/* 50 */     info.fMask = 64;
/* 51 */     info.lpVerb = "runas";
/* 52 */     throwIf(!Shell32.INSTANCE.ShellExecuteEx(info), scriptPath, arguments);
/* 53 */     Kernel32.INSTANCE.WaitForSingleObject(info.hProcess, -1);
/*    */   }
/*    */   
/*    */   private static void throwIf(boolean condition, String scriptPath, String arguments) throws Exception {
/* 57 */     if (condition)
/* 58 */       throw new Exception("Unabled to run script '" + scriptPath + "' with args [" + arguments + "]"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/CommandRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */