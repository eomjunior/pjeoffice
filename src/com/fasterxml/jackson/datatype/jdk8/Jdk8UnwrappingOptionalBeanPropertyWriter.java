/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.io.SerializedString;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jdk8UnwrappingOptionalBeanPropertyWriter
/*    */   extends UnwrappingBeanPropertyWriter
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Object _empty;
/*    */   
/*    */   public Jdk8UnwrappingOptionalBeanPropertyWriter(BeanPropertyWriter base, NameTransformer transformer, Object empty) {
/* 21 */     super(base, transformer);
/* 22 */     this._empty = empty;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Jdk8UnwrappingOptionalBeanPropertyWriter(Jdk8UnwrappingOptionalBeanPropertyWriter base, NameTransformer transformer, SerializedString name) {
/* 27 */     super(base, transformer, name);
/* 28 */     this._empty = base._empty;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected UnwrappingBeanPropertyWriter _new(NameTransformer transformer, SerializedString newName) {
/* 34 */     return new Jdk8UnwrappingOptionalBeanPropertyWriter(this, transformer, newName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/* 40 */     if (this._nullSerializer == null) {
/* 41 */       Object value = get(bean);
/* 42 */       if (value == null || value.equals(this._empty)) {
/*    */         return;
/*    */       }
/*    */     } 
/* 46 */     super.serializeAsField(bean, gen, prov);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8UnwrappingOptionalBeanPropertyWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */