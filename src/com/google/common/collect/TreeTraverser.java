/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.function.Consumer;
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
/*     */ @Deprecated
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class TreeTraverser<T>
/*     */ {
/*     */   @Deprecated
/*     */   public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> nodeToChildrenFunction) {
/*  94 */     Preconditions.checkNotNull(nodeToChildrenFunction);
/*  95 */     return new TreeTraverser<T>()
/*     */       {
/*     */         public Iterable<T> children(T root) {
/*  98 */           return (Iterable<T>)nodeToChildrenFunction.apply(root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<T> children(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final FluentIterable<T> preOrderTraversal(final T root) {
/* 118 */     Preconditions.checkNotNull(root);
/* 119 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 122 */           return TreeTraverser.this.preOrderIterator((T)root);
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(final Consumer<? super T> action) {
/* 127 */           Preconditions.checkNotNull(action);
/* 128 */           (new Consumer<T>()
/*     */             {
/*     */               public void accept(T t) {
/* 131 */                 action.accept(t);
/* 132 */                 TreeTraverser.this.children(t).forEach(this);
/*     */               }
/* 134 */             }).accept((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   UnmodifiableIterator<T> preOrderIterator(T root) {
/* 140 */     return new PreOrderIterator(root);
/*     */   }
/*     */   
/*     */   private final class PreOrderIterator extends UnmodifiableIterator<T> {
/*     */     private final Deque<Iterator<T>> stack;
/*     */     
/*     */     PreOrderIterator(T root) {
/* 147 */       this.stack = new ArrayDeque<>();
/* 148 */       this.stack.addLast(Iterators.singletonIterator((T)Preconditions.checkNotNull(root)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 153 */       return !this.stack.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 158 */       Iterator<T> itr = this.stack.getLast();
/* 159 */       T result = (T)Preconditions.checkNotNull(itr.next());
/* 160 */       if (!itr.hasNext()) {
/* 161 */         this.stack.removeLast();
/*     */       }
/* 163 */       Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
/* 164 */       if (childItr.hasNext()) {
/* 165 */         this.stack.addLast(childItr);
/*     */       }
/* 167 */       return result;
/*     */     }
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
/*     */   @Deprecated
/*     */   public final FluentIterable<T> postOrderTraversal(final T root) {
/* 183 */     Preconditions.checkNotNull(root);
/* 184 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 187 */           return TreeTraverser.this.postOrderIterator((T)root);
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(final Consumer<? super T> action) {
/* 192 */           Preconditions.checkNotNull(action);
/* 193 */           (new Consumer<T>()
/*     */             {
/*     */               public void accept(T t) {
/* 196 */                 TreeTraverser.this.children(t).forEach(this);
/* 197 */                 action.accept(t);
/*     */               }
/* 199 */             }).accept((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   UnmodifiableIterator<T> postOrderIterator(T root) {
/* 205 */     return new PostOrderIterator(root);
/*     */   }
/*     */   
/*     */   private static final class PostOrderNode<T> {
/*     */     final T root;
/*     */     final Iterator<T> childIterator;
/*     */     
/*     */     PostOrderNode(T root, Iterator<T> childIterator) {
/* 213 */       this.root = (T)Preconditions.checkNotNull(root);
/* 214 */       this.childIterator = (Iterator<T>)Preconditions.checkNotNull(childIterator);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PostOrderIterator extends AbstractIterator<T> {
/*     */     private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
/*     */     
/*     */     PostOrderIterator(T root) {
/* 222 */       this.stack = new ArrayDeque<>();
/* 223 */       this.stack.addLast(expand(root));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected T computeNext() {
/* 229 */       while (!this.stack.isEmpty()) {
/* 230 */         TreeTraverser.PostOrderNode<T> top = this.stack.getLast();
/* 231 */         if (top.childIterator.hasNext()) {
/* 232 */           T child = top.childIterator.next();
/* 233 */           this.stack.addLast(expand(child)); continue;
/*     */         } 
/* 235 */         this.stack.removeLast();
/* 236 */         return top.root;
/*     */       } 
/*     */       
/* 239 */       return endOfData();
/*     */     }
/*     */     
/*     */     private TreeTraverser.PostOrderNode<T> expand(T t) {
/* 243 */       return new TreeTraverser.PostOrderNode<>(t, TreeTraverser.this.children(t).iterator());
/*     */     }
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
/*     */   @Deprecated
/*     */   public final FluentIterable<T> breadthFirstTraversal(final T root) {
/* 259 */     Preconditions.checkNotNull(root);
/* 260 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 263 */           return new TreeTraverser.BreadthFirstIterator((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private final class BreadthFirstIterator
/*     */     extends UnmodifiableIterator<T> implements PeekingIterator<T> {
/*     */     private final Queue<T> queue;
/*     */     
/*     */     BreadthFirstIterator(T root) {
/* 273 */       this.queue = new ArrayDeque<>();
/* 274 */       this.queue.add(root);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 279 */       return !this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T peek() {
/* 284 */       return this.queue.element();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 289 */       T result = this.queue.remove();
/* 290 */       Iterables.addAll(this.queue, TreeTraverser.this.children(result));
/* 291 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TreeTraverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */