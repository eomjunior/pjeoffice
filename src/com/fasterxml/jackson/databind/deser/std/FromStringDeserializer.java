/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*     */ import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*     */ import com.fasterxml.jackson.databind.type.LogicalType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Currency;
/*     */ import java.util.IllformedLocaleException;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FromStringDeserializer<T>
/*     */   extends StdScalarDeserializer<T>
/*     */ {
/*     */   public static Class<?>[] types() {
/*  61 */     return new Class[] { File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class, StringBuilder.class, StringBuffer.class };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FromStringDeserializer(Class<?> vc) {
/*  88 */     super(vc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FromStringDeserializer<?> findDeserializer(Class<?> rawType) {
/*  97 */     int kind = 0;
/*  98 */     if (rawType == File.class)
/*  99 */     { kind = 1; }
/* 100 */     else if (rawType == URL.class)
/* 101 */     { kind = 2; }
/* 102 */     else if (rawType == URI.class)
/* 103 */     { kind = 3; }
/* 104 */     else if (rawType == Class.class)
/* 105 */     { kind = 4; }
/* 106 */     else if (rawType == JavaType.class)
/* 107 */     { kind = 5; }
/* 108 */     else if (rawType == Currency.class)
/* 109 */     { kind = 6; }
/* 110 */     else if (rawType == Pattern.class)
/* 111 */     { kind = 7; }
/* 112 */     else if (rawType == Locale.class)
/* 113 */     { kind = 8; }
/* 114 */     else if (rawType == Charset.class)
/* 115 */     { kind = 9; }
/* 116 */     else if (rawType == TimeZone.class)
/* 117 */     { kind = 10; }
/* 118 */     else if (rawType == InetAddress.class)
/* 119 */     { kind = 11; }
/* 120 */     else if (rawType == InetSocketAddress.class)
/* 121 */     { kind = 12; }
/* 122 */     else { if (rawType == StringBuilder.class)
/* 123 */         return new StringBuilderDeserializer(); 
/* 124 */       if (rawType == StringBuffer.class) {
/* 125 */         return new StringBufferDeserializer();
/*     */       }
/* 127 */       return null; }
/*     */     
/* 129 */     return new Std(rawType, kind);
/*     */   }
/*     */ 
/*     */   
/*     */   public LogicalType logicalType() {
/* 134 */     return LogicalType.OtherScalar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 148 */     String text = p.getValueAsString();
/* 149 */     if (text == null) {
/* 150 */       JsonToken t = p.currentToken();
/* 151 */       if (t != JsonToken.START_OBJECT) {
/* 152 */         return (T)_deserializeFromOther(p, ctxt, t);
/*     */       }
/*     */       
/* 155 */       text = ctxt.extractScalarFromObject(p, this, this._valueClass);
/*     */     } 
/* 157 */     if (text.isEmpty())
/*     */     {
/* 159 */       return (T)_deserializeFromEmptyString(ctxt);
/*     */     }
/* 161 */     if (_shouldTrim()) {
/* 162 */       String old = text;
/* 163 */       text = text.trim();
/* 164 */       if (text != old && 
/* 165 */         text.isEmpty()) {
/* 166 */         return (T)_deserializeFromEmptyString(ctxt);
/*     */       }
/*     */     } 
/*     */     
/* 170 */     Exception cause = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 175 */       return _deserialize(text, ctxt);
/* 176 */     } catch (IllegalArgumentException|java.net.MalformedURLException e) {
/* 177 */       cause = e;
/*     */ 
/*     */       
/* 180 */       String msg = "not a valid textual representation";
/* 181 */       String m2 = cause.getMessage();
/* 182 */       if (m2 != null) {
/* 183 */         msg = msg + ", problem: " + m2;
/*     */       }
/*     */       
/* 186 */       throw ctxt.weirdStringException(text, this._valueClass, msg)
/* 187 */         .withCause(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext) throws IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _shouldTrim() {
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object _deserializeFromOther(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/* 206 */     if (t == JsonToken.START_ARRAY) {
/* 207 */       return _deserializeFromArray(p, ctxt);
/*     */     }
/* 209 */     if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/*     */       
/* 211 */       Object ob = p.getEmbeddedObject();
/* 212 */       if (ob == null) {
/* 213 */         return null;
/*     */       }
/* 215 */       if (this._valueClass.isAssignableFrom(ob.getClass())) {
/* 216 */         return ob;
/*     */       }
/* 218 */       return _deserializeEmbedded(ob, ctxt);
/*     */     } 
/* 220 */     return ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
/* 230 */     ctxt.reportInputMismatch(this, "Don't know how to convert embedded Object of type %s into %s", new Object[] { ob
/*     */           
/* 232 */           .getClass().getName(), this._valueClass.getName() });
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected final T _deserializeFromEmptyString() throws IOException {
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object _deserializeFromEmptyString(DeserializationContext ctxt) throws IOException {
/* 245 */     CoercionAction act = ctxt.findCoercionAction(logicalType(), this._valueClass, CoercionInputShape.EmptyString);
/*     */     
/* 247 */     if (act == CoercionAction.Fail)
/* 248 */       ctxt.reportInputMismatch(this, "Cannot coerce empty String (\"\") to %s (but could if enabling coercion using `CoercionConfig`)", new Object[] {
/*     */             
/* 250 */             _coercedTypeDesc()
/*     */           }); 
/* 252 */     if (act == CoercionAction.AsNull) {
/* 253 */       return getNullValue(ctxt);
/*     */     }
/* 255 */     if (act == CoercionAction.AsEmpty) {
/* 256 */       return getEmptyValue(ctxt);
/*     */     }
/*     */ 
/*     */     
/* 260 */     return _deserializeFromEmptyStringDefault(ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object _deserializeFromEmptyStringDefault(DeserializationContext ctxt) throws IOException {
/* 268 */     return getNullValue(ctxt);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Std
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public static final int STD_FILE = 1;
/*     */     
/*     */     public static final int STD_URL = 2;
/*     */     
/*     */     public static final int STD_URI = 3;
/*     */     
/*     */     public static final int STD_CLASS = 4;
/*     */     
/*     */     public static final int STD_JAVA_TYPE = 5;
/*     */     
/*     */     public static final int STD_CURRENCY = 6;
/*     */     
/*     */     public static final int STD_PATTERN = 7;
/*     */     
/*     */     public static final int STD_LOCALE = 8;
/*     */     
/*     */     public static final int STD_CHARSET = 9;
/*     */     
/*     */     public static final int STD_TIME_ZONE = 10;
/*     */     
/*     */     public static final int STD_INET_ADDRESS = 11;
/*     */     
/*     */     public static final int STD_INET_SOCKET_ADDRESS = 12;
/*     */     
/*     */     protected static final String LOCALE_EXT_MARKER = "_#";
/*     */     
/*     */     protected final int _kind;
/*     */ 
/*     */     
/*     */     protected Std(Class<?> valueType, int kind) {
/* 307 */       super(valueType);
/* 308 */       this._kind = kind;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
/*     */       int ix;
/* 314 */       switch (this._kind) {
/*     */         case 1:
/* 316 */           return new File(value);
/*     */         case 2:
/* 318 */           return new URL(value);
/*     */         case 3:
/* 320 */           return URI.create(value);
/*     */         case 4:
/*     */           try {
/* 323 */             return ctxt.findClass(value);
/* 324 */           } catch (Exception e) {
/* 325 */             return ctxt.handleInstantiationProblem(this._valueClass, value, 
/* 326 */                 ClassUtil.getRootCause(e));
/*     */           } 
/*     */         case 5:
/* 329 */           return ctxt.getTypeFactory().constructFromCanonical(value);
/*     */         
/*     */         case 6:
/* 332 */           return Currency.getInstance(value);
/*     */         
/*     */         case 7:
/* 335 */           return Pattern.compile(value);
/*     */         case 8:
/* 337 */           return _deserializeLocale(value, ctxt);
/*     */         case 9:
/* 339 */           return Charset.forName(value);
/*     */         case 10:
/* 341 */           return TimeZone.getTimeZone(value);
/*     */         case 11:
/* 343 */           return InetAddress.getByName(value);
/*     */         case 12:
/* 345 */           if (value.startsWith("[")) {
/*     */ 
/*     */             
/* 348 */             int i = value.lastIndexOf(']');
/* 349 */             if (i == -1) {
/* 350 */               throw new InvalidFormatException(ctxt.getParser(), "Bracketed IPv6 address must contain closing bracket", value, InetSocketAddress.class);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 355 */             int j = value.indexOf(':', i);
/* 356 */             int port = (j > -1) ? Integer.parseInt(value.substring(j + 1)) : 0;
/* 357 */             return new InetSocketAddress(value.substring(0, i + 1), port);
/*     */           } 
/* 359 */           ix = value.indexOf(':');
/* 360 */           if (ix >= 0 && value.indexOf(':', ix + 1) < 0) {
/*     */             
/* 362 */             int port = Integer.parseInt(value.substring(ix + 1));
/* 363 */             return new InetSocketAddress(value.substring(0, ix), port);
/*     */           } 
/*     */           
/* 366 */           return new InetSocketAddress(value, 0);
/*     */       } 
/* 368 */       VersionUtil.throwInternal();
/* 369 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 376 */       switch (this._kind) {
/*     */         
/*     */         case 3:
/* 379 */           return URI.create("");
/*     */         
/*     */         case 8:
/* 382 */           return Locale.ROOT;
/*     */       } 
/* 384 */       return super.getEmptyValue(ctxt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object _deserializeFromEmptyStringDefault(DeserializationContext ctxt) throws IOException {
/* 392 */       return getEmptyValue(ctxt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean _shouldTrim() {
/* 399 */       return (this._kind != 7);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int _firstHyphenOrUnderscore(String str) {
/* 404 */       for (int i = 0, end = str.length(); i < end; i++) {
/* 405 */         char c = str.charAt(i);
/* 406 */         if (c == '_' || c == '-') {
/* 407 */           return i;
/*     */         }
/*     */       } 
/* 410 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Locale _deserializeLocale(String value, DeserializationContext ctxt) throws IOException {
/* 416 */       int ix = _firstHyphenOrUnderscore(value);
/* 417 */       if (ix < 0) {
/* 418 */         return new Locale(value);
/*     */       }
/* 420 */       String first = value.substring(0, ix);
/* 421 */       value = value.substring(ix + 1);
/* 422 */       ix = _firstHyphenOrUnderscore(value);
/* 423 */       if (ix < 0) {
/* 424 */         return new Locale(first, value);
/*     */       }
/* 426 */       String second = value.substring(0, ix);
/*     */       
/* 428 */       int extMarkerIx = value.indexOf("_#");
/* 429 */       if (extMarkerIx < 0) {
/* 430 */         return new Locale(first, second, value.substring(ix + 1));
/*     */       }
/* 432 */       return _deSerializeBCP47Locale(value, ix, first, second, extMarkerIx);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Locale _deSerializeBCP47Locale(String value, int ix, String first, String second, int extMarkerIx) {
/* 438 */       String third = "";
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 443 */         if (extMarkerIx > 0 && extMarkerIx > ix) {
/* 444 */           third = value.substring(ix + 1, extMarkerIx);
/*     */         }
/* 446 */         value = value.substring(extMarkerIx + 2);
/*     */         
/* 448 */         if (value.indexOf('_') < 0 && value.indexOf('-') < 0) {
/* 449 */           return (new Locale.Builder()).setLanguage(first)
/* 450 */             .setRegion(second).setVariant(third).setScript(value).build();
/*     */         }
/* 452 */         if (value.indexOf('_') < 0) {
/* 453 */           ix = value.indexOf('-');
/* 454 */           return (new Locale.Builder()).setLanguage(first)
/* 455 */             .setRegion(second).setVariant(third)
/* 456 */             .setExtension(value.charAt(0), value.substring(ix + 1))
/* 457 */             .build();
/*     */         } 
/* 459 */         ix = value.indexOf('_');
/* 460 */         return (new Locale.Builder()).setLanguage(first)
/* 461 */           .setRegion(second).setVariant(third)
/* 462 */           .setScript(value.substring(0, ix))
/* 463 */           .setExtension(value.charAt(ix + 1), value.substring(ix + 3))
/* 464 */           .build();
/* 465 */       } catch (IllformedLocaleException ex) {
/*     */         
/* 467 */         return new Locale(first, second, third);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class StringBuilderDeserializer
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     public StringBuilderDeserializer() {
/* 476 */       super(StringBuilder.class);
/*     */     }
/*     */ 
/*     */     
/*     */     public LogicalType logicalType() {
/* 481 */       return LogicalType.Textual;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 488 */       return new StringBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 494 */       String text = p.getValueAsString();
/* 495 */       if (text != null) {
/* 496 */         return _deserialize(text, ctxt);
/*     */       }
/* 498 */       return super.deserialize(p, ctxt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
/* 505 */       return new StringBuilder(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class StringBufferDeserializer
/*     */     extends FromStringDeserializer<Object> {
/*     */     public StringBufferDeserializer() {
/* 512 */       super(StringBuffer.class);
/*     */     }
/*     */     public LogicalType logicalType() {
/* 515 */       return LogicalType.Textual;
/*     */     }
/*     */     
/*     */     public Object getEmptyValue(DeserializationContext ctxt) {
/* 519 */       return new StringBuffer();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 525 */       String text = p.getValueAsString();
/* 526 */       if (text != null) {
/* 527 */         return _deserialize(text, ctxt);
/*     */       }
/* 529 */       return super.deserialize(p, ctxt);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
/* 536 */       return new StringBuffer(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/std/FromStringDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */