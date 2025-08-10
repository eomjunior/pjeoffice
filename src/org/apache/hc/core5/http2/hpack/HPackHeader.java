/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import org.apache.hc.core5.http.Header;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HPackHeader
/*     */   implements Header
/*     */ {
/*     */   private static final int ENTRY_SIZE_OVERHEAD = 32;
/*     */   private final String name;
/*     */   private final int nameLen;
/*     */   private final String value;
/*     */   private final int valueLen;
/*     */   private final boolean sensitive;
/*     */   
/*     */   HPackHeader(String name, int nameLen, String value, int valueLen, boolean sensitive) {
/*  47 */     this.name = name;
/*  48 */     this.nameLen = nameLen;
/*  49 */     this.value = value;
/*  50 */     this.valueLen = valueLen;
/*  51 */     this.sensitive = sensitive;
/*     */   }
/*     */   
/*     */   HPackHeader(String name, String value, boolean sensitive) {
/*  55 */     this(name, name.length(), value, value.length(), sensitive);
/*     */   }
/*     */   
/*     */   HPackHeader(String name, String value) {
/*  59 */     this(name, value, false);
/*     */   }
/*     */   
/*     */   HPackHeader(Header header) {
/*  63 */     this(header.getName(), header.getValue(), header.isSensitive());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  68 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getNameLen() {
/*  72 */     return this.nameLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  77 */     return this.value;
/*     */   }
/*     */   
/*     */   public int getValueLen() {
/*  81 */     return this.valueLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/*  86 */     return this.sensitive;
/*     */   }
/*     */   
/*     */   public int getTotalSize() {
/*  90 */     return this.nameLen + this.valueLen + 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     StringBuilder buf = new StringBuilder();
/*  96 */     buf.append(this.name).append(": ");
/*  97 */     if (this.value != null) {
/*  98 */       buf.append(this.value);
/*     */     }
/* 100 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HPackHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */