/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.IBootable;
/*    */ import br.jus.cnj.pje.office.core.IPjeCommandFactory;
/*    */ import br.jus.cnj.pje.office.core.IPjeLifeCycle;
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
/*    */ public enum PjeLifeCycleFactory
/*    */   implements IPjeCommandFactory
/*    */ {
/* 35 */   STDIO
/*    */   {
/*    */     public IPjeLifeCycle create(IBootable boot) {
/* 38 */       return (IPjeLifeCycle)new PjeStdioServer(boot);
/*    */     }
/*    */   },
/* 41 */   CLIP
/*    */   {
/*    */     public IPjeLifeCycle create(IBootable boot) {
/* 44 */       return (IPjeLifeCycle)new PjeClipServer(boot);
/*    */     }
/*    */   },
/* 47 */   WEB
/*    */   {
/*    */     public IPjeLifeCycle create(IBootable boot) {
/* 50 */       return (IPjeLifeCycle)new PjeWebServer(boot);
/*    */     }
/*    */   },
/* 53 */   FILEWATCH
/*    */   {
/*    */     public IPjeLifeCycle create(IBootable boot) {
/* 56 */       return (IPjeLifeCycle)new PjeFileWatchServer(boot);
/*    */     }
/*    */   },
/* 59 */   PRO
/*    */   {
/*    */     public IPjeLifeCycle create(IBootable boot) {
/* 62 */       return new PjeCompositeLifeCycle(new IPjeLifeCycle[] { WEB
/* 63 */             .create(boot), FILEWATCH
/* 64 */             .create(boot) });
/*    */     }
/*    */   };
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeLifeCycleFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */