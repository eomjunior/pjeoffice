/*    */ package org.apache.tools.ant.filters;
/*    */ 
/*    */ import java.io.StringReader;
/*    */ import org.apache.tools.ant.util.ReaderInputStream;
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
/*    */ public class StringInputStream
/*    */   extends ReaderInputStream
/*    */ {
/*    */   public StringInputStream(String source) {
/* 36 */     super(new StringReader(source));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringInputStream(String source, String encoding) {
/* 46 */     super(new StringReader(source), encoding);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/StringInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */