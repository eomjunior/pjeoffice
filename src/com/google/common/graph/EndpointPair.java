/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Iterator;
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
/*     */ @Immutable(containerOf = {"N"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public abstract class EndpointPair<N>
/*     */   implements Iterable<N>
/*     */ {
/*     */   private final N nodeU;
/*     */   private final N nodeV;
/*     */   
/*     */   private EndpointPair(N nodeU, N nodeV) {
/*  48 */     this.nodeU = (N)Preconditions.checkNotNull(nodeU);
/*  49 */     this.nodeV = (N)Preconditions.checkNotNull(nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> ordered(N source, N target) {
/*  54 */     return new Ordered<>(source, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> unordered(N nodeU, N nodeV) {
/*  60 */     return new Unordered<>(nodeV, nodeU);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV) {
/*  65 */     return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV) {
/*  70 */     return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N nodeU() {
/*  92 */     return this.nodeU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N nodeV() {
/* 100 */     return this.nodeV;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N adjacentNode(N node) {
/* 110 */     if (node.equals(this.nodeU))
/* 111 */       return this.nodeV; 
/* 112 */     if (node.equals(this.nodeV)) {
/* 113 */       return this.nodeU;
/*     */     }
/* 115 */     throw new IllegalArgumentException("EndpointPair " + this + " does not contain node " + node);
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
/*     */   public final UnmodifiableIterator<N> iterator() {
/* 128 */     return Iterators.forArray(new Object[] { this.nodeU, this.nodeV });
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract N source();
/*     */ 
/*     */   
/*     */   public abstract N target();
/*     */ 
/*     */   
/*     */   public abstract boolean isOrdered();
/*     */ 
/*     */   
/*     */   public abstract boolean equals(@CheckForNull Object paramObject);
/*     */   
/*     */   public abstract int hashCode();
/*     */   
/*     */   private static final class Ordered<N>
/*     */     extends EndpointPair<N>
/*     */   {
/*     */     private Ordered(N source, N target) {
/* 149 */       super(source, target);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 154 */       return nodeU();
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 159 */       return nodeV();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 164 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 169 */       if (obj == this) {
/* 170 */         return true;
/*     */       }
/* 172 */       if (!(obj instanceof EndpointPair)) {
/* 173 */         return false;
/*     */       }
/*     */       
/* 176 */       EndpointPair<?> other = (EndpointPair)obj;
/* 177 */       if (isOrdered() != other.isOrdered()) {
/* 178 */         return false;
/*     */       }
/*     */       
/* 181 */       return (source().equals(other.source()) && target().equals(other.target()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 186 */       return Objects.hashCode(new Object[] { source(), target() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 191 */       return "<" + source() + " -> " + target() + ">";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Unordered<N> extends EndpointPair<N> {
/*     */     private Unordered(N nodeU, N nodeV) {
/* 197 */       super(nodeU, nodeV);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 202 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 207 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 212 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 217 */       if (obj == this) {
/* 218 */         return true;
/*     */       }
/* 220 */       if (!(obj instanceof EndpointPair)) {
/* 221 */         return false;
/*     */       }
/*     */       
/* 224 */       EndpointPair<?> other = (EndpointPair)obj;
/* 225 */       if (isOrdered() != other.isOrdered()) {
/* 226 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       if (nodeU().equals(other.nodeU()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 239 */         return nodeV().equals(other.nodeV());
/*     */       }
/* 241 */       return (nodeU().equals(other.nodeV()) && nodeV().equals(other.nodeU()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 246 */       return nodeU().hashCode() + nodeV().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 251 */       return "[" + nodeU() + ", " + nodeV() + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/EndpointPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */