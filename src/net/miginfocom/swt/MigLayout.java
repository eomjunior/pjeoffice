package net.miginfocom.swt;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;

public final class MigLayout extends Layout implements Externalizable {
  private final Map<Control, Object> scrConstrMap = new IdentityHashMap<Control, Object>(8);
  
  private Object layoutConstraints = "";
  
  private Object colConstraints = "";
  
  private Object rowConstraints = "";
  
  private transient ContainerWrapper cacheParentW = null;
  
  private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<ComponentWrapper, CC>(8);
  
  private transient LC lc = null;
  
  private transient AC colSpecs = null;
  
  private transient AC rowSpecs = null;
  
  private transient Grid grid = null;
  
  private transient Timer debugTimer = null;
  
  private transient long curDelay = -1L;
  
  private transient int lastModCount = PlatformDefaults.getModCount();
  
  private transient int lastHash = -1;
  
  private transient ArrayList<LayoutCallback> callbackList = null;
  
  public MigLayout() {
    this("", "", "");
  }
  
  public MigLayout(String paramString) {
    this(paramString, "", "");
  }
  
  public MigLayout(String paramString1, String paramString2) {
    this(paramString1, paramString2, "");
  }
  
  public MigLayout(String paramString1, String paramString2, String paramString3) {
    setLayoutConstraints(paramString1);
    setColumnConstraints(paramString2);
    setRowConstraints(paramString3);
  }
  
  public MigLayout(LC paramLC) {
    this(paramLC, (AC)null, (AC)null);
  }
  
  public MigLayout(LC paramLC, AC paramAC) {
    this(paramLC, paramAC, (AC)null);
  }
  
  public MigLayout(LC paramLC, AC paramAC1, AC paramAC2) {
    setLayoutConstraints(paramLC);
    setColumnConstraints(paramAC1);
    setRowConstraints(paramAC2);
  }
  
  public Object getLayoutConstraints() {
    return this.layoutConstraints;
  }
  
  public void setLayoutConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.lc = ConstraintParser.parseLayoutConstraint((String)paramObject);
    } else if (paramObject instanceof LC) {
      this.lc = (LC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.layoutConstraints = paramObject;
    this.grid = null;
  }
  
  public Object getColumnConstraints() {
    return this.colConstraints;
  }
  
  public void setColumnConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.colSpecs = ConstraintParser.parseColumnConstraints((String)paramObject);
    } else if (paramObject instanceof AC) {
      this.colSpecs = (AC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.colConstraints = paramObject;
    this.grid = null;
  }
  
  public Object getRowConstraints() {
    return this.rowConstraints;
  }
  
  public void setRowConstraints(Object paramObject) {
    if (paramObject == null || paramObject instanceof String) {
      paramObject = ConstraintParser.prepare((String)paramObject);
      this.rowSpecs = ConstraintParser.parseRowConstraints((String)paramObject);
    } else if (paramObject instanceof AC) {
      this.rowSpecs = (AC)paramObject;
    } else {
      throw new IllegalArgumentException("Illegal constraint type: " + paramObject.getClass().toString());
    } 
    this.rowConstraints = paramObject;
    this.grid = null;
  }
  
  public Map<Control, Object> getConstraintMap() {
    return new IdentityHashMap<Control, Object>(this.scrConstrMap);
  }
  
  public void setConstraintMap(Map<Control, Object> paramMap) {
    this.scrConstrMap.clear();
    this.ccMap.clear();
    for (Map.Entry<Control, Object> entry : paramMap.entrySet())
      setComponentConstraintsImpl((Control)entry.getKey(), entry.getValue(), true); 
  }
  
  private void setComponentConstraintsImpl(Control paramControl, Object paramObject, boolean paramBoolean) {
    if (!paramBoolean && !this.scrConstrMap.containsKey(paramControl))
      throw new IllegalArgumentException("Component must already be added to parent!"); 
    SwtComponentWrapper swtComponentWrapper = new SwtComponentWrapper(paramControl);
    if (paramObject == null || paramObject instanceof String) {
      String str = ConstraintParser.prepare((String)paramObject);
      this.scrConstrMap.put(paramControl, paramObject);
      this.ccMap.put(swtComponentWrapper, ConstraintParser.parseComponentConstraint(str));
    } else if (paramObject instanceof CC) {
      this.scrConstrMap.put(paramControl, paramObject);
      this.ccMap.put(swtComponentWrapper, (CC)paramObject);
    } else {
      throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + paramObject.getClass().toString());
    } 
    this.grid = null;
  }
  
  public boolean isManagingComponent(Control paramControl) {
    return this.scrConstrMap.containsKey(paramControl);
  }
  
  public void addLayoutCallback(LayoutCallback paramLayoutCallback) {
    if (paramLayoutCallback == null)
      throw new NullPointerException(); 
    if (this.callbackList == null)
      this.callbackList = new ArrayList<LayoutCallback>(1); 
    this.callbackList.add(paramLayoutCallback);
  }
  
  public void removeLayoutCallback(LayoutCallback paramLayoutCallback) {
    if (this.callbackList != null)
      this.callbackList.remove(paramLayoutCallback); 
  }
  
  private synchronized void setDebug(ComponentWrapper paramComponentWrapper, boolean paramBoolean) {
    if (paramBoolean && (this.debugTimer == null || this.curDelay != getDebugMillis())) {
      if (this.debugTimer != null)
        this.debugTimer.cancel(); 
      this.debugTimer = new Timer(true);
      this.curDelay = getDebugMillis();
      this.debugTimer.schedule(new MyDebugRepaintTask(this), this.curDelay, this.curDelay);
      ContainerWrapper containerWrapper = paramComponentWrapper.getParent();
      Composite composite = (containerWrapper != null) ? (Composite)containerWrapper.getComponent() : null;
      if (composite != null)
        composite.layout(); 
    } else if (!paramBoolean && this.debugTimer != null) {
      this.debugTimer.cancel();
      this.debugTimer = null;
    } 
  }
  
  private boolean getDebug() {
    return (this.debugTimer != null);
  }
  
  private int getDebugMillis() {
    int i = LayoutUtil.getGlobalDebugMillis();
    return (i > 0) ? i : this.lc.getDebugMillis();
  }
  
  private void checkCache(Composite paramComposite) {
    if (paramComposite == null)
      return; 
    checkConstrMap(paramComposite);
    ContainerWrapper containerWrapper = checkParent(paramComposite);
    int i = PlatformDefaults.getModCount();
    if (this.lastModCount != i) {
      this.grid = null;
      this.lastModCount = i;
    } 
    int j = paramComposite.getSize().hashCode();
    Iterator<ComponentWrapper> iterator = this.ccMap.keySet().iterator();
    while (iterator.hasNext())
      j += ((ComponentWrapper)iterator.next()).getLayoutHashCode(); 
    if (j != this.lastHash) {
      this.grid = null;
      this.lastHash = j;
    } 
    setDebug((ComponentWrapper)containerWrapper, (getDebugMillis() > 0));
    if (this.grid == null)
      this.grid = new Grid(containerWrapper, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList); 
  }
  
  private boolean checkConstrMap(Composite paramComposite) {
    Control[] arrayOfControl = paramComposite.getChildren();
    boolean bool = (arrayOfControl.length != this.scrConstrMap.size()) ? true : false;
    if (!bool)
      for (byte b = 0; b < arrayOfControl.length; b++) {
        Control control = arrayOfControl[b];
        if (this.scrConstrMap.get(control) != control.getLayoutData()) {
          bool = true;
          break;
        } 
      }  
    if (bool) {
      this.scrConstrMap.clear();
      for (byte b = 0; b < arrayOfControl.length; b++) {
        Control control = arrayOfControl[b];
        setComponentConstraintsImpl(control, control.getLayoutData(), true);
      } 
    } 
    return bool;
  }
  
  private ContainerWrapper checkParent(Composite paramComposite) {
    if (paramComposite == null)
      return null; 
    if (this.cacheParentW == null || this.cacheParentW.getComponent() != paramComposite)
      this.cacheParentW = new SwtContainerWrapper(paramComposite); 
    return this.cacheParentW;
  }
  
  public float getLayoutAlignmentX(Composite paramComposite) {
    return (this.lc != null && this.lc.getAlignX() != null) ? this.lc.getAlignX().getPixels(1.0F, checkParent(paramComposite), null) : 0.0F;
  }
  
  public float getLayoutAlignmentY(Composite paramComposite) {
    return (this.lc != null && this.lc.getAlignY() != null) ? this.lc.getAlignY().getPixels(1.0F, checkParent(paramComposite), null) : 0.0F;
  }
  
  protected Point computeSize(Composite paramComposite, int paramInt1, int paramInt2, boolean paramBoolean) {
    checkCache(paramComposite);
    int i = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getWidth() : null, 1);
    int j = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getHeight() : null, 1);
    return new Point(i, j);
  }
  
  protected void layout(Composite paramComposite, boolean paramBoolean) {
    checkCache(paramComposite);
    Rectangle rectangle = paramComposite.getClientArea();
    int[] arrayOfInt = { rectangle.x, rectangle.y, rectangle.width, rectangle.height };
    boolean bool = this.grid.layout(arrayOfInt, this.lc.getAlignX(), this.lc.getAlignY(), getDebug(), true);
    if (bool) {
      this.grid = null;
      checkCache(paramComposite);
      this.grid.layout(arrayOfInt, this.lc.getAlignX(), this.lc.getAlignY(), getDebug(), false);
    } 
  }
  
  protected boolean flushCache(Control paramControl) {
    this.grid = null;
    return true;
  }
  
  private Object readResolve() throws ObjectStreamException {
    return LayoutUtil.getSerializedObject(this);
  }
  
  public void readExternal(ObjectInput paramObjectInput) throws IOException, ClassNotFoundException {
    LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(paramObjectInput));
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput) throws IOException {
    if (getClass() == MigLayout.class)
      LayoutUtil.writeAsXML(paramObjectOutput, this); 
  }
  
  static {
    if (PlatformDefaults.getPlatform() == 1)
      PlatformDefaults.setDefaultDPI(Integer.valueOf(72)); 
  }
  
  private static class MyDebugRepaintTask extends TimerTask {
    private final WeakReference<MigLayout> layoutRef;
    
    private MyDebugRepaintTask(MigLayout param1MigLayout) {
      this.layoutRef = new WeakReference<MigLayout>(param1MigLayout);
    }
    
    public void run() {
      final MigLayout layout = this.layoutRef.get();
      if (migLayout != null && migLayout.grid != null)
        Display.getDefault().asyncExec(new Runnable() {
              public void run() {
                if (layout.grid != null)
                  layout.grid.paintDebug(); 
              }
            }); 
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/swt/MigLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */