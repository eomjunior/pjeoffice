/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ 
/*    */ public class OptionalDoubleSerializer
/*    */   extends StdScalarSerializer<OptionalDouble>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   static final OptionalDoubleSerializer INSTANCE = new OptionalDoubleSerializer();
/*    */   
/*    */   public OptionalDoubleSerializer() {
/* 22 */     super(OptionalDouble.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty(SerializerProvider provider, OptionalDouble value) {
/* 27 */     return (value == null || !value.isPresent());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 33 */     JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(typeHint);
/* 34 */     if (v2 != null) {
/* 35 */       v2.numberType(JsonParser.NumberType.DOUBLE);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(OptionalDouble value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 43 */     if (value.isPresent()) {
/* 44 */       gen.writeNumber(value.getAsDouble());
/*    */     } else {
/* 46 */       gen.writeNull();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalDoubleSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */