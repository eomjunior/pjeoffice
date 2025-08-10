package net.miginfocom.layout;

public abstract class LayoutCallback {
  public UnitValue[] getPosition(ComponentWrapper paramComponentWrapper) {
    return null;
  }
  
  public BoundSize[] getSize(ComponentWrapper paramComponentWrapper) {
    return null;
  }
  
  public void correctBounds(ComponentWrapper paramComponentWrapper) {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/layout/LayoutCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */