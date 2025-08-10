/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
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
/*    */ class PjeStdioResponse
/*    */   implements IPjeResponse
/*    */ {
/*    */   public void write(byte[] data) throws IOException {
/* 38 */     System.out.write(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 43 */     System.out.flush();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data, String contentType) throws IOException {
/* 48 */     write(data);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStdioResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */