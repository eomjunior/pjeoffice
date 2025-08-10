/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*    */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import com.fasterxml.jackson.databind.type.TypeModifier;
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ public class Jdk8TypeModifier
/*    */   extends TypeModifier
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory) {
/*    */     JavaType refType;
/* 23 */     if (type.isReferenceType() || type.isContainerType()) {
/* 24 */       return type;
/*    */     }
/* 26 */     Class<?> raw = type.getRawClass();
/*    */ 
/*    */ 
/*    */     
/* 30 */     if (raw == Optional.class) {
/*    */ 
/*    */       
/* 33 */       refType = type.containedTypeOrUnknown(0);
/* 34 */     } else if (raw == OptionalInt.class) {
/* 35 */       refType = typeFactory.constructType(int.class);
/* 36 */     } else if (raw == OptionalLong.class) {
/* 37 */       refType = typeFactory.constructType(long.class);
/* 38 */     } else if (raw == OptionalDouble.class) {
/* 39 */       refType = typeFactory.constructType(double.class);
/*    */     } else {
/* 41 */       return type;
/*    */     } 
/* 43 */     return (JavaType)ReferenceType.upgradeFrom(type, refType);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8TypeModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */