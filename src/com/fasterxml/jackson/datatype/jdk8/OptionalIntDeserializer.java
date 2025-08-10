/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JacksonException;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*    */ import com.fasterxml.jackson.databind.type.LogicalType;
/*    */ import java.io.IOException;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OptionalIntDeserializer
/*    */   extends BaseScalarOptionalDeserializer<OptionalInt>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 18 */   static final OptionalIntDeserializer INSTANCE = new OptionalIntDeserializer();
/*    */   
/*    */   public OptionalIntDeserializer() {
/* 21 */     super(OptionalInt.class, OptionalInt.empty());
/*    */   }
/*    */   
/*    */   public LogicalType logicalType() {
/* 25 */     return LogicalType.Integer;
/*    */   }
/*    */   public OptionalInt deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*    */     CoercionAction act;
/*    */     String text;
/* 30 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/* 31 */       return OptionalInt.of(p.getIntValue());
/*    */     }
/*    */     
/* 34 */     switch (p.currentTokenId()) {
/*    */       case 6:
/* 36 */         text = p.getText();
/* 37 */         act = _checkFromStringCoercion(ctxt, text);
/*    */         
/* 39 */         if (act == CoercionAction.AsNull || act == CoercionAction.AsEmpty) {
/* 40 */           return this._empty;
/*    */         }
/* 42 */         text = text.trim();
/*    */ 
/*    */         
/* 45 */         return OptionalInt.of(_parseIntPrimitive(ctxt, text));
/*    */       case 8:
/* 47 */         act = _checkFloatToIntCoercion(p, ctxt, this._valueClass);
/* 48 */         if (act == CoercionAction.AsNull || act == CoercionAction.AsEmpty) {
/* 49 */           return this._empty;
/*    */         }
/* 51 */         return OptionalInt.of(p.getValueAsInt());
/*    */       case 11:
/* 53 */         return this._empty;
/*    */       case 3:
/* 55 */         return (OptionalInt)_deserializeFromArray(p, ctxt);
/*    */     } 
/*    */     
/* 58 */     return (OptionalInt)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalIntDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */