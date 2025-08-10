/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotMock;
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
/*     */ @DoNotMock
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class GraphBuilder<N>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private GraphBuilder(boolean directed) {
/*  79 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> directed() {
/*  84 */     return new GraphBuilder(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> undirected() {
/*  89 */     return new GraphBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> GraphBuilder<N> from(Graph<N> graph) {
/* 100 */     return (new GraphBuilder(graph.isDirected()))
/* 101 */       .allowsSelfLoops(graph.allowsSelfLoops())
/* 102 */       .nodeOrder(graph.nodeOrder())
/* 103 */       .incidentEdgeOrder(graph.incidentEdgeOrder());
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
/*     */   public <N1 extends N> ImmutableGraph.Builder<N1> immutable() {
/* 117 */     GraphBuilder<N1> castBuilder = cast();
/* 118 */     return new ImmutableGraph.Builder<>(castBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
/* 130 */     this.allowsSelfLoops = allowsSelfLoops;
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
/* 141 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 151 */     GraphBuilder<N1> newBuilder = cast();
/* 152 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 153 */     return newBuilder;
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
/*     */   public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(ElementOrder<N1> incidentEdgeOrder) {
/* 170 */     Preconditions.checkArgument((incidentEdgeOrder
/* 171 */         .type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder
/* 172 */         .type() == ElementOrder.Type.STABLE), "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
/*     */ 
/*     */ 
/*     */     
/* 176 */     GraphBuilder<N1> newBuilder = cast();
/* 177 */     newBuilder.incidentEdgeOrder = (ElementOrder<N>)Preconditions.checkNotNull(incidentEdgeOrder);
/* 178 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> MutableGraph<N1> build() {
/* 183 */     return new StandardMutableGraph<>(this);
/*     */   }
/*     */   
/*     */   GraphBuilder<N> copy() {
/* 187 */     GraphBuilder<N> newBuilder = new GraphBuilder(this.directed);
/* 188 */     newBuilder.allowsSelfLoops = this.allowsSelfLoops;
/* 189 */     newBuilder.nodeOrder = this.nodeOrder;
/* 190 */     newBuilder.expectedNodeCount = this.expectedNodeCount;
/* 191 */     newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
/* 192 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N> GraphBuilder<N1> cast() {
/* 197 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/GraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */