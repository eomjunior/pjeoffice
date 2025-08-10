/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Ids;
/*     */ import com.github.utils4j.imp.Stack;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.util.function.Consumer;
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
/*     */ public class DefaultProgress
/*     */   implements IProgress
/*     */ {
/*     */   private boolean closed = false;
/*     */   private volatile boolean interrupted = false;
/*  52 */   private final Stack<State> stack = new Stack();
/*     */   
/*     */   private BehaviorSubject<IStepEvent> stepSubject;
/*     */   
/*     */   private BehaviorSubject<IStageEvent> stageSubject;
/*     */   
/*     */   private BehaviorSubject<IProgress> disposeSubject;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public DefaultProgress() {
/*  63 */     this(Ids.next());
/*     */   }
/*     */   
/*     */   public DefaultProgress(String name) {
/*  67 */     this.name = Args.requireText(name, "name can't be null");
/*  68 */     resetObservables();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(String stage) throws InterruptedException {
/*  78 */     begin(new Stage(stage));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(String stage, int total) throws InterruptedException {
/*  83 */     begin(new Stage(stage), total);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage) throws InterruptedException {
/*  88 */     begin(stage, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage, int total) throws InterruptedException {
/*  93 */     checkInterrupted();
/*  94 */     this.closed = this.interrupted = false;
/*  95 */     State state = new State(this.stack.isEmpty() ? null : (IState)this.stack.peek(), stage, total);
/*  96 */     notifyStage(state, stage.beginString(), false);
/*  97 */     this.stack.push(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void step(String message, Object... args) throws InterruptedException {
/* 102 */     send(true, message, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void skip(long steps) throws InterruptedException {
/* 107 */     checkInterrupted();
/*     */     State currentState;
/* 109 */     if (this.stack.isEmpty() || (currentState = (State)this.stack.peek()).isAborted())
/*     */       return; 
/* 111 */     currentState.incrementAndGet(steps);
/* 112 */     notifyStep(currentState, "Skip", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void info(String message, Object... args) throws InterruptedException {
/* 117 */     send(false, message, args);
/*     */   }
/*     */   
/*     */   private void send(boolean advance, String message, Object... args) throws InterruptedException {
/* 121 */     checkInterrupted();
/* 122 */     if (this.stack.isEmpty())
/*     */       return; 
/* 124 */     State currentState = (State)this.stack.peek();
/* 125 */     if (advance) {
/* 126 */       if (currentState.isAborted())
/*     */         return; 
/* 128 */       currentState.incrementAndGet(1L);
/*     */     } 
/* 130 */     notifyStep(currentState, String.format(message, args), !advance);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void end() throws InterruptedException {
/* 135 */     checkInterrupted();
/* 136 */     if (this.stack.isEmpty())
/*     */       return; 
/* 138 */     State state = (State)this.stack.pop();
/* 139 */     if (state.isAborted() && 
/* 140 */       !this.stack.isEmpty()) {
/* 141 */       ((State)this.stack.peek()).abort(state.getAbortCause());
/*     */     }
/*     */     
/* 144 */     state.end();
/* 145 */     String message = state.getStage().endString() + " em " + state.getTime() + "ms";
/* 146 */     notifyStage(state, message, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void interrupt() {
/* 151 */     this.interrupted = true;
/*     */   }
/*     */   
/*     */   private void checkInterrupted() throws InterruptedException {
/* 155 */     if (this.interrupted) {
/* 156 */       Thread.currentThread().interrupt();
/* 157 */       this.interrupted = false;
/*     */     } 
/* 159 */     if (Thread.currentThread().isInterrupted()) {
/* 160 */       throw (InterruptedException)abort(new InterruptedException("A thread foi interrompida!"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T extends Throwable> T abort(T exception) {
/* 166 */     Args.requireNonNull(exception, "exception is null");
/* 167 */     if (this.stack.isEmpty())
/* 168 */       return exception; 
/* 169 */     State currentState = (State)this.stack.peek();
/* 170 */     if (currentState.isAborted()) {
/* 171 */       return exception;
/*     */     }
/* 173 */     String message = Strings.trim(exception.getMessage()) + ". Causa: " + Throwables.rootTrace((Throwable)exception);
/* 174 */     notifyStep(currentState.abort((Throwable)exception), message, true);
/* 175 */     message = currentState.getStage().endString() + " abortado em " + currentState.getTime() + "ms";
/* 176 */     notifyStage(currentState, message, true);
/* 177 */     return exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable getAbortCause() {
/* 182 */     return this.stack.isEmpty() ? null : ((State)this.stack.peek()).getAbortCause();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void throwIfInterrupted() throws InterruptedException {
/* 187 */     Throwable cause = getAbortCause();
/* 188 */     if (cause instanceof InterruptedException) {
/* 189 */       throw (InterruptedException)cause;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/* 196 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IProgress reset() {
/* 201 */     if (!isClosed()) {
/*     */       try {
/* 203 */         this.stack.clear();
/* 204 */         complete();
/*     */       } finally {
/* 206 */         resetObservables();
/* 207 */         this.closed = true;
/*     */       } 
/*     */     }
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   private void complete() {
/*     */     try {
/* 215 */       this.stepSubject.onComplete();
/*     */     } finally {
/*     */       try {
/* 218 */         this.stageSubject.onComplete();
/*     */       } finally {
/* 220 */         this.disposeSubject.onComplete();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyStep(IState state, String message, boolean info) {
/* 226 */     this.stepSubject.onNext(new StepEvent(state, message, this.stack.size(), info));
/*     */   }
/*     */   
/*     */   private void notifyStage(IState state, String message, boolean end) {
/* 230 */     this.stageSubject.onNext(new StageEvent(state, message, this.stack.size(), end));
/*     */   }
/*     */   
/*     */   private void resetObservables() {
/* 234 */     this.stepSubject = BehaviorSubject.create();
/* 235 */     this.stageSubject = BehaviorSubject.create();
/* 236 */     this.disposeSubject = BehaviorSubject.create();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStepEvent> stepObservable() {
/* 241 */     return (Observable<IStepEvent>)this.stepSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStageEvent> stageObservable() {
/* 246 */     return (Observable<IStageEvent>)this.stageSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IProgress> disposeObservable() {
/* 251 */     return (Observable<IProgress>)this.disposeSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IProgress stackTracer(Consumer<IState> consumer) {
/* 256 */     this.stack.forEach(consumer);
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 262 */     this.disposeSubject.onNext(this);
/* 263 */     reset();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/DefaultProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */