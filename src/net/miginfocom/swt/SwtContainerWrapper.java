package net.miginfocom.swt;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public final class SwtContainerWrapper extends SwtComponentWrapper implements ContainerWrapper {
  public SwtContainerWrapper(Composite paramComposite) {
    super((Control)paramComposite);
  }
  
  public ComponentWrapper[] getComponents() {
    Composite composite = (Composite)getComponent();
    Control[] arrayOfControl = composite.getChildren();
    ComponentWrapper[] arrayOfComponentWrapper = new ComponentWrapper[arrayOfControl.length];
    for (byte b = 0; b < arrayOfComponentWrapper.length; b++)
      arrayOfComponentWrapper[b] = new SwtComponentWrapper(arrayOfControl[b]); 
    return arrayOfComponentWrapper;
  }
  
  public int getComponentCount() {
    return (((Composite)getComponent()).getChildren()).length;
  }
  
  public Object getLayout() {
    return ((Composite)getComponent()).getLayout();
  }
  
  public final boolean isLeftToRight() {
    return ((((Composite)getComponent()).getStyle() & 0x2000000) > 0);
  }
  
  public final void paintDebugCell(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public int getComponetType(boolean paramBoolean) {
    return 1;
  }
  
  public int getLayoutHashCode() {
    int i = super.getLayoutHashCode();
    if (isLeftToRight())
      i |= 0x4000000; 
    return i;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/swt/SwtContainerWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */