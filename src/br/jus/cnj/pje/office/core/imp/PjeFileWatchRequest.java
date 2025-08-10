/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
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
/*    */ class PjeFileWatchRequest
/*    */   extends PjeUriRequest
/*    */ {
/*    */   public PjeFileWatchRequest(String uri) throws URISyntaxException {
/* 36 */     super(new URI(uri));
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isInternal() {
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeFileWatchRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */