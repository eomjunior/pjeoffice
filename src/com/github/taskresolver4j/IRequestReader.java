/*    */ package com.github.taskresolver4j;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IRequestReader<T extends com.github.utils4j.imp.Params>
/*    */ {
/*    */   default T read(String text, T params) throws IOException {
/* 38 */     return read(text, params, o -> o);
/*    */   }
/*    */   
/*    */   T read(String paramString, T paramT, Function<?, ?> paramFunction) throws IOException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/IRequestReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */