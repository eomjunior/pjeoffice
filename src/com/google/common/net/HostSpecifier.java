/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.net.InetAddress;
/*     */ import java.text.ParseException;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class HostSpecifier
/*     */ {
/*     */   private final String canonicalForm;
/*     */   
/*     */   private HostSpecifier(String canonicalForm) {
/*  53 */     this.canonicalForm = canonicalForm;
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
/*     */ 
/*     */   
/*     */   public static HostSpecifier fromValid(String specifier) {
/*  75 */     HostAndPort parsedHost = HostAndPort.fromString(specifier);
/*  76 */     Preconditions.checkArgument(!parsedHost.hasPort());
/*  77 */     String host = parsedHost.getHost();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     InetAddress addr = null;
/*     */     try {
/*  85 */       addr = InetAddresses.forString(host);
/*  86 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (addr != null) {
/*  91 */       return new HostSpecifier(InetAddresses.toUriString(addr));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     InternetDomainName domain = InternetDomainName.from(host);
/*     */     
/*  99 */     if (domain.hasPublicSuffix()) {
/* 100 */       return new HostSpecifier(domain.toString());
/*     */     }
/*     */     
/* 103 */     throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + host);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static HostSpecifier from(String specifier) throws ParseException {
/*     */     try {
/* 117 */       return fromValid(specifier);
/* 118 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       ParseException parseException = new ParseException("Invalid host specifier: " + specifier, 0);
/* 124 */       parseException.initCause(e);
/* 125 */       throw parseException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String specifier) {
/*     */     try {
/* 135 */       HostSpecifier unused = fromValid(specifier);
/* 136 */       return true;
/* 137 */     } catch (IllegalArgumentException e) {
/* 138 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object other) {
/* 144 */     if (this == other) {
/* 145 */       return true;
/*     */     }
/*     */     
/* 148 */     if (other instanceof HostSpecifier) {
/* 149 */       HostSpecifier that = (HostSpecifier)other;
/* 150 */       return this.canonicalForm.equals(that.canonicalForm);
/*     */     } 
/*     */     
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 158 */     return this.canonicalForm.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     return this.canonicalForm;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/HostSpecifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */