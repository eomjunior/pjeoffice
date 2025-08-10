/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import com.itextpdf.text.ExceptionConverter;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.security.KeyStore;
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
/*    */ public class KeyStoreUtil
/*    */ {
/*    */   public static KeyStore loadCacertsKeyStore(String provider) {
/* 63 */     File file = new File(System.getProperty("java.home"), "lib");
/* 64 */     file = new File(file, "security");
/* 65 */     file = new File(file, "cacerts");
/* 66 */     FileInputStream fin = null; try {
/*    */       KeyStore k;
/* 68 */       fin = new FileInputStream(file);
/*    */       
/* 70 */       if (provider == null) {
/* 71 */         k = KeyStore.getInstance("JKS");
/*    */       } else {
/* 73 */         k = KeyStore.getInstance("JKS", provider);
/* 74 */       }  k.load(fin, null);
/* 75 */       return k;
/*    */     }
/* 77 */     catch (Exception e) {
/* 78 */       throw new ExceptionConverter(e);
/*    */     } finally {
/*    */       
/* 81 */       try { if (fin != null) fin.close();  } catch (Exception exception) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static KeyStore loadCacertsKeyStore() {
/* 91 */     return loadCacertsKeyStore(null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/KeyStoreUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */