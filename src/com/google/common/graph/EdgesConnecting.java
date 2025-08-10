/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ final class EdgesConnecting<E>
/*    */   extends AbstractSet<E>
/*    */ {
/*    */   private final Map<?, E> nodeToOutEdge;
/*    */   private final Object targetNode;
/*    */   
/*    */   EdgesConnecting(Map<?, E> nodeToEdgeMap, Object targetNode) {
/* 44 */     this.nodeToOutEdge = (Map<?, E>)Preconditions.checkNotNull(nodeToEdgeMap);
/* 45 */     this.targetNode = Preconditions.checkNotNull(targetNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<E> iterator() {
/* 50 */     E connectingEdge = getConnectingEdge();
/* 51 */     return (connectingEdge == null) ? 
/* 52 */       ImmutableSet.of().iterator() : 
/* 53 */       Iterators.singletonIterator(connectingEdge);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 58 */     return (getConnectingEdge() == null) ? 0 : 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object edge) {
/* 63 */     E connectingEdge = getConnectingEdge();
/* 64 */     return (connectingEdge != null && connectingEdge.equals(edge));
/*    */   }
/*    */   
/*    */   @CheckForNull
/*    */   private E getConnectingEdge() {
/* 69 */     return this.nodeToOutEdge.get(this.targetNode);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/EdgesConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */