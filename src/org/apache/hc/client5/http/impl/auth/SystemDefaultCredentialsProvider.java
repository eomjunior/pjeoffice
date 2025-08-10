/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsStore;
/*     */ import org.apache.hc.client5.http.auth.NTCredentials;
/*     */ import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class SystemDefaultCredentialsProvider
/*     */   implements CredentialsStore
/*     */ {
/*  66 */   private final BasicCredentialsProvider internal = new BasicCredentialsProvider();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCredentials(AuthScope authScope, Credentials credentials) {
/*  71 */     this.internal.setCredentials(authScope, credentials);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static PasswordAuthentication getSystemCreds(String protocol, AuthScope authScope, Authenticator.RequestorType requestorType, HttpClientContext context) {
/*     */     URL targetHostURL;
/*  79 */     HttpRequest request = (context != null) ? context.getRequest() : null;
/*     */     
/*     */     try {
/*  82 */       URI uri = (request != null) ? request.getUri() : null;
/*  83 */       targetHostURL = (uri != null) ? uri.toURL() : null;
/*  84 */     } catch (URISyntaxException|java.net.MalformedURLException ignore) {
/*  85 */       targetHostURL = null;
/*     */     } 
/*     */     
/*  88 */     return Authenticator.requestPasswordAuthentication(authScope
/*  89 */         .getHost(), null, authScope
/*     */         
/*  91 */         .getPort(), protocol, authScope
/*     */         
/*  93 */         .getRealm(), authScope
/*  94 */         .getSchemeName(), targetHostURL, requestorType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Credentials getCredentials(AuthScope authScope, HttpContext context) {
/* 101 */     Args.notNull(authScope, "Auth scope");
/* 102 */     Credentials localcreds = this.internal.getCredentials(authScope, context);
/* 103 */     if (localcreds != null) {
/* 104 */       return localcreds;
/*     */     }
/* 106 */     String host = authScope.getHost();
/* 107 */     if (host != null) {
/* 108 */       HttpClientContext clientContext = (context != null) ? HttpClientContext.adapt(context) : null;
/* 109 */       String protocol = (authScope.getProtocol() != null) ? authScope.getProtocol() : ((authScope.getPort() == 443) ? URIScheme.HTTPS.id : URIScheme.HTTP.id);
/* 110 */       PasswordAuthentication systemcreds = getSystemCreds(protocol, authScope, Authenticator.RequestorType.SERVER, clientContext);
/*     */       
/* 112 */       if (systemcreds == null) {
/* 113 */         systemcreds = getSystemCreds(protocol, authScope, Authenticator.RequestorType.PROXY, clientContext);
/*     */       }
/*     */       
/* 116 */       if (systemcreds == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         systemcreds = getProxyCredentials(URIScheme.HTTP.getId(), authScope);
/* 122 */         if (systemcreds == null) {
/* 123 */           systemcreds = getProxyCredentials(URIScheme.HTTPS.getId(), authScope);
/*     */         }
/*     */       } 
/* 126 */       if (systemcreds != null) {
/* 127 */         String domain = System.getProperty("http.auth.ntlm.domain");
/* 128 */         if (domain != null) {
/* 129 */           return (Credentials)new NTCredentials(systemcreds.getUserName(), systemcreds.getPassword(), null, domain);
/*     */         }
/* 131 */         if ("NTLM".equalsIgnoreCase(authScope.getSchemeName()))
/*     */         {
/* 133 */           return (Credentials)new NTCredentials(systemcreds
/* 134 */               .getUserName(), systemcreds.getPassword(), null, null);
/*     */         }
/* 136 */         return (Credentials)new UsernamePasswordCredentials(systemcreds.getUserName(), systemcreds.getPassword());
/*     */       } 
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */   
/*     */   private static PasswordAuthentication getProxyCredentials(String protocol, AuthScope authScope) {
/* 143 */     String proxyHost = System.getProperty(protocol + ".proxyHost");
/* 144 */     if (proxyHost == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     String proxyPort = System.getProperty(protocol + ".proxyPort");
/* 148 */     if (proxyPort == null) {
/* 149 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 153 */       AuthScope systemScope = new AuthScope(proxyHost, Integer.parseInt(proxyPort));
/* 154 */       if (authScope.match(systemScope) >= 0) {
/* 155 */         String proxyUser = System.getProperty(protocol + ".proxyUser");
/* 156 */         if (proxyUser == null) {
/* 157 */           return null;
/*     */         }
/* 159 */         String proxyPassword = System.getProperty(protocol + ".proxyPassword");
/*     */         
/* 161 */         return new PasswordAuthentication(proxyUser, (proxyPassword != null) ? proxyPassword
/* 162 */             .toCharArray() : new char[0]);
/*     */       } 
/* 164 */     } catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 172 */     this.internal.clear();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/SystemDefaultCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */