/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import com.github.utils4j.IConstants;
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
/*    */ class PjeClipTaskResponse
/*    */   extends PjeStringTaskResponse
/*    */ {
/*    */   public PjeClipTaskResponse(String output) {
/* 37 */     this(output, true);
/*    */   }
/*    */   
/*    */   public PjeClipTaskResponse(String output, boolean success) {
/* 41 */     this(output, IConstants.DEFAULT_CHARSET, success);
/*    */   }
/*    */   
/*    */   public PjeClipTaskResponse(String output, Charset charset) {
/* 45 */     this(output, charset, true);
/*    */   }
/*    */   
/*    */   public PjeClipTaskResponse(String output, Charset charset, boolean success) {
/* 49 */     super(output, charset, success);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClipTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */