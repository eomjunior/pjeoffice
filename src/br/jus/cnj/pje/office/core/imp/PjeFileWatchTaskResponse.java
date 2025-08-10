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
/*    */ class PjeFileWatchTaskResponse
/*    */   extends PjeStringTaskResponse
/*    */ {
/*    */   public PjeFileWatchTaskResponse(String output) {
/* 37 */     this(output, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public PjeFileWatchTaskResponse(String output, Charset charset) {
/* 41 */     this(output, charset, true);
/*    */   }
/*    */   
/*    */   public PjeFileWatchTaskResponse(String output, Charset charset, boolean success) {
/* 45 */     super(output, charset, success);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeFileWatchTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */