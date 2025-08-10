/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.IConstants;
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
/*    */ class PjeStdioTaskResponse
/*    */   extends PjeStringTaskResponse
/*    */ {
/*    */   public PjeStdioTaskResponse(String output) {
/* 40 */     this(output, true);
/*    */   }
/*    */   
/*    */   public PjeStdioTaskResponse(String output, boolean success) {
/* 44 */     this(output, IConstants.DEFAULT_CHARSET, success);
/*    */   }
/*    */   
/*    */   public PjeStdioTaskResponse(String output, Charset charset) {
/* 48 */     this(output, charset, true);
/*    */   }
/*    */   
/*    */   public PjeStdioTaskResponse(String output, Charset charset, boolean success) {
/* 52 */     super(output, charset, success);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doProcessResponse(IPjeResponse response) throws IOException {
/* 57 */     response.write(toBytes(this.output.length()));
/* 58 */     super.doProcessResponse(response);
/*    */   }
/*    */   
/*    */   private static byte[] toBytes(int length) {
/* 62 */     byte[] bytes = new byte[4];
/* 63 */     bytes[0] = (byte)(length & 0xFF);
/* 64 */     bytes[1] = (byte)(length >> 8 & 0xFF);
/* 65 */     bytes[2] = (byte)(length >> 16 & 0xFF);
/* 66 */     bytes[3] = (byte)(length >> 24 & 0xFF);
/* 67 */     return bytes;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStdioTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */