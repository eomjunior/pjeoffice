/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.stream.LongStream;
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
/*    */ public class LongStreamSerializer
/*    */   extends StdSerializer<LongStream>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 23 */   public static final LongStreamSerializer INSTANCE = new LongStreamSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LongStreamSerializer() {
/* 29 */     super(LongStream.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(LongStream stream, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 35 */     try (LongStream ls = stream) {
/* 36 */       jgen.writeStartArray();
/*    */       
/* 38 */       ls.forEachOrdered(value -> {
/*    */             try {
/*    */               jgen.writeNumber(value);
/* 41 */             } catch (IOException e) {
/*    */               throw new WrappedIOException(e);
/*    */             } 
/*    */           });
/*    */       
/* 46 */       jgen.writeEndArray();
/* 47 */     } catch (WrappedIOException e) {
/* 48 */       throw e.getCause();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/LongStreamSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */