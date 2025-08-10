package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class ExampleGood {
  protected void buildControls(Composite paramComposite) {
    paramComposite.setLayout((Layout)new MigLayout("inset 0", "[fill, grow]", "[fill, grow]"));
    Table table = new Table(paramComposite, 2816);
    table.setLayoutData("id table, hmin 100, wmin 300");
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    Label label = new Label(paramComposite, 2048);
    label.setText("Label Text");
    label.moveAbove(null);
    label.setLayoutData("pos 0 0");
    for (byte b = 0; b < 10; b++) {
      TableItem tableItem = new TableItem(table, 0);
      tableItem.setText("item #" + b);
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    Display display = new Display();
    Shell shell = new Shell(display);
    (new ExampleGood()).buildControls((Composite)shell);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep(); 
    } 
    display.dispose();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/ExampleGood.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */