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
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ 
/*    */ final class OptionalLongSerializer
/*    */   extends StdScalarSerializer<OptionalLong>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   static final OptionalLongSerializer INSTANCE = new OptionalLongSerializer();
/*    */   
/*    */   public OptionalLongSerializer() {
/* 22 */     super(OptionalLong.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider provider, OptionalLong value) {
/* 28 */     return (value == null || !value.isPresent());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 35 */     JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 36 */     if (v2 != null) {
/* 37 */       v2.numberType(JsonParser.NumberType.LONG);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(OptionalLong value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 45 */     if (value.isPresent()) {
/* 46 */       jgen.writeNumber(value.getAsLong());
/*    */     } else {
/* 48 */       jgen.writeNull();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalLongSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */