/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
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
/*    */ public class Browsers
/*    */ {
/*    */   public static void launch(String url) {
/* 38 */     String so = System.getProperty("os.name").toLowerCase();
/*    */     try {
/* 40 */       if (Desktop.isDesktopSupported()) {
/* 41 */         Desktop.getDesktop().browse(new URI(url));
/*    */       } else {
/* 43 */         Runtime runtime = Runtime.getRuntime();
/* 44 */         if (so.contains("mac")) {
/* 45 */           runtime.exec("open " + url);
/* 46 */         } else if (so.contains("nix") || so.contains("nux")) {
/* 47 */           runtime.exec("xdg-open " + url);
/*    */         }
/*    */       
/*    */       }
/*    */     
/* 52 */     } catch (IOException|java.net.URISyntaxException iOException) {}
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Browsers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */