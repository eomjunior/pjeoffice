package net.miginfocom.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SwtDemo {
  public static final int SELECTED_INDEX = 0;
  
  private static final String[][] panels = new String[][] { 
      { "Welcome", "\n\n         \"MigLayout makes complex layouts easy and normal layouts one-liners.\"" }, { "Quick Start", "This is an example of how to build a common dialog type. Note that there are no special components, nested panels or absolute references to cell positions. If you look at the source code you will see that the layout code is very simple to understand." }, { "Plain", "A simple example on how simple it is to create normal forms. No builders needed since the whole layout manager works like a builder." }, { "Alignments", "Shows how the alignment of components are specified. At the top/left is the alignment for the column/row. The components have no alignments specified.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6." }, { "Cell Alignments", "Shows how components are aligned when both column/row alignments and component constraints are specified. At the top/left are the alignment for the column/row and the text on the buttons is the component constraint that will override the column/row alignment if it is an alignment.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6." }, { "Basic Sizes", "A simple example that shows how to use the column or row min/preferred/max size to set the sizes of the contained components and also an example that shows how to do this directly in the component constraints." }, { "Growing", "A simple example that shows how to use the growx and growy constraint to set the sizes and how they should grow to fit the available size. Both the column/row and the component grow/shrink constraints can be set, but the components will always be confined to the space given by its column/row." }, { "Grow Shrink", "Demonstrates the very flexible grow and shrink constraints that can be set on a component.\nComponents can be divided into grow/shrink groups and also have grow/shrink weight within each of those groups.\n\nBy default components shrink to their inherent (or specified) minimum size, but they don't grow." }, { "Span", "This example shows the powerful spanning and splitting that can be specified in the component constraints. With spanning any number of cells can be merged with the additional option to split that space for more than one component. This makes layouts very flexible and reduces the number of times you will need nested panels to very few." }, { "Flow Direction", "Shows the different flow directions. Flow direction for the layout specifies if the next cell will be in the x or y dimension. Note that it can be a different flow direction in the slit cell (the middle cell is slit in two). Wrap is set to 3 for all panels." }, 
      { "Grouping", "Sizes for both components and columns/rows can be grouped so they get the same size. For instance buttons in a button bar can be given a size-group so that they will all get the same minimum and preferred size (the largest within the group). Size-groups can be set for the width, height or both." }, { "Units", "Demonstrates the basic units that are understood by MigLayout. These units can be extended by the user by adding one or more UnitConverter(s)." }, { "Component Sizes", "Minimum, preferred and maximum component sizes can be overridden in the component constraints using any unit type. The format to do this is short and simple to understand. You simply specify the min, preferred and max sizes with a colon between.\n\nAbove are some examples of this. An exclamation mark means that the value will be used for all sizes." }, { "Bound Sizes", "Shows how to create columns that are stable between tabs using minimum sizes." }, { "Cell Position", "Even though MigLayout has automatic grid flow you can still specify the cell position explicitly. You can even combine absolute (x, y) and flow (skip, wrap and newline) constraints to build your layout." }, { "Orientation", "MigLayout supports not only right-to-left orientation, but also bottom-to-top. You can even set the flow direction so that the flow is vertical instead of horizontal. It will automatically pick up if right-to-left is to be used depending on the ComponentWrapper, but it can also be manually set for every layout." }, { "Absolute Position", "Demonstrates the option to place any number of components using absolute coordinates. This can be just the position (if min/preferred size) using \"x y p p\" format orthe bounds using the \"x1 y1 x2 y2\" format. Any unit can be used and percent is relative to the parent.\nAbsolute components will not disturb the flow or occupy cells in the grid. Absolute positioned components will be taken into account when calculating the container's preferred size." }, { "Component Links", "Components can be linked to any side of any other component. It can be a forward, backward or cyclic link references, as long as it is stable and won't continue to change value over many iterations.Links are referencing the ID of another component. The ID can be overridden by the component's constrains or is provided by the ComponentWrapper. For instance it will use the component's 'name' on Swing.\nSince the links can be combined with any expression (such as 'butt1.x+10' or 'max(button.x, 200)' the links are very customizable." }, { "Docking", "Docking components can be added around the grid. The docked component will get the whole width/height on the docked side by default, however this can be overridden. When all docked components are laid out, whatever space is left will be available for the normal grid laid out components. Docked components does not in any way affect the flow in the grid.\n\nSince the docking runs in the same code path as the normal layout code the same properties can be specified for the docking components. You can for instance set the sizes and alignment or link other components to their docked component's bounds." }, { "Button Bars", "Button order is very customizable and are by default different on the supported platforms. E.g. Gaps, button order and minimum button size are properties that are 'per platform'. MigLayout picks up the current platform automatically and adjusts the button order and minimum button size accordingly, all without using a button builder or any other special code construct." }, 
      { "Debug", "Demonstrates the non-intrusive way to get visual debugging aid. There is no need to use a special DebugPanel or anything that will need code changes. The user can simply turn on debug on the layout manager by using the ìdebugî constraint and it will continuously repaint the panel with debug information on top. This means you don't have to change your code to debug!" }, { "Layout Showdown", "This is an implementation of the Layout Showdown posted on java.net by John O'Conner. The first tab is a pure implemenetation of the showdown that follows all the rules. The second tab is a slightly fixed version that follows some improved layout guidelines.The source code is for bothe the first and for the fixed version. Note the simplification of the code for the fixed version. Writing better layouts with MiG Layout is reasier that writing bad.\n\nReference: http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html" }, { "API Constraints1", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details." }, { "API Constraints2", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details." } };
  
  private static int DOUBLE_BUFFER = 0;
  
  private static int benchRuns = 0;
  
  private static long startupMillis = 0L;
  
  private static long timeToShowMillis = 0L;
  
  private static long benchRunTime = 0L;
  
  private static String benchOutFileName = null;
  
  private static boolean append = false;
  
  private static long lastRunTimeStart = 0L;
  
  private static StringBuffer runTimeSB = null;
  
  private static Display display = null;
  
  final List pickerList;
  
  final Composite layoutDisplayPanel;
  
  final StyledText descrTextArea;
  
  private static Control[] comps = null;
  
  private static Control[] tabs = null;
  
  public static void main(String[] paramArrayOfString) {
    startupMillis = System.currentTimeMillis();
    if (paramArrayOfString.length > 0)
      for (byte b = 0; b < paramArrayOfString.length; b++) {
        String str = paramArrayOfString[b].trim();
        if (str.startsWith("-bench")) {
          benchRuns = 10;
          try {
            if (str.length() > 6)
              benchRuns = Integer.parseInt(str.substring(6)); 
          } catch (Exception exception) {}
        } else if (str.startsWith("-bout")) {
          benchOutFileName = str.substring(5);
        } else if (str.startsWith("-append")) {
          append = true;
        } else if (str.startsWith("-verbose")) {
          runTimeSB = new StringBuffer(256);
        } else {
          System.out.println("Usage: [-bench[#_of_runs]] [-bout[benchmark_results_filename]] [-append]\n -bench Run demo as benchmark. Run count can be appended. 10 is default.\n -bout  Benchmark results output filename.\n -append Appends the result to the \"-bout\" file.\n -verbose Print the times of every run.\n\nExamples:\n java -jar swtdemoapp.jar -bench -boutC:/bench.txt -append\n java -jar swtdemoapp.jar -bench20\nNOTE! swt-win32-3232.dll must be in the current directory!");
          System.exit(0);
        } 
      }  
    if (benchRuns == 0)
      LayoutUtil.setDesignTime(null, true); 
    new SwtDemo();
  }
  
  public SwtDemo() {
    display = new Display();
    Shell shell = new Shell();
    shell.setLayout((Layout)new MigLayout("wrap", "[]u[grow,fill]", "[grow,fill][pref!]"));
    shell.setText("MigLayout SWT Demo v2.5 - Mig Layout v" + LayoutUtil.getVersion());
    TabFolder tabFolder1 = new TabFolder((Composite)shell, DOUBLE_BUFFER);
    tabFolder1.setLayoutData("spany,grow");
    this.pickerList = new List((Composite)tabFolder1, 0x4 | DOUBLE_BUFFER);
    this.pickerList.setBackground(tabFolder1.getBackground());
    deriveFont((Control)this.pickerList, 1, -1);
    TabItem tabItem = new TabItem(tabFolder1, DOUBLE_BUFFER);
    tabItem.setControl((Control)this.pickerList);
    tabItem.setText("Example Browser");
    for (byte b = 0; b < panels.length; b++)
      this.pickerList.add(panels[b][0]); 
    this.layoutDisplayPanel = new Composite((Composite)shell, DOUBLE_BUFFER);
    this.layoutDisplayPanel.setLayout((Layout)new MigLayout("fill, insets 0"));
    TabFolder tabFolder2 = new TabFolder((Composite)shell, DOUBLE_BUFFER);
    tabFolder2.setLayoutData("growx,hmin 120,w 500:500");
    this.descrTextArea = createTextArea(tabFolder2, "", "", 66);
    this.descrTextArea.setBackground(tabFolder2.getBackground());
    tabItem = new TabItem(tabFolder2, DOUBLE_BUFFER);
    tabItem.setControl((Control)this.descrTextArea);
    tabItem.setText("Description");
    this.pickerList.addSelectionListener((SelectionListener)new SelectionAdapter() {
          public void widgetSelected(SelectionEvent param1SelectionEvent) {
            SwtDemo.this.dispatchSelection();
          }
        });
    shell.setSize(900, 650);
    shell.open();
    shell.layout();
    if (benchRuns > 0) {
      doBenchmark();
    } else {
      this.pickerList.select(0);
      dispatchSelection();
      display.addFilter(1, new Listener() {
            public void handleEvent(Event param1Event) {
              if (param1Event.character == 'b') {
                SwtDemo.startupMillis = System.currentTimeMillis();
                SwtDemo.timeToShowMillis = System.currentTimeMillis() - SwtDemo.startupMillis;
                SwtDemo.benchRuns = 1;
                SwtDemo.this.doBenchmark();
              } 
            }
          });
    } 
    System.out.println(Display.getCurrent().getDPI());
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep(); 
    } 
    display.dispose();
  }
  
  private void doBenchmark() {
    final int pCnt = this.pickerList.getItemCount();
    Thread thread = new Thread() {
        public void run() {
          for (byte b = 0; b < SwtDemo.benchRuns; b++) {
            SwtDemo.lastRunTimeStart = System.currentTimeMillis();
            byte b1 = b;
            for (byte b2 = 0; b2 < pCnt; b2++) {
              final byte ii = b2;
              try {
                SwtDemo.display.syncExec(new Runnable() {
                      public void run() {
                        SwtDemo.this.pickerList.setSelection(ii);
                        SwtDemo.this.dispatchSelection();
                      }
                    });
              } catch (Exception exception) {
                exception.printStackTrace();
              } 
              SwtDemo.display.syncExec(new Runnable() {
                    public void run() {
                      SwtDemo.comps = SwtDemo.this.layoutDisplayPanel.getChildren();
                    }
                  });
              for (byte b4 = 0; b4 < SwtDemo.comps.length; b4++) {
                if (SwtDemo.comps[b4] instanceof TabFolder) {
                  final TabFolder tp = (TabFolder)SwtDemo.comps[b4];
                  SwtDemo.display.syncExec(new Runnable() {
                        public void run() {
                          SwtDemo.tabs = tp.getTabList();
                        }
                      });
                  for (byte b5 = 0; b5 < SwtDemo.tabs.length; b5++) {
                    final byte kk = b5;
                    try {
                      SwtDemo.display.syncExec(new Runnable() {
                            public void run() {
                              tp.setSelection(kk);
                              if (SwtDemo.timeToShowMillis == 0L)
                                SwtDemo.timeToShowMillis = System.currentTimeMillis() - SwtDemo.startupMillis; 
                            }
                          });
                    } catch (Exception exception) {
                      exception.printStackTrace();
                    } 
                  } 
                } 
              } 
            } 
            if (SwtDemo.runTimeSB != null) {
              SwtDemo.runTimeSB.append("Run ").append(b1).append(": ");
              SwtDemo.runTimeSB.append(System.currentTimeMillis() - SwtDemo.lastRunTimeStart).append(" millis.\n");
            } 
          } 
          SwtDemo.benchRunTime = System.currentTimeMillis() - SwtDemo.startupMillis - SwtDemo.timeToShowMillis;
          final String message = "Java Version:       " + System.getProperty("java.version") + "\n" + "Time to Show:       " + SwtDemo.timeToShowMillis + " millis.\n" + ((SwtDemo.runTimeSB != null) ? SwtDemo.runTimeSB.toString() : "") + "Benchmark Run Time: " + SwtDemo.benchRunTime + " millis.\n" + "Average Run Time:   " + (SwtDemo.benchRunTime / SwtDemo.benchRuns) + " millis (" + SwtDemo.benchRuns + " runs).\n\n";
          SwtDemo.display.syncExec(new Runnable() {
                public void run() {
                  if (SwtDemo.benchOutFileName == null) {
                    MessageBox messageBox = new MessageBox(SwtDemo.display.getActiveShell(), 34);
                    messageBox.setText("Results");
                    messageBox.setMessage(message);
                    messageBox.open();
                  } else {
                    FileWriter fileWriter = null;
                    try {
                      fileWriter = new FileWriter(SwtDemo.benchOutFileName, SwtDemo.append);
                      fileWriter.write(message);
                    } catch (IOException iOException) {
                      iOException.printStackTrace();
                    } finally {
                      if (fileWriter != null)
                        try {
                          fileWriter.close();
                        } catch (IOException iOException) {} 
                    } 
                  } 
                }
              });
          System.out.println(str);
          if (SwtDemo.benchOutFileName != null)
            System.exit(0); 
        }
      };
    thread.start();
  }
  
  private void dispatchSelection() {
    int i = this.pickerList.getSelectionIndex();
    if (i == -1)
      return; 
    String str = "create" + panels[i][0].replace(' ', '_');
    Control[] arrayOfControl = this.layoutDisplayPanel.getChildren();
    for (byte b = 0; b < arrayOfControl.length; b++)
      arrayOfControl[b].dispose(); 
    try {
      Control control = (Control)SwtDemo.class.getMethod(str, new Class[] { Composite.class }).invoke(this, new Object[] { this.layoutDisplayPanel });
      control.setLayoutData("grow, wmin 500");
      this.descrTextArea.setText(panels[i][1]);
      this.layoutDisplayPanel.layout();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public Control createTest(Composite paramComposite) {
    Composite composite = new Composite(paramComposite, 0);
    composite.setLayout((Layout)new MigLayout("debug", "[right][grow]", ""));
    Button button = new Button(composite, 8);
    button.setText("New");
    button.setLayoutData("span 2, align left, split, sgx button");
    button = new Button(composite, 8);
    button.setText("Edit");
    button.setLayoutData("sgx button");
    button = new Button(composite, 8);
    button.setText("Cancel");
    button.setLayoutData("sgx button");
    button = new Button(composite, 8);
    button.setText("Save");
    button.setLayoutData("sgx button, wrap");
    (new Label(composite, 0)).setText("Name");
    Text text = new Text(composite, 2048);
    text.setLayoutData("sgy control, pushx, growx, wrap");
    (new Label(composite, 0)).setText("Sex");
    Combo combo = new Combo(composite, 4);
    combo.setLayoutData("sgy control, w 50!, wrap");
    combo.setItems(new String[] { "M", "F", "-" });
    return (Control)composite;
  }
  
  public Control createWelcome(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    TabItem tabItem = createTabPanel(tabFolder, "Welcome", (Layout)new MigLayout());
    MigLayout migLayout = new MigLayout("ins 20, fill");
    Composite composite = createPanel(tabFolder, (Layout)migLayout);
    tabItem.setControl((Control)composite);
    String str = "MigLayout's main purpose is to make layouts for SWT and Swing, and possibly other frameworks, much more powerful and a lot easier to create, especially for manual coding.\n\nThe motto is: \"MigLayout makes complex layouts easy and normal layouts one-liners.\"\n\nThe layout engine is very flexible and advanced, something that is needed to make it simple to use yet handle almost all layout use-cases.\n\nMigLayout can handle all layouts that the commonly used Swing Layout Managers can handle and this with a lot of extra features. It also incorporates most, if not all, of the open source alternatives FormLayout's and TableLayout's functionality.\n\n\nThanks to Karsten Lentzsch from JGoodies.com for allowing the reuse of the main demo application layout and for his inspiring talks that led to this layout Manager.\n\n\nMikael Grev\nMiG InfoCom AB\nmiglayout@miginfocom.com";
    StyledText styledText = createTextArea(composite, str, "w 500:500, ay top, grow, push", 0);
    styledText.setBackground(composite.getBackground());
    styledText.setBackgroundMode(0);
    return (Control)tabFolder;
  }
  
  public Composite createAPI_Constraints1(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    LC lC = (new LC()).fill().wrap();
    AC aC1 = (new AC()).align("right", new int[] { 0 }).fill(new int[] { 1, 3 }).grow(100.0F, new int[] { 1, 3 }).align("right", new int[] { 2 }).gap("15", new int[] { 1 });
    AC aC2 = (new AC()).align("top", new int[] { 7 }).gap("15!", new int[] { 6 }).grow(100.0F, new int[] { 8 });
    TabItem tabItem = createTabPanel(tabFolder, "Layout Showdown (improved)", (Layout)new MigLayout(lC, aC1, aC2));
    createList(tabItem, "Mouse, Mickey", (new CC()).dockWest().minWidth("150").gapX(null, "10"));
    createLabel(tabItem, "Last Name", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "First Name", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createLabel(tabItem, "Phone", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Email", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Address 1", "");
    createTextField(tabItem, "", (new CC()).spanX().growX());
    createLabel(tabItem, "Address 2", "");
    createTextField(tabItem, "", (new CC()).spanX().growX());
    createLabel(tabItem, "City", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createLabel(tabItem, "State", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Postal Code", "");
    createTextField(tabItem, "", (new CC()).spanX(2).growX(0.0F));
    createLabel(tabItem, "Country", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createButton(tabItem, "New", (new CC()).spanX(5).split(5).tag("other"));
    createButton(tabItem, "Delete", (new CC()).tag("other"));
    createButton(tabItem, "Edit", (new CC()).tag("other"));
    createButton(tabItem, "Save", (new CC()).tag("other"));
    createButton(tabItem, "Cancel", (new CC()).tag("cancel"));
    return (Composite)tabFolder;
  }
  
  public Composite createAPI_Constraints2(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    LC lC = (new LC()).fill().wrap();
    AC aC1 = (new AC()).align("right", new int[] { 0 }).fill(new int[] { 1, 3 }).grow(100.0F, new int[] { 1, 3 }).align("right", new int[] { 2 }).gap("15", new int[] { 1 });
    AC aC2 = (new AC()).index(6).gap("15!").align("top").grow(100.0F, new int[] { 8 });
    TabItem tabItem = createTabPanel(tabFolder, "Layout Showdown (improved)", (Layout)new MigLayout(lC, aC1, aC2));
    createLabel(tabItem, "Last Name", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "First Name", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createLabel(tabItem, "Phone", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Email", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Address 1", "");
    createTextField(tabItem, "", (new CC()).spanX().growX());
    createLabel(tabItem, "Address 2", "");
    createTextField(tabItem, "", (new CC()).spanX().growX());
    createLabel(tabItem, "City", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createLabel(tabItem, "State", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Postal Code", "");
    createTextField(tabItem, "", (new CC()).spanX(2).growX(0.0F));
    createLabel(tabItem, "Country", "");
    createTextField(tabItem, "", (new CC()).wrap());
    createButton(tabItem, "New", (new CC()).spanX(5).split(5).tag("other"));
    createButton(tabItem, "Delete", (new CC()).tag("other"));
    createButton(tabItem, "Edit", (new CC()).tag("other"));
    createButton(tabItem, "Save", (new CC()).tag("other"));
    createButton(tabItem, "Cancel", (new CC()).tag("cancel"));
    return (Composite)tabFolder;
  }
  
  public Composite createLayout_Showdown(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    TabItem tabItem1 = createTabPanel(tabFolder, "Layout Showdown (pure)", (Layout)new MigLayout("", "[]15[][grow,fill]15[grow]"));
    createList(tabItem1, "Mouse, Mickey", "spany, growy, wmin 150");
    createLabel(tabItem1, "Last Name", "");
    createTextField(tabItem1, "", "");
    createLabel(tabItem1, "First Name", "split");
    createTextField(tabItem1, "", "growx, wrap");
    createLabel(tabItem1, "Phone", "");
    createTextField(tabItem1, "", "");
    createLabel(tabItem1, "Email", "split");
    createTextField(tabItem1, "", "growx, wrap");
    createLabel(tabItem1, "Address 1", "");
    createTextField(tabItem1, "", "span, growx");
    createLabel(tabItem1, "Address 2", "");
    createTextField(tabItem1, "", "span, growx");
    createLabel(tabItem1, "City", "");
    createTextField(tabItem1, "", "wrap");
    createLabel(tabItem1, "State", "");
    createTextField(tabItem1, "", "");
    createLabel(tabItem1, "Postal Code", "split");
    createTextField(tabItem1, "", "growx, wrap");
    createLabel(tabItem1, "Country", "");
    createTextField(tabItem1, "", "wrap 15");
    createButton(tabItem1, "New", "span, split, align right");
    createButton(tabItem1, "Delete", "");
    createButton(tabItem1, "Edit", "");
    createButton(tabItem1, "Save", "");
    createButton(tabItem1, "Cancel", "wrap push");
    TabItem tabItem2 = createTabPanel(tabFolder, "Layout Showdown (improved)", (Layout)new MigLayout("", "[]15[][grow,fill]15[][grow,fill]"));
    createList(tabItem2, "Mouse, Mickey", "spany, growy, wmin 150");
    createLabel(tabItem2, "Last Name", "");
    createTextField(tabItem2, "", "");
    createLabel(tabItem2, "First Name", "");
    createTextField(tabItem2, "", "wrap");
    createLabel(tabItem2, "Phone", "");
    createTextField(tabItem2, "", "");
    createLabel(tabItem2, "Email", "");
    createTextField(tabItem2, "", "wrap");
    createLabel(tabItem2, "Address 1", "");
    createTextField(tabItem2, "", "span");
    createLabel(tabItem2, "Address 2", "");
    createTextField(tabItem2, "", "span");
    createLabel(tabItem2, "City", "");
    createTextField(tabItem2, "", "wrap");
    createLabel(tabItem2, "State", "");
    createTextField(tabItem2, "", "");
    createLabel(tabItem2, "Postal Code", "");
    createTextField(tabItem2, "", "width 50, grow 0, wrap");
    createLabel(tabItem2, "Country", "");
    createTextField(tabItem2, "", "wrap 15");
    createButton(tabItem2, "New", "tag other, span, split");
    createButton(tabItem2, "Delete", "tag other");
    createButton(tabItem2, "Edit", "tag other");
    createButton(tabItem2, "Save", "tag other");
    createButton(tabItem2, "Cancel", "tag cancel, wrap push");
    return (Composite)tabFolder;
  }
  
  public Composite createDocking(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    tabFolder.setLayoutData("grow");
    TabItem tabItem1 = createTabPanel(tabFolder, "Docking 1", (Layout)new MigLayout("fill"));
    createPanel(tabItem1, "1. North", "north");
    createPanel(tabItem1, "2. West", "west");
    createPanel(tabItem1, "3. East", "east");
    createPanel(tabItem1, "4. South", "south");
    Table table = new Table(getComposite(tabItem1), DOUBLE_BUFFER);
    byte b1;
    for (b1 = 0; b1 < 5; b1++) {
      TableColumn tableColumn = new TableColumn(table, 16897);
      tableColumn.setText("Column " + (b1 + 1));
      tableColumn.setWidth(100);
    } 
    for (b1 = 0; b1 < 15; b1++) {
      TableItem tableItem = new TableItem(table, 0);
      String[] arrayOfString1 = new String[6];
      for (byte b = 0; b < arrayOfString1.length; b++)
        arrayOfString1[b] = "Cell " + (b1 + 1) + ", " + (b + 1); 
      tableItem.setText(arrayOfString1);
    } 
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.setLayoutData("grow");
    TabItem tabItem2 = createTabPanel(tabFolder, "Docking 2 (fill)", (Layout)new MigLayout("fill", "[c]", ""));
    createPanel(tabItem2, "1. North", "north");
    createPanel(tabItem2, "2. North", "north");
    createPanel(tabItem2, "3. West", "west");
    createPanel(tabItem2, "4. West", "west");
    createPanel(tabItem2, "5. South", "south");
    createPanel(tabItem2, "6. East", "east");
    createButton(tabItem2, "7. Normal", "");
    createButton(tabItem2, "8. Normal", "");
    createButton(tabItem2, "9. Normal", "");
    TabItem tabItem3 = createTabPanel(tabFolder, "Docking 3", (Layout)new MigLayout());
    createPanel(tabItem3, "1. North", "north");
    createPanel(tabItem3, "2. South", "south");
    createPanel(tabItem3, "3. West", "west");
    createPanel(tabItem3, "4. East", "east");
    createButton(tabItem3, "5. Normal", "");
    TabItem tabItem4 = createTabPanel(tabFolder, "Docking 4", (Layout)new MigLayout());
    createPanel(tabItem4, "1. North", "north");
    createPanel(tabItem4, "2. North", "north");
    createPanel(tabItem4, "3. West", "west");
    createPanel(tabItem4, "4. West", "west");
    createPanel(tabItem4, "5. South", "south");
    createPanel(tabItem4, "6. East", "east");
    createButton(tabItem4, "7. Normal", "");
    createButton(tabItem4, "8. Normal", "");
    createButton(tabItem4, "9. Normal", "");
    TabItem tabItem5 = createTabPanel(tabFolder, "Docking 5 (fillx)", (Layout)new MigLayout("fillx", "[c]", ""));
    createPanel(tabItem5, "1. North", "north");
    createPanel(tabItem5, "2. North", "north");
    createPanel(tabItem5, "3. West", "west");
    createPanel(tabItem5, "4. West", "west");
    createPanel(tabItem5, "5. South", "south");
    createPanel(tabItem5, "6. East", "east");
    createButton(tabItem5, "7. Normal", "");
    createButton(tabItem5, "8. Normal", "");
    createButton(tabItem5, "9. Normal", "");
    TabItem tabItem6 = createTabPanel(tabFolder, "Random Docking", (Layout)new MigLayout("fill"));
    String[] arrayOfString = { "north", "east", "south", "west" };
    Random random = new Random();
    for (byte b2 = 0; b2 < 20; b2++) {
      int i = random.nextInt(4);
      createPanel(tabItem6, (b2 + 1) + " " + arrayOfString[i], arrayOfString[i]);
    } 
    createPanel(tabItem6, "I'm in the Center!", "grow");
    return (Composite)tabFolder;
  }
  
  public Control createAbsolute_Position(final Composite parent) {
    TabFolder tabFolder = new TabFolder(parent, DOUBLE_BUFFER);
    TabItem tabItem1 = createTabPanel(tabFolder, "X Y Positions", (Layout)new FillLayout());
    final Composite posPanel = createPanel(tabItem1, (Layout)new MigLayout());
    createButton(composite1, "pos 0.5al 0al", null);
    createButton(composite1, "pos 1al 0al", null);
    createButton(composite1, "pos 0.5al 0.5al", null);
    createButton(composite1, "pos 5in 45lp", null);
    createButton(composite1, "pos 0.5al 0.5al", null);
    createButton(composite1, "pos 0.5al 1al", null);
    createButton(composite1, "pos 1al .25al", null);
    createButton(composite1, "pos visual.x2-pref visual.y2-pref", null);
    createButton(composite1, "pos 1al -1in", null);
    createButton(composite1, "pos 100 100", null);
    createButton(composite1, "pos (10+(20*3lp)) 200", null);
    createButton(composite1, "Drag Window! (pos 500-container.xpos 500-container.ypos)", "pos 500-container.xpos 500-container.ypos");
    TabItem tabItem2 = createTabPanel(tabFolder, "X1 Y1 X2 Y2 Bounds", (Layout)new FillLayout());
    Composite composite2 = createPanel(tabItem2, (Layout)new MigLayout());
    Label label = createLabel(composite2, "pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2", null, 16779264);
    label.setBackground(new Color((Device)display, 200, 200, 255));
    deriveFont((Control)label, 1, 10);
    createButton(composite2, "pos 0 0 container.x2 n", null);
    createButton(composite2, "pos visual.x 40 visual.x2 70", null);
    createButton(composite2, "pos visual.x 100 visual.x2 p", null);
    createButton(composite2, "pos 0.1al 0.4al n visual.y2-10", null);
    createButton(composite2, "pos 0.9al 0.4al n visual.y2-10", null);
    createButton(composite2, "pos 0.5al 0.5al, pad 3 0 -3 0", null);
    createButton(composite2, "pos n n 50% 50%", null);
    createButton(composite2, "pos 50% 50% n n", null);
    createButton(composite2, "pos 50% n n 50%", null);
    createButton(composite2, "pos n 50% 50% n", null);
    parent.getShell().addControlListener((ControlListener)new ControlAdapter() {
          public void controlMoved(ControlEvent param1ControlEvent) {
            if (!posPanel.isDisposed()) {
              posPanel.layout();
            } else {
              parent.getShell().removeControlListener((ControlListener)this);
            } 
          }
        });
    return (Control)tabFolder;
  }
  
  public Control createComponent_Links(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    TabItem tabItem1 = createTabPanel(tabFolder, "Component Links", (Layout)new MigLayout());
    createButton(tabItem1, "Mini", "pos null ta.y ta.x2 null, pad 3 0 -3 0");
    createTextArea(tabItem1, "Components, Please Link to Me!\nMy ID is: 'ta'", "id ta, pos 0.5al 0.5al, w 300");
    createButton(tabItem1, "id b1,pos ta.x2 ta.y2", null);
    createButton(tabItem1, "pos b1.x2+rel b1.y visual.x2 null", null);
    createCheck(tabItem1, "pos (ta.x+indent) (ta.y2+rel)", null);
    createButton(tabItem1, "pos ta.x2+rel ta.y visual.x2 null", null);
    createButton(tabItem1, "pos null ta.y+(ta.h-pref)/2 ta.x-rel null", null);
    createButton(tabItem1, "pos ta.x ta.y2+100 ta.x2 null", null);
    TabItem tabItem2 = createTabPanel(tabFolder, "External Components", (Layout)new MigLayout());
    Button button = createButton(tabItem2, "Bounds Externally Set!", "id ext, external");
    button.setBounds(250, 130, 200, 40);
    createButton(tabItem2, "pos ext.x2 ext.y2", "pos ext.x2 ext.y2");
    createButton(tabItem2, "pos null null ext.x ext.y", "pos null null ext.x ext.y");
    TabItem tabItem3 = createTabPanel(tabFolder, "End Grouping", (Layout)new FillLayout());
    Composite composite1 = createPanel(tabItem3, (Layout)new MigLayout());
    createButton(composite1, "id b1, endgroupx g1, pos 200 200", null);
    createButton(composite1, "id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)", null);
    createButton(composite1, "id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)", null);
    createButton(composite1, "id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)", null);
    TabItem tabItem4 = createTabPanel(tabFolder, "Group Bounds", (Layout)new FillLayout());
    Composite composite2 = createPanel(tabItem4, (Layout)new MigLayout());
    createButton(composite2, "id grp1.b1, pos n 0.5al 50% n", null);
    createButton(composite2, "id grp1.b2, pos 50% 0.5al n n", null);
    createButton(composite2, "id grp1.b3, pos 0.5al n n b1.y", null);
    createButton(composite2, "id grp1.b4, pos 0.5al b1.y2 n n", null);
    createButton(composite2, "pos n grp1.y2 grp1.x n", null);
    createButton(composite2, "pos n n grp1.x grp1.y", null);
    createButton(composite2, "pos grp1.x2 n n grp1.y", null);
    createButton(composite2, "pos grp1.x2 grp1.y2", null);
    Composite composite3 = createPanel(composite2, (Layout)null);
    composite3.setLayoutData("pos grp1.x grp1.y grp1.x2 grp1.y2");
    composite3.setBackground(new Color((Device)display, 200, 200, 255));
    return (Control)tabFolder;
  }
  
  public Control createFlow_Direction(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    createFlowPanel(tabFolder, "Layout: flowx, Cell: flowx", "", "flowx");
    createFlowPanel(tabFolder, "Layout: flowx, Cell: flowy", "", "flowy");
    createFlowPanel(tabFolder, "Layout: flowy, Cell: flowx", "flowy", "flowx");
    createFlowPanel(tabFolder, "Layout: flowy, Cell: flowy", "flowy", "flowy");
    return (Control)tabFolder;
  }
  
  private TabItem createFlowPanel(TabFolder paramTabFolder, String paramString1, String paramString2, String paramString3) {
    MigLayout migLayout = new MigLayout("center, wrap 3," + paramString2, "[110,fill]", "[110,fill]");
    TabItem tabItem = createTabPanel(paramTabFolder, paramString1, (Layout)migLayout);
    for (byte b = 0; b < 9; b++) {
      Composite composite1 = createPanel(tabItem, "" + (b + 1), paramString3);
      Font font1 = deriveFont((Control)composite1, -1, 20);
      composite1.getChildren()[0].setFont(font1);
    } 
    Composite composite = createPanel(tabItem, "5:2", paramString3 + ",cell 1 1");
    Font font = deriveFont((Control)composite, -1, 20);
    composite.getChildren()[0].setFont(font);
    return tabItem;
  }
  
  public Control createDebug(Composite paramComposite) {
    return createPlainImpl(paramComposite, true);
  }
  
  public Control createButton_Bars(final Composite parent) {
    MigLayout migLayout = new MigLayout("ins 0 0 15lp 0", "[grow]", "[grow]u[baseline,nogrid]");
    final Composite mainPanel = new Composite(parent, DOUBLE_BUFFER);
    composite.setLayout((Layout)migLayout);
    TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
    tabFolder.setLayoutData("grow, wrap");
    createButtonBarsPanel(tabFolder, "Buttons", "help", false);
    createButtonBarsPanel(tabFolder, "Buttons with Help2", "help2", false);
    createButtonBarsPanel(tabFolder, "Buttons (Same width)", "help", true);
    createLabel(composite, "Button Order:", "");
    final Label formatLabel = createLabel(composite, "", "growx");
    deriveFont((Control)label, 1, -1);
    final Button winButt = createToggleButton(composite, "Windows", "wmin button");
    final Button macButt = createToggleButton(composite, "Mac OS X", "wmin button");
    button1.addSelectionListener((SelectionListener)new SelectionAdapter() {
          public void widgetSelected(SelectionEvent param1SelectionEvent) {
            if (winButt.getSelection()) {
              PlatformDefaults.setPlatform(0);
              formatLabel.setText("'" + PlatformDefaults.getButtonOrder() + "'");
              macButt.setSelection(false);
              mainPanel.layout();
            } 
          }
        });
    button2.addSelectionListener((SelectionListener)new SelectionAdapter() {
          public void widgetSelected(SelectionEvent param1SelectionEvent) {
            if (macButt.getSelection()) {
              PlatformDefaults.setPlatform(1);
              formatLabel.setText("'" + PlatformDefaults.getButtonOrder() + "'");
              winButt.setSelection(false);
              mainPanel.layout();
            } 
          }
        });
    Button button3 = createButton(composite, "Help", "gap unrel,wmin button");
    button3.addSelectionListener((SelectionListener)new SelectionAdapter() {
          public void widgetSelected(SelectionEvent param1SelectionEvent) {
            MessageBox messageBox = new MessageBox(parent.getShell());
            messageBox.setMessage("See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.");
            messageBox.open();
          }
        });
    ((PlatformDefaults.getPlatform() == 0) ? button1 : button2).setSelection(true);
    return (Control)composite;
  }
  
  private TabItem createButtonBarsPanel(TabFolder paramTabFolder, String paramString1, String paramString2, boolean paramBoolean) {
    MigLayout migLayout = new MigLayout("nogrid, fillx, aligny 100%, gapy unrel");
    TabItem tabItem = createTabPanel(paramTabFolder, paramString1, (Layout)migLayout);
    String[][] arrayOfString = { { "No", "Yes" }, { "Help", "Close" }, { "OK", "Help" }, { "OK", "Cancel", "Help" }, { "OK", "Cancel", "Apply", "Help" }, { "No", "Yes", "Cancel" }, { "Help", "< Move Back", "Move Forward >", "Cancel" }, { "Print...", "Cancel", "Help" } };
    for (byte b = 0; b < arrayOfString.length; b++) {
      for (byte b1 = 0; b1 < (arrayOfString[b]).length; b1++) {
        String str1 = arrayOfString[b][b1];
        String str2 = str1;
        if (str1.equals("Help")) {
          str2 = paramString2;
        } else if (str1.equals("< Move Back")) {
          str2 = "back";
        } else if (str1.equals("Close")) {
          str2 = "cancel";
        } else if (str1.equals("Move Forward >")) {
          str2 = "next";
        } else if (str1.equals("Print...")) {
          str2 = "other";
        } 
        String str3 = (b1 == (arrayOfString[b]).length - 1) ? ",wrap" : "";
        String str4 = paramBoolean ? ("sgx " + b + ",") : "";
        createButton(tabItem, str1, str4 + "tag " + str2 + str3);
      } 
    } 
    return tabItem;
  }
  
  public Control createOrientation(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("flowy", "[grow,fill]", "[]0[]15lp[]0[]");
    TabItem tabItem = createTabPanel(tabFolder, "Orientation", (Layout)migLayout1);
    MigLayout migLayout2 = new MigLayout("", "[][grow,fill]", "");
    Composite composite1 = createPanel(tabItem, (Layout)migLayout2);
    addSeparator(composite1, "Default Orientation");
    createLabel(composite1, "Level", "");
    createTextField(composite1, "", "span,growx");
    createLabel(composite1, "Radar", "");
    createTextField(composite1, "", "");
    createTextField(composite1, "", "");
    MigLayout migLayout3 = new MigLayout("rtl,ttb", "[][grow,fill]", "");
    Composite composite2 = createPanel(tabItem, (Layout)migLayout3);
    addSeparator(composite2, "Right to Left");
    createLabel(composite2, "Level", "");
    createTextField(composite2, "", "span,growx");
    createLabel(composite2, "Radar", "");
    createTextField(composite2, "", "");
    createTextField(composite2, "", "");
    MigLayout migLayout4 = new MigLayout("rtl,btt", "[][grow,fill]", "");
    Composite composite3 = createPanel(tabItem, (Layout)migLayout4);
    addSeparator(composite3, "Right to Left, Bottom to Top");
    createLabel(composite3, "Level", "");
    createTextField(composite3, "", "span,growx");
    createLabel(composite3, "Radar", "");
    createTextField(composite3, "", "");
    createTextField(composite3, "", "");
    MigLayout migLayout5 = new MigLayout("ltr,btt", "[][grow,fill]", "");
    Composite composite4 = createPanel(tabItem, (Layout)migLayout5);
    addSeparator(composite4, "Left to Right, Bottom to Top");
    createLabel(composite4, "Level", "");
    createTextField(composite4, "", "span,growx");
    createLabel(composite4, "Radar", "");
    createTextField(composite4, "", "");
    createTextField(composite4, "", "");
    return (Control)tabFolder;
  }
  
  public Control createCell_Position(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
    TabItem tabItem1 = createTabPanel(tabFolder, "Absolute", (Layout)migLayout1);
    createPanel(tabItem1, "cell 0 0", (Object)null);
    createPanel(tabItem1, "cell 2 0", (Object)null);
    createPanel(tabItem1, "cell 3 0", (Object)null);
    createPanel(tabItem1, "cell 1 1", (Object)null);
    createPanel(tabItem1, "cell 0 2", (Object)null);
    createPanel(tabItem1, "cell 2 2", (Object)null);
    createPanel(tabItem1, "cell 2 2", (Object)null);
    MigLayout migLayout2 = new MigLayout("wrap", "[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]", "[100:pref,fill]");
    TabItem tabItem2 = createTabPanel(tabFolder, "Relative + Wrap", (Layout)migLayout2);
    createPanel(tabItem2, "", (Object)null);
    createPanel(tabItem2, "skip", (Object)null);
    createPanel(tabItem2, "", (Object)null);
    createPanel(tabItem2, "skip,wrap", (Object)null);
    createPanel(tabItem2, "", (Object)null);
    createPanel(tabItem2, "skip,split", (Object)null);
    createPanel(tabItem2, "", (Object)null);
    MigLayout migLayout3 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
    TabItem tabItem3 = createTabPanel(tabFolder, "Relative", (Layout)migLayout3);
    createPanel(tabItem3, "", (Object)null);
    createPanel(tabItem3, "skip", (Object)null);
    createPanel(tabItem3, "wrap", (Object)null);
    createPanel(tabItem3, "skip,wrap", (Object)null);
    createPanel(tabItem3, "", (Object)null);
    createPanel(tabItem3, "skip,split", (Object)null);
    createPanel(tabItem3, "", (Object)null);
    MigLayout migLayout4 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
    TabItem tabItem4 = createTabPanel(tabFolder, "Mixed", (Layout)migLayout4);
    createPanel(tabItem4, "", (Object)null);
    createPanel(tabItem4, "cell 2 0", (Object)null);
    createPanel(tabItem4, "", (Object)null);
    createPanel(tabItem4, "cell 1 1,wrap", (Object)null);
    createPanel(tabItem4, "", (Object)null);
    createPanel(tabItem4, "cell 2 2,split", (Object)null);
    createPanel(tabItem4, "", (Object)null);
    return (Control)tabFolder;
  }
  
  public Control createPlain(Composite paramComposite) {
    return createPlainImpl(paramComposite, false);
  }
  
  private Control createPlainImpl(Composite paramComposite, boolean paramBoolean) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout = new MigLayout((paramBoolean && benchRuns == 0) ? "debug" : "", "[r][100lp, fill][60lp][95lp, fill]", "");
    TabItem tabItem = createTabPanel(tabFolder, "Plain", (Layout)migLayout);
    addSeparator(tabItem, "Manufacturer");
    createLabel(tabItem, "Company", "");
    createTextField(tabItem, "", "span,growx");
    createLabel(tabItem, "Contact", "");
    createTextField(tabItem, "", "span,growx");
    createLabel(tabItem, "Order No", "");
    createTextField(tabItem, "", "wmin 15*6,wrap");
    addSeparator(tabItem, "Inspector");
    createLabel(tabItem, "Name", "");
    createTextField(tabItem, "", "span,growx");
    createLabel(tabItem, "Reference No", "");
    createTextField(tabItem, "", "wrap");
    createLabel(tabItem, "Status", "");
    createCombo(tabItem, new String[] { "In Progress", "Finnished", "Released" }, "wrap");
    addSeparator(tabItem, "Ship");
    createLabel(tabItem, "Shipyard", "");
    createTextField(tabItem, "", "span,growx");
    createLabel(tabItem, "Register No", "");
    createTextField(tabItem, "", "");
    createLabel(tabItem, "Hull No", "right");
    createTextField(tabItem, "", "wmin 15*6,wrap");
    createLabel(tabItem, "Project StructureType", "");
    createCombo(tabItem, new String[] { "New Building", "Convention", "Repair" }, "wrap");
    if (paramBoolean)
      createLabel(tabItem, "Blue is component bounds. Cell bounds (red) can not be shown in SWT", "newline,ax left,span,gaptop 40"); 
    return (Control)tabFolder;
  }
  
  public Control createBound_Sizes(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    for (byte b = 0; b < 2; b++) {
      String str = (b == 0) ? "[right][300]" : "[right, 150lp:pref][300]";
      MigLayout migLayout1 = new MigLayout("wrap", str, "");
      TabItem tabItem1 = createTabPanel(tabFolder, (b == 0) ? "Jumping 1" : "Stable 1", (Layout)migLayout1);
      createLabel(tabItem1, "File Number:", "");
      createTextField(tabItem1, "", "growx");
      createLabel(tabItem1, "RFQ Number:", "");
      createTextField(tabItem1, "", "growx");
      createLabel(tabItem1, "Entry Date:", "");
      createTextField(tabItem1, "        ", "wmin 6*6");
      createLabel(tabItem1, "Sales Person:", "");
      createTextField(tabItem1, "", "growx");
      MigLayout migLayout2 = new MigLayout("wrap", str, "");
      TabItem tabItem2 = createTabPanel(tabFolder, (b == 0) ? "Jumping 2" : "Stable 2", (Layout)migLayout2);
      createLabel(tabItem2, "Shipper:", "");
      createTextField(tabItem2, "        ", "split 2");
      createTextField(tabItem2, "", "growx");
      createLabel(tabItem2, "Consignee:", "");
      createTextField(tabItem2, "        ", "split 2");
      createTextField(tabItem2, "", "growx");
      createLabel(tabItem2, "Departure:", "");
      createTextField(tabItem2, "        ", "split 2");
      createTextField(tabItem2, "", "growx");
      createLabel(tabItem2, "Destination:", "");
      createTextField(tabItem2, "        ", "split 2");
      createTextField(tabItem2, "", "growx");
    } 
    return (Control)tabFolder;
  }
  
  public Control createComponent_Sizes(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout = new MigLayout("wrap", "[right][0:pref,grow]", "");
    TabItem tabItem = createTabPanel(tabFolder, "Component Sizes", (Layout)new FillLayout());
    SashForm sashForm = new SashForm(getComposite(tabItem), 65792);
    sashForm.setBackground(new Color((Device)display, 255, 255, 255));
    sashForm.setBackgroundMode(2);
    Composite composite = createPanel(sashForm, (Layout)migLayout, 2048);
    createTextArea(sashForm, "Use slider to see how the components grow and shrink depending on the constraints set on them.", "");
    createLabel(composite, "", "");
    createTextField(composite, "8       ", "");
    createLabel(composite, "width min!", null);
    createTextField(composite, "3  ", "width min!");
    createLabel(composite, "width pref!", "");
    createTextField(composite, "3  ", "width pref!");
    createLabel(composite, "width min:pref", null);
    createTextField(composite, "8       ", "width min:pref");
    createLabel(composite, "width min:100:150", null);
    createTextField(composite, "8       ", "width min:100:150");
    createLabel(composite, "width min:100:150, growx", null);
    createTextField(composite, "8       ", "width min:100:150, growx");
    createLabel(composite, "width min:100, growx", null);
    createTextField(composite, "8       ", "width min:100, growx");
    createLabel(composite, "width 40!", null);
    createTextField(composite, "8       ", "width 40!");
    createLabel(composite, "width 40:40:40", null);
    createTextField(composite, "8       ", "width 40:40:40");
    return (Control)tabFolder;
  }
  
  public Control createCell_Alignments(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("wrap", "[grow,left][grow,center][grow,right][grow,fill,center]", "[]unrel[][]");
    TabItem tabItem1 = createTabPanel(tabFolder, "Horizontal", (Layout)migLayout1);
    String[] arrayOfString1 = { "", "growx", "growx 0", "left", "center", "right", "leading", "trailing" };
    createLabel(tabItem1, "[left]", "c");
    createLabel(tabItem1, "[center]", "c");
    createLabel(tabItem1, "[right]", "c");
    createLabel(tabItem1, "[fill,center]", "c, growx 0");
    for (byte b1 = 0; b1 < arrayOfString1.length; b1++) {
      for (byte b = 0; b < 4; b++) {
        String str = (arrayOfString1[b1].length() > 0) ? arrayOfString1[b1] : "default";
        createButton(tabItem1, str, arrayOfString1[b1]);
      } 
    } 
    MigLayout migLayout2 = new MigLayout("wrap,flowy", "[right][]", "[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]");
    TabItem tabItem2 = createTabPanel(tabFolder, "Vertical", (Layout)migLayout2);
    String[] arrayOfString2 = { "", "growy", "growy 0", "top", "center", "bottom" };
    createLabel(tabItem2, "[top]", "center");
    createLabel(tabItem2, "[center]", "center");
    createLabel(tabItem2, "[bottom]", "center");
    createLabel(tabItem2, "[fill, bottom]", "center, growy 0");
    createLabel(tabItem2, "[fill, baseline]", "center");
    for (byte b2 = 0; b2 < arrayOfString2.length; b2++) {
      for (byte b = 0; b < 5; b++) {
        String str = (arrayOfString2[b2].length() > 0) ? arrayOfString2[b2] : "default";
        createButton(tabItem2, str, arrayOfString2[b2]);
      } 
    } 
    return (Control)tabFolder;
  }
  
  public Control createUnits(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("wrap", "[right][]", "");
    TabItem tabItem1 = createTabPanel(tabFolder, "Horizontal", (Layout)migLayout1);
    String[] arrayOfString1 = { "72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp" };
    for (byte b1 = 0; b1 < arrayOfString1.length; b1++) {
      createLabel(tabItem1, arrayOfString1[b1], "");
      createTextField(tabItem1, "", "width " + arrayOfString1[b1] + "");
    } 
    MigLayout migLayout2 = new MigLayout("", "[right][][]", "");
    TabItem tabItem2 = createTabPanel(tabFolder, "Horizontal LP", (Layout)migLayout2);
    createLabel(tabItem2, "9 cols", "");
    createTextField(tabItem2, "", "wmin 9*6");
    String[] arrayOfString2 = { "75lp", "75px", "88px", "100px" };
    createLabel(tabItem2, "", "wrap");
    for (byte b2 = 0; b2 < arrayOfString2.length; b2++) {
      createLabel(tabItem2, arrayOfString2[b2], "");
      createTextField(tabItem2, "", "width " + arrayOfString2[b2] + ", wrap");
    } 
    MigLayout migLayout3 = new MigLayout("wrap,flowy", "[c]", "[top][top]");
    TabItem tabItem3 = createTabPanel(tabFolder, "Vertical", (Layout)migLayout3);
    String[] arrayOfString3 = { "72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp" };
    for (byte b3 = 0; b3 < arrayOfString1.length; b3++) {
      createLabel(tabItem3, arrayOfString3[b3], "");
      createTextArea(tabItem3, "", "width 50!, height " + arrayOfString3[b3] + "");
    } 
    MigLayout migLayout4 = new MigLayout("wrap,flowy", "[c]", "[top][top]40px[top][top]");
    TabItem tabItem4 = createTabPanel(tabFolder, "Vertical LP", (Layout)migLayout4);
    createLabel(tabItem4, "4 rows", "");
    createTextArea(tabItem4, "\n\n\n\n", "width 50!");
    createLabel(tabItem4, "field", "");
    createTextField(tabItem4, "", "wmin 5*9");
    String[] arrayOfString4 = { "63lp", "57px", "63px", "68px", "25%" };
    String[] arrayOfString5 = { "21lp", "21px", "23px", "24px", "10%" };
    for (byte b4 = 0; b4 < arrayOfString4.length; b4++) {
      createLabel(tabItem4, arrayOfString4[b4], "");
      createTextArea(tabItem4, "", "width 50!, height " + arrayOfString4[b4] + "");
      createLabel(tabItem4, arrayOfString5[b4], "");
      createTextField(tabItem4, "", "height " + arrayOfString5[b4] + "!,wmin 5*6");
    } 
    createLabel(tabItem4, "button", "skip 2");
    createButton(tabItem4, "...", "");
    return (Control)tabFolder;
  }
  
  public Control createGrouping(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("", "[]push[][][]", "");
    TabItem tabItem1 = createTabPanel(tabFolder, "Ungrouped", (Layout)migLayout1);
    createButton(tabItem1, "Help", "");
    createButton(tabItem1, "< Back", "gap push");
    createButton(tabItem1, "Forward >", "");
    createButton(tabItem1, "Apply", "gap unrel");
    createButton(tabItem1, "Cancel", "gap unrel");
    MigLayout migLayout2 = new MigLayout("nogrid, fillx");
    TabItem tabItem2 = createTabPanel(tabFolder, "Grouped (Components)", (Layout)migLayout2);
    createButton(tabItem2, "Help", "sg");
    createButton(tabItem2, "< Back", "sg, gap push");
    createButton(tabItem2, "Forward >", "sg");
    createButton(tabItem2, "Apply", "sg, gap unrel");
    createButton(tabItem2, "Cancel", "sg, gap unrel");
    MigLayout migLayout3 = new MigLayout("", "[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]", "");
    TabItem tabItem3 = createTabPanel(tabFolder, "Grouped (Columns)", (Layout)migLayout3);
    createButton(tabItem3, "Help", "");
    createButton(tabItem3, "< Back", "");
    createButton(tabItem3, "Forward >", "");
    createButton(tabItem3, "Apply", "");
    createButton(tabItem3, "Cancel", "");
    MigLayout migLayout4 = new MigLayout();
    TabItem tabItem4 = createTabPanel(tabFolder, "Ungrouped Rows", (Layout)migLayout4);
    createLabel(tabItem4, "File Number:", "");
    createTextField(tabItem4, "30                            ", "wrap");
    createLabel(tabItem4, "BL/MBL number:", "");
    createTextField(tabItem4, "7      ", "split 2");
    createTextField(tabItem4, "7      ", "wrap");
    createLabel(tabItem4, "Entry Date:", "");
    createTextField(tabItem4, "7      ", "wrap");
    createLabel(tabItem4, "RFQ Number:", "");
    createTextField(tabItem4, "30                            ", "wrap");
    createLabel(tabItem4, "Goods:", "");
    createCheck(tabItem4, "Dangerous", "wrap");
    createLabel(tabItem4, "Shipper:", "");
    createTextField(tabItem4, "30                            ", "wrap");
    createLabel(tabItem4, "Customer:", "");
    createTextField(tabItem4, "", "split 2,growx");
    createButton(tabItem4, "...", "width 60px:pref,wrap");
    createLabel(tabItem4, "Port of Loading:", "");
    createTextField(tabItem4, "30                            ", "wrap");
    createLabel(tabItem4, "Destination:", "");
    createTextField(tabItem4, "30                            ", "wrap");
    MigLayout migLayout5 = new MigLayout("", "[]", "[sg]");
    TabItem tabItem5 = createTabPanel(tabFolder, "Grouped Rows", (Layout)migLayout5);
    createLabel(tabItem5, "File Number:", "");
    createTextField(tabItem5, "30                            ", "wrap");
    createLabel(tabItem5, "BL/MBL number:", "");
    createTextField(tabItem5, "7      ", "split 2");
    createTextField(tabItem5, "7      ", "wrap");
    createLabel(tabItem5, "Entry Date:", "");
    createTextField(tabItem5, "7      ", "wrap");
    createLabel(tabItem5, "RFQ Number:", "");
    createTextField(tabItem5, "30                            ", "wrap");
    createLabel(tabItem5, "Goods:", "");
    createCheck(tabItem5, "Dangerous", "wrap");
    createLabel(tabItem5, "Shipper:", "");
    createTextField(tabItem5, "30                            ", "wrap");
    createLabel(tabItem5, "Customer:", "");
    createTextField(tabItem5, "", "split 2,growx");
    createButton(tabItem5, "...", "width 50px:pref,wrap");
    createLabel(tabItem5, "Port of Loading:", "");
    createTextField(tabItem5, "30                            ", "wrap");
    createLabel(tabItem5, "Destination:", "");
    createTextField(tabItem5, "30                            ", "wrap");
    return (Control)tabFolder;
  }
  
  public Control createSpan(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("", "[fill][25%,fill][105lp!,fill][150px!,fill]", "[]15[][]");
    TabItem tabItem1 = createTabPanel(tabFolder, "Column Span/Split", (Layout)migLayout1);
    createTextField(tabItem1, "Col1 [ ]", "");
    createTextField(tabItem1, "Col2 [25%]", "");
    createTextField(tabItem1, "Col3 [105lp!]", "");
    createTextField(tabItem1, "Col4 [150px!]", "wrap");
    createLabel(tabItem1, "Full Name:", "");
    createTextField(tabItem1, "span, growx                              ", "span,growx");
    createLabel(tabItem1, "Phone:", "");
    createTextField(tabItem1, "   ", "span 3, split 5");
    createTextField(tabItem1, "     ", null);
    createTextField(tabItem1, "     ", null);
    createTextField(tabItem1, "       ", null);
    createLabel(tabItem1, "(span 3, split 4)", "wrap");
    createLabel(tabItem1, "Zip/City:", "");
    createTextField(tabItem1, "     ", "");
    createTextField(tabItem1, "span 2, growx", null);
    MigLayout migLayout2 = new MigLayout("wrap", "[225lp]para[225lp]", "[]3[]unrel[]3[]unrel[]3[]");
    TabItem tabItem2 = createTabPanel(tabFolder, "Row Span", (Layout)migLayout2);
    createLabel(tabItem2, "Name", "");
    createLabel(tabItem2, "Notes", "");
    createTextField(tabItem2, "growx", null);
    createTextArea(tabItem2, "spany,grow          ", "spany,grow,hmin 13*5");
    createLabel(tabItem2, "Phone", "");
    createTextField(tabItem2, "growx", null);
    createLabel(tabItem2, "Fax", "");
    createTextField(tabItem2, "growx", null);
    return (Control)tabFolder;
  }
  
  public Control createGrowing(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
    TabItem tabItem1 = createTabPanel(tabFolder, "All", (Layout)migLayout1);
    createLabel(tabItem1, "Fixed", "");
    createLabel(tabItem1, "Gets all extra space", "wrap");
    createTextField(tabItem1, "     ", "");
    createTextField(tabItem1, "     ", "");
    MigLayout migLayout2 = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
    TabItem tabItem2 = createTabPanel(tabFolder, "Half", (Layout)migLayout2);
    createLabel(tabItem2, "Fixed", "");
    createLabel(tabItem2, "Gets half of extra space", "");
    createLabel(tabItem2, "Gets half of extra space", "wrap");
    createTextField(tabItem2, "     ", "");
    createTextField(tabItem2, "     ", "");
    createTextField(tabItem2, "     ", "");
    MigLayout migLayout3 = new MigLayout("", "[pref!][0:0,grow 25,fill][0:0,grow 75,fill]", "[]15[]");
    TabItem tabItem3 = createTabPanel(tabFolder, "Percent 1", (Layout)migLayout3);
    createLabel(tabItem3, "Fixed", "");
    createLabel(tabItem3, "Gets 25% of extra space", "");
    createLabel(tabItem3, "Gets 75% of extra space", "wrap");
    createTextField(tabItem3, "     ", "");
    createTextField(tabItem3, "     ", "");
    createTextField(tabItem3, "     ", "");
    MigLayout migLayout4 = new MigLayout("", "[0:0,grow 33,fill][0:0,grow 67,fill]", "[]15[]");
    TabItem tabItem4 = createTabPanel(tabFolder, "Percent 2", (Layout)migLayout4);
    createLabel(tabItem4, "Gets 33% of extra space", "");
    createLabel(tabItem4, "Gets 67% of extra space", "wrap");
    createTextField(tabItem4, "     ", "");
    createTextField(tabItem4, "     ", "");
    MigLayout migLayout5 = new MigLayout("flowy", "[]15[]", "[][c,pref!][c,grow 25,fill][c,grow 75,fill]");
    TabItem tabItem5 = createTabPanel(tabFolder, "Vertical 1", (Layout)migLayout5);
    createLabel(tabItem5, "Fixed", "skip");
    createLabel(tabItem5, "Gets 25% of extra space", "");
    createLabel(tabItem5, "Gets 75% of extra space", "wrap");
    createLabel(tabItem5, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
    createTextArea(tabItem5, "", "hmin 4*13");
    createTextArea(tabItem5, "", "hmin 4*13");
    createTextArea(tabItem5, "", "hmin 4*13");
    MigLayout migLayout6 = new MigLayout("flowy", "[]15[]", "[][c,grow 33,fill][c,grow 67,fill]");
    TabItem tabItem6 = createTabPanel(tabFolder, "Vertical 2", (Layout)migLayout6);
    createLabel(tabItem6, "Gets 33% of extra space", "skip");
    createLabel(tabItem6, "Gets 67% of extra space", "wrap");
    createLabel(tabItem6, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
    createTextArea(tabItem6, "", "hmin 4*13");
    createTextArea(tabItem6, "", "hmin 4*13");
    return (Control)tabFolder;
  }
  
  public Control createBasic_Sizes(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("", "[]15[75px]25[min]25[]", "[]15");
    TabItem tabItem1 = createTabPanel(tabFolder, "Horizontal - Column size set", (Layout)migLayout1);
    createLabel(tabItem1, "75px", "skip");
    createLabel(tabItem1, "Min", "");
    createLabel(tabItem1, "Pref", "wrap");
    createLabel(tabItem1, "new Text(15)", "");
    createTextField(tabItem1, "               ", "wmin 10");
    createTextField(tabItem1, "               ", "wmin 10");
    createTextField(tabItem1, "               ", "wmin 10");
    MigLayout migLayout2 = new MigLayout("flowy,wrap", "[]15[]", "[]15[c,45:45]15[c,min]15[c,pref]");
    TabItem tabItem2 = createTabPanel(tabFolder, "\"Vertical - Row sized\"", (Layout)migLayout2);
    createLabel(tabItem2, "45px", "skip");
    createLabel(tabItem2, "Min", "");
    createLabel(tabItem2, "Pref", "");
    createLabel(tabItem2, "new Text(SWT.MULTI)", "");
    createTextArea(tabItem2, "", "");
    createTextArea(tabItem2, "", "");
    createTextArea(tabItem2, "", "");
    MigLayout migLayout3 = new MigLayout("flowy,wrap", "[]15[]", "[]15[baseline]15[baseline]15[baseline]");
    TabItem tabItem3 = createTabPanel(tabFolder, "\"Vertical - Component sized + Baseline\"", (Layout)migLayout3);
    createLabel(tabItem3, "45px", "skip");
    createLabel(tabItem3, "Min", "");
    createLabel(tabItem3, "Pref", "");
    createLabel(tabItem3, "new Text(SWT.MULTI)", "");
    createTextArea(tabItem3, "", "height 45");
    createTextArea(tabItem3, "", "height min");
    createTextArea(tabItem3, "", "height pref");
    return (Control)tabFolder;
  }
  
  public Control createAlignments(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout1 = new MigLayout("wrap", "[label]15[left]15[center]15[right]15[fill]15[]", "[]15[]");
    String[] arrayOfString1 = { "[label]", "[left]", "[center]", "[right]", "[fill]", "[] (Default)" };
    TabItem tabItem1 = createTabPanel(tabFolder, "Horizontal", (Layout)migLayout1);
    String[] arrayOfString2 = { "First Name", "Phone Number", "Facsmile", "Email", "Address", "Other" };
    byte b1;
    for (b1 = 0; b1 < arrayOfString1.length; b1++)
      createLabel(tabItem1, arrayOfString1[b1], ""); 
    for (b1 = 0; b1 < arrayOfString1.length; b1++) {
      for (byte b = 0; b < arrayOfString2.length; b++) {
        if (b == 0) {
          createLabel(tabItem1, arrayOfString2[b1] + ":", "");
        } else {
          createButton(tabItem1, arrayOfString2[b1], "");
        } 
      } 
    } 
    MigLayout migLayout2 = new MigLayout("wrap,flowy", "[]unrel[]rel[]", "[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]");
    String[] arrayOfString3 = { "[top]", "[center]", "[bottom]", "[fill]", "[fill,baseline]", "[baseline]", "[] (Default)" };
    TabItem tabItem2 = createTabPanel(tabFolder, "Vertical", (Layout)migLayout2);
    byte b2;
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createLabel(tabItem2, arrayOfString3[b2], ""); 
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createButton(tabItem2, "A Button", ""); 
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createTextField(tabItem2, "JTextFied", ""); 
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createTextArea(tabItem2, "Text    ", ""); 
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createTextArea(tabItem2, "Text\nwith two lines", ""); 
    for (b2 = 0; b2 < arrayOfString3.length; b2++)
      createTextArea(tabItem2, "Scrolling Text\nwith two lines", ""); 
    return (Control)tabFolder;
  }
  
  public Control createQuick_Start(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout = new MigLayout("wrap", "[right][fill,sizegroup]unrel[right][fill,sizegroup]", "");
    TabItem tabItem = createTabPanel(tabFolder, "Quick Start", (Layout)migLayout);
    addSeparator(tabItem, "General");
    createLabel(tabItem, "Company", "gap indent");
    createTextField(tabItem, "", "span,growx");
    createLabel(tabItem, "Contact", "gap indent");
    createTextField(tabItem, "", "span,growx");
    addSeparator(tabItem, "Propeller");
    createLabel(tabItem, "PTI/kW", "gap indent");
    createTextField(tabItem, "", "wmin 130");
    createLabel(tabItem, "Power/kW", "gap indent");
    createTextField(tabItem, "", "wmin 130");
    createLabel(tabItem, "R/mm", "gap indent");
    createTextField(tabItem, "", "wmin 130");
    createLabel(tabItem, "D/mm", "gap indent");
    createTextField(tabItem, "", "wmin 130");
    return (Control)tabFolder;
  }
  
  public Control createGrow_Shrink(Composite paramComposite) {
    TabFolder tabFolder = new TabFolder(paramComposite, DOUBLE_BUFFER);
    MigLayout migLayout = new MigLayout("nogrid");
    TabItem tabItem1 = createTabPanel(tabFolder, "Shrink", (Layout)new FillLayout());
    SashForm sashForm1 = new SashForm(getComposite(tabItem1), 65792);
    sashForm1.setBackground(new Color((Device)display, 255, 255, 255));
    sashForm1.setBackgroundMode(2);
    Composite composite1 = createPanel(sashForm1, (Layout)migLayout, 2048);
    composite1.setLayoutData("wmin 100");
    createTextField(composite1, "shp 110", "shp 110,w 10:130");
    createTextField(composite1, "Default (100)", "w 10:130");
    createTextField(composite1, "shp 90", "shp 90,w 10:130");
    createTextField(composite1, "shrink 25", "newline,shrink 25,w 10:130");
    createTextField(composite1, "shrink 75", "shrink 75,w 10:130");
    createTextField(composite1, "Default", "newline, w 10:130");
    createTextField(composite1, "Default", "w 10:130");
    createTextField(composite1, "shrink 0", "newline,shrink 0,w 10:130");
    createTextField(composite1, "shp 110", "newline,shp 110,w 10:130");
    createTextField(composite1, "shp 100,shrink 25", "shp 100,shrink 25,w 10:130");
    createTextField(composite1, "shp 100,shrink 75", "shp 100,shrink 75,w 10:130");
    createTextArea(sashForm1, "Use the slider to see how the components shrink depending on the constraints set on them.\n\n'shp' means Shrink Priority. Lower values will be shrunk before higer ones and the default value is 100.\n\n'shrink' means Shrink Weight. Lower values relative to other's means they will shrink less when space is scarse. Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\n\nThe component's minimum size will always be honored.\n\nFor SWT, which doesn't have a component notion of minimum, preferred or maximum size, those sizes are set explicitly to minimum 10 and preferred 130 pixels.", "");
    TabItem tabItem2 = createTabPanel(tabFolder, "Grow", (Layout)new FillLayout());
    SashForm sashForm2 = new SashForm(getComposite(tabItem2), 65792);
    sashForm2.setBackground(new Color((Device)display, 255, 255, 255));
    sashForm2.setBackgroundMode(2);
    Composite composite2 = createPanel(sashForm2, (Layout)new MigLayout("nogrid", "[grow]"), 2048);
    composite2.setLayoutData("wmin 100");
    createButton(composite2, "gp 110, grow", "gp 110, grow, wmax 170");
    createButton(composite2, "Default (100), grow", "grow, wmax 170");
    createButton(composite2, "gp 90, grow", "gp 90, grow, wmax 170");
    createButton(composite2, "Default Button", "newline");
    createButton(composite2, "growx", "newline,growx,wrap");
    createButton(composite2, "gp 110, grow", "gp 110, grow, wmax 170");
    createButton(composite2, "gp 100, grow 25", "gp 100, grow 25, wmax 170");
    createButton(composite2, "gp 100, grow 75", "gp 100, grow 75, wmax 170");
    createTextArea(sashForm2, "'gp' means Grow Priority. Lower values will be grown before higher ones and the default value is 100.\n\n'grow' means Grow Weight. Higher values relative to other's means they will grow more when space is up for takes. Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means components will normally not grow. \n\nNote that the buttons in the first and last row have max width set to 170 to emphasize Grow Priority.\n\nThe component's maximum size will always be honored.", "");
    return (Control)tabFolder;
  }
  
  private Combo createCombo(Object paramObject1, String[] paramArrayOfString, Object paramObject2) {
    Combo combo = new Combo(getComposite(paramObject1), 4);
    for (byte b = 0; b < paramArrayOfString.length; b++)
      combo.add(paramArrayOfString[b]); 
    combo.setLayoutData(paramObject2);
    return combo;
  }
  
  private Label createLabel(Object paramObject1, String paramString, Object paramObject2) {
    return createLabel(paramObject1, paramString, paramObject2, 16384);
  }
  
  private Label createLabel(Object paramObject1, String paramString, Object paramObject2, int paramInt) {
    Label label = new Label(getComposite(paramObject1), paramInt | DOUBLE_BUFFER);
    label.setText(paramString);
    label.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    return label;
  }
  
  private Text createTextField(Object paramObject1, String paramString, Object paramObject2) {
    Text text = new Text(getComposite(paramObject1), 0x804 | DOUBLE_BUFFER);
    text.setText(paramString);
    text.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    return text;
  }
  
  private Button createButton(Object paramObject1, String paramString, Object paramObject2) {
    return createButton(getComposite(paramObject1), paramString, paramObject2, false);
  }
  
  private Button createButton(Object paramObject1, String paramString, Object paramObject2, boolean paramBoolean) {
    Button button = new Button(getComposite(paramObject1), 0x40008 | DOUBLE_BUFFER);
    button.setText((paramString.length() == 0) ? "\"\"" : paramString);
    button.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    return button;
  }
  
  private Composite createPanel(Object paramObject1, String paramString, Object paramObject2) {
    Color color = new Color((Device)display.getActiveShell().getDisplay(), 255, 255, 255);
    Composite composite = new Composite(getComposite(paramObject1), DOUBLE_BUFFER | 0x800);
    composite.setLayout((Layout)new MigLayout("fill"));
    composite.setBackground(color);
    composite.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    paramString = (paramString.length() == 0) ? "\"\"" : paramString;
    Label label = createLabel(composite, paramString, "grow", 17039360);
    label.setBackground(color);
    return composite;
  }
  
  private TabItem createTabPanel(TabFolder paramTabFolder, String paramString, Layout paramLayout) {
    Composite composite = new Composite((Composite)paramTabFolder, DOUBLE_BUFFER);
    TabItem tabItem = new TabItem(paramTabFolder, DOUBLE_BUFFER);
    tabItem.setControl((Control)composite);
    tabItem.setText(paramString);
    if (paramLayout != null)
      composite.setLayout(paramLayout); 
    return tabItem;
  }
  
  private Composite createPanel(Object paramObject, Layout paramLayout) {
    return createPanel(paramObject, paramLayout, 0);
  }
  
  private Composite createPanel(Object paramObject, Layout paramLayout, int paramInt) {
    Composite composite = new Composite(getComposite(paramObject), DOUBLE_BUFFER | paramInt);
    composite.setLayout(paramLayout);
    return composite;
  }
  
  private Button createToggleButton(Object paramObject1, String paramString, Object paramObject2) {
    Button button = new Button(getComposite(paramObject1), 0x2 | DOUBLE_BUFFER);
    button.setText((paramString.length() == 0) ? "\"\"" : paramString);
    button.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    return button;
  }
  
  private Button createCheck(Object paramObject1, String paramString, Object paramObject2) {
    Button button = new Button(getComposite(paramObject1), 0x20 | DOUBLE_BUFFER);
    button.setText(paramString);
    button.setLayoutData((paramObject2 != null) ? paramObject2 : paramString);
    return button;
  }
  
  private List createList(Object paramObject1, String paramString, Object paramObject2) {
    List list = new List(getComposite(paramObject1), DOUBLE_BUFFER | 0x800);
    list.add(paramString);
    list.setLayoutData(paramObject2);
    return list;
  }
  
  private StyledText createTextArea(Object paramObject, String paramString1, String paramString2) {
    return createTextArea(paramObject, paramString1, paramString2, 0x842 | DOUBLE_BUFFER);
  }
  
  private StyledText createTextArea(Object paramObject, String paramString1, String paramString2, int paramInt) {
    StyledText styledText = new StyledText(getComposite(paramObject), 0x42 | paramInt | DOUBLE_BUFFER);
    styledText.setText(paramString1);
    styledText.setLayoutData((paramString2 != null) ? paramString2 : paramString1);
    return styledText;
  }
  
  public Composite getComposite(Object paramObject) {
    return (paramObject instanceof Control) ? (Composite)paramObject : (Composite)((TabItem)paramObject).getControl();
  }
  
  private Font deriveFont(Control paramControl, int paramInt1, int paramInt2) {
    Font font1 = paramControl.getFont();
    FontData fontData = font1.getFontData()[0];
    if (paramInt1 != -1)
      fontData.setStyle(paramInt1); 
    if (paramInt2 != -1)
      fontData.setHeight(paramInt2); 
    Font font2 = new Font((Device)display, fontData);
    paramControl.setFont(font2);
    return font2;
  }
  
  private void addSeparator(Object paramObject, String paramString) {
    Label label1 = createLabel(paramObject, paramString, "gaptop para, span, split 2");
    label1.setForeground(new Color((Device)display, 0, 70, 213));
    Label label2 = new Label(getComposite(paramObject), 0x102 | DOUBLE_BUFFER);
    label2.setLayoutData("gapleft rel, gaptop para, growx");
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/demo/SwtDemo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */