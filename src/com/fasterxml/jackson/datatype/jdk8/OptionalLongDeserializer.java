/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JacksonException;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*    */ import com.fasterxml.jackson.databind.type.LogicalType;
/*    */ import java.io.IOException;
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OptionalLongDeserializer
/*    */   extends BaseScalarOptionalDeserializer<OptionalLong>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 18 */   static final OptionalLongDeserializer INSTANCE = new OptionalLongDeserializer();
/*    */   
/*    */   public OptionalLongDeserializer() {
/* 21 */     super(OptionalLong.class, OptionalLong.empty());
/*    */   }
/*    */   
/*    */   public LogicalType logicalType() {
/* 25 */     return LogicalType.Integer;
/*    */   }
/*    */   
/*    */   public OptionalLong deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*    */     CoercionAction act;
/*    */     String text;
/* 31 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/* 32 */       return OptionalLong.of(p.getLongValue());
/*    */     }
/*    */     
/* 35 */     switch (p.currentTokenId()) {
/*    */       case 6:
/* 37 */         text = p.getText();
/* 38 */         act = _checkFromStringCoercion(ctxt, text);
/*    */         
/* 40 */         if (act == CoercionAction.AsNull || act == CoercionAction.AsEmpty) {
/* 41 */           return this._empty;
/*    */         }
/* 43 */         text = text.trim();
/*    */ 
/*    */         
/* 46 */         return OptionalLong.of(_parseLongPrimitive(ctxt, text));
/*    */       case 8:
/* 48 */         act = _checkFloatToIntCoercion(p, ctxt, this._valueClass);
/* 49 */         if (act == CoercionAction.AsNull || act == CoercionAction.AsEmpty) {
/* 50 */           return this._empty;
/*    */         }
/* 52 */         return OptionalLong.of(p.getValueAsLong());
/*    */       case 11:
/* 54 */         return this._empty;
/*    */       case 3:
/* 56 */         return (OptionalLong)_deserializeFromArray(p, ctxt);
/*    */     } 
/* 58 */     return (OptionalLong)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalLongDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */