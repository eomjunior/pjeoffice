package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class SwtTest {
  public static void main(String[] paramArrayOfString) {
    Display display = new Display();
    Shell shell = new Shell();
    Composite composite = new Composite((Composite)shell, 536870912);
    shell.setLayout((Layout)new FillLayout());
    MigLayout migLayout = new MigLayout("debug,wrap 2");
    composite.setLayout((Layout)migLayout);
    Label label1 = new Label(composite, 64);
    label1.setText("This is an even longer label that just goes on and on...");
    label1.setLayoutData("wmin 50");
    Label label2 = new Label(composite, 0);
    label2.setText("Label 2");
    label2 = new Label(composite, 0);
    label2.setText("Label 3");
    label2 = new Label(composite, 0);
    label2.setText("Label 4");
    shell.setSize(300, 300);
    shell.open();
    shell.layout();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep(); 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/examples/SwtTest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */