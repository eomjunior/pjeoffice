/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ public class PjeClientConnectionTimeoutException
/*    */   extends PjeClientException {
/*    */   private static final long serialVersionUID = 2643954374297451108L;
/*    */   
/*    */   public PjeClientConnectionTimeoutException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public PjeClientConnectionTimeoutException(Exception e) {
/* 12 */     super(e);
/*    */   }
/*    */   
/*    */   public PjeClientConnectionTimeoutException(String message, Throwable cause) {
/* 16 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientConnectionTimeoutException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */