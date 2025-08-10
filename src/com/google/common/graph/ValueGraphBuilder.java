/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class ValueGraphBuilder<N, V>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private ValueGraphBuilder(boolean directed) {
/*  81 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> directed() {
/*  86 */     return new ValueGraphBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> undirected() {
/*  91 */     return new ValueGraphBuilder<>(false);
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
/*     */   public static <N, V> ValueGraphBuilder<N, V> from(ValueGraph<N, V> graph) {
/* 103 */     return (new ValueGraphBuilder<>(graph.isDirected()))
/* 104 */       .allowsSelfLoops(graph.allowsSelfLoops())
/* 105 */       .nodeOrder(graph.nodeOrder())
/* 106 */       .incidentEdgeOrder(graph.incidentEdgeOrder());
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
/*     */   public <N1 extends N, V1 extends V> ImmutableValueGraph.Builder<N1, V1> immutable() {
/* 121 */     ValueGraphBuilder<N1, V1> castBuilder = cast();
/* 122 */     return new ImmutableValueGraph.Builder<>(castBuilder);
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
/*     */   public ValueGraphBuilder<N, V> allowsSelfLoops(boolean allowsSelfLoops) {
/* 134 */     this.allowsSelfLoops = allowsSelfLoops;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ValueGraphBuilder<N, V> expectedNodeCount(int expectedNodeCount) {
/* 145 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 155 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 156 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 157 */     return newBuilder;
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
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> incidentEdgeOrder(ElementOrder<N1> incidentEdgeOrder) {
/* 175 */     Preconditions.checkArgument((incidentEdgeOrder
/* 176 */         .type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder
/* 177 */         .type() == ElementOrder.Type.STABLE), "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", incidentEdgeOrder);
/*     */ 
/*     */ 
/*     */     
/* 181 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 182 */     newBuilder.incidentEdgeOrder = (ElementOrder<N>)Preconditions.checkNotNull(incidentEdgeOrder);
/* 183 */     return newBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
/* 190 */     return new StandardMutableValueGraph<>(this);
/*     */   }
/*     */   
/*     */   ValueGraphBuilder<N, V> copy() {
/* 194 */     ValueGraphBuilder<N, V> newBuilder = new ValueGraphBuilder(this.directed);
/* 195 */     newBuilder.allowsSelfLoops = this.allowsSelfLoops;
/* 196 */     newBuilder.nodeOrder = this.nodeOrder;
/* 197 */     newBuilder.expectedNodeCount = this.expectedNodeCount;
/* 198 */     newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
/* 199 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
/* 204 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ValueGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */