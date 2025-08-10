/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class EndpointPairIterator<N>
/*     */   extends AbstractIterator<EndpointPair<N>>
/*     */ {
/*     */   private final BaseGraph<N> graph;
/*     */   private final Iterator<N> nodeIterator;
/*     */   @CheckForNull
/*  40 */   N node = null;
/*     */ 
/*     */   
/*  43 */   Iterator<N> successorIterator = (Iterator<N>)ImmutableSet.of().iterator();
/*     */   
/*     */   static <N> EndpointPairIterator<N> of(BaseGraph<N> graph) {
/*  46 */     return graph.isDirected() ? new Directed<>(graph) : new Undirected<>(graph);
/*     */   }
/*     */   
/*     */   private EndpointPairIterator(BaseGraph<N> graph) {
/*  50 */     this.graph = graph;
/*  51 */     this.nodeIterator = graph.nodes().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean advance() {
/*  59 */     Preconditions.checkState(!this.successorIterator.hasNext());
/*  60 */     if (!this.nodeIterator.hasNext()) {
/*  61 */       return false;
/*     */     }
/*  63 */     this.node = this.nodeIterator.next();
/*  64 */     this.successorIterator = this.graph.successors(this.node).iterator();
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Directed<N>
/*     */     extends EndpointPairIterator<N>
/*     */   {
/*     */     private Directed(BaseGraph<N> graph) {
/*  74 */       super(graph);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected EndpointPair<N> computeNext() {
/*     */       while (true) {
/*  81 */         if (this.successorIterator.hasNext())
/*     */         {
/*  83 */           return EndpointPair.ordered(Objects.requireNonNull(this.node), this.successorIterator.next());
/*     */         }
/*  85 */         if (!advance()) {
/*  86 */           return (EndpointPair<N>)endOfData();
/*     */         }
/*     */       } 
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
/*     */ 
/*     */   
/*     */   private static final class Undirected<N>
/*     */     extends EndpointPairIterator<N>
/*     */   {
/*     */     @CheckForNull
/*     */     private Set<N> visitedNodes;
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
/*     */     private Undirected(BaseGraph<N> graph) {
/* 123 */       super(graph);
/* 124 */       this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected EndpointPair<N> computeNext() {
/*     */       while (true) {
/* 135 */         Objects.requireNonNull(this.visitedNodes);
/* 136 */         while (this.successorIterator.hasNext()) {
/* 137 */           N otherNode = this.successorIterator.next();
/* 138 */           if (!this.visitedNodes.contains(otherNode))
/*     */           {
/* 140 */             return EndpointPair.unordered(Objects.requireNonNull(this.node), otherNode);
/*     */           }
/*     */         } 
/*     */         
/* 144 */         this.visitedNodes.add(this.node);
/* 145 */         if (!advance()) {
/* 146 */           this.visitedNodes = null;
/* 147 */           return (EndpointPair<N>)endOfData();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/EndpointPairIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */