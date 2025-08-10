/*     */ package org.apache.hc.client5.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class NTCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7385699315228907265L;
/*     */   private final NTUserPrincipal principal;
/*     */   private final char[] password;
/*     */   private final String workstation;
/*     */   private final String netbiosDomain;
/*     */   
/*     */   public NTCredentials(String userName, char[] password, String workstation, String domain) {
/*  76 */     this(userName, password, convertHost(workstation), domain, convertDomain(domain));
/*     */   }
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
/*     */   public NTCredentials(String userName, char[] password, String workstation, String domain, String netbiosDomain) {
/*  96 */     Args.notNull(userName, "User name");
/*  97 */     this.principal = new NTUserPrincipal(domain, userName);
/*  98 */     this.password = password;
/*  99 */     if (workstation != null) {
/* 100 */       this.workstation = workstation.toUpperCase(Locale.ROOT);
/*     */     } else {
/* 102 */       this.workstation = null;
/*     */     } 
/* 104 */     this.netbiosDomain = netbiosDomain;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 109 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 113 */     return this.principal.getUsername();
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getPassword() {
/* 118 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomain() {
/* 127 */     return this.principal.getDomain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNetbiosDomain() {
/* 135 */     return this.netbiosDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWorkstation() {
/* 144 */     return this.workstation;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     int hash = 17;
/* 150 */     hash = LangUtils.hashCode(hash, this.principal);
/* 151 */     hash = LangUtils.hashCode(hash, this.workstation);
/* 152 */     hash = LangUtils.hashCode(hash, this.netbiosDomain);
/* 153 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 158 */     if (this == o) {
/* 159 */       return true;
/*     */     }
/* 161 */     if (o instanceof NTCredentials) {
/* 162 */       NTCredentials that = (NTCredentials)o;
/* 163 */       return (Objects.equals(this.principal, that.principal) && 
/* 164 */         Objects.equals(this.workstation, that.workstation) && 
/* 165 */         Objects.equals(this.netbiosDomain, that.netbiosDomain));
/*     */     } 
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     StringBuilder buffer = new StringBuilder();
/* 173 */     buffer.append("[principal: ");
/* 174 */     buffer.append(this.principal);
/* 175 */     buffer.append("][workstation: ");
/* 176 */     buffer.append(this.workstation);
/* 177 */     buffer.append("][netbiosDomain: ");
/* 178 */     buffer.append(this.netbiosDomain);
/* 179 */     buffer.append("]");
/* 180 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String stripDotSuffix(String value) {
/* 185 */     if (value == null) {
/* 186 */       return null;
/*     */     }
/* 188 */     int index = value.indexOf('.');
/* 189 */     if (index != -1) {
/* 190 */       return value.substring(0, index);
/*     */     }
/* 192 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String convertHost(String host) {
/* 197 */     return stripDotSuffix(host);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String convertDomain(String domain) {
/* 202 */     String returnString = stripDotSuffix(domain);
/* 203 */     return (returnString == null) ? returnString : returnString.toUpperCase(Locale.ROOT);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/NTCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */