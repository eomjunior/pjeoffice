/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class StdKeySerializer
/*    */   extends StdSerializer<Object>
/*    */ {
/*    */   public StdKeySerializer() {
/* 18 */     super(Object.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 23 */     g.writeFieldName(value.toString());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/ser/std/StdKeySerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */