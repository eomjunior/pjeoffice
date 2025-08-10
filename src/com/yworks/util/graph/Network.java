package com.yworks.util.graph;

import java.util.Iterator;

public interface Network {
  Object createNode();
  
  Object createEdge(Object paramObject1, Object paramObject2);
  
  Object getSource(Object paramObject);
  
  Object getTarget(Object paramObject);
  
  Iterator nodes();
  
  Iterator edges();
  
  Integer nodesSize();
  
  Iterator inEdges(Object paramObject);
  
  Iterator outEdges(Object paramObject);
  
  Object firstInEdge(Object paramObject);
  
  Object firstOutEdge(Object paramObject);
  
  Object nextInEdge(Object paramObject);
  
  Object nextOutEdge(Object paramObject);
  
  Iterator edgesConnecting(Object paramObject1, Object paramObject2);
  
  Object opposite(Object paramObject1, Object paramObject2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/graph/Network.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */