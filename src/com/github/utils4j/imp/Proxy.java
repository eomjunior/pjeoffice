/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Proxy
/*     */ {
/*  35 */   private static String proxyEndereco = null;
/*  36 */   private static String proxyPorta = null;
/*  37 */   private static String proxyUsuario = null;
/*  38 */   private static String proxySenha = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getProxyEndereco() {
/*  44 */     return proxyEndereco;
/*     */   }
/*     */   
/*     */   public static void setProxyEndereco(String proxyEndereco) {
/*  48 */     Proxy.proxyEndereco = proxyEndereco;
/*     */   }
/*     */   
/*     */   public static String getProxyPorta() {
/*  52 */     return proxyPorta;
/*     */   }
/*     */   
/*     */   public static void setProxyPorta(String proxyPorta) {
/*  56 */     Proxy.proxyPorta = proxyPorta;
/*     */   }
/*     */   
/*     */   public static String getProxyUsuario() {
/*  60 */     return proxyUsuario;
/*     */   }
/*     */   
/*     */   public static void setProxyUsuario(String proxyUsuario) {
/*  64 */     Proxy.proxyUsuario = proxyUsuario;
/*     */   }
/*     */   
/*     */   public static String getProxySenha() {
/*  68 */     return proxySenha;
/*     */   }
/*     */   
/*     */   public static void setProxySenha(String proxySenha) {
/*  72 */     Proxy.proxySenha = proxySenha;
/*     */   }
/*     */   
/*     */   public static void setSystem() throws Exception {
/*  76 */     System.clearProperty("http.proxyHost");
/*  77 */     System.clearProperty("http.proxyPort");
/*  78 */     System.clearProperty("http.proxyUser");
/*  79 */     System.clearProperty("http.proxyPassword");
/*  80 */     System.clearProperty("https.proxyHost");
/*  81 */     System.clearProperty("https.proxyPort");
/*  82 */     System.clearProperty("https.proxyUser");
/*  83 */     System.clearProperty("https.proxyPassword");
/*  84 */     Authenticator.setDefault(null);
/*  85 */     System.setProperty("java.net.useSystemProxies", "true");
/*     */   }
/*     */   
/*     */   public static void setProxy() throws Exception {
/*     */     try {
/*  90 */       if (proxyEndereco == null || proxyEndereco.trim().isEmpty() || proxyPorta == null || proxyPorta.trim().isEmpty()) {
/*  91 */         throw new Exception("invalid proxy parameters");
/*     */       }
/*     */       
/*  94 */       Authenticator.setDefault(new Authenticator()
/*     */           {
/*     */             public PasswordAuthentication getPasswordAuthentication() {
/*  97 */               return new PasswordAuthentication(Proxy.proxyUsuario, Proxy.proxySenha.toCharArray());
/*     */             }
/*     */           });
/*     */       
/* 101 */       System.setProperty("http.proxyHost", proxyEndereco);
/* 102 */       System.setProperty("http.proxyPort", proxyPorta);
/* 103 */       System.setProperty("http.proxyUser", proxyUsuario);
/* 104 */       System.setProperty("http.proxyPassword", proxySenha);
/* 105 */       System.setProperty("https.proxyHost", proxyEndereco);
/* 106 */       System.setProperty("https.proxyPort", proxyPorta);
/* 107 */       System.setProperty("https.proxyUser", proxyUsuario);
/* 108 */       System.setProperty("https.proxyPassword", proxySenha);
/*     */     }
/* 110 */     catch (Exception e) {
/* 111 */       throw new Exception("unabled to set proxy", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Proxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */