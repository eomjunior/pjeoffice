/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.imp.function.IExecutable;
/*    */ import java.io.IOException;
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
/*    */ public class PjeOneTimeWritingResponse<T extends IPjeResponse>
/*    */   extends PjeResponseWrapper<T>
/*    */ {
/*    */   private boolean written = false;
/*    */   
/*    */   protected PjeOneTimeWritingResponse(T response) {
/* 41 */     super(response);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data) throws IOException {
/* 46 */     checkAndRun(() -> super.write(data));
/*    */   }
/*    */   
/*    */   protected final void checkAndRun(IExecutable<IOException> r) throws IOException {
/* 50 */     if (!this.written) {
/* 51 */       r.execute();
/* 52 */       this.written = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeOneTimeWritingResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */