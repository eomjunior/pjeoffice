package org.bouncycastle.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeAlgorithmSpec implements AlgorithmParameterSpec {
  private final List<String> algorithmNames;
  
  private final List<AlgorithmParameterSpec> parameterSpecs;
  
  public CompositeAlgorithmSpec(Builder paramBuilder) {
    this.algorithmNames = Collections.unmodifiableList(new ArrayList<String>(paramBuilder.algorithmNames));
    this.parameterSpecs = Collections.unmodifiableList(new ArrayList<AlgorithmParameterSpec>(paramBuilder.parameterSpecs));
  }
  
  public List<String> getAlgorithmNames() {
    return this.algorithmNames;
  }
  
  public List<AlgorithmParameterSpec> getParameterSpecs() {
    return this.parameterSpecs;
  }
  
  public static class Builder {
    private List<String> algorithmNames = new ArrayList<String>();
    
    private List<AlgorithmParameterSpec> parameterSpecs = new ArrayList<AlgorithmParameterSpec>();
    
    public Builder add(String param1String) {
      this.algorithmNames.add(param1String);
      this.parameterSpecs.add(null);
      return this;
    }
    
    public Builder add(String param1String, AlgorithmParameterSpec param1AlgorithmParameterSpec) {
      this.algorithmNames.add(param1String);
      this.parameterSpecs.add(param1AlgorithmParameterSpec);
      return this;
    }
    
    public CompositeAlgorithmSpec build() {
      if (this.algorithmNames.isEmpty())
        throw new IllegalStateException("cannot call build with no algorithm names added"); 
      return new CompositeAlgorithmSpec(this);
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/spec/CompositeAlgorithmSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */