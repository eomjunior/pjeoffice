/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeResponse;
/*    */ import java.io.File;
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
/*    */ public class PjeOneTimeWritingHttpExchangeResponse
/*    */   extends PjeOneTimeWritingResponse<IPjeHttpExchangeResponse>
/*    */   implements IPjeHttpExchangeResponse
/*    */ {
/*    */   protected PjeOneTimeWritingHttpExchangeResponse(IPjeHttpExchangeResponse response) {
/* 38 */     super(response);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(File file) throws IOException {
/* 43 */     checkAndRun(() -> this.response.write(file));
/*    */   }
/*    */ 
/*    */   
/*    */   public void notFound() throws IOException {
/* 48 */     checkAndRun(() -> this.response.notFound());
/*    */   }
/*    */ 
/*    */   
/*    */   public void success() throws IOException {
/* 53 */     checkAndRun(() -> this.response.success());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data, String contentType) throws IOException {
/* 58 */     checkAndRun(() -> this.response.write(data, contentType));
/*    */   }
/*    */ 
/*    */   
/*    */   public void fail(int code) throws IOException {
/* 63 */     checkAndRun(() -> this.response.fail(code));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeOneTimeWritingHttpExchangeResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */