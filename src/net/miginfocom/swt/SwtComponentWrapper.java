package net.miginfocom.swt;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

public class SwtComponentWrapper implements ComponentWrapper {
  private static Color DB_COMP_OUTLINE = new Color((Device)Display.getCurrent(), 0, 0, 200);
  
  private static boolean vp = false;
  
  private static boolean mz = false;
  
  private final Control c;
  
  private int compType = -1;
  
  public SwtComponentWrapper(Control paramControl) {
    this.c = paramControl;
  }
  
  public final int getBaseline(int paramInt1, int paramInt2) {
    return -1;
  }
  
  public final Object getComponent() {
    return this.c;
  }
  
  public final float getPixelUnitFactor(boolean paramBoolean) {
    GC gC;
    FontMetrics fontMetrics;
    float f;
    Float float_;
    switch (PlatformDefaults.getLogicalPixelBase()) {
      case 100:
        gC = new GC((Drawable)this.c);
        fontMetrics = gC.getFontMetrics();
        f = paramBoolean ? (fontMetrics.getAverageCharWidth() / 5.0F) : (fontMetrics.getHeight() / 13.0F);
        gC.dispose();
        return f;
      case 101:
        float_ = paramBoolean ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
        return (float_ != null) ? float_.floatValue() : ((paramBoolean ? getHorizontalScreenDPI() : getVerticalScreenDPI()) / PlatformDefaults.getDefaultDPI());
    } 
    return 1.0F;
  }
  
  public final int getX() {
    return (this.c.getLocation()).x;
  }
  
  public final int getY() {
    return (this.c.getLocation()).y;
  }
  
  public final int getWidth() {
    return (this.c.getSize()).x;
  }
  
  public final int getHeight() {
    return (this.c.getSize()).y;
  }
  
  public final int getScreenLocationX() {
    return (this.c.toDisplay(0, 0)).x;
  }
  
  public final int getScreenLocationY() {
    return (this.c.toDisplay(0, 0)).y;
  }
  
  public final int getMinimumHeight(int paramInt) {
    return mz ? 0 : (computeSize(false, paramInt)).y;
  }
  
  public final int getMinimumWidth(int paramInt) {
    return mz ? 0 : (computeSize(true, paramInt)).x;
  }
  
  public final int getPreferredHeight(int paramInt) {
    return (computeSize(false, paramInt)).y;
  }
  
  public final int getPreferredWidth(int paramInt) {
    return (computeSize(true, paramInt)).x;
  }
  
  public final int getMaximumHeight(int paramInt) {
    return 32767;
  }
  
  public final int getMaximumWidth(int paramInt) {
    return 32767;
  }
  
  private Point computeSize(boolean paramBoolean, int paramInt) {
    int i = paramBoolean ? -1 : paramInt;
    int j = !paramBoolean ? -1 : paramInt;
    if (i != -1 || j != -1) {
      int k = 0;
      if (this.c instanceof Scrollable) {
        Rectangle rectangle = ((Scrollable)this.c).computeTrim(0, 0, 0, 0);
        k = paramBoolean ? rectangle.width : rectangle.height;
      } else {
        k = this.c.getBorderWidth() << 1;
      } 
      if (i == -1) {
        j = Math.max(0, j - k);
      } else {
        i = Math.max(0, i - k);
      } 
    } 
    return this.c.computeSize(i, j);
  }
  
  public final ContainerWrapper getParent() {
    return new SwtContainerWrapper(this.c.getParent());
  }
  
  public int getHorizontalScreenDPI() {
    return (this.c.getDisplay().getDPI()).x;
  }
  
  public int getVerticalScreenDPI() {
    return (this.c.getDisplay().getDPI()).y;
  }
  
  public final int getScreenWidth() {
    return (this.c.getDisplay().getBounds()).width;
  }
  
  public final int getScreenHeight() {
    return (this.c.getDisplay().getBounds()).height;
  }
  
  public final boolean hasBaseline() {
    return false;
  }
  
  public final String getLinkId() {
    return null;
  }
  
  public final void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.c.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean isVisible() {
    return this.c.getVisible();
  }
  
  public final int[] getVisualPadding() {
    return null;
  }
  
  public static boolean isUseVisualPadding() {
    return vp;
  }
  
  public static void setUseVisualPadding(boolean paramBoolean) {
    vp = paramBoolean;
  }
  
  public static boolean isMinimumSizeZero() {
    return mz;
  }
  
  public static void setMinimumSizeZero(boolean paramBoolean) {
    mz = paramBoolean;
  }
  
  public int getLayoutHashCode() {
    if (this.c.isDisposed())
      return -1; 
    Point point1 = this.c.getSize();
    Point point2 = this.c.computeSize(-1, -1, false);
    int i = point2.x + (point2.y << 12) + (point1.x << 22) + (point1.y << 16);
    if (this.c.isVisible())
      i |= 0x2000000; 
    String str = getLinkId();
    if (str != null)
      i += str.hashCode(); 
    return i;
  }
  
  public final void paintDebugOutline() {
    if (this.c.isDisposed())
      return; 
    GC gC = new GC((Drawable)this.c);
    gC.setLineJoin(1);
    gC.setLineCap(3);
    gC.setLineStyle(3);
    gC.setForeground(DB_COMP_OUTLINE);
    gC.drawRectangle(0, 0, getWidth() - 1, getHeight() - 1);
    gC.dispose();
  }
  
  public int getComponetType(boolean paramBoolean) {
    if (this.compType == -1)
      this.compType = checkType(); 
    return this.compType;
  }
  
  private int checkType() {
    int i = this.c.getStyle();
    return (this.c instanceof org.eclipse.swt.widgets.Text || this.c instanceof org.eclipse.swt.custom.StyledText) ? (((i & 0x2) > 0) ? 4 : 3) : ((this.c instanceof org.eclipse.swt.widgets.Label) ? (((i & 0x2) > 0) ? 18 : 2) : ((this.c instanceof org.eclipse.swt.widgets.Button) ? (((i & 0x20) > 0 || (i & 0x10) > 0) ? 16 : 5) : ((this.c instanceof org.eclipse.swt.widgets.Canvas) ? 10 : ((this.c instanceof org.eclipse.swt.widgets.List) ? 6 : ((this.c instanceof org.eclipse.swt.widgets.Table) ? 7 : ((this.c instanceof org.eclipse.swt.widgets.Spinner) ? 13 : ((this.c instanceof org.eclipse.swt.widgets.ProgressBar) ? 14 : ((this.c instanceof org.eclipse.swt.widgets.Slider) ? 12 : ((this.c instanceof org.eclipse.swt.widgets.Composite) ? 1 : 0)))))))));
  }
  
  public final int hashCode() {
    return this.c.hashCode();
  }
  
  public final boolean equals(Object paramObject) {
    return (paramObject == null || !(paramObject instanceof ComponentWrapper)) ? false : getComponent().equals(((ComponentWrapper)paramObject).getComponent());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/swt/SwtComponentWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */