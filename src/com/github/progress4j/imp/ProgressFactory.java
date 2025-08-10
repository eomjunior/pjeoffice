/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.progress4j.IProgressFactory;
/*    */ import com.github.progress4j.IProgressView;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Pair;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import java.awt.Image;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class ProgressFactory<T extends IProgressView>
/*    */   implements IProgressFactory
/*    */ {
/* 45 */   private final Map<String, Pair<T, Disposable>> pool = Collections.synchronizedMap(new HashMap<>());
/*    */   
/*    */   private final Supplier<T> creator;
/*    */   
/*    */   public ProgressFactory(Supplier<T> creator) {
/* 50 */     this(Images.PROGRESS_ICON.asImage(), creator);
/*    */   }
/*    */   
/*    */   public ProgressFactory(Image icon, Supplier<T> creator) {
/* 54 */     this.creator = (Supplier<T>)Args.requireNonNull(creator, "supplier is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final void display() {
/* 59 */     synchronized (this.pool) {
/* 60 */       this.pool.values().forEach(e -> ((IProgressView)e.getKey()).display());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final void undisplay() {
/* 66 */     synchronized (this.pool) {
/* 67 */       this.pool.values().forEach(e -> ((IProgressView)e.getKey()).undisplay());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final void interrupt() {
/* 73 */     synchronized (this.pool) {
/* 74 */       this.pool.values().forEach(e -> ((IProgressView)e.getKey()).interrupt());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel(Thread thread) {
/* 80 */     if (thread != null) {
/* 81 */       synchronized (this.pool) {
/* 82 */         this.pool.values().stream().map(Pair::getKey).filter(p -> p.isFrom(thread)).forEach(IProgressView::cancel);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final T get() {
/* 89 */     IProgressView iProgressView = (IProgressView)this.creator.get();
/* 90 */     this.pool.put(iProgressView.getName(), Pair.of(iProgressView, iProgressView.disposeObservable().subscribe(p -> {
/*    */               Pair<?, Disposable> item = this.pool.remove(p.getName());
/*    */               if (item != null) {
/*    */                 ((Disposable)item.getValue()).dispose();
/*    */               }
/*    */               onDisposed((T)pv);
/*    */             })));
/* 97 */     return (T)iProgressView;
/*    */   }
/*    */   
/*    */   protected void onDisposed(T progress) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */