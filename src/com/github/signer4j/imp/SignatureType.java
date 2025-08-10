/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import com.github.signer4j.ISignatureType;
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
/*    */ public enum SignatureType
/*    */   implements ISignatureType
/*    */ {
/* 37 */   ATTACHED,
/* 38 */   DETACHED;
/*    */   static {
/* 40 */     VALUES = values();
/*    */   }
/*    */   private static final SignatureType[] VALUES;
/*    */   public String getName() {
/* 44 */     return name();
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public static SignatureType fromString(String key) {
/* 49 */     return get(key).orElse(null);
/*    */   }
/*    */   
/*    */   @JsonValue
/*    */   public String getKey() {
/* 54 */     return name();
/*    */   }
/*    */   
/*    */   public static SignatureType getDefault() {
/* 58 */     return ATTACHED;
/*    */   }
/*    */   
/*    */   public static SignatureType getOrDefault(String name) {
/* 62 */     return getOfDefault(name, getDefault());
/*    */   }
/*    */   
/*    */   public static SignatureType getOfDefault(String name, SignatureType defaultIfNot) {
/* 66 */     return get(name).orElse(defaultIfNot);
/*    */   }
/*    */   
/*    */   public static boolean isSupported(String algorithm) {
/* 70 */     return get(algorithm).isPresent();
/*    */   }
/*    */   
/*    */   public static boolean isSupported(ISignatureType algorithm) {
/* 74 */     return (algorithm != null && get(algorithm.getName()).isPresent());
/*    */   }
/*    */   
/*    */   public static Optional<SignatureType> get(String name) {
/* 78 */     for (SignatureType a : VALUES) {
/* 79 */       if (a.getName().equalsIgnoreCase(name))
/* 80 */         return Optional.of(a); 
/*    */     } 
/* 82 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SignatureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */