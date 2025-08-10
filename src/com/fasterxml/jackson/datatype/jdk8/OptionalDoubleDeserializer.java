/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JacksonException;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*    */ import com.fasterxml.jackson.databind.type.LogicalType;
/*    */ import java.io.IOException;
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ 
/*    */ class OptionalDoubleDeserializer
/*    */   extends BaseScalarOptionalDeserializer<OptionalDouble>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 17 */   static final OptionalDoubleDeserializer INSTANCE = new OptionalDoubleDeserializer();
/*    */   
/*    */   public OptionalDoubleDeserializer() {
/* 20 */     super(OptionalDouble.class, OptionalDouble.empty());
/*    */   }
/*    */   
/*    */   public LogicalType logicalType() {
/* 24 */     return LogicalType.Float;
/*    */   } public OptionalDouble deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*    */     String text;
/*    */     Double specialValue;
/*    */     CoercionAction act;
/* 29 */     if (p.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
/* 30 */       return OptionalDouble.of(p.getDoubleValue());
/*    */     }
/* 32 */     switch (p.currentTokenId()) {
/*    */       case 6:
/* 34 */         text = p.getText();
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         specialValue = _checkDoubleSpecialValue(text);
/* 40 */         if (specialValue != null) {
/* 41 */           return OptionalDouble.of(specialValue.doubleValue());
/*    */         }
/* 43 */         act = _checkFromStringCoercion(ctxt, text);
/*    */         
/* 45 */         if (act == CoercionAction.AsNull || act == CoercionAction.AsEmpty) {
/* 46 */           return this._empty;
/*    */         }
/*    */ 
/*    */         
/* 50 */         text = text.trim();
/* 51 */         return OptionalDouble.of(_parseDoublePrimitive(ctxt, text));
/*    */       case 7:
/* 53 */         return OptionalDouble.of(p.getDoubleValue());
/*    */       case 11:
/* 55 */         return getNullValue(ctxt);
/*    */       case 3:
/* 57 */         return (OptionalDouble)_deserializeFromArray(p, ctxt);
/*    */     } 
/* 59 */     return (OptionalDouble)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalDoubleDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */