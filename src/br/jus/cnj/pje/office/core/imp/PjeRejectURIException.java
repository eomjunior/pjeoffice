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
/*    */ public class PjeRejectURIException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 2969433125674547900L;
/*    */   
/*    */   public PjeRejectURIException(String uri) {
/* 35 */     super("URI inválida ou já processada: " + uri);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeRejectURIException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */