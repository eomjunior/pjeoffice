/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ 
/*    */ class PjeStringTaskResponse
/*    */   extends PjeTaskResponse
/*    */ {
/*    */   protected final String output;
/*    */   protected final Charset charset;
/*    */   
/*    */   public PjeStringTaskResponse(String output) {
/* 44 */     this(output, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public PjeStringTaskResponse(String output, Charset charset) {
/* 48 */     this(output, charset, true);
/*    */   }
/*    */   
/*    */   public PjeStringTaskResponse(String output, Charset charset, boolean success) {
/* 52 */     super(success);
/* 53 */     this.output = (String)Args.requireNonNull(output, "output is null");
/* 54 */     this.charset = (Charset)Args.requireNonNull(charset, "charset is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void doProcessResponse(IPjeResponse response) throws IOException {
/* 59 */     response.write(this.output.getBytes(this.charset));
/* 60 */     response.flush();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStringTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */