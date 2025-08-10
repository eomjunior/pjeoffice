/*     */ package com.github.signer4j.gui;
/*     */ 
/*     */ import com.github.signer4j.IGadget;
/*     */ import com.github.signer4j.IPasswordCallbackHandler;
/*     */ import com.github.signer4j.IPasswordCollector;
/*     */ import com.github.signer4j.imp.Config;
/*     */ import com.github.signer4j.imp.ResponseCallback;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.GuiTools;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.util.function.Supplier;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPasswordField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PasswordDialogCallbackHandler
/*     */   implements IPasswordCallbackHandler
/*     */ {
/*     */   private static final String DEFAULT_PIN_TITLE = "Informe a senha";
/*     */   protected final String title;
/*     */   protected final IGadget gadget;
/*     */   private final IPasswordCollector collector;
/*     */   
/*     */   public PasswordDialogCallbackHandler(IGadget gadget) {
/*  66 */     this(gadget, IPasswordCollector.NOTHING, "Informe a senha");
/*     */   }
/*     */   
/*     */   public PasswordDialogCallbackHandler(IGadget gadget, IPasswordCollector collector) {
/*  70 */     this(gadget, collector, "Informe a senha");
/*     */   }
/*     */   
/*     */   public PasswordDialogCallbackHandler(IGadget gadget, IPasswordCollector collector, String title) {
/*  74 */     this.gadget = (IGadget)Args.requireNonNull(gadget, "token is null");
/*  75 */     this.collector = (IPasswordCollector)Args.requireNonNull(collector, "collector is null");
/*  76 */     this.title = Strings.trim(title, "Informe a senha");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ResponseCallback doHandle(PasswordCallback callback) {
/*     */     // Byte code:
/*     */     //   0: invokestatic currentThread : ()Ljava/lang/Thread;
/*     */     //   3: invokevirtual isInterrupted : ()Z
/*     */     //   6: ifeq -> 13
/*     */     //   9: getstatic com/github/signer4j/imp/ResponseCallback.CANCEL : Lcom/github/signer4j/imp/ResponseCallback;
/*     */     //   12: areturn
/*     */     //   13: aload_0
/*     */     //   14: invokevirtual createDialog : ()Lcom/github/signer4j/gui/PasswordDialogCallbackHandler$DefaultPasswordDialog;
/*     */     //   17: astore_2
/*     */     //   18: aconst_null
/*     */     //   19: astore_3
/*     */     //   20: iconst_0
/*     */     //   21: invokestatic valueOf : (I)Ljava/lang/Integer;
/*     */     //   24: astore #4
/*     */     //   26: aload_2
/*     */     //   27: invokestatic access$000 : (Lcom/github/signer4j/gui/PasswordDialogCallbackHandler$DefaultPasswordDialog;)V
/*     */     //   30: aload #4
/*     */     //   32: aload_2
/*     */     //   33: invokestatic access$100 : (Lcom/github/signer4j/gui/PasswordDialogCallbackHandler$DefaultPasswordDialog;)Ljava/lang/Object;
/*     */     //   36: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   39: ifne -> 80
/*     */     //   42: getstatic com/github/signer4j/imp/ResponseCallback.CANCEL : Lcom/github/signer4j/imp/ResponseCallback;
/*     */     //   45: astore #5
/*     */     //   47: aload_2
/*     */     //   48: ifnull -> 77
/*     */     //   51: aload_3
/*     */     //   52: ifnull -> 73
/*     */     //   55: aload_2
/*     */     //   56: invokevirtual close : ()V
/*     */     //   59: goto -> 77
/*     */     //   62: astore #6
/*     */     //   64: aload_3
/*     */     //   65: aload #6
/*     */     //   67: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   70: goto -> 77
/*     */     //   73: aload_2
/*     */     //   74: invokevirtual close : ()V
/*     */     //   77: aload #5
/*     */     //   79: areturn
/*     */     //   80: aload_2
/*     */     //   81: invokestatic access$200 : (Lcom/github/signer4j/gui/PasswordDialogCallbackHandler$DefaultPasswordDialog;)[C
/*     */     //   84: astore #5
/*     */     //   86: aload #5
/*     */     //   88: arraylength
/*     */     //   89: ifle -> 147
/*     */     //   92: aload_1
/*     */     //   93: aload #5
/*     */     //   95: invokevirtual setPassword : ([C)V
/*     */     //   98: aload_0
/*     */     //   99: getfield collector : Lcom/github/signer4j/IPasswordCollector;
/*     */     //   102: aload #5
/*     */     //   104: invokeinterface collect : ([C)V
/*     */     //   109: getstatic com/github/signer4j/imp/ResponseCallback.OK : Lcom/github/signer4j/imp/ResponseCallback;
/*     */     //   112: astore #6
/*     */     //   114: aload_2
/*     */     //   115: ifnull -> 144
/*     */     //   118: aload_3
/*     */     //   119: ifnull -> 140
/*     */     //   122: aload_2
/*     */     //   123: invokevirtual close : ()V
/*     */     //   126: goto -> 144
/*     */     //   129: astore #7
/*     */     //   131: aload_3
/*     */     //   132: aload #7
/*     */     //   134: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   137: goto -> 144
/*     */     //   140: aload_2
/*     */     //   141: invokevirtual close : ()V
/*     */     //   144: aload #6
/*     */     //   146: areturn
/*     */     //   147: goto -> 26
/*     */     //   150: astore #4
/*     */     //   152: aload #4
/*     */     //   154: astore_3
/*     */     //   155: aload #4
/*     */     //   157: athrow
/*     */     //   158: astore #8
/*     */     //   160: aload_2
/*     */     //   161: ifnull -> 190
/*     */     //   164: aload_3
/*     */     //   165: ifnull -> 186
/*     */     //   168: aload_2
/*     */     //   169: invokevirtual close : ()V
/*     */     //   172: goto -> 190
/*     */     //   175: astore #9
/*     */     //   177: aload_3
/*     */     //   178: aload #9
/*     */     //   180: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   183: goto -> 190
/*     */     //   186: aload_2
/*     */     //   187: invokevirtual close : ()V
/*     */     //   190: aload #8
/*     */     //   192: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #81	-> 0
/*     */     //   #82	-> 9
/*     */     //   #84	-> 13
/*     */     //   #85	-> 20
/*     */     //   #86	-> 26
/*     */     //   #87	-> 30
/*     */     //   #88	-> 42
/*     */     //   #96	-> 47
/*     */     //   #88	-> 77
/*     */     //   #89	-> 80
/*     */     //   #90	-> 86
/*     */     //   #91	-> 92
/*     */     //   #92	-> 98
/*     */     //   #93	-> 109
/*     */     //   #96	-> 114
/*     */     //   #93	-> 144
/*     */     //   #95	-> 147
/*     */     //   #84	-> 150
/*     */     //   #96	-> 158
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   86	61	5	password	[C
/*     */     //   26	124	4	ok	Ljava/lang/Integer;
/*     */     //   18	175	2	dialog	Lcom/github/signer4j/gui/PasswordDialogCallbackHandler$DefaultPasswordDialog;
/*     */     //   0	193	0	this	Lcom/github/signer4j/gui/PasswordDialogCallbackHandler;
/*     */     //   0	193	1	callback	Ljavax/security/auth/callback/PasswordCallback;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   20	47	150	java/lang/Throwable
/*     */     //   20	47	158	finally
/*     */     //   55	59	62	java/lang/Throwable
/*     */     //   80	114	150	java/lang/Throwable
/*     */     //   80	114	158	finally
/*     */     //   122	126	129	java/lang/Throwable
/*     */     //   147	150	150	java/lang/Throwable
/*     */     //   147	160	158	finally
/*     */     //   168	172	175	java/lang/Throwable
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultPasswordDialog createDialog() {
/* 100 */     JPasswordField passwordField = new JPasswordField();
/* 101 */     JComponent[] components = new JComponent[5];
/* 102 */     components[0] = new JLabel(String.format("Token: %s - Modelo: %s - %s", new Object[] { this.gadget
/* 103 */             .getLabel(), this.gadget
/* 104 */             .getCategory(), this.gadget
/* 105 */             .getModel() }));
/*     */     
/* 107 */     components[1] = new JLabel(String.format("Fabricante: %s ", new Object[] { this.gadget.getManufacturer() }));
/* 108 */     components[2] = new JLabel("Número de série: " + this.gadget.getSerial());
/* 109 */     components[3] = new JLabel("Senha/PIN: ");
/* 110 */     components[4] = passwordField;
/* 111 */     JOptionPane panel = new JOptionPane(components, 3, 2);
/*     */ 
/*     */ 
/*     */     
/* 115 */     return new DefaultPasswordDialog(passwordField, panel::getValue, panel.createDialog(this.title));
/*     */   }
/*     */ 
/*     */   
/*     */   protected class DefaultPasswordDialog
/*     */     implements AutoCloseable
/*     */   {
/*     */     private JDialog dialog;
/*     */     private final Supplier<Object> chooser;
/*     */     private final JPasswordField passwordField;
/*     */     
/*     */     protected DefaultPasswordDialog(final JPasswordField passwordField, Supplier<Object> chooser, JDialog dialog) {
/* 127 */       this.passwordField = passwordField;
/* 128 */       this.chooser = chooser;
/* 129 */       this.dialog = dialog;
/* 130 */       this.dialog.setIconImage(Config.getIcon());
/* 131 */       this.dialog.setAlwaysOnTop(true);
/* 132 */       this.dialog.addComponentListener(new ComponentAdapter()
/*     */           {
/*     */             public void componentShown(ComponentEvent e) {
/* 135 */               passwordField.requestFocus();
/*     */             }
/*     */           });
/* 138 */       GuiTools.mouseTracker(this.dialog);
/*     */     }
/*     */     
/*     */     private void showOnMousePointer() {
/* 142 */       if (this.dialog != null) {
/* 143 */         GuiTools.showOnMousePointer(this.dialog);
/*     */       }
/*     */     }
/*     */     
/*     */     private Object getChoice() {
/* 148 */       return this.chooser.get();
/*     */     }
/*     */     
/*     */     private char[] getPassword() {
/* 152 */       return this.passwordField.getPassword();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 157 */       if (this.dialog != null) {
/* 158 */         Throwables.quietly(this.dialog::dispose);
/* 159 */         this.dialog = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/PasswordDialogCallbackHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */