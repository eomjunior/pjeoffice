package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;

public interface AppendableResourceCollection extends ResourceCollection {
  void add(ResourceCollection paramResourceCollection) throws BuildException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/AppendableResourceCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */