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
/*    */ public enum NothingResponse
/*    */   implements IPjeResponse
/*    */ {
/* 35 */   INSTANCE;
/*    */   
/*    */   public void write(byte[] data) throws IOException {}
/*    */   
/*    */   public void flush() throws IOException {}
/*    */   
/*    */   public void write(byte[] data, String contentType) throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/NothingResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */