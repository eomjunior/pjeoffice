/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.util.Set;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @Beta
/*    */ public abstract class AbstractGraph<N>
/*    */   extends AbstractBaseGraph<N>
/*    */   implements Graph<N>
/*    */ {
/*    */   public final boolean equals(@CheckForNull Object obj) {
/* 36 */     if (obj == this) {
/* 37 */       return true;
/*    */     }
/* 39 */     if (!(obj instanceof Graph)) {
/* 40 */       return false;
/*    */     }
/* 42 */     Graph<?> other = (Graph)obj;
/*    */     
/* 44 */     return (isDirected() == other.isDirected() && 
/* 45 */       nodes().equals(other.nodes()) && 
/* 46 */       edges().equals(other.edges()));
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 51 */     return edges().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "isDirected: " + 
/* 58 */       isDirected() + ", allowsSelfLoops: " + 
/*    */       
/* 60 */       allowsSelfLoops() + ", nodes: " + 
/*    */       
/* 62 */       nodes() + ", edges: " + 
/*    */       
/* 64 */       edges();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */