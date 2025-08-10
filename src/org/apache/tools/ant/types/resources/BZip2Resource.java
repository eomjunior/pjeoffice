/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.tools.ant.types.ResourceCollection;
/*    */ import org.apache.tools.bzip2.CBZip2InputStream;
/*    */ import org.apache.tools.bzip2.CBZip2OutputStream;
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
/*    */ public class BZip2Resource
/*    */   extends CompressedResource
/*    */ {
/* 36 */   private static final char[] MAGIC = new char[] { 'B', 'Z' };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BZip2Resource() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BZip2Resource(ResourceCollection other) {
/* 47 */     super(other);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected InputStream wrapStream(InputStream in) throws IOException {
/* 58 */     for (char ch : MAGIC) {
/* 59 */       if (in.read() != ch) {
/* 60 */         throw new IOException("Invalid bz2 stream.");
/*    */       }
/*    */     } 
/* 63 */     return (InputStream)new CBZip2InputStream(in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected OutputStream wrapStream(OutputStream out) throws IOException {
/* 74 */     for (char ch : MAGIC) {
/* 75 */       out.write(ch);
/*    */     }
/* 77 */     return (OutputStream)new CBZip2OutputStream(out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getCompressionName() {
/* 86 */     return "Bzip2";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/BZip2Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */