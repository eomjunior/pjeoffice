/*    */ package com.github.taskresolver4j;
/*    */ 
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Function;
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
/*    */ public enum NotImplementedReader
/*    */   implements IRequestReader<Params>
/*    */ {
/* 36 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public Params read(String text, Params output, Function<?, ?> wrapper) throws IOException {
/* 40 */     throw new IOException("Unrecognizable text format " + text);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/NotImplementedReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */