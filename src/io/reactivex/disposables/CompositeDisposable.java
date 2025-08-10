/*     */ package io.reactivex.disposables;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableContainer;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.OpenHashSet;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CompositeDisposable
/*     */   implements Disposable, DisposableContainer
/*     */ {
/*     */   OpenHashSet<Disposable> resources;
/*     */   volatile boolean disposed;
/*     */   
/*     */   public CompositeDisposable() {}
/*     */   
/*     */   public CompositeDisposable(@NonNull Disposable... disposables) {
/*  45 */     ObjectHelper.requireNonNull(disposables, "disposables is null");
/*  46 */     this.resources = new OpenHashSet(disposables.length + 1);
/*  47 */     for (Disposable d : disposables) {
/*  48 */       ObjectHelper.requireNonNull(d, "A Disposable in the disposables array is null");
/*  49 */       this.resources.add(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeDisposable(@NonNull Iterable<? extends Disposable> disposables) {
/*  59 */     ObjectHelper.requireNonNull(disposables, "disposables is null");
/*  60 */     this.resources = new OpenHashSet();
/*  61 */     for (Disposable d : disposables) {
/*  62 */       ObjectHelper.requireNonNull(d, "A Disposable item in the disposables sequence is null");
/*  63 */       this.resources.add(d);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dispose() {
/*     */     OpenHashSet<Disposable> set;
/*  69 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     synchronized (this) {
/*  74 */       if (this.disposed) {
/*     */         return;
/*     */       }
/*  77 */       this.disposed = true;
/*  78 */       set = this.resources;
/*  79 */       this.resources = null;
/*     */     } 
/*     */     
/*  82 */     dispose(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  87 */     return this.disposed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(@NonNull Disposable disposable) {
/*  99 */     ObjectHelper.requireNonNull(disposable, "disposable is null");
/* 100 */     if (!this.disposed) {
/* 101 */       synchronized (this) {
/* 102 */         if (!this.disposed) {
/* 103 */           OpenHashSet<Disposable> set = this.resources;
/* 104 */           if (set == null) {
/* 105 */             set = new OpenHashSet();
/* 106 */             this.resources = set;
/*     */           } 
/* 108 */           set.add(disposable);
/* 109 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/* 113 */     disposable.dispose();
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(@NonNull Disposable... disposables) {
/* 125 */     ObjectHelper.requireNonNull(disposables, "disposables is null");
/* 126 */     if (!this.disposed) {
/* 127 */       synchronized (this) {
/* 128 */         if (!this.disposed) {
/* 129 */           OpenHashSet<Disposable> set = this.resources;
/* 130 */           if (set == null) {
/* 131 */             set = new OpenHashSet(disposables.length + 1);
/* 132 */             this.resources = set;
/*     */           } 
/* 134 */           for (Disposable d : disposables) {
/* 135 */             ObjectHelper.requireNonNull(d, "A Disposable in the disposables array is null");
/* 136 */             set.add(d);
/*     */           } 
/* 138 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/* 142 */     for (Disposable d : disposables) {
/* 143 */       d.dispose();
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(@NonNull Disposable disposable) {
/* 156 */     if (delete(disposable)) {
/* 157 */       disposable.dispose();
/* 158 */       return true;
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean delete(@NonNull Disposable disposable) {
/* 172 */     ObjectHelper.requireNonNull(disposable, "disposables is null");
/* 173 */     if (this.disposed) {
/* 174 */       return false;
/*     */     }
/* 176 */     synchronized (this) {
/* 177 */       if (this.disposed) {
/* 178 */         return false;
/*     */       }
/*     */       
/* 181 */       OpenHashSet<Disposable> set = this.resources;
/* 182 */       if (set == null || !set.remove(disposable)) {
/* 183 */         return false;
/*     */       }
/*     */     } 
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*     */     OpenHashSet<Disposable> set;
/* 193 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/* 197 */     synchronized (this) {
/* 198 */       if (this.disposed) {
/*     */         return;
/*     */       }
/*     */       
/* 202 */       set = this.resources;
/* 203 */       this.resources = null;
/*     */     } 
/*     */     
/* 206 */     dispose(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 214 */     if (this.disposed) {
/* 215 */       return 0;
/*     */     }
/* 217 */     synchronized (this) {
/* 218 */       if (this.disposed) {
/* 219 */         return 0;
/*     */       }
/* 221 */       OpenHashSet<Disposable> set = this.resources;
/* 222 */       return (set != null) ? set.size() : 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void dispose(OpenHashSet<Disposable> set) {
/* 232 */     if (set == null) {
/*     */       return;
/*     */     }
/* 235 */     List<Throwable> errors = null;
/* 236 */     Object[] array = set.keys();
/* 237 */     for (Object o : array) {
/* 238 */       if (o instanceof Disposable) {
/*     */         try {
/* 240 */           ((Disposable)o).dispose();
/* 241 */         } catch (Throwable ex) {
/* 242 */           Exceptions.throwIfFatal(ex);
/* 243 */           if (errors == null) {
/* 244 */             errors = new ArrayList<Throwable>();
/*     */           }
/* 246 */           errors.add(ex);
/*     */         } 
/*     */       }
/*     */     } 
/* 250 */     if (errors != null) {
/* 251 */       if (errors.size() == 1) {
/* 252 */         throw ExceptionHelper.wrapOrThrow((Throwable)errors.get(0));
/*     */       }
/* 254 */       throw new CompositeException(errors);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/CompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */