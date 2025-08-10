/*    */ package com.github.taskresolver4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IProgressFactory;
/*    */ import com.github.taskresolver4j.IExecutorContext;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.taskresolver4j.ITaskRequest;
/*    */ import com.github.taskresolver4j.ITaskRequestExecutor;
/*    */ import com.github.utils4j.imp.Params;
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
/*    */ public class DefaultTaskRequest<T>
/*    */   extends Params
/*    */   implements ITaskRequest<T>
/*    */ {
/* 39 */   public static final String PARAM_TASK = ITask.class.getSimpleName() + ".task";
/*    */   
/* 41 */   public static final String PARAM_PROGRESS_FACTORY = IProgressFactory.class.getSimpleName() + ".factory";
/*    */   
/* 43 */   public static final String PARAM_EXECUTOR_CONTEXT = ITaskRequestExecutor.class.getSimpleName() + ".context";
/*    */ 
/*    */   
/*    */   public final ITask<T> getTask(IProgressFactory factory, IExecutorContext context) {
/* 47 */     return (ITask<T>)of(PARAM_PROGRESS_FACTORY, factory).of(PARAM_EXECUTOR_CONTEXT, context).getValue(PARAM_TASK);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isValid(StringBuilder whyNot) {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/DefaultTaskRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */