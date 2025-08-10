/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class ProxySetup
/*     */ {
/*     */   private Project owner;
/*     */   public static final String USE_SYSTEM_PROXIES = "java.net.useSystemProxies";
/*     */   public static final String HTTP_PROXY_HOST = "http.proxyHost";
/*     */   public static final String HTTP_PROXY_PORT = "http.proxyPort";
/*     */   public static final String HTTPS_PROXY_HOST = "https.proxyHost";
/*     */   public static final String HTTPS_PROXY_PORT = "https.proxyPort";
/*     */   public static final String FTP_PROXY_HOST = "ftp.proxyHost";
/*     */   public static final String FTP_PROXY_PORT = "ftp.proxyPort";
/*     */   public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts";
/*     */   public static final String HTTPS_NON_PROXY_HOSTS = "https.nonProxyHosts";
/*     */   public static final String FTP_NON_PROXY_HOSTS = "ftp.nonProxyHosts";
/*     */   public static final String HTTP_PROXY_USERNAME = "http.proxyUser";
/*     */   public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
/*     */   public static final String SOCKS_PROXY_HOST = "socksProxyHost";
/*     */   public static final String SOCKS_PROXY_PORT = "socksProxyPort";
/*     */   public static final String SOCKS_PROXY_USERNAME = "java.net.socks.username";
/*     */   public static final String SOCKS_PROXY_PASSWORD = "java.net.socks.password";
/*     */   
/*     */   public ProxySetup(Project owner) {
/*  75 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSystemProxySetting() {
/*     */     try {
/*  84 */       return System.getProperty("java.net.useSystemProxies");
/*  85 */     } catch (SecurityException e) {
/*     */       
/*  87 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableProxies() {
/*  98 */     if (getSystemProxySetting() == null) {
/*  99 */       String proxies = this.owner.getProperty("java.net.useSystemProxies");
/* 100 */       if (proxies == null || Project.toBoolean(proxies)) {
/* 101 */         proxies = "true";
/*     */       }
/* 103 */       String message = "setting java.net.useSystemProxies to " + proxies;
/*     */       try {
/* 105 */         this.owner.log(message, 4);
/* 106 */         System.setProperty("java.net.useSystemProxies", proxies);
/* 107 */       } catch (SecurityException e) {
/*     */ 
/*     */         
/* 110 */         this.owner.log("Security Exception when " + message);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ProxySetup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */