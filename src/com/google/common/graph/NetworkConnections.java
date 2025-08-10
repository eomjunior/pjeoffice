package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
interface NetworkConnections<N, E> {
  Set<N> adjacentNodes();
  
  Set<N> predecessors();
  
  Set<N> successors();
  
  Set<E> incidentEdges();
  
  Set<E> inEdges();
  
  Set<E> outEdges();
  
  Set<E> edgesConnecting(N paramN);
  
  N adjacentNode(E paramE);
  
  @CheckForNull
  @CanIgnoreReturnValue
  N removeInEdge(E paramE, boolean paramBoolean);
  
  @CanIgnoreReturnValue
  N removeOutEdge(E paramE);
  
  void addInEdge(E paramE, N paramN, boolean paramBoolean);
  
  void addOutEdge(E paramE, N paramN);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/NetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */