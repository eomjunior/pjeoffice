/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*    */ import com.fasterxml.jackson.databind.ser.Serializers;
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
/*    */ public class Jdk8Module
/*    */   extends Module
/*    */ {
/*    */   protected boolean _cfgHandleAbsentAsNull = false;
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 29 */     context.addSerializers((Serializers)new Jdk8Serializers());
/* 30 */     context.addDeserializers((Deserializers)new Jdk8Deserializers());
/*    */     
/* 32 */     context.addTypeModifier(new Jdk8TypeModifier());
/*    */ 
/*    */     
/* 35 */     if (this._cfgHandleAbsentAsNull) {
/* 36 */       context.addBeanSerializerModifier(new Jdk8BeanSerializerModifier());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Version version() {
/* 42 */     return PackageVersion.VERSION;
/*    */   }
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
/*    */   @Deprecated
/*    */   public Jdk8Module configureAbsentsAsNulls(boolean state) {
/* 67 */     this._cfgHandleAbsentAsNull = state;
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 73 */     return getClass().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 78 */     return (this == o);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModuleName() {
/* 83 */     return "Jdk8Module";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */