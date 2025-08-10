/*    */ package br.jus.cnj.pje.office.core.imp;
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
/*    */ public class PjeClientException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 4973079611496546423L;
/*    */   
/*    */   public PjeClientException(String message) {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */   public PjeClientException(Exception e) {
/* 39 */     super(e);
/*    */   }
/*    */   
/*    */   public PjeClientException(String message, Throwable cause) {
/* 43 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */