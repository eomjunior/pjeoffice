/*    */ package com.github.signer4j;
/*    */ 
/*    */ import javax.swing.filechooser.FileNameExtensionFilter;
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
/*    */ public interface AllowedExtensions
/*    */ {
/* 34 */   public static final FileNameExtensionFilter LIBRARIES = new FileNameExtensionFilter("Biblioteca PKCS11 do Dispositivo (*.dll,*.dylib,*.so)", new String[] { "dll", "dylib", "so", "so.1", "so.1.0.0", "so.2", "so.3", "so.3.0", "so.4", "so.8", "so.8.0", "so.1.2.2", "so.3.0.x" });
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
/* 51 */   public static final FileNameExtensionFilter CERTIFICATES = new FileNameExtensionFilter("Arquivo PKCS12 (*.p12,*.pfx)", new String[] { "p12", "pfx" });
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/AllowedExtensions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */