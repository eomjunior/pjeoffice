/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  57 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  60 */           return AbstractNetwork.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  65 */           if (AbstractNetwork.this.allowsParallelEdges()) {
/*  66 */             return super.edges();
/*     */           }
/*     */ 
/*     */           
/*  70 */           return new AbstractSet<EndpointPair<N>>()
/*     */             {
/*     */               public Iterator<EndpointPair<N>> iterator() {
/*  73 */                 return Iterators.transform(AbstractNetwork.this
/*  74 */                     .edges().iterator(), edge -> AbstractNetwork.this.incidentNodes(edge));
/*     */               }
/*     */ 
/*     */               
/*     */               public int size() {
/*  79 */                 return AbstractNetwork.this.edges().size();
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public boolean contains(@CheckForNull Object obj) {
/*  88 */                 if (!(obj instanceof EndpointPair)) {
/*  89 */                   return false;
/*     */                 }
/*  91 */                 EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  92 */                 return (AbstractNetwork.null.this.isOrderingCompatible(endpointPair) && AbstractNetwork.null.this
/*  93 */                   .nodes().contains(endpointPair.nodeU()) && AbstractNetwork.null.this
/*  94 */                   .successors((N)endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/* 101 */           return AbstractNetwork.this.nodeOrder();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public ElementOrder<N> incidentEdgeOrder() {
/* 108 */           return ElementOrder.unordered();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/* 113 */           return AbstractNetwork.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/* 118 */           return AbstractNetwork.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/* 123 */           return AbstractNetwork.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/* 128 */           return AbstractNetwork.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/* 133 */           return AbstractNetwork.this.successors(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 142 */     if (isDirected()) {
/* 143 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 145 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 151 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 156 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(E edge) {
/* 161 */     EndpointPair<N> endpointPair = incidentNodes(edge);
/*     */     
/* 163 */     Sets.SetView setView = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 164 */     return (Set<E>)Sets.difference((Set)setView, (Set)ImmutableSet.of(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 169 */     Set<E> outEdgesU = outEdges(nodeU);
/* 170 */     Set<E> inEdgesV = inEdges(nodeV);
/* 171 */     return (outEdgesU.size() <= inEdgesV.size()) ? 
/* 172 */       Collections.<E>unmodifiableSet(Sets.filter(outEdgesU, connectedPredicate(nodeU, nodeV))) : 
/* 173 */       Collections.<E>unmodifiableSet(Sets.filter(inEdgesV, connectedPredicate(nodeV, nodeU)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 178 */     validateEndpoints(endpoints);
/* 179 */     return edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
/* 183 */     return new Predicate<E>()
/*     */       {
/*     */         public boolean apply(E edge) {
/* 186 */           return AbstractNetwork.this.incidentNodes(edge).adjacentNode((N)nodePresent).equals(nodeToCheck);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 193 */     return Optional.ofNullable(edgeConnectingOrNull(nodeU, nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 198 */     validateEndpoints(endpoints);
/* 199 */     return edgeConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 205 */     Set<E> edgesConnecting = edgesConnecting(nodeU, nodeV);
/* 206 */     switch (edgesConnecting.size()) {
/*     */       case 0:
/* 208 */         return null;
/*     */       case 1:
/* 210 */         return edgesConnecting.iterator().next();
/*     */     } 
/* 212 */     throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", new Object[] { nodeU, nodeV }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 219 */     validateEndpoints(endpoints);
/* 220 */     return edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 225 */     Preconditions.checkNotNull(nodeU);
/* 226 */     Preconditions.checkNotNull(nodeV);
/* 227 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 232 */     Preconditions.checkNotNull(endpoints);
/* 233 */     if (!isOrderingCompatible(endpoints)) {
/* 234 */       return false;
/*     */     }
/* 236 */     return hasEdgeConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 244 */     Preconditions.checkNotNull(endpoints);
/* 245 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: endpoints' ordering is not compatible with directionality of the graph");
/*     */   }
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 249 */     return (endpoints.isOrdered() == isDirected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(@CheckForNull Object obj) {
/* 254 */     if (obj == this) {
/* 255 */       return true;
/*     */     }
/* 257 */     if (!(obj instanceof Network)) {
/* 258 */       return false;
/*     */     }
/* 260 */     Network<?, ?> other = (Network<?, ?>)obj;
/*     */     
/* 262 */     return (isDirected() == other.isDirected() && 
/* 263 */       nodes().equals(other.nodes()) && 
/* 264 */       edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 269 */     return edgeIncidentNodesMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 275 */     return "isDirected: " + 
/* 276 */       isDirected() + ", allowsParallelEdges: " + 
/*     */       
/* 278 */       allowsParallelEdges() + ", allowsSelfLoops: " + 
/*     */       
/* 280 */       allowsSelfLoops() + ", nodes: " + 
/*     */       
/* 282 */       nodes() + ", edges: " + 
/*     */       
/* 284 */       edgeIncidentNodesMap(this);
/*     */   }
/*     */   
/*     */   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(Network<N, E> network) {
/* 288 */     Objects.requireNonNull(network); return Maps.asMap(network.edges(), network::incidentNodes);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */