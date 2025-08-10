/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.AbstractIterator;
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
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ abstract class MultiEdgesConnecting<E>
/*    */   extends AbstractSet<E>
/*    */ {
/*    */   private final Map<E, ?> outEdgeToNode;
/*    */   private final Object targetNode;
/*    */   
/*    */   MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode) {
/* 45 */     this.outEdgeToNode = (Map<E, ?>)Preconditions.checkNotNull(outEdgeToNode);
/* 46 */     this.targetNode = Preconditions.checkNotNull(targetNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<E> iterator() {
/* 51 */     final Iterator<? extends Map.Entry<E, ?>> entries = this.outEdgeToNode.entrySet().iterator();
/* 52 */     return (UnmodifiableIterator<E>)new AbstractIterator<E>()
/*    */       {
/*    */         @CheckForNull
/*    */         protected E computeNext() {
/* 56 */           while (entries.hasNext()) {
/* 57 */             Map.Entry<E, ?> entry = entries.next();
/* 58 */             if (MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
/* 59 */               return entry.getKey();
/*    */             }
/*    */           } 
/* 62 */           return (E)endOfData();
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object edge) {
/* 69 */     return this.targetNode.equals(this.outEdgeToNode.get(edge));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/MultiEdgesConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */