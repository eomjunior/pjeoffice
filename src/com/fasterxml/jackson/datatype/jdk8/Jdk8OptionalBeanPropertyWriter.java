/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jdk8OptionalBeanPropertyWriter
/*    */   extends BeanPropertyWriter
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Object _empty;
/*    */   
/*    */   protected Jdk8OptionalBeanPropertyWriter(BeanPropertyWriter base, Object empty) {
/* 22 */     super(base);
/* 23 */     this._empty = empty;
/*    */   }
/*    */   
/*    */   protected Jdk8OptionalBeanPropertyWriter(Jdk8OptionalBeanPropertyWriter base, PropertyName newName) {
/* 27 */     super(base, newName);
/* 28 */     this._empty = base._empty;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BeanPropertyWriter _new(PropertyName newName) {
/* 33 */     return new Jdk8OptionalBeanPropertyWriter(this, newName);
/*    */   }
/*    */ 
/*    */   
/*    */   public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
/* 38 */     return (BeanPropertyWriter)new Jdk8UnwrappingOptionalBeanPropertyWriter(this, unwrapper, this._empty);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeAsField(Object bean, JsonGenerator g, SerializerProvider prov) throws Exception {
/* 44 */     if (this._nullSerializer == null) {
/* 45 */       Object value = get(bean);
/* 46 */       if (value == null || value.equals(this._empty)) {
/*    */         return;
/*    */       }
/*    */     } 
/* 50 */     super.serializeAsField(bean, g, prov);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8OptionalBeanPropertyWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */