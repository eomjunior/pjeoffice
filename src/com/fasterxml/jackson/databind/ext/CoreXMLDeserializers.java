/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*     */ import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class CoreXMLDeserializers
/*     */   extends Deserializers.Base {
/*     */   static final DatatypeFactory _dataTypeFactory;
/*     */   protected static final int TYPE_DURATION = 1;
/*     */   
/*     */   static {
/*     */     try {
/*  30 */       _dataTypeFactory = DatatypeFactory.newInstance();
/*  31 */     } catch (DatatypeConfigurationException e) {
/*  32 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static final int TYPE_G_CALENDAR = 2;
/*     */   protected static final int TYPE_QNAME = 3;
/*     */   
/*     */   public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) {
/*  40 */     Class<?> raw = type.getRawClass();
/*  41 */     if (raw == QName.class) {
/*  42 */       return (JsonDeserializer<?>)new Std(raw, 3);
/*     */     }
/*  44 */     if (raw == XMLGregorianCalendar.class) {
/*  45 */       return (JsonDeserializer<?>)new Std(raw, 2);
/*     */     }
/*  47 */     if (raw == Duration.class) {
/*  48 */       return (JsonDeserializer<?>)new Std(raw, 1);
/*     */     }
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
/*  55 */     return (valueType == QName.class || valueType == XMLGregorianCalendar.class || valueType == Duration.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Std
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final int _kind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Std(Class<?> raw, int kind) {
/*  86 */       super(raw);
/*  87 */       this._kind = kind;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  96 */       if (this._kind == 2 && 
/*  97 */         p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  98 */         return _gregorianFromDate(ctxt, _parseDate(p, ctxt));
/*     */       }
/*     */       
/* 101 */       return super.deserialize(p, ctxt);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
/*     */       Date d;
/* 108 */       switch (this._kind) {
/*     */         case 1:
/* 110 */           return CoreXMLDeserializers._dataTypeFactory.newDuration(value);
/*     */         case 3:
/* 112 */           return QName.valueOf(value);
/*     */         
/*     */         case 2:
/*     */           try {
/* 116 */             d = _parseDate(value, ctxt);
/*     */           }
/* 118 */           catch (JsonMappingException e) {
/*     */ 
/*     */             
/* 121 */             return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(value);
/*     */           } 
/* 123 */           return _gregorianFromDate(ctxt, d);
/*     */       } 
/* 125 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected XMLGregorianCalendar _gregorianFromDate(DeserializationContext ctxt, Date d) {
/* 131 */       if (d == null) {
/* 132 */         return null;
/*     */       }
/* 134 */       GregorianCalendar calendar = new GregorianCalendar();
/* 135 */       calendar.setTime(d);
/* 136 */       TimeZone tz = ctxt.getTimeZone();
/* 137 */       if (tz != null) {
/* 138 */         calendar.setTimeZone(tz);
/*     */       }
/* 140 */       return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(calendar);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/ext/CoreXMLDeserializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */