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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class NTUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6870169797924406894L;
/*     */   private final String username;
/*     */   private final String domain;
/*     */   private final String ntname;
/*     */   
/*     */   public NTUserPrincipal(String domain, String username) {
/*  57 */     Args.notNull(username, "User name");
/*  58 */     this.username = username;
/*  59 */     if (domain != null) {
/*  60 */       this.domain = domain.toUpperCase(Locale.ROOT);
/*     */     } else {
/*  62 */       this.domain = null;
/*     */     } 
/*  64 */     if (this.domain != null && !this.domain.isEmpty()) {
/*  65 */       StringBuilder buffer = new StringBuilder();
/*  66 */       buffer.append(this.domain);
/*  67 */       buffer.append('\\');
/*  68 */       buffer.append(this.username);
/*  69 */       this.ntname = buffer.toString();
/*     */     } else {
/*  71 */       this.ntname = this.username;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  77 */     return this.ntname;
/*     */   }
/*     */   
/*     */   public String getDomain() {
/*  81 */     return this.domain;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  85 */     return this.username;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     int hash = 17;
/*  91 */     hash = LangUtils.hashCode(hash, this.username);
/*  92 */     hash = LangUtils.hashCode(hash, this.domain);
/*  93 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (this == o) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (o instanceof NTUserPrincipal) {
/* 102 */       NTUserPrincipal that = (NTUserPrincipal)o;
/* 103 */       return (Objects.equals(this.username, that.username) && 
/* 104 */         Objects.equals(this.domain, that.domain));
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     return this.ntname;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/NTUserPrincipal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */