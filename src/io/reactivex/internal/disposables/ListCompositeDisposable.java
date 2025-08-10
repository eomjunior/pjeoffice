/*     */ package io.reactivex.internal.disposables;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListCompositeDisposable
/*     */   implements Disposable, DisposableContainer
/*     */ {
/*     */   List<Disposable> resources;
/*     */   volatile boolean disposed;
/*     */   
/*     */   public ListCompositeDisposable() {}
/*     */   
/*     */   public ListCompositeDisposable(Disposable... resources) {
/*  35 */     ObjectHelper.requireNonNull(resources, "resources is null");
/*  36 */     this.resources = new LinkedList<Disposable>();
/*  37 */     for (Disposable d : resources) {
/*  38 */       ObjectHelper.requireNonNull(d, "Disposable item is null");
/*  39 */       this.resources.add(d);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ListCompositeDisposable(Iterable<? extends Disposable> resources) {
/*  44 */     ObjectHelper.requireNonNull(resources, "resources is null");
/*  45 */     this.resources = new LinkedList<Disposable>();
/*  46 */     for (Disposable d : resources) {
/*  47 */       ObjectHelper.requireNonNull(d, "Disposable item is null");
/*  48 */       this.resources.add(d);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dispose() {
/*     */     List<Disposable> set;
/*  54 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     synchronized (this) {
/*  59 */       if (this.disposed) {
/*     */         return;
/*     */       }
/*  62 */       this.disposed = true;
/*  63 */       set = this.resources;
/*  64 */       this.resources = null;
/*     */     } 
/*     */     
/*  67 */     dispose(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  72 */     return this.disposed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(Disposable d) {
/*  77 */     ObjectHelper.requireNonNull(d, "d is null");
/*  78 */     if (!this.disposed) {
/*  79 */       synchronized (this) {
/*  80 */         if (!this.disposed) {
/*  81 */           List<Disposable> set = this.resources;
/*  82 */           if (set == null) {
/*  83 */             set = new LinkedList<Disposable>();
/*  84 */             this.resources = set;
/*     */           } 
/*  86 */           set.add(d);
/*  87 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*  91 */     d.dispose();
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   public boolean addAll(Disposable... ds) {
/*  96 */     ObjectHelper.requireNonNull(ds, "ds is null");
/*  97 */     if (!this.disposed) {
/*  98 */       synchronized (this) {
/*  99 */         if (!this.disposed) {
/* 100 */           List<Disposable> set = this.resources;
/* 101 */           if (set == null) {
/* 102 */             set = new LinkedList<Disposable>();
/* 103 */             this.resources = set;
/*     */           } 
/* 105 */           for (Disposable d : ds) {
/* 106 */             ObjectHelper.requireNonNull(d, "d is null");
/* 107 */             set.add(d);
/*     */           } 
/* 109 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/* 113 */     for (Disposable d : ds) {
/* 114 */       d.dispose();
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Disposable d) {
/* 121 */     if (delete(d)) {
/* 122 */       d.dispose();
/* 123 */       return true;
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(Disposable d) {
/* 130 */     ObjectHelper.requireNonNull(d, "Disposable item is null");
/* 131 */     if (this.disposed) {
/* 132 */       return false;
/*     */     }
/* 134 */     synchronized (this) {
/* 135 */       if (this.disposed) {
/* 136 */         return false;
/*     */       }
/*     */       
/* 139 */       List<Disposable> set = this.resources;
/* 140 */       if (set == null || !set.remove(d)) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/* 144 */     return true;
/*     */   }
/*     */   public void clear() {
/*     */     List<Disposable> set;
/* 148 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     synchronized (this) {
/* 153 */       if (this.disposed) {
/*     */         return;
/*     */       }
/*     */       
/* 157 */       set = this.resources;
/* 158 */       this.resources = null;
/*     */     } 
/*     */     
/* 161 */     dispose(set);
/*     */   }
/*     */   
/*     */   void dispose(List<Disposable> set) {
/* 165 */     if (set == null) {
/*     */       return;
/*     */     }
/* 168 */     List<Throwable> errors = null;
/* 169 */     for (Disposable o : set) {
/*     */       try {
/* 171 */         o.dispose();
/* 172 */       } catch (Throwable ex) {
/* 173 */         Exceptions.throwIfFatal(ex);
/* 174 */         if (errors == null) {
/* 175 */           errors = new ArrayList<Throwable>();
/*     */         }
/* 177 */         errors.add(ex);
/*     */       } 
/*     */     } 
/* 180 */     if (errors != null) {
/* 181 */       if (errors.size() == 1) {
/* 182 */         throw ExceptionHelper.wrapOrThrow((Throwable)errors.get(0));
/*     */       }
/* 184 */       throw new CompositeException(errors);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/ListCompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */