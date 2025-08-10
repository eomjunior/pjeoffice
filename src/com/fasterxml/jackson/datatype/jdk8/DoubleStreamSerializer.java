/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.stream.DoubleStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleStreamSerializer
/*    */   extends StdSerializer<DoubleStream>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 24 */   public static final DoubleStreamSerializer INSTANCE = new DoubleStreamSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private DoubleStreamSerializer() {
/* 30 */     super(DoubleStream.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(DoubleStream stream, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 36 */     try (DoubleStream ds = stream) {
/* 37 */       jgen.writeStartArray();
/*    */       
/* 39 */       ds.forEachOrdered(value -> {
/*    */             try {
/*    */               jgen.writeNumber(value);
/* 42 */             } catch (IOException e) {
/*    */               throw new WrappedIOException(e);
/*    */             } 
/*    */           });
/*    */       
/* 47 */       jgen.writeEndArray();
/* 48 */     } catch (WrappedIOException e) {
/* 49 */       throw e.getCause();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/DoubleStreamSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */