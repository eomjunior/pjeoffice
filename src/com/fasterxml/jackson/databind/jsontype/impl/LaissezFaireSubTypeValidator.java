/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*    */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
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
/*    */ public final class LaissezFaireSubTypeValidator
/*    */   extends PolymorphicTypeValidator.Base
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 22 */   public static final LaissezFaireSubTypeValidator instance = new LaissezFaireSubTypeValidator();
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateBaseType(MapperConfig<?> ctxt, JavaType baseType) {
/* 26 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateSubClassName(MapperConfig<?> ctxt, JavaType baseType, String subClassName) {
/* 32 */     return PolymorphicTypeValidator.Validity.ALLOWED;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PolymorphicTypeValidator.Validity validateSubType(MapperConfig<?> ctxt, JavaType baseType, JavaType subType) {
/* 38 */     return PolymorphicTypeValidator.Validity.ALLOWED;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/jsontype/impl/LaissezFaireSubTypeValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */