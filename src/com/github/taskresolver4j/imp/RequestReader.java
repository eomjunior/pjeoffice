/*    */ package com.github.taskresolver4j.imp;
/*    */ 
/*    */ import com.github.taskresolver4j.IRequestReader;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.ITextReader;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.JsonTextReader;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Function;
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
/*    */ public abstract class RequestReader<P extends Params, Pojo>
/*    */   implements IRequestReader<P>
/*    */ {
/*    */   private ITextReader pojoReader;
/*    */   
/*    */   public RequestReader(Class<?> jsonClass) {
/* 45 */     this((ITextReader)new JsonTextReader(jsonClass));
/*    */   }
/*    */   
/*    */   public RequestReader(ITextReader pojoReader) {
/* 49 */     this.pojoReader = (ITextReader)Args.requireNonNull(pojoReader, "pojoReader is null");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final P read(String text, P params, Function<?, ?> decorator) throws IOException {
/* 55 */     ITask<?> task = createTask(params, (Pojo)decorator.apply(this.pojoReader.read(text)));
/* 56 */     StringBuilder whyNot = new StringBuilder();
/* 57 */     if (!task.isValid(whyNot)) {
/* 58 */       throw new IOException("Unabled to create a valid task with parameter: " + text + " reason: " + whyNot);
/*    */     }
/* 60 */     return params;
/*    */   }
/*    */   
/*    */   protected abstract ITask<?> createTask(P paramP, Pojo paramPojo) throws IOException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/RequestReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */