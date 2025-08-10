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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class NetworkBuilder<N, E>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   boolean allowsParallelEdges = false;
/*  79 */   ElementOrder<? super E> edgeOrder = ElementOrder.insertion();
/*  80 */   Optional<Integer> expectedEdgeCount = Optional.absent();
/*     */ 
/*     */   
/*     */   private NetworkBuilder(boolean directed) {
/*  84 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> directed() {
/*  89 */     return new NetworkBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> undirected() {
/*  94 */     return new NetworkBuilder<>(false);
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
/*     */   public static <N, E> NetworkBuilder<N, E> from(Network<N, E> network) {
/* 106 */     return (new NetworkBuilder<>(network.isDirected()))
/* 107 */       .allowsParallelEdges(network.allowsParallelEdges())
/* 108 */       .allowsSelfLoops(network.allowsSelfLoops())
/* 109 */       .nodeOrder(network.nodeOrder())
/* 110 */       .edgeOrder(network.edgeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> ImmutableNetwork.Builder<N1, E1> immutable() {
/* 121 */     NetworkBuilder<N1, E1> castBuilder = cast();
/* 122 */     return new ImmutableNetwork.Builder<>(castBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public NetworkBuilder<N, E> allowsParallelEdges(boolean allowsParallelEdges) {
/* 133 */     this.allowsParallelEdges = allowsParallelEdges;
/* 134 */     return this;
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
/*     */   public NetworkBuilder<N, E> allowsSelfLoops(boolean allowsSelfLoops) {
/* 146 */     this.allowsSelfLoops = allowsSelfLoops;
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public NetworkBuilder<N, E> expectedNodeCount(int expectedNodeCount) {
/* 157 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public NetworkBuilder<N, E> expectedEdgeCount(int expectedEdgeCount) {
/* 168 */     this.expectedEdgeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedEdgeCount)));
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 178 */     NetworkBuilder<N1, E> newBuilder = cast();
/* 179 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 180 */     return newBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(ElementOrder<E1> edgeOrder) {
/* 189 */     NetworkBuilder<N, E1> newBuilder = cast();
/* 190 */     newBuilder.edgeOrder = (ElementOrder<? super E>)Preconditions.checkNotNull(edgeOrder);
/* 191 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
/* 196 */     return new StandardMutableNetwork<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
/* 201 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/NetworkBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */