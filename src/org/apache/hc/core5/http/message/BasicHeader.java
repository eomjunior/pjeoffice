/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.Header;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class BasicHeader
/*     */   implements Header, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5427236326487562174L;
/*     */   private final String name;
/*     */   private final boolean sensitive;
/*     */   private final String value;
/*     */   
/*     */   public BasicHeader(String name, Object value) {
/*  59 */     this(name, value, false);
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
/*     */   public BasicHeader(String name, Object value, boolean sensitive) {
/*  73 */     this.name = (String)Args.notNull(name, "Name");
/*  74 */     this.value = Objects.toString(value, null);
/*  75 */     this.sensitive = sensitive;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  80 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  85 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/*  90 */     return this.sensitive;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     StringBuilder buf = new StringBuilder();
/*  96 */     buf.append(getName()).append(": ");
/*  97 */     if (getValue() != null) {
/*  98 */       buf.append(getValue());
/*     */     }
/* 100 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHeader clone() throws CloneNotSupportedException {
/* 105 */     return (BasicHeader)super.clone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */