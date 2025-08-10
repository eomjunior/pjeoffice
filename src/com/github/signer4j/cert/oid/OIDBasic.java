/*    */ package com.github.signer4j.cert.oid;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
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
/*    */ class OIDBasic
/*    */ {
/*    */   private final String id;
/*    */   private final String content;
/* 44 */   private final Map<IMetadata, String> properties = new HashMap<>();
/*    */   
/*    */   protected OIDBasic(String id, String content) {
/* 47 */     this.id = Args.requireText(id, "Unabled to create OID with empty id").trim();
/* 48 */     this.content = ((String)Args.requireNonNull(content, "Unabled to create OID with null data")).trim();
/*    */   }
/*    */   
/*    */   public final String getOid() {
/* 52 */     return this.id;
/*    */   }
/*    */   
/*    */   protected final Optional<String> get(IMetadata field) {
/* 56 */     return Strings.optional(this.properties.get(field));
/*    */   }
/*    */   
/*    */   protected final String getContent() {
/* 60 */     return this.content;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setup() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void setup(IMetadata[] fields) {
/* 71 */     if (this.content.isEmpty())
/*    */       return; 
/* 73 */     Args.requireNonNull(fields, "fields is null");
/* 74 */     int it = 0;
/* 75 */     for (IMetadata f : fields) {
/* 76 */       int length = f.length();
/* 77 */       this.properties.put(f, nullIfDirty(this.content.substring(it, Math.min(it + length, this.content.length()))));
/* 78 */       it += length;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static String nullIfDirty(String value) {
/* 83 */     if (value == null)
/* 84 */       return null; 
/* 85 */     int length = (value = value.trim()).length();
/* 86 */     int i = 0;
/* 87 */     while (i < length && value.charAt(i) == '0')
/* 88 */       i++; 
/* 89 */     if (i == length)
/* 90 */       return null; 
/* 91 */     return value;
/*    */   }
/*    */   
/*    */   protected static interface IMetadata {
/*    */     int length();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OIDBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */