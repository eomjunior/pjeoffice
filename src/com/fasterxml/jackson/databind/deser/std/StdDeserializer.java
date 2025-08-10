/*      */ package com.fasterxml.jackson.databind.deser.std;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.Nulls;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.StreamReadCapability;
/*      */ import com.fasterxml.jackson.core.exc.StreamReadException;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*      */ import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*      */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*      */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsAsEmptyProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsFailProvider;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.type.LogicalType;
/*      */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class StdDeserializer<T>
/*      */   extends JsonDeserializer<T>
/*      */   implements Serializable, ValueInstantiator.Gettable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   51 */   protected static final int F_MASK_INT_COERCIONS = DeserializationFeature.USE_BIG_INTEGER_FOR_INTS
/*   52 */     .getMask() | DeserializationFeature.USE_LONG_FOR_INTS
/*   53 */     .getMask();
/*      */   
/*      */   @Deprecated
/*   56 */   protected static final int F_MASK_ACCEPT_ARRAYS = DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS
/*   57 */     .getMask() | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT
/*   58 */     .getMask();
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Class<?> _valueClass;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _valueType;
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(Class<?> vc) {
/*   71 */     this._valueClass = vc;
/*   72 */     this._valueType = null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected StdDeserializer(JavaType valueType) {
/*   77 */     this._valueClass = (valueType == null) ? Object.class : valueType.getRawClass();
/*   78 */     this._valueType = valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(StdDeserializer<?> src) {
/*   88 */     this._valueClass = src._valueClass;
/*   89 */     this._valueType = src._valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> handledType() {
/*   99 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getValueClass() {
/*  111 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */   
/*      */   public JavaType getValueType() {
/*  116 */     return this._valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaType getValueType(DeserializationContext ctxt) {
/*  132 */     if (this._valueType != null) {
/*  133 */       return this._valueType;
/*      */     }
/*  135 */     return ctxt.constructType(this._valueClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ValueInstantiator getValueInstantiator() {
/*  142 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
/*  151 */     return ClassUtil.isJacksonStdImpl(deserializer);
/*      */   }
/*      */   
/*      */   protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
/*  155 */     return ClassUtil.isJacksonStdImpl(keyDeser);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/*  172 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected T _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  200 */     CoercionAction act = _findCoercionFromEmptyArray(ctxt);
/*  201 */     boolean unwrap = ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
/*      */     
/*  203 */     if (unwrap || act != CoercionAction.Fail) {
/*  204 */       JsonToken t = p.nextToken();
/*  205 */       if (t == JsonToken.END_ARRAY) {
/*  206 */         switch (act) {
/*      */           case AsEmpty:
/*  208 */             return (T)getEmptyValue(ctxt);
/*      */           case AsNull:
/*      */           case TryConvert:
/*  211 */             return (T)getNullValue(ctxt);
/*      */         } 
/*      */       
/*  214 */       } else if (unwrap) {
/*  215 */         T parsed = _deserializeWrappedValue(p, ctxt);
/*  216 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/*  217 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  219 */         return parsed;
/*      */       } 
/*      */     } 
/*  222 */     return (T)ctxt.handleUnexpectedToken(getValueType(ctxt), JsonToken.START_ARRAY, p, null, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected T _deserializeFromEmpty(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  238 */     if (p.hasToken(JsonToken.START_ARRAY) && 
/*  239 */       ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  240 */       JsonToken t = p.nextToken();
/*  241 */       if (t == JsonToken.END_ARRAY) {
/*  242 */         return null;
/*      */       }
/*  244 */       return (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */     } 
/*      */     
/*  247 */     return (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected T _deserializeFromString(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  260 */     ValueInstantiator inst = getValueInstantiator();
/*  261 */     Class<?> rawTargetType = handledType();
/*  262 */     String value = p.getValueAsString();
/*      */     
/*  264 */     if (inst != null && inst.canCreateFromString()) {
/*  265 */       return (T)inst.createFromString(ctxt, value);
/*      */     }
/*  267 */     if (value.isEmpty()) {
/*  268 */       CoercionAction act = ctxt.findCoercionAction(logicalType(), rawTargetType, CoercionInputShape.EmptyString);
/*      */       
/*  270 */       return (T)_deserializeFromEmptyString(p, ctxt, act, rawTargetType, "empty String (\"\")");
/*      */     } 
/*      */     
/*  273 */     if (_isBlank(value)) {
/*  274 */       CoercionAction act = ctxt.findCoercionFromBlankString(logicalType(), rawTargetType, CoercionAction.Fail);
/*      */       
/*  276 */       return (T)_deserializeFromEmptyString(p, ctxt, act, rawTargetType, "blank String (all whitespace)");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  283 */     if (inst != null) {
/*  284 */       value = value.trim();
/*  285 */       if (inst.canCreateFromInt() && 
/*  286 */         ctxt.findCoercionAction(LogicalType.Integer, Integer.class, CoercionInputShape.String) == CoercionAction.TryConvert)
/*      */       {
/*  288 */         return (T)inst.createFromInt(ctxt, _parseIntPrimitive(ctxt, value));
/*      */       }
/*      */       
/*  291 */       if (inst.canCreateFromLong() && 
/*  292 */         ctxt.findCoercionAction(LogicalType.Integer, Long.class, CoercionInputShape.String) == CoercionAction.TryConvert)
/*      */       {
/*  294 */         return (T)inst.createFromLong(ctxt, _parseLongPrimitive(ctxt, value));
/*      */       }
/*      */       
/*  297 */       if (inst.canCreateFromBoolean())
/*      */       {
/*  299 */         if (ctxt.findCoercionAction(LogicalType.Boolean, Boolean.class, CoercionInputShape.String) == CoercionAction.TryConvert) {
/*      */           
/*  301 */           String str = value.trim();
/*  302 */           if ("true".equals(str)) {
/*  303 */             return (T)inst.createFromBoolean(ctxt, true);
/*      */           }
/*  305 */           if ("false".equals(str)) {
/*  306 */             return (T)inst.createFromBoolean(ctxt, false);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  311 */     return (T)ctxt.handleMissingInstantiator(rawTargetType, inst, ctxt.getParser(), "no String-argument constructor/factory method to deserialize from String value ('%s')", new Object[] { value });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _deserializeFromEmptyString(JsonParser p, DeserializationContext ctxt, CoercionAction act, Class<?> rawTargetType, String desc) throws IOException {
/*  320 */     switch (act) {
/*      */       case AsEmpty:
/*  322 */         return getEmptyValue(ctxt);
/*      */       
/*      */       case Fail:
/*  325 */         _checkCoercionFail(ctxt, act, rawTargetType, "", "empty String (\"\")");
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  331 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected T _deserializeWrappedValue(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  359 */     if (p.hasToken(JsonToken.START_ARRAY)) {
/*      */       
/*  361 */       T result = (T)handleNestedArrayForSingle(p, ctxt);
/*  362 */       return result;
/*      */     } 
/*  364 */     return (T)deserialize(p, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected final boolean _parseBooleanPrimitive(DeserializationContext ctxt, JsonParser p, Class<?> targetType) throws IOException {
/*  378 */     return _parseBooleanPrimitive(p, ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _parseBooleanPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  389 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  391 */         text = p.getText();
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 7:
/*  397 */         return Boolean.TRUE.equals(_coerceBooleanFromInt(p, ctxt, boolean.class));
/*      */       case 9:
/*  399 */         return true;
/*      */       case 10:
/*  401 */         return false;
/*      */       case 11:
/*  403 */         _verifyNullForPrimitive(ctxt);
/*  404 */         return false;
/*      */       
/*      */       case 1:
/*  407 */         text = ctxt.extractScalarFromObject(p, this, boolean.class);
/*      */         break;
/*      */       
/*      */       case 3:
/*  411 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  412 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/*  413 */             return ((Boolean)handleNestedArrayForSingle(p, ctxt)).booleanValue();
/*      */           }
/*  415 */           boolean parsed = _parseBooleanPrimitive(p, ctxt);
/*  416 */           _verifyEndArrayForSingle(p, ctxt);
/*  417 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/*  421 */         return ((Boolean)ctxt.handleUnexpectedToken(boolean.class, p)).booleanValue();
/*      */     } 
/*      */     
/*  424 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Boolean, boolean.class);
/*      */     
/*  426 */     if (act == CoercionAction.AsNull) {
/*  427 */       _verifyNullForPrimitive(ctxt);
/*  428 */       return false;
/*      */     } 
/*  430 */     if (act == CoercionAction.AsEmpty) {
/*  431 */       return false;
/*      */     }
/*  433 */     String text = text.trim();
/*  434 */     int len = text.length();
/*      */ 
/*      */ 
/*      */     
/*  438 */     if (len == 4) {
/*  439 */       if (_isTrue(text)) {
/*  440 */         return true;
/*      */       }
/*  442 */     } else if (len == 5 && 
/*  443 */       _isFalse(text)) {
/*  444 */       return false;
/*      */     } 
/*      */     
/*  447 */     if (_hasTextualNull(text)) {
/*  448 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/*  449 */       return false;
/*      */     } 
/*  451 */     Boolean b = (Boolean)ctxt.handleWeirdStringValue(boolean.class, text, "only \"true\"/\"True\"/\"TRUE\" or \"false\"/\"False\"/\"FALSE\" recognized", new Object[0]);
/*      */     
/*  453 */     return Boolean.TRUE.equals(b);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean _isTrue(String text) {
/*  458 */     char c = text.charAt(0);
/*  459 */     if (c == 't') {
/*  460 */       return "true".equals(text);
/*      */     }
/*  462 */     if (c == 'T') {
/*  463 */       return ("TRUE".equals(text) || "True".equals(text));
/*      */     }
/*  465 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean _isFalse(String text) {
/*  469 */     char c = text.charAt(0);
/*  470 */     if (c == 'f') {
/*  471 */       return "false".equals(text);
/*      */     }
/*  473 */     if (c == 'F') {
/*  474 */       return ("FALSE".equals(text) || "False".equals(text));
/*      */     }
/*  476 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Boolean _parseBoolean(JsonParser p, DeserializationContext ctxt, Class<?> targetType) throws IOException {
/*  502 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  504 */         text = p.getText();
/*      */         break;
/*      */       
/*      */       case 7:
/*  508 */         return _coerceBooleanFromInt(p, ctxt, targetType);
/*      */       case 9:
/*  510 */         return Boolean.valueOf(true);
/*      */       case 10:
/*  512 */         return Boolean.valueOf(false);
/*      */       case 11:
/*  514 */         return null;
/*      */       
/*      */       case 1:
/*  517 */         text = ctxt.extractScalarFromObject(p, this, targetType);
/*      */         break;
/*      */       case 3:
/*  520 */         return (Boolean)_deserializeFromArray(p, ctxt);
/*      */       default:
/*  522 */         return (Boolean)ctxt.handleUnexpectedToken(targetType, p);
/*      */     } 
/*      */     
/*  525 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Boolean, targetType);
/*      */     
/*  527 */     if (act == CoercionAction.AsNull) {
/*  528 */       return null;
/*      */     }
/*  530 */     if (act == CoercionAction.AsEmpty) {
/*  531 */       return Boolean.valueOf(false);
/*      */     }
/*  533 */     String text = text.trim();
/*  534 */     int len = text.length();
/*      */ 
/*      */ 
/*      */     
/*  538 */     if (len == 4) {
/*  539 */       if (_isTrue(text)) {
/*  540 */         return Boolean.valueOf(true);
/*      */       }
/*  542 */     } else if (len == 5 && 
/*  543 */       _isFalse(text)) {
/*  544 */       return Boolean.valueOf(false);
/*      */     } 
/*      */     
/*  547 */     if (_checkTextualNull(ctxt, text)) {
/*  548 */       return null;
/*      */     }
/*  550 */     return (Boolean)ctxt.handleWeirdStringValue(targetType, text, "only \"true\" or \"false\" recognized", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final byte _parseBytePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     int value;
/*  558 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  560 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  563 */         act = _checkFloatToIntCoercion(p, ctxt, byte.class);
/*  564 */         if (act == CoercionAction.AsNull) {
/*  565 */           return 0;
/*      */         }
/*  567 */         if (act == CoercionAction.AsEmpty) {
/*  568 */           return 0;
/*      */         }
/*  570 */         return p.getByteValue();
/*      */       case 7:
/*  572 */         return p.getByteValue();
/*      */       case 11:
/*  574 */         _verifyNullForPrimitive(ctxt);
/*  575 */         return 0;
/*      */       
/*      */       case 1:
/*  578 */         text = ctxt.extractScalarFromObject(p, this, byte.class);
/*      */         break;
/*      */       
/*      */       case 3:
/*  582 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  583 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/*  584 */             return ((Byte)handleNestedArrayForSingle(p, ctxt)).byteValue();
/*      */           }
/*  586 */           byte parsed = _parseBytePrimitive(p, ctxt);
/*  587 */           _verifyEndArrayForSingle(p, ctxt);
/*  588 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/*  592 */         return ((Byte)ctxt.handleUnexpectedToken(ctxt.constructType(byte.class), p)).byteValue();
/*      */     } 
/*      */ 
/*      */     
/*  596 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, byte.class);
/*      */     
/*  598 */     if (act == CoercionAction.AsNull) {
/*      */       
/*  600 */       _verifyNullForPrimitive(ctxt);
/*  601 */       return 0;
/*      */     } 
/*  603 */     if (act == CoercionAction.AsEmpty) {
/*  604 */       return 0;
/*      */     }
/*  606 */     String text = text.trim();
/*  607 */     if (_hasTextualNull(text)) {
/*  608 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/*  609 */       return 0;
/*      */     } 
/*      */     
/*      */     try {
/*  613 */       value = NumberInput.parseInt(text);
/*  614 */     } catch (IllegalArgumentException iae) {
/*  615 */       return ((Byte)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid `byte` value", new Object[0])).byteValue();
/*      */     } 
/*      */ 
/*      */     
/*  619 */     if (_byteOverflow(value)) {
/*  620 */       return ((Byte)ctxt.handleWeirdStringValue(this._valueClass, text, "overflow, value cannot be represented as 8-bit value", new Object[0])).byteValue();
/*      */     }
/*      */     
/*  623 */     return (byte)value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final short _parseShortPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     int value;
/*  630 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  632 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  635 */         act = _checkFloatToIntCoercion(p, ctxt, short.class);
/*  636 */         if (act == CoercionAction.AsNull) {
/*  637 */           return 0;
/*      */         }
/*  639 */         if (act == CoercionAction.AsEmpty) {
/*  640 */           return 0;
/*      */         }
/*  642 */         return p.getShortValue();
/*      */       case 7:
/*  644 */         return p.getShortValue();
/*      */       case 11:
/*  646 */         _verifyNullForPrimitive(ctxt);
/*  647 */         return 0;
/*      */       
/*      */       case 1:
/*  650 */         text = ctxt.extractScalarFromObject(p, this, short.class);
/*      */         break;
/*      */       
/*      */       case 3:
/*  654 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  655 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/*  656 */             return ((Short)handleNestedArrayForSingle(p, ctxt)).shortValue();
/*      */           }
/*  658 */           short parsed = _parseShortPrimitive(p, ctxt);
/*  659 */           _verifyEndArrayForSingle(p, ctxt);
/*  660 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/*  664 */         return ((Short)ctxt.handleUnexpectedToken(ctxt.constructType(short.class), p)).shortValue();
/*      */     } 
/*      */     
/*  667 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, short.class);
/*      */     
/*  669 */     if (act == CoercionAction.AsNull) {
/*      */       
/*  671 */       _verifyNullForPrimitive(ctxt);
/*  672 */       return 0;
/*      */     } 
/*  674 */     if (act == CoercionAction.AsEmpty) {
/*  675 */       return 0;
/*      */     }
/*  677 */     String text = text.trim();
/*  678 */     if (_hasTextualNull(text)) {
/*  679 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/*  680 */       return 0;
/*      */     } 
/*      */     
/*      */     try {
/*  684 */       value = NumberInput.parseInt(text);
/*  685 */     } catch (IllegalArgumentException iae) {
/*  686 */       return ((Short)ctxt.handleWeirdStringValue(short.class, text, "not a valid `short` value", new Object[0])).shortValue();
/*      */     } 
/*      */     
/*  689 */     if (_shortOverflow(value)) {
/*  690 */       return ((Short)ctxt.handleWeirdStringValue(short.class, text, "overflow, value cannot be represented as 16-bit value", new Object[0])).shortValue();
/*      */     }
/*      */     
/*  693 */     return (short)value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  700 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  702 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  705 */         act = _checkFloatToIntCoercion(p, ctxt, int.class);
/*  706 */         if (act == CoercionAction.AsNull) {
/*  707 */           return 0;
/*      */         }
/*  709 */         if (act == CoercionAction.AsEmpty) {
/*  710 */           return 0;
/*      */         }
/*  712 */         return p.getValueAsInt();
/*      */       case 7:
/*  714 */         return p.getIntValue();
/*      */       case 11:
/*  716 */         _verifyNullForPrimitive(ctxt);
/*  717 */         return 0;
/*      */       
/*      */       case 1:
/*  720 */         text = ctxt.extractScalarFromObject(p, this, int.class);
/*      */         break;
/*      */       case 3:
/*  723 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  724 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/*  725 */             return ((Integer)handleNestedArrayForSingle(p, ctxt)).intValue();
/*      */           }
/*  727 */           int parsed = _parseIntPrimitive(p, ctxt);
/*  728 */           _verifyEndArrayForSingle(p, ctxt);
/*  729 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/*  733 */         return ((Number)ctxt.handleUnexpectedToken(int.class, p)).intValue();
/*      */     } 
/*      */     
/*  736 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, int.class);
/*      */     
/*  738 */     if (act == CoercionAction.AsNull) {
/*      */       
/*  740 */       _verifyNullForPrimitive(ctxt);
/*  741 */       return 0;
/*      */     } 
/*  743 */     if (act == CoercionAction.AsEmpty) {
/*  744 */       return 0;
/*      */     }
/*  746 */     String text = text.trim();
/*  747 */     if (_hasTextualNull(text)) {
/*  748 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/*  749 */       return 0;
/*      */     } 
/*  751 */     return _parseIntPrimitive(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  760 */       if (text.length() > 9) {
/*  761 */         long l = NumberInput.parseLong(text);
/*  762 */         if (_intOverflow(l)) {
/*  763 */           Number v = (Number)ctxt.handleWeirdStringValue(int.class, text, "Overflow: numeric value (%s) out of range of int (%d -%d)", new Object[] { text, 
/*      */                 
/*  765 */                 Integer.valueOf(-2147483648), Integer.valueOf(2147483647) });
/*  766 */           return _nonNullNumber(v).intValue();
/*      */         } 
/*  768 */         return (int)l;
/*      */       } 
/*  770 */       return NumberInput.parseInt(text);
/*  771 */     } catch (IllegalArgumentException iae) {
/*  772 */       Number v = (Number)ctxt.handleWeirdStringValue(int.class, text, "not a valid `int` value", new Object[0]);
/*      */       
/*  774 */       return _nonNullNumber(v).intValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Integer _parseInteger(JsonParser p, DeserializationContext ctxt, Class<?> targetType) throws IOException {
/*  786 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  788 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  791 */         act = _checkFloatToIntCoercion(p, ctxt, targetType);
/*  792 */         if (act == CoercionAction.AsNull) {
/*  793 */           return (Integer)getNullValue(ctxt);
/*      */         }
/*  795 */         if (act == CoercionAction.AsEmpty) {
/*  796 */           return (Integer)getEmptyValue(ctxt);
/*      */         }
/*  798 */         return Integer.valueOf(p.getValueAsInt());
/*      */       case 7:
/*  800 */         return Integer.valueOf(p.getIntValue());
/*      */       case 11:
/*  802 */         return (Integer)getNullValue(ctxt);
/*      */       
/*      */       case 1:
/*  805 */         text = ctxt.extractScalarFromObject(p, this, targetType);
/*      */         break;
/*      */       case 3:
/*  808 */         return (Integer)_deserializeFromArray(p, ctxt);
/*      */       default:
/*  810 */         return (Integer)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */     } 
/*      */     
/*  813 */     CoercionAction act = _checkFromStringCoercion(ctxt, text);
/*  814 */     if (act == CoercionAction.AsNull) {
/*  815 */       return (Integer)getNullValue(ctxt);
/*      */     }
/*  817 */     if (act == CoercionAction.AsEmpty) {
/*  818 */       return (Integer)getEmptyValue(ctxt);
/*      */     }
/*  820 */     String text = text.trim();
/*  821 */     if (_checkTextualNull(ctxt, text)) {
/*  822 */       return (Integer)getNullValue(ctxt);
/*      */     }
/*  824 */     return _parseInteger(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Integer _parseInteger(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  833 */       if (text.length() > 9) {
/*  834 */         long l = NumberInput.parseLong(text);
/*  835 */         if (_intOverflow(l)) {
/*  836 */           return (Integer)ctxt.handleWeirdStringValue(Integer.class, text, "Overflow: numeric value (%s) out of range of `java.lang.Integer` (%d -%d)", new Object[] { text, 
/*      */                 
/*  838 */                 Integer.valueOf(-2147483648), Integer.valueOf(2147483647) });
/*      */         }
/*  840 */         return Integer.valueOf((int)l);
/*      */       } 
/*  842 */       return Integer.valueOf(NumberInput.parseInt(text));
/*  843 */     } catch (IllegalArgumentException iae) {
/*  844 */       return (Integer)ctxt.handleWeirdStringValue(Integer.class, text, "not a valid `java.lang.Integer` value", new Object[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  853 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  855 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  858 */         act = _checkFloatToIntCoercion(p, ctxt, long.class);
/*  859 */         if (act == CoercionAction.AsNull) {
/*  860 */           return 0L;
/*      */         }
/*  862 */         if (act == CoercionAction.AsEmpty) {
/*  863 */           return 0L;
/*      */         }
/*  865 */         return p.getValueAsLong();
/*      */       case 7:
/*  867 */         return p.getLongValue();
/*      */       case 11:
/*  869 */         _verifyNullForPrimitive(ctxt);
/*  870 */         return 0L;
/*      */       
/*      */       case 1:
/*  873 */         text = ctxt.extractScalarFromObject(p, this, long.class);
/*      */         break;
/*      */       case 3:
/*  876 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  877 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/*  878 */             return ((Long)handleNestedArrayForSingle(p, ctxt)).longValue();
/*      */           }
/*  880 */           long parsed = _parseLongPrimitive(p, ctxt);
/*  881 */           _verifyEndArrayForSingle(p, ctxt);
/*  882 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/*  886 */         return ((Number)ctxt.handleUnexpectedToken(long.class, p)).longValue();
/*      */     } 
/*      */     
/*  889 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, long.class);
/*      */     
/*  891 */     if (act == CoercionAction.AsNull) {
/*      */       
/*  893 */       _verifyNullForPrimitive(ctxt);
/*  894 */       return 0L;
/*      */     } 
/*  896 */     if (act == CoercionAction.AsEmpty) {
/*  897 */       return 0L;
/*      */     }
/*  899 */     String text = text.trim();
/*  900 */     if (_hasTextualNull(text)) {
/*  901 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/*  902 */       return 0L;
/*      */     } 
/*  904 */     return _parseLongPrimitive(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  913 */       return NumberInput.parseLong(text);
/*  914 */     } catch (IllegalArgumentException illegalArgumentException) {
/*      */       
/*  916 */       Number v = (Number)ctxt.handleWeirdStringValue(long.class, text, "not a valid `long` value", new Object[0]);
/*      */       
/*  918 */       return _nonNullNumber(v).longValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Long _parseLong(JsonParser p, DeserializationContext ctxt, Class<?> targetType) throws IOException {
/*  930 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  932 */         text = p.getText();
/*      */         break;
/*      */       case 8:
/*  935 */         act = _checkFloatToIntCoercion(p, ctxt, targetType);
/*  936 */         if (act == CoercionAction.AsNull) {
/*  937 */           return (Long)getNullValue(ctxt);
/*      */         }
/*  939 */         if (act == CoercionAction.AsEmpty) {
/*  940 */           return (Long)getEmptyValue(ctxt);
/*      */         }
/*  942 */         return Long.valueOf(p.getValueAsLong());
/*      */       case 11:
/*  944 */         return (Long)getNullValue(ctxt);
/*      */       case 7:
/*  946 */         return Long.valueOf(p.getLongValue());
/*      */       
/*      */       case 1:
/*  949 */         text = ctxt.extractScalarFromObject(p, this, targetType);
/*      */         break;
/*      */       case 3:
/*  952 */         return (Long)_deserializeFromArray(p, ctxt);
/*      */       default:
/*  954 */         return (Long)ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */     } 
/*      */     
/*  957 */     CoercionAction act = _checkFromStringCoercion(ctxt, text);
/*  958 */     if (act == CoercionAction.AsNull) {
/*  959 */       return (Long)getNullValue(ctxt);
/*      */     }
/*  961 */     if (act == CoercionAction.AsEmpty) {
/*  962 */       return (Long)getEmptyValue(ctxt);
/*      */     }
/*  964 */     String text = text.trim();
/*  965 */     if (_checkTextualNull(ctxt, text)) {
/*  966 */       return (Long)getNullValue(ctxt);
/*      */     }
/*      */     
/*  969 */     return _parseLong(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Long _parseLong(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  978 */       return Long.valueOf(NumberInput.parseLong(text));
/*  979 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  980 */       return (Long)ctxt.handleWeirdStringValue(Long.class, text, "not a valid `java.lang.Long` value", new Object[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  988 */     switch (p.currentTokenId()) {
/*      */       case 6:
/*  990 */         text = p.getText();
/*      */         break;
/*      */       case 7:
/*      */       case 8:
/*  994 */         return p.getFloatValue();
/*      */       case 11:
/*  996 */         _verifyNullForPrimitive(ctxt);
/*  997 */         return 0.0F;
/*      */       
/*      */       case 1:
/* 1000 */         text = ctxt.extractScalarFromObject(p, this, float.class);
/*      */         break;
/*      */       case 3:
/* 1003 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 1004 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/* 1005 */             return ((Float)handleNestedArrayForSingle(p, ctxt)).floatValue();
/*      */           }
/* 1007 */           float parsed = _parseFloatPrimitive(p, ctxt);
/* 1008 */           _verifyEndArrayForSingle(p, ctxt);
/* 1009 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/* 1013 */         return ((Number)ctxt.handleUnexpectedToken(float.class, p)).floatValue();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1020 */     Float nan = _checkFloatSpecialValue(text);
/* 1021 */     if (nan != null) {
/* 1022 */       return nan.floatValue();
/*      */     }
/*      */ 
/*      */     
/* 1026 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, float.class);
/*      */     
/* 1028 */     if (act == CoercionAction.AsNull) {
/*      */       
/* 1030 */       _verifyNullForPrimitive(ctxt);
/* 1031 */       return 0.0F;
/*      */     } 
/* 1033 */     if (act == CoercionAction.AsEmpty) {
/* 1034 */       return 0.0F;
/*      */     }
/* 1036 */     String text = text.trim();
/* 1037 */     if (_hasTextualNull(text)) {
/* 1038 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/* 1039 */       return 0.0F;
/*      */     } 
/* 1041 */     return _parseFloatPrimitive(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/* 1051 */       return Float.parseFloat(text);
/* 1052 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 1053 */       Number v = (Number)ctxt.handleWeirdStringValue(float.class, text, "not a valid `float` value", new Object[0]);
/*      */       
/* 1055 */       return _nonNullNumber(v).floatValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Float _checkFloatSpecialValue(String text) {
/* 1072 */     if (!text.isEmpty()) {
/* 1073 */       switch (text.charAt(0)) {
/*      */         case 'I':
/* 1075 */           if (_isPosInf(text)) {
/* 1076 */             return Float.valueOf(Float.POSITIVE_INFINITY);
/*      */           }
/*      */           break;
/*      */         case 'N':
/* 1080 */           if (_isNaN(text)) return Float.valueOf(Float.NaN); 
/*      */           break;
/*      */         case '-':
/* 1083 */           if (_isNegInf(text)) {
/* 1084 */             return Float.valueOf(Float.NEGATIVE_INFINITY);
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     }
/* 1090 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final double _parseDoublePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1097 */     switch (p.currentTokenId()) {
/*      */       case 6:
/* 1099 */         text = p.getText();
/*      */         break;
/*      */       case 7:
/*      */       case 8:
/* 1103 */         return p.getDoubleValue();
/*      */       case 11:
/* 1105 */         _verifyNullForPrimitive(ctxt);
/* 1106 */         return 0.0D;
/*      */       
/*      */       case 1:
/* 1109 */         text = ctxt.extractScalarFromObject(p, this, double.class);
/*      */         break;
/*      */       case 3:
/* 1112 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 1113 */           if (p.nextToken() == JsonToken.START_ARRAY) {
/* 1114 */             return ((Double)handleNestedArrayForSingle(p, ctxt)).doubleValue();
/*      */           }
/* 1116 */           double parsed = _parseDoublePrimitive(p, ctxt);
/* 1117 */           _verifyEndArrayForSingle(p, ctxt);
/* 1118 */           return parsed;
/*      */         } 
/*      */       
/*      */       default:
/* 1122 */         return ((Number)ctxt.handleUnexpectedToken(double.class, p)).doubleValue();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1129 */     Double nan = _checkDoubleSpecialValue(text);
/* 1130 */     if (nan != null) {
/* 1131 */       return nan.doubleValue();
/*      */     }
/*      */ 
/*      */     
/* 1135 */     CoercionAction act = _checkFromStringCoercion(ctxt, text, LogicalType.Integer, double.class);
/*      */     
/* 1137 */     if (act == CoercionAction.AsNull) {
/*      */       
/* 1139 */       _verifyNullForPrimitive(ctxt);
/* 1140 */       return 0.0D;
/*      */     } 
/* 1142 */     if (act == CoercionAction.AsEmpty) {
/* 1143 */       return 0.0D;
/*      */     }
/* 1145 */     String text = text.trim();
/* 1146 */     if (_hasTextualNull(text)) {
/* 1147 */       _verifyNullForPrimitiveCoercion(ctxt, text);
/* 1148 */       return 0.0D;
/*      */     } 
/* 1150 */     return _parseDoublePrimitive(ctxt, text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final double _parseDoublePrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/* 1160 */       return _parseDouble(text);
/* 1161 */     } catch (IllegalArgumentException illegalArgumentException) {
/* 1162 */       Number v = (Number)ctxt.handleWeirdStringValue(double.class, text, "not a valid `double` value (as String to convert)", new Object[0]);
/*      */       
/* 1164 */       return _nonNullNumber(v).doubleValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final double _parseDouble(String numStr) throws NumberFormatException {
/* 1174 */     if ("2.2250738585072012e-308".equals(numStr)) {
/* 1175 */       return 2.2250738585072014E-308D;
/*      */     }
/* 1177 */     return Double.parseDouble(numStr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Double _checkDoubleSpecialValue(String text) {
/* 1194 */     if (!text.isEmpty()) {
/* 1195 */       switch (text.charAt(0)) {
/*      */         case 'I':
/* 1197 */           if (_isPosInf(text)) {
/* 1198 */             return Double.valueOf(Double.POSITIVE_INFINITY);
/*      */           }
/*      */           break;
/*      */         case 'N':
/* 1202 */           if (_isNaN(text)) {
/* 1203 */             return Double.valueOf(Double.NaN);
/*      */           }
/*      */           break;
/*      */         case '-':
/* 1207 */           if (_isNegInf(text)) {
/* 1208 */             return Double.valueOf(Double.NEGATIVE_INFINITY);
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     }
/* 1214 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*      */     long ts;
/* 1221 */     switch (p.currentTokenId()) {
/*      */       case 6:
/* 1223 */         text = p.getText();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1250 */         return _parseDate(text.trim(), ctxt);case 7: try { ts = p.getLongValue(); } catch (StreamReadException e) { Number v = (Number)ctxt.handleWeirdNumberValue(this._valueClass, p.getNumberValue(), "not a valid 64-bit `long` for creating `java.util.Date`", new Object[0]); ts = v.longValue(); }  return new Date(ts);case 11: return (Date)getNullValue(ctxt);case 1: text = ctxt.extractScalarFromObject(p, this, this._valueClass); return _parseDate(text.trim(), ctxt);
/*      */       case 3:
/*      */         return _parseDateFromArray(p, ctxt);
/*      */     } 
/*      */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   protected Date _parseDateFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1257 */     CoercionAction act = _findCoercionFromEmptyArray(ctxt);
/* 1258 */     boolean unwrap = ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
/*      */     
/* 1260 */     if (unwrap || act != CoercionAction.Fail) {
/* 1261 */       JsonToken t = p.nextToken();
/* 1262 */       if (t == JsonToken.END_ARRAY) {
/* 1263 */         switch (act) {
/*      */           case AsEmpty:
/* 1265 */             return (Date)getEmptyValue(ctxt);
/*      */           case AsNull:
/*      */           case TryConvert:
/* 1268 */             return (Date)getNullValue(ctxt);
/*      */         } 
/*      */       
/* 1271 */       } else if (unwrap) {
/* 1272 */         if (t == JsonToken.START_ARRAY) {
/* 1273 */           return (Date)handleNestedArrayForSingle(p, ctxt);
/*      */         }
/* 1275 */         Date parsed = _parseDate(p, ctxt);
/* 1276 */         _verifyEndArrayForSingle(p, ctxt);
/* 1277 */         return parsed;
/*      */       } 
/*      */     } 
/* 1280 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, JsonToken.START_ARRAY, p, null, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date _parseDate(String value, DeserializationContext ctxt) throws IOException {
/*      */     try {
/* 1291 */       if (value.isEmpty()) {
/* 1292 */         CoercionAction act = _checkFromStringCoercion(ctxt, value);
/* 1293 */         switch (act) {
/*      */           case AsEmpty:
/* 1295 */             return new Date(0L);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1300 */         return null;
/*      */       } 
/*      */       
/* 1303 */       if (_hasTextualNull(value)) {
/* 1304 */         return null;
/*      */       }
/* 1306 */       return ctxt.parseDate(value);
/* 1307 */     } catch (IllegalArgumentException iae) {
/* 1308 */       return (Date)ctxt.handleWeirdStringValue(this._valueClass, value, "not a valid representation (error: %s)", new Object[] {
/*      */             
/* 1310 */             ClassUtil.exceptionMessage(iae)
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final String _parseString(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1322 */     if (p.hasToken(JsonToken.VALUE_STRING)) {
/* 1323 */       return p.getText();
/*      */     }
/*      */     
/* 1326 */     if (p.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
/* 1327 */       Object ob = p.getEmbeddedObject();
/* 1328 */       if (ob instanceof byte[]) {
/* 1329 */         return ctxt.getBase64Variant().encode((byte[])ob, false);
/*      */       }
/* 1331 */       if (ob == null) {
/* 1332 */         return null;
/*      */       }
/*      */       
/* 1335 */       return ob.toString();
/*      */     } 
/*      */     
/* 1338 */     if (p.hasToken(JsonToken.START_OBJECT)) {
/* 1339 */       return ctxt.extractScalarFromObject(p, this, this._valueClass);
/*      */     }
/*      */     
/* 1342 */     String value = p.getValueAsString();
/* 1343 */     if (value != null) {
/* 1344 */       return value;
/*      */     }
/* 1346 */     return (String)ctxt.handleUnexpectedToken(String.class, p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasTextualNull(String value) {
/* 1357 */     return "null".equals(value);
/*      */   }
/*      */   
/*      */   protected final boolean _isNegInf(String text) {
/* 1361 */     return ("-Infinity".equals(text) || "-INF".equals(text));
/*      */   }
/*      */   
/*      */   protected final boolean _isPosInf(String text) {
/* 1365 */     return ("Infinity".equals(text) || "INF".equals(text));
/*      */   }
/*      */   protected final boolean _isNaN(String text) {
/* 1368 */     return "NaN".equals(text);
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final boolean _isBlank(String text) {
/* 1373 */     int len = text.length();
/* 1374 */     for (int i = 0; i < len; i++) {
/* 1375 */       if (text.charAt(i) > ' ') {
/* 1376 */         return false;
/*      */       }
/*      */     } 
/* 1379 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _checkFromStringCoercion(DeserializationContext ctxt, String value) throws IOException {
/* 1394 */     return _checkFromStringCoercion(ctxt, value, logicalType(), handledType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _checkFromStringCoercion(DeserializationContext ctxt, String value, LogicalType logicalType, Class<?> rawTargetType) throws IOException {
/* 1411 */     if (value.isEmpty()) {
/* 1412 */       CoercionAction coercionAction = ctxt.findCoercionAction(logicalType, rawTargetType, CoercionInputShape.EmptyString);
/*      */       
/* 1414 */       return _checkCoercionFail(ctxt, coercionAction, rawTargetType, value, "empty String (\"\")");
/*      */     } 
/* 1416 */     if (_isBlank(value)) {
/* 1417 */       CoercionAction coercionAction = ctxt.findCoercionFromBlankString(logicalType, rawTargetType, CoercionAction.Fail);
/* 1418 */       return _checkCoercionFail(ctxt, coercionAction, rawTargetType, value, "blank String (all whitespace)");
/*      */     } 
/*      */ 
/*      */     
/* 1422 */     if (ctxt.isEnabled(StreamReadCapability.UNTYPED_SCALARS)) {
/* 1423 */       return CoercionAction.TryConvert;
/*      */     }
/* 1425 */     CoercionAction act = ctxt.findCoercionAction(logicalType, rawTargetType, CoercionInputShape.String);
/* 1426 */     if (act == CoercionAction.Fail)
/*      */     {
/* 1428 */       ctxt.reportInputMismatch(this, "Cannot coerce String value (\"%s\") to %s (but might if coercion using `CoercionConfig` was enabled)", new Object[] { value, 
/*      */             
/* 1430 */             _coercedTypeDesc() });
/*      */     }
/*      */     
/* 1433 */     return act;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _checkFloatToIntCoercion(JsonParser p, DeserializationContext ctxt, Class<?> rawTargetType) throws IOException {
/* 1443 */     CoercionAction act = ctxt.findCoercionAction(LogicalType.Integer, rawTargetType, CoercionInputShape.Float);
/*      */     
/* 1445 */     if (act == CoercionAction.Fail) {
/* 1446 */       return _checkCoercionFail(ctxt, act, rawTargetType, p.getNumberValue(), "Floating-point value (" + p
/* 1447 */           .getText() + ")");
/*      */     }
/* 1449 */     return act;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Boolean _coerceBooleanFromInt(JsonParser p, DeserializationContext ctxt, Class<?> rawTargetType) throws IOException {
/* 1459 */     CoercionAction act = ctxt.findCoercionAction(LogicalType.Boolean, rawTargetType, CoercionInputShape.Integer);
/* 1460 */     switch (act) {
/*      */       case Fail:
/* 1462 */         _checkCoercionFail(ctxt, act, rawTargetType, p.getNumberValue(), "Integer value (" + p
/* 1463 */             .getText() + ")");
/* 1464 */         return Boolean.FALSE;
/*      */       case AsNull:
/* 1466 */         return null;
/*      */       case AsEmpty:
/* 1468 */         return Boolean.FALSE;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1474 */     if (p.getNumberType() == JsonParser.NumberType.INT)
/*      */     {
/* 1476 */       return Boolean.valueOf((p.getIntValue() != 0));
/*      */     }
/* 1478 */     return Boolean.valueOf(!"0".equals(p.getText()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _checkCoercionFail(DeserializationContext ctxt, CoercionAction act, Class<?> targetType, Object inputValue, String inputDesc) throws IOException {
/* 1489 */     if (act == CoercionAction.Fail) {
/* 1490 */       ctxt.reportBadCoercion(this, targetType, inputValue, "Cannot coerce %s to %s (but could if coercion was enabled using `CoercionConfig`)", new Object[] { inputDesc, 
/*      */             
/* 1492 */             _coercedTypeDesc() });
/*      */     }
/* 1494 */     return act;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _checkTextualNull(DeserializationContext ctxt, String text) throws JsonMappingException {
/* 1507 */     if (_hasTextualNull(text)) {
/* 1508 */       if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/* 1509 */         _reportFailedNullCoerce(ctxt, true, (Enum<?>)MapperFeature.ALLOW_COERCION_OF_SCALARS, "String \"null\"");
/*      */       }
/* 1511 */       return true;
/*      */     } 
/* 1513 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1535 */     if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 1536 */       return p.getBigIntegerValue();
/*      */     }
/* 1538 */     if (ctxt.isEnabled(DeserializationFeature.USE_LONG_FOR_INTS)) {
/* 1539 */       return Long.valueOf(p.getLongValue());
/*      */     }
/* 1541 */     return p.getNumberValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitive(DeserializationContext ctxt) throws JsonMappingException {
/* 1554 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/* 1555 */       ctxt.reportInputMismatch(this, "Cannot coerce `null` to %s (disable `DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES` to allow)", new Object[] {
/*      */             
/* 1557 */             _coercedTypeDesc()
/*      */           });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitiveCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/* 1574 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/* 1575 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/* 1576 */       enable = true;
/* 1577 */     } else if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/* 1578 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/* 1579 */       enable = false;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 1583 */     String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/* 1584 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, strDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportFailedNullCoerce(DeserializationContext ctxt, boolean state, Enum<?> feature, String inputDesc) throws JsonMappingException {
/* 1590 */     String enableDesc = state ? "enable" : "disable";
/* 1591 */     ctxt.reportInputMismatch(this, "Cannot coerce %s to Null value as %s (%s `%s.%s` to allow)", new Object[] { inputDesc, 
/* 1592 */           _coercedTypeDesc(), enableDesc, feature.getDeclaringClass().getSimpleName(), feature.name() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _coercedTypeDesc() {
/*      */     boolean structured;
/*      */     String typeDesc;
/* 1608 */     JavaType t = getValueType();
/* 1609 */     if (t != null && !t.isPrimitive()) {
/* 1610 */       structured = (t.isContainerType() || t.isReferenceType());
/* 1611 */       typeDesc = ClassUtil.getTypeDescription(t);
/*      */     } else {
/* 1613 */       Class<?> cls = handledType();
/*      */       
/* 1615 */       structured = (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
/* 1616 */       typeDesc = ClassUtil.getClassDescription(cls);
/*      */     } 
/* 1618 */     if (structured) {
/* 1619 */       return "element of " + typeDesc;
/*      */     }
/* 1621 */     return typeDesc + " value";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected boolean _parseBooleanFromInt(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1638 */     _verifyNumberForScalarCoercion(ctxt, p);
/*      */ 
/*      */     
/* 1641 */     return !"0".equals(p.getText());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void _verifyStringForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/* 1650 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/* 1651 */     if (!ctxt.isEnabled(feat)) {
/* 1652 */       ctxt.reportInputMismatch(this, "Cannot coerce String \"%s\" to %s (enable `%s.%s` to allow)", new Object[] { str, 
/* 1653 */             _coercedTypeDesc(), feat.getDeclaringClass().getSimpleName(), feat.name() });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected Object _coerceEmptyString(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/* 1668 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/* 1669 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/* 1670 */       enable = true;
/* 1671 */     } else if (isPrimitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/* 1672 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/* 1673 */       enable = false;
/*      */     } else {
/* 1675 */       return getNullValue(ctxt);
/*      */     } 
/* 1677 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, "empty String (\"\")");
/* 1678 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void _failDoubleToIntCoercion(JsonParser p, DeserializationContext ctxt, String type) throws IOException {
/* 1685 */     ctxt.reportInputMismatch(handledType(), "Cannot coerce a floating-point value ('%s') into %s (enable `DeserializationFeature.ACCEPT_FLOAT_AS_INT` to allow)", new Object[] { p
/*      */           
/* 1687 */           .getValueAsString(), type });
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected final void _verifyNullForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/* 1693 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/* 1694 */       String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/* 1695 */       _reportFailedNullCoerce(ctxt, true, (Enum<?>)MapperFeature.ALLOW_COERCION_OF_SCALARS, strDesc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void _verifyNumberForScalarCoercion(DeserializationContext ctxt, JsonParser p) throws IOException {
/* 1702 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/* 1703 */     if (!ctxt.isEnabled(feat)) {
/*      */ 
/*      */       
/* 1706 */       String valueDesc = p.getText();
/* 1707 */       ctxt.reportInputMismatch(this, "Cannot coerce Number (%s) to %s (enable `%s.%s` to allow)", new Object[] { valueDesc, 
/* 1708 */             _coercedTypeDesc(), feat.getDeclaringClass().getSimpleName(), feat.name() });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected Object _coerceNullToken(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/* 1715 */     if (isPrimitive) {
/* 1716 */       _verifyNullForPrimitive(ctxt);
/*      */     }
/* 1718 */     return getNullValue(ctxt);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected Object _coerceTextualNull(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/* 1723 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/* 1724 */       _reportFailedNullCoerce(ctxt, true, (Enum<?>)MapperFeature.ALLOW_COERCION_OF_SCALARS, "String \"null\"");
/*      */     }
/* 1726 */     return getNullValue(ctxt);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected boolean _isEmptyOrTextualNull(String value) {
/* 1731 */     return (value.isEmpty() || "null".equals(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property) throws JsonMappingException {
/* 1753 */     return ctxt.findContextualValueDeserializer(type, property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _isIntNumber(String text) {
/* 1765 */     int len = text.length();
/* 1766 */     if (len > 0) {
/* 1767 */       int i; char c = text.charAt(0);
/*      */ 
/*      */ 
/*      */       
/* 1771 */       if (c == '-' || c == '+') {
/* 1772 */         if (len == 1) {
/* 1773 */           return false;
/*      */         }
/* 1775 */         i = 1;
/*      */       } else {
/* 1777 */         i = 0;
/*      */       } 
/*      */       
/* 1780 */       for (; i < len; i++) {
/* 1781 */         int ch = text.charAt(i);
/* 1782 */         if (ch > 57 || ch < 48) {
/* 1783 */           return false;
/*      */         }
/*      */       } 
/* 1786 */       return true;
/*      */     } 
/* 1788 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer) throws JsonMappingException {
/* 1811 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1812 */     if (_neitherNull(intr, prop)) {
/* 1813 */       AnnotatedMember member = prop.getMember();
/* 1814 */       if (member != null) {
/* 1815 */         Object convDef = intr.findDeserializationContentConverter(member);
/* 1816 */         if (convDef != null) {
/* 1817 */           Converter<Object, Object> conv = ctxt.converterInstance((Annotated)prop.getMember(), convDef);
/* 1818 */           JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 1819 */           if (existingDeserializer == null) {
/* 1820 */             existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
/*      */           }
/* 1822 */           return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1826 */     return existingDeserializer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonFormat.Value findFormatOverrides(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults) {
/* 1847 */     if (prop != null) {
/* 1848 */       return prop.findPropertyFormat((MapperConfig)ctxt.getConfig(), typeForDefaults);
/*      */     }
/*      */     
/* 1851 */     return ctxt.getDefaultPropertyFormat(typeForDefaults);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Boolean findFormatFeature(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat) {
/* 1867 */     JsonFormat.Value format = findFormatOverrides(ctxt, prop, typeForDefaults);
/* 1868 */     if (format != null) {
/* 1869 */       return format.getFeature(feat);
/*      */     }
/* 1871 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final NullValueProvider findValueNullProvider(DeserializationContext ctxt, SettableBeanProperty prop, PropertyMetadata propMetadata) throws JsonMappingException {
/* 1885 */     if (prop != null) {
/* 1886 */       return _findNullProvider(ctxt, (BeanProperty)prop, propMetadata.getValueNulls(), prop
/* 1887 */           .getValueDeserializer());
/*      */     }
/* 1889 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NullValueProvider findContentNullProvider(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1904 */     Nulls nulls = findContentNullStyle(ctxt, prop);
/* 1905 */     if (nulls == Nulls.SKIP) {
/* 1906 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/*      */ 
/*      */     
/* 1910 */     if (nulls == Nulls.FAIL) {
/* 1911 */       if (prop == null) {
/* 1912 */         JavaType type = ctxt.constructType(valueDeser.handledType());
/*      */         
/* 1914 */         if (type.isContainerType()) {
/* 1915 */           type = type.getContentType();
/*      */         }
/* 1917 */         return (NullValueProvider)NullsFailProvider.constructForRootValue(type);
/*      */       } 
/* 1919 */       return (NullValueProvider)NullsFailProvider.constructForProperty(prop, prop.getType().getContentType());
/*      */     } 
/*      */     
/* 1922 */     NullValueProvider prov = _findNullProvider(ctxt, prop, nulls, valueDeser);
/* 1923 */     if (prov != null) {
/* 1924 */       return prov;
/*      */     }
/* 1926 */     return (NullValueProvider)valueDeser;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Nulls findContentNullStyle(DeserializationContext ctxt, BeanProperty prop) throws JsonMappingException {
/* 1932 */     if (prop != null) {
/* 1933 */       return prop.getMetadata().getContentNulls();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1938 */     return ctxt.getConfig().getDefaultSetterInfo().getContentNulls();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final NullValueProvider _findNullProvider(DeserializationContext ctxt, BeanProperty prop, Nulls nulls, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1946 */     if (nulls == Nulls.FAIL) {
/* 1947 */       if (prop == null) {
/* 1948 */         Class<?> rawType = (valueDeser == null) ? Object.class : valueDeser.handledType();
/* 1949 */         return (NullValueProvider)NullsFailProvider.constructForRootValue(ctxt.constructType(rawType));
/*      */       } 
/* 1951 */       return (NullValueProvider)NullsFailProvider.constructForProperty(prop);
/*      */     } 
/* 1953 */     if (nulls == Nulls.AS_EMPTY) {
/*      */ 
/*      */       
/* 1956 */       if (valueDeser == null) {
/* 1957 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1963 */       if (valueDeser instanceof BeanDeserializerBase) {
/* 1964 */         BeanDeserializerBase bd = (BeanDeserializerBase)valueDeser;
/* 1965 */         ValueInstantiator vi = bd.getValueInstantiator();
/* 1966 */         if (!vi.canCreateUsingDefault()) {
/* 1967 */           JavaType type = (prop == null) ? bd.getValueType() : prop.getType();
/* 1968 */           return (NullValueProvider)ctxt.reportBadDefinition(type, 
/* 1969 */               String.format("Cannot create empty instance of %s, no default Creator", new Object[] { type }));
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1974 */       AccessPattern access = valueDeser.getEmptyAccessPattern();
/* 1975 */       if (access == AccessPattern.ALWAYS_NULL) {
/* 1976 */         return (NullValueProvider)NullsConstantProvider.nuller();
/*      */       }
/* 1978 */       if (access == AccessPattern.CONSTANT) {
/* 1979 */         return (NullValueProvider)NullsConstantProvider.forValue(valueDeser.getEmptyValue(ctxt));
/*      */       }
/*      */       
/* 1982 */       return (NullValueProvider)new NullsAsEmptyProvider(valueDeser);
/*      */     } 
/* 1984 */     if (nulls == Nulls.SKIP) {
/* 1985 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/* 1987 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected CoercionAction _findCoercionFromEmptyString(DeserializationContext ctxt) {
/* 1992 */     return ctxt.findCoercionAction(logicalType(), handledType(), CoercionInputShape.EmptyString);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _findCoercionFromEmptyArray(DeserializationContext ctxt) {
/* 1998 */     return ctxt.findCoercionAction(logicalType(), handledType(), CoercionInputShape.EmptyArray);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected CoercionAction _findCoercionFromBlankString(DeserializationContext ctxt) {
/* 2004 */     return ctxt.findCoercionFromBlankString(logicalType(), handledType(), CoercionAction.Fail);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object<?> instanceOrClass, String propName) throws IOException {
/* 2032 */     if (instanceOrClass == null) {
/* 2033 */       instanceOrClass = (Object<?>)handledType();
/*      */     }
/*      */     
/* 2036 */     if (ctxt.handleUnknownProperty(p, this, instanceOrClass, propName)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2042 */     p.skipChildren();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleMissingEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 2048 */     ctxt.reportWrongTokenException(this, JsonToken.END_ARRAY, "Attempted to unwrap '%s' value from an array (with `DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS`) but it contains more than one value", new Object[] {
/*      */           
/* 2050 */           handledType().getName()
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object handleNestedArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 2063 */     String msg = String.format("Cannot deserialize instance of %s out of %s token: nested Arrays not allowed with %s", new Object[] {
/*      */           
/* 2065 */           ClassUtil.nameOf(this._valueClass), JsonToken.START_ARRAY, "DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS"
/*      */         });
/* 2067 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p.currentToken(), p, msg, new Object[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _verifyEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 2072 */     JsonToken t = p.nextToken();
/* 2073 */     if (t != JsonToken.END_ARRAY) {
/* 2074 */       handleMissingEndArrayForSingle(p, ctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final boolean _neitherNull(Object a, Object b) {
/* 2088 */     return (a != null && b != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _byteOverflow(int value) {
/* 2097 */     return (value < -128 || value > 255);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _shortOverflow(int value) {
/* 2104 */     return (value < -32768 || value > 32767);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _intOverflow(long value) {
/* 2111 */     return (value < -2147483648L || value > 2147483647L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Number _nonNullNumber(Number n) {
/* 2118 */     if (n == null) {
/* 2119 */       n = Integer.valueOf(0);
/*      */     }
/* 2121 */     return n;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/std/StdDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */