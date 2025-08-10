/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.collect.BiMap;
/*    */ import com.google.common.collect.HashBiMap;
/*    */ import com.google.common.collect.ImmutableBiMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ final class UndirectedNetworkConnections<N, E>
/*    */   extends AbstractUndirectedNetworkConnections<N, E>
/*    */ {
/*    */   UndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
/* 39 */     super(incidentEdgeMap);
/*    */   }
/*    */   
/*    */   static <N, E> UndirectedNetworkConnections<N, E> of() {
/* 43 */     return new UndirectedNetworkConnections<>((Map<E, N>)HashBiMap.create(2));
/*    */   }
/*    */   
/*    */   static <N, E> UndirectedNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
/* 47 */     return new UndirectedNetworkConnections<>((Map<E, N>)ImmutableBiMap.copyOf(incidentEdges));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> adjacentNodes() {
/* 52 */     return Collections.unmodifiableSet(((BiMap)this.incidentEdgeMap).values());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> edgesConnecting(N node) {
/* 57 */     return new EdgesConnecting<>((Map<?, E>)((BiMap)this.incidentEdgeMap).inverse(), node);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/UndirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */