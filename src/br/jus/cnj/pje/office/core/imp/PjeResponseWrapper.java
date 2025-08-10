/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class PjeResponseWrapper<T extends IPjeResponse>
/*    */   implements IPjeResponse
/*    */ {
/*    */   protected final T response;
/*    */   
/*    */   protected PjeResponseWrapper(T response) {
/* 40 */     this.response = (T)Args.requireNonNull(response, "response is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data) throws IOException {
/* 45 */     this.response.write(data);
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 50 */     this.response.flush();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data, String contentType) throws IOException {
/* 55 */     this.response.write(data, contentType);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */