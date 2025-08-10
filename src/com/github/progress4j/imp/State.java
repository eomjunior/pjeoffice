/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.StopWatch;
/*     */ import com.github.utils4j.imp.Throwables;
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
/*     */ class State
/*     */   implements IState
/*     */ {
/*     */   private int step;
/*     */   private int total;
/*     */   private final IState parent;
/*     */   private final IStage stage;
/*  42 */   private Throwable abortCause = null;
/*     */   
/*     */   private final StopWatch stopWatch;
/*     */   
/*     */   State(IState parent, IStage stage, int total) {
/*  47 */     this(parent, stage, 0, total);
/*     */   }
/*     */   
/*     */   private State(IState parent, IStage stage, int step, int total) {
/*  51 */     this.parent = parent;
/*  52 */     this.stage = stage;
/*  53 */     this.step = step;
/*  54 */     this.total = total;
/*  55 */     this.stopWatch = new StopWatch();
/*  56 */     this.stopWatch.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getStep() {
/*  61 */     return this.step;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getStepTree() {
/*  66 */     return ((this.parent != null) ? (this.parent.getStepTree() + ".") : "") + getStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable getAbortCause() {
/*  71 */     return this.abortCause;
/*     */   }
/*     */   
/*     */   public final long incrementAndGet(long steps) {
/*  75 */     if (isAborted())
/*  76 */       return -1L; 
/*  77 */     if ((this.step = (int)(this.step + steps)) > this.total && this.total > 0) {
/*  78 */       this.stopWatch.getLogger().warn("Stage {} tem mais passos que o total. Step: {}, Total: {} ", new Object[] { this.stage, 
/*     */ 
/*     */ 
/*     */             
/*  82 */             Integer.valueOf(this.step), 
/*  83 */             Integer.valueOf(this.total) });
/*     */ 
/*     */       
/*  86 */       this.total = this.step;
/*     */     } 
/*  88 */     return this.step;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getTotal() {
/*  93 */     return this.total;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IStage getStage() {
/*  98 */     return this.stage;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getTime() {
/* 103 */     return this.stopWatch.getTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAborted() {
/* 108 */     return (this.abortCause != null);
/*     */   }
/*     */   
/*     */   final IState end() {
/* 112 */     this.stopWatch.stop();
/* 113 */     this.step = this.total;
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   final IState abort(Throwable exception) {
/* 118 */     Args.requireNonNull(exception, "exception is null");
/* 119 */     this.stopWatch.stop((this.abortCause = exception).getMessage());
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     StringBuilder builder = new StringBuilder();
/* 126 */     builder.append("State [step=");
/* 127 */     builder.append(this.step);
/* 128 */     builder.append(", total=");
/* 129 */     builder.append(this.total);
/* 130 */     builder.append(", ");
/* 131 */     if (this.stage != null) {
/* 132 */       builder.append("stage=");
/* 133 */       builder.append(this.stage);
/* 134 */       builder.append(", ");
/*     */     } 
/* 136 */     builder.append("time=");
/* 137 */     builder.append(getTime());
/* 138 */     builder.append(", ");
/* 139 */     builder.append("abort=");
/* 140 */     builder.append(isAborted());
/* 141 */     if (this.abortCause != null) {
/* 142 */       builder.append(", ");
/* 143 */       builder.append("abortCause:\n");
/* 144 */       builder.append(Throwables.rootTrace(this.abortCause));
/*     */     } 
/* 146 */     builder.append("]");
/* 147 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/State.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */