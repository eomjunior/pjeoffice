/*    */ package com.github.taskresolver4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IProgressFactory;
/*    */ import com.github.progress4j.IProgressView;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.ICanceller;
/*    */ import com.github.utils4j.IParam;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.util.function.Supplier;
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
/*    */ public abstract class ParamBasedTask<T>
/*    */   implements ITask<T>
/*    */ {
/*    */   private final Params params;
/*    */   
/*    */   protected ParamBasedTask(Params params) {
/* 44 */     this.params = params.of(DefaultTaskRequest.PARAM_TASK, this);
/*    */   }
/*    */   
/*    */   protected final Params getParams() {
/* 48 */     return this.params;
/*    */   }
/*    */   
/*    */   protected final IParam getParameter(String key) {
/* 52 */     return this.params.get(key);
/*    */   }
/*    */   
/*    */   protected final <V> V getParameterValue(String key) {
/* 56 */     return (V)this.params.getValue(key);
/*    */   }
/*    */   
/*    */   protected final IProgressView getProgress() {
/* 60 */     return (IProgressView)getFactory().get();
/*    */   }
/*    */   
/*    */   protected final void progressWakeUp() {
/* 64 */     getFactory().display();
/*    */   }
/*    */   
/*    */   protected final void progressSleep() {
/* 68 */     getFactory().undisplay();
/*    */   }
/*    */   
/*    */   private IProgressFactory getFactory() {
/* 72 */     return (IProgressFactory)getParameter(DefaultTaskRequest.PARAM_PROGRESS_FACTORY).get();
/*    */   }
/*    */   
/*    */   protected final Supplier<ICanceller> getCanceller() {
/* 76 */     return getFactory()::get;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(StringBuilder whyNot) {
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 86 */     this.params.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/ParamBasedTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */