/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.LangUtils;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ public class BasicNameValuePair
/*     */   implements NameValuePair, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6437800749411518984L;
/*     */   private final String name;
/*     */   private final String value;
/*     */   
/*     */   public BasicNameValuePair(String name, String value) {
/*  61 */     this.name = (String)Args.notNull(name, "Name");
/*  62 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  67 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  72 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  79 */     if (this.value == null) {
/*  80 */       return this.name;
/*     */     }
/*  82 */     int len = this.name.length() + 1 + this.value.length();
/*  83 */     StringBuilder buffer = new StringBuilder(len);
/*  84 */     buffer.append(this.name);
/*  85 */     buffer.append("=");
/*  86 */     buffer.append(this.value);
/*  87 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  92 */     if (this == obj) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (obj instanceof BasicNameValuePair) {
/*  96 */       BasicNameValuePair that = (BasicNameValuePair)obj;
/*  97 */       return (this.name.equalsIgnoreCase(that.name) && Objects.equals(this.value, that.value));
/*     */     } 
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     int hash = 17;
/* 105 */     hash = LangUtils.hashCode(hash, TextUtils.toLowerCase(this.name));
/* 106 */     hash = LangUtils.hashCode(hash, this.value);
/* 107 */     return hash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicNameValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */