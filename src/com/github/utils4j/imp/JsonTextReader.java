/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.ITextReader;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonTextReader
/*    */   implements ITextReader
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final Charset charset;
/*    */   
/*    */   public JsonTextReader(Class<?> clazz) {
/* 42 */     this(clazz, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public JsonTextReader(Class<?> clazz, Charset charset) {
/* 46 */     this.clazz = Args.<Class<?>>requireNonNull(clazz, "clazz is null");
/* 47 */     this.charset = Args.<Charset>requireNonNull(charset, "charset is null");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T read(String text) throws IOException {
/* 53 */     return (T)JsonTools.mapper().readValue(text.getBytes(this.charset), this.clazz);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/JsonTextReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */