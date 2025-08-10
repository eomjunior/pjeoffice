/*     */ package io.reactivex.internal.queue;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class MpscLinkedQueue<T>
/*     */   implements SimplePlainQueue<T>
/*     */ {
/*  35 */   private final AtomicReference<LinkedQueueNode<T>> producerNode = new AtomicReference<LinkedQueueNode<T>>();
/*  36 */   private final AtomicReference<LinkedQueueNode<T>> consumerNode = new AtomicReference<LinkedQueueNode<T>>(); public MpscLinkedQueue() {
/*  37 */     LinkedQueueNode<T> node = new LinkedQueueNode<T>();
/*  38 */     spConsumerNode(node);
/*  39 */     xchgProducerNode(node);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(T e) {
/*  59 */     if (null == e) {
/*  60 */       throw new NullPointerException("Null is not a valid element");
/*     */     }
/*  62 */     LinkedQueueNode<T> nextNode = new LinkedQueueNode<T>(e);
/*  63 */     LinkedQueueNode<T> prevProducerNode = xchgProducerNode(nextNode);
/*     */ 
/*     */     
/*  66 */     prevProducerNode.soNext(nextNode);
/*  67 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T poll() {
/*  88 */     LinkedQueueNode<T> currConsumerNode = lpConsumerNode();
/*  89 */     LinkedQueueNode<T> nextNode = currConsumerNode.lvNext();
/*  90 */     if (nextNode != null) {
/*     */       
/*  92 */       T nextValue = nextNode.getAndNullValue();
/*  93 */       spConsumerNode(nextNode);
/*  94 */       return nextValue;
/*     */     } 
/*  96 */     if (currConsumerNode != lvProducerNode()) {
/*     */       
/*  98 */       while ((nextNode = currConsumerNode.lvNext()) == null);
/*     */ 
/*     */ 
/*     */       
/* 102 */       T nextValue = nextNode.getAndNullValue();
/* 103 */       spConsumerNode(nextNode);
/* 104 */       return nextValue;
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(T v1, T v2) {
/* 111 */     offer(v1);
/* 112 */     offer(v2);
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 118 */     while (poll() != null && !isEmpty());
/*     */   }
/*     */   LinkedQueueNode<T> lvProducerNode() {
/* 121 */     return this.producerNode.get();
/*     */   }
/*     */   LinkedQueueNode<T> xchgProducerNode(LinkedQueueNode<T> node) {
/* 124 */     return this.producerNode.getAndSet(node);
/*     */   }
/*     */   LinkedQueueNode<T> lvConsumerNode() {
/* 127 */     return this.consumerNode.get();
/*     */   }
/*     */   
/*     */   LinkedQueueNode<T> lpConsumerNode() {
/* 131 */     return this.consumerNode.get();
/*     */   }
/*     */   void spConsumerNode(LinkedQueueNode<T> node) {
/* 134 */     this.consumerNode.lazySet(node);
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
/*     */   public boolean isEmpty() {
/* 147 */     return (lvConsumerNode() == lvProducerNode());
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LinkedQueueNode<E>
/*     */     extends AtomicReference<LinkedQueueNode<E>>
/*     */   {
/*     */     private static final long serialVersionUID = 2404266111789071508L;
/*     */     private E value;
/*     */     
/*     */     LinkedQueueNode() {}
/*     */     
/*     */     LinkedQueueNode(E val) {
/* 160 */       spValue(val);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E getAndNullValue() {
/* 168 */       E temp = lpValue();
/* 169 */       spValue((E)null);
/* 170 */       return temp;
/*     */     }
/*     */     
/*     */     public E lpValue() {
/* 174 */       return this.value;
/*     */     }
/*     */     
/*     */     public void spValue(E newValue) {
/* 178 */       this.value = newValue;
/*     */     }
/*     */     
/*     */     public void soNext(LinkedQueueNode<E> n) {
/* 182 */       lazySet(n);
/*     */     }
/*     */     
/*     */     public LinkedQueueNode<E> lvNext() {
/* 186 */       return get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/queue/MpscLinkedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */