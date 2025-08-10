/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class UndirectedGraphConnections<N, V>
/*     */   implements GraphConnections<N, V>
/*     */ {
/*     */   private final Map<N, V> adjacentNodeValues;
/*     */   
/*     */   private UndirectedGraphConnections(Map<N, V> adjacentNodeValues) {
/*  45 */     this.adjacentNodeValues = (Map<N, V>)Preconditions.checkNotNull(adjacentNodeValues);
/*     */   }
/*     */   
/*     */   static <N, V> UndirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
/*  49 */     switch (incidentEdgeOrder.type()) {
/*     */       case UNORDERED:
/*  51 */         return new UndirectedGraphConnections<>(new HashMap<>(2, 1.0F));
/*     */       
/*     */       case STABLE:
/*  54 */         return new UndirectedGraphConnections<>(new LinkedHashMap<>(2, 1.0F));
/*     */     } 
/*     */     
/*  57 */     throw new AssertionError(incidentEdgeOrder.type());
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues) {
/*  62 */     return new UndirectedGraphConnections<>((Map<N, V>)ImmutableMap.copyOf(adjacentNodeValues));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  67 */     return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  72 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  77 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EndpointPair<N>> incidentEdgeIterator(N thisNode) {
/*  82 */     return Iterators.transform(this.adjacentNodeValues
/*  83 */         .keySet().iterator(), incidentNode -> EndpointPair.unordered(thisNode, incidentNode));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V value(N node) {
/*  90 */     return this.adjacentNodeValues.get(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePredecessor(N node) {
/*  96 */     V unused = removeSuccessor(node);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V removeSuccessor(N node) {
/* 102 */     return this.adjacentNodeValues.remove(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPredecessor(N node, V value) {
/* 108 */     V unused = addSuccessor(node, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V addSuccessor(N node, V value) {
/* 114 */     return this.adjacentNodeValues.put(node, value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/UndirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */