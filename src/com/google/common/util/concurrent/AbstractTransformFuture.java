/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractTransformFuture<I, O, F, T>
/*     */   extends FluentFuture.TrustedFuture<O>
/*     */   implements Runnable
/*     */ {
/*     */   @CheckForNull
/*     */   ListenableFuture<? extends I> inputFuture;
/*     */   @CheckForNull
/*     */   F function;
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  42 */     Preconditions.checkNotNull(executor);
/*  43 */     AsyncTransformFuture<I, O> output = new AsyncTransformFuture<>(input, function);
/*  44 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  45 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  50 */     Preconditions.checkNotNull(function);
/*  51 */     TransformFuture<I, O> output = new TransformFuture<>(input, function);
/*  52 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  53 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
/*  64 */     this.inputFuture = (ListenableFuture<? extends I>)Preconditions.checkNotNull(inputFuture);
/*  65 */     this.function = (F)Preconditions.checkNotNull(function);
/*     */   }
/*     */   
/*     */   public final void run() {
/*     */     I sourceResult;
/*     */     T transformResult;
/*  71 */     ListenableFuture<? extends I> localInputFuture = this.inputFuture;
/*  72 */     F localFunction = this.function;
/*  73 */     if ((isCancelled() | ((localInputFuture == null) ? 1 : 0) | ((localFunction == null) ? 1 : 0)) != 0) {
/*     */       return;
/*     */     }
/*  76 */     this.inputFuture = null;
/*     */     
/*  78 */     if (localInputFuture.isCancelled()) {
/*     */ 
/*     */       
/*  81 */       boolean unused = setFuture(localInputFuture);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  95 */       sourceResult = Futures.getDone((Future)localInputFuture);
/*  96 */     } catch (CancellationException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       cancel(false);
/*     */       return;
/* 104 */     } catch (ExecutionException e) {
/*     */       
/* 106 */       setException(e.getCause());
/*     */       return;
/* 108 */     } catch (Exception e) {
/*     */       
/* 110 */       setException(e);
/*     */       return;
/* 112 */     } catch (Error e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       setException(e);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 124 */       transformResult = doTransform(localFunction, sourceResult);
/* 125 */     } catch (Throwable t) {
/* 126 */       Platform.restoreInterruptIfIsInterruptedException(t);
/*     */       
/* 128 */       setException(t);
/*     */       return;
/*     */     } finally {
/* 131 */       this.function = null;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     setResult(transformResult);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @ForOverride
/*     */   abstract T doTransform(F paramF, @ParametricNullness I paramI) throws Exception;
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(@ParametricNullness T paramT);
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/* 184 */     maybePropagateCancellationTo(this.inputFuture);
/* 185 */     this.inputFuture = null;
/* 186 */     this.function = null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected String pendingToString() {
/* 192 */     ListenableFuture<? extends I> localInputFuture = this.inputFuture;
/* 193 */     F localFunction = this.function;
/* 194 */     String superString = super.pendingToString();
/* 195 */     String resultString = "";
/* 196 */     if (localInputFuture != null) {
/* 197 */       resultString = "inputFuture=[" + localInputFuture + "], ";
/*     */     }
/* 199 */     if (localFunction != null)
/* 200 */       return resultString + "function=[" + localFunction + "]"; 
/* 201 */     if (superString != null) {
/* 202 */       return resultString + superString;
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class AsyncTransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
/*     */   {
/*     */     AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
/* 217 */       super(inputFuture, function);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @ParametricNullness I input) throws Exception {
/* 224 */       ListenableFuture<? extends O> outputFuture = function.apply(input);
/* 225 */       Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", function);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       return outputFuture;
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(ListenableFuture<? extends O> result) {
/* 235 */       setFuture(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
/*     */   {
/*     */     TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
/* 247 */       super(inputFuture, function);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     O doTransform(Function<? super I, ? extends O> function, @ParametricNullness I input) {
/* 253 */       return (O)function.apply(input);
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(@ParametricNullness O result) {
/* 258 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractTransformFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */