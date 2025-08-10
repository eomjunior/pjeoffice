/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ 
/*    */ final class OptionalIntSerializer
/*    */   extends StdScalarSerializer<OptionalInt>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   static final OptionalIntSerializer INSTANCE = new OptionalIntSerializer();
/*    */   
/*    */   public OptionalIntSerializer() {
/* 22 */     super(OptionalInt.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider provider, OptionalInt value) {
/* 27 */     return (value == null || !value.isPresent());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 34 */     JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 35 */     if (v2 != null) {
/* 36 */       v2.numberType(JsonParser.NumberType.INT);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(OptionalInt value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 44 */     if (value.isPresent()) {
/* 45 */       gen.writeNumber(value.getAsInt());
/*    */     } else {
/* 47 */       gen.writeNull();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalIntSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */