/*     */ package org.reactivestreams;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Flow;
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
/*     */ public final class FlowAdapters
/*     */ {
/*     */   private FlowAdapters() {
/*  23 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Publisher<T> toPublisher(Flow.Publisher<? extends T> flowPublisher) {
/*     */     Publisher<T> publisher;
/*  35 */     Objects.requireNonNull(flowPublisher, "flowPublisher");
/*     */     
/*  37 */     if (flowPublisher instanceof FlowPublisherFromReactive) {
/*  38 */       publisher = ((FlowPublisherFromReactive)flowPublisher).reactiveStreams;
/*  39 */     } else if (flowPublisher instanceof Publisher) {
/*  40 */       publisher = (Publisher)flowPublisher;
/*     */     } else {
/*  42 */       publisher = new ReactivePublisherFromFlow<T>(flowPublisher);
/*     */     } 
/*  44 */     return publisher;
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
/*     */   public static <T> Flow.Publisher<T> toFlowPublisher(Publisher<? extends T> reactiveStreamsPublisher) {
/*     */     Flow.Publisher<T> flowPublisher;
/*  57 */     Objects.requireNonNull(reactiveStreamsPublisher, "reactiveStreamsPublisher");
/*     */     
/*  59 */     if (reactiveStreamsPublisher instanceof ReactivePublisherFromFlow) {
/*  60 */       flowPublisher = ((ReactivePublisherFromFlow)reactiveStreamsPublisher).flow;
/*  61 */     } else if (reactiveStreamsPublisher instanceof Flow.Publisher) {
/*  62 */       flowPublisher = (Flow.Publisher)reactiveStreamsPublisher;
/*     */     } else {
/*  64 */       flowPublisher = new FlowPublisherFromReactive<T>(reactiveStreamsPublisher);
/*     */     } 
/*  66 */     return flowPublisher;
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
/*     */   
/*     */   public static <T, U> Processor<T, U> toProcessor(Flow.Processor<? super T, ? extends U> flowProcessor) {
/*     */     Processor<T, U> processor;
/*  80 */     Objects.requireNonNull(flowProcessor, "flowProcessor");
/*     */     
/*  82 */     if (flowProcessor instanceof FlowToReactiveProcessor) {
/*  83 */       processor = ((FlowToReactiveProcessor)flowProcessor).reactiveStreams;
/*  84 */     } else if (flowProcessor instanceof Processor) {
/*  85 */       processor = (Processor)flowProcessor;
/*     */     } else {
/*  87 */       processor = new ReactiveToFlowProcessor<T, U>(flowProcessor);
/*     */     } 
/*  89 */     return processor;
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
/*     */   
/*     */   public static <T, U> Flow.Processor<T, U> toFlowProcessor(Processor<? super T, ? extends U> reactiveStreamsProcessor) {
/*     */     Flow.Processor<T, U> flowProcessor;
/* 103 */     Objects.requireNonNull(reactiveStreamsProcessor, "reactiveStreamsProcessor");
/*     */     
/* 105 */     if (reactiveStreamsProcessor instanceof ReactiveToFlowProcessor) {
/* 106 */       flowProcessor = ((ReactiveToFlowProcessor)reactiveStreamsProcessor).flow;
/* 107 */     } else if (reactiveStreamsProcessor instanceof Flow.Processor) {
/* 108 */       flowProcessor = (Flow.Processor)reactiveStreamsProcessor;
/*     */     } else {
/* 110 */       flowProcessor = new FlowToReactiveProcessor<T, U>(reactiveStreamsProcessor);
/*     */     } 
/* 112 */     return flowProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Flow.Subscriber<T> toFlowSubscriber(Subscriber<T> reactiveStreamsSubscriber) {
/*     */     Flow.Subscriber<T> flowSubscriber;
/* 123 */     Objects.requireNonNull(reactiveStreamsSubscriber, "reactiveStreamsSubscriber");
/*     */     
/* 125 */     if (reactiveStreamsSubscriber instanceof ReactiveToFlowSubscriber) {
/* 126 */       flowSubscriber = ((ReactiveToFlowSubscriber)reactiveStreamsSubscriber).flow;
/* 127 */     } else if (reactiveStreamsSubscriber instanceof Flow.Subscriber) {
/* 128 */       flowSubscriber = (Flow.Subscriber<T>)reactiveStreamsSubscriber;
/*     */     } else {
/* 130 */       flowSubscriber = new FlowToReactiveSubscriber<T>(reactiveStreamsSubscriber);
/*     */     } 
/* 132 */     return flowSubscriber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Subscriber<T> toSubscriber(Flow.Subscriber<T> flowSubscriber) {
/*     */     Subscriber<T> subscriber;
/* 143 */     Objects.requireNonNull(flowSubscriber, "flowSubscriber");
/*     */     
/* 145 */     if (flowSubscriber instanceof FlowToReactiveSubscriber) {
/* 146 */       subscriber = ((FlowToReactiveSubscriber)flowSubscriber).reactiveStreams;
/* 147 */     } else if (flowSubscriber instanceof Subscriber) {
/* 148 */       subscriber = (Subscriber<T>)flowSubscriber;
/*     */     } else {
/* 150 */       subscriber = new ReactiveToFlowSubscriber<T>(flowSubscriber);
/*     */     } 
/* 152 */     return subscriber;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlowToReactiveSubscription
/*     */     implements Flow.Subscription
/*     */   {
/*     */     final Subscription reactiveStreams;
/*     */     
/*     */     public FlowToReactiveSubscription(Subscription reactive) {
/* 162 */       this.reactiveStreams = reactive;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 167 */       this.reactiveStreams.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 172 */       this.reactiveStreams.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReactiveToFlowSubscription
/*     */     implements Subscription
/*     */   {
/*     */     final Flow.Subscription flow;
/*     */ 
/*     */     
/*     */     public ReactiveToFlowSubscription(Flow.Subscription flow) {
/* 184 */       this.flow = flow;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 189 */       this.flow.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 194 */       this.flow.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class FlowToReactiveSubscriber<T>
/*     */     implements Flow.Subscriber<T>
/*     */   {
/*     */     final Subscriber<? super T> reactiveStreams;
/*     */ 
/*     */ 
/*     */     
/*     */     public FlowToReactiveSubscriber(Subscriber<? super T> reactive) {
/* 208 */       this.reactiveStreams = reactive;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Flow.Subscription subscription) {
/* 213 */       this.reactiveStreams.onSubscribe((subscription == null) ? null : new FlowAdapters.ReactiveToFlowSubscription(subscription));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T item) {
/* 218 */       this.reactiveStreams.onNext(item);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable throwable) {
/* 223 */       this.reactiveStreams.onError(throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 228 */       this.reactiveStreams.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ReactiveToFlowSubscriber<T>
/*     */     implements Subscriber<T>
/*     */   {
/*     */     final Flow.Subscriber<? super T> flow;
/*     */ 
/*     */     
/*     */     public ReactiveToFlowSubscriber(Flow.Subscriber<? super T> flow) {
/* 241 */       this.flow = flow;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 246 */       this.flow.onSubscribe((subscription == null) ? null : new FlowAdapters.FlowToReactiveSubscription(subscription));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T item) {
/* 251 */       this.flow.onNext(item);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable throwable) {
/* 256 */       this.flow.onError(throwable);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 261 */       this.flow.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ReactiveToFlowProcessor<T, U>
/*     */     implements Processor<T, U>
/*     */   {
/*     */     final Flow.Processor<? super T, ? extends U> flow;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReactiveToFlowProcessor(Flow.Processor<? super T, ? extends U> flow) {
/* 275 */       this.flow = flow;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 280 */       this.flow.onSubscribe((subscription == null) ? null : new FlowAdapters.FlowToReactiveSubscription(subscription));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 285 */       this.flow.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 290 */       this.flow.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 295 */       this.flow.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Subscriber<? super U> s) {
/* 300 */       this.flow.subscribe((s == null) ? null : new FlowAdapters.FlowToReactiveSubscriber<U>(s));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class FlowToReactiveProcessor<T, U>
/*     */     implements Flow.Processor<T, U>
/*     */   {
/*     */     final Processor<? super T, ? extends U> reactiveStreams;
/*     */ 
/*     */     
/*     */     public FlowToReactiveProcessor(Processor<? super T, ? extends U> reactive) {
/* 313 */       this.reactiveStreams = reactive;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Flow.Subscription subscription) {
/* 318 */       this.reactiveStreams.onSubscribe((subscription == null) ? null : new FlowAdapters.ReactiveToFlowSubscription(subscription));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 323 */       this.reactiveStreams.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 328 */       this.reactiveStreams.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 333 */       this.reactiveStreams.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Flow.Subscriber<? super U> s) {
/* 338 */       this.reactiveStreams.subscribe((s == null) ? null : new FlowAdapters.ReactiveToFlowSubscriber<U>(s));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReactivePublisherFromFlow<T>
/*     */     implements Publisher<T>
/*     */   {
/*     */     final Flow.Publisher<? extends T> flow;
/*     */ 
/*     */     
/*     */     public ReactivePublisherFromFlow(Flow.Publisher<? extends T> flowPublisher) {
/* 350 */       this.flow = flowPublisher;
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Subscriber<? super T> reactive) {
/* 355 */       this.flow.subscribe((reactive == null) ? null : new FlowAdapters.FlowToReactiveSubscriber<T>(reactive));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class FlowPublisherFromReactive<T>
/*     */     implements Flow.Publisher<T>
/*     */   {
/*     */     final Publisher<? extends T> reactiveStreams;
/*     */ 
/*     */     
/*     */     public FlowPublisherFromReactive(Publisher<? extends T> reactivePublisher) {
/* 368 */       this.reactiveStreams = reactivePublisher;
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Flow.Subscriber<? super T> flow) {
/* 373 */       this.reactiveStreams.subscribe((flow == null) ? null : new FlowAdapters.ReactiveToFlowSubscriber<T>(flow));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/reactivestreams/FlowAdapters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */