/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ class PjeTarget
/*    */   implements IPjeEndpoint
/*    */ {
/*    */   private final String endPoint;
/*    */   private final String session;
/*    */   
/*    */   PjeTarget(String endPoint, String session) {
/* 40 */     this.endPoint = Args.requireText(endPoint, "endpoint is null");
/* 41 */     this.session = (String)Args.requireNonNull(session, "session is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPath() {
/* 46 */     return this.endPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSession() {
/* 51 */     return this.session;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */