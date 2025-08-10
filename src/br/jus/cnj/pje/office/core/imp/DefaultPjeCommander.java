/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.IBootable;
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.taskresolver4j.ITaskRequestExecutor;
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
/*    */ abstract class DefaultPjeCommander<I extends IPjeRequest, O extends IPjeResponse>
/*    */   extends AbstractPjeCommander<I, O>
/*    */ {
/*    */   protected DefaultPjeCommander(IBootable boot, String serverEndpoint) {
/* 37 */     super((ITaskRequestExecutor<IPjeRequest, IPjeResponse>)new PjeTaskRequestExecutor(), boot, serverEndpoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isProcessing() {
/* 42 */     return (getExecutor().getRunningTasks() > 0L);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/DefaultPjeCommander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */