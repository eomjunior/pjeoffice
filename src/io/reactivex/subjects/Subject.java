/*    */ package io.reactivex.subjects;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.annotations.Nullable;
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
/*    */ public abstract class Subject<T>
/*    */   extends Observable<T>
/*    */   implements Observer<T>
/*    */ {
/*    */   public abstract boolean hasObservers();
/*    */   
/*    */   public abstract boolean hasThrowable();
/*    */   
/*    */   public abstract boolean hasComplete();
/*    */   
/*    */   @Nullable
/*    */   public abstract Throwable getThrowable();
/*    */   
/*    */   @NonNull
/*    */   public final Subject<T> toSerialized() {
/* 72 */     if (this instanceof SerializedSubject) {
/* 73 */       return this;
/*    */     }
/* 75 */     return new SerializedSubject<T>(this);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/Subject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */