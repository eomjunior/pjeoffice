/*    */ package com.github.taskresolver4j.imp;
/*    */ 
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class TaskResponses<T>
/*    */   implements ITaskResponse<T>
/*    */ {
/* 38 */   private List<ITaskResponse<T>> responses = new ArrayList<>(4);
/*    */ 
/*    */   
/*    */   public boolean isSuccess() {
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public TaskResponses<T> add(ITaskResponse<T> response) {
/* 46 */     if (response != null) {
/* 47 */       this.responses.add(response);
/*    */     }
/* 49 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void processResponse(T response) throws IOException {
/* 54 */     for (ITaskResponse<T> r : this.responses) {
/* 55 */       r.processResponse(response);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final ITaskResponse<T> asJson() {
/* 61 */     for (int i = 0; i < this.responses.size(); i++) {
/* 62 */       this.responses.set(i, ((ITaskResponse)this.responses.get(i)).asJson());
/*    */     }
/* 64 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/TaskResponses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */