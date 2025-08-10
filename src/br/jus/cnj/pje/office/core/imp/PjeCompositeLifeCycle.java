/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeLifeCycle;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.Predicates;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeCompositeLifeCycle
/*     */   implements IPjeLifeCycle
/*     */ {
/*     */   private final List<IPjeLifeCycle> cycles;
/*     */   private boolean started = false;
/*  50 */   private final BehaviorSubject<IPjeLifeCycle.LifeCycle> startup = BehaviorSubject.create();
/*     */   
/*     */   private Thread gc;
/*     */   
/*     */   PjeCompositeLifeCycle(IPjeLifeCycle... cycles) {
/*  55 */     this.cycles = Containers.arrayList(Predicates.notNull(), (Object[])cycles);
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<IPjeLifeCycle.LifeCycle> lifeCycle() {
/*  60 */     return (Observable<IPjeLifeCycle.LifeCycle>)this.startup;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  65 */     return this.started;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws IOException {
/*  70 */     if (!isStarted()) {
/*  71 */       doStart();
/*  72 */       this.gc = Threads.startDaemon("pjeofficepro: GC-profiling", PjeCompositeLifeCycle::suggestGC);
/*  73 */       this.started = true;
/*  74 */       this.startup.onNext(IPjeLifeCycle.LifeCycle.STARTUP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doStart() throws IOException {
/*  79 */     for (IPjeLifeCycle cycle : this.cycles) {
/*     */       try {
/*  81 */         cycle.start();
/*  82 */       } catch (IOException e) {
/*  83 */         Throwables.quietly(() -> doStop(false));
/*  84 */         throw e;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stop(boolean kill) throws IOException {
/*  91 */     if (isStarted()) {
/*  92 */       doStop(kill);
/*  93 */       this.gc.interrupt();
/*  94 */       this.started = false;
/*  95 */       this.startup.onNext(IPjeLifeCycle.LifeCycle.SHUTDOWN);
/*  96 */       if (kill) {
/*  97 */         this.startup.onNext(IPjeLifeCycle.LifeCycle.KILL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean setDevmode(boolean devmode) {
/* 104 */     for (IPjeLifeCycle cycle : this.cycles) {
/* 105 */       cycle.setDevmode(devmode);
/*     */     }
/* 107 */     return devmode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProcessing() {
/* 112 */     for (IPjeLifeCycle cycle : this.cycles) {
/* 113 */       if (cycle.isProcessing())
/* 114 */         return true; 
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showAbout() {
/* 121 */     for (IPjeLifeCycle cycle : this.cycles) {
/* 122 */       cycle.showAbout();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(List<File> files) {
/* 128 */     for (IPjeLifeCycle cycle : this.cycles) {
/* 129 */       cycle.dispatch(files);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doStop(boolean kill) throws IOException {
/* 134 */     IOException error = null;
/* 135 */     for (IPjeLifeCycle cycle : this.cycles) {
/*     */       try {
/* 137 */         cycle.stop(kill);
/* 138 */       } catch (IOException e) {
/* 139 */         if (error == null) {
/* 140 */           error = e; continue;
/*     */         } 
/* 142 */         error.addSuppressed(e);
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (error != null) {
/* 147 */       throw error;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stop() throws IOException {
/* 153 */     stop(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void suggestGC() {
/*     */     do {
/*     */       try {
/* 160 */         Thread.sleep(20000L);
/* 161 */       } catch (InterruptedException e) {
/*     */         break;
/*     */       } 
/* 164 */       System.gc();
/* 165 */     } while (!Thread.currentThread().isInterrupted());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeCompositeLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */