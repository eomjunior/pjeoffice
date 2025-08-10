/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.StringSelection;
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
/*    */ class PjeClipResponse
/*    */   implements IPjeResponse
/*    */ {
/*    */   private final Charset charset;
/*    */   
/*    */   public PjeClipResponse() {
/* 45 */     this(IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public PjeClipResponse(Charset charset) {
/* 49 */     this.charset = (Charset)Args.requireNonNull(charset, "charset is null");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(byte[] data) throws IOException {
/* 59 */     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(new String(data, this.charset)), null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] data, String contentType) throws IOException {
/* 64 */     write(data);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClipResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */