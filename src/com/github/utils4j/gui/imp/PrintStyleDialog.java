/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.gui.IPrintStyleDialog;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.Optional;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import net.miginfocom.swing.MigLayout;
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
/*     */ public final class PrintStyleDialog
/*     */   extends SimpleDialog
/*     */   implements IPrintStyleDialog
/*     */ {
/*     */   private static final String TOOLTIP = "<html><style>.c{ color: 'blue';}</style><table><tr>  <td class='c'>1 <b>;</b> 2 <b>;</b> 5</td>  <td>Três arquivos: [1], [2] e [5]</td></tr><tr>  <td class='c'>10 <b>-</b> 13</td>  <td>Um arquivo: [10, 11, 12, 13]</td></tr><tr>  <td class='c'>7 <b>;</b> 9 <b>-</b> 12 <b>;</b> 21</td>  <td>Três arquivos: [7], [9, 10, 11, 12] e [21]</td></tr><tr>  <td class='c'>11 <b>-</b> *</td>  <td>Um arquivo: [11, 12, 13.... última página]</td></tr><tr>  <td class='c'>3 <b>;</b> 27 <b>-</b> *</td>  <td>Dois arquivos: [3], [27, 28... última página]</td></tr></html>";
/*  82 */   private static final Border RED_BORDER = BorderFactory.createLineBorder(Color.RED, 2);
/*     */   
/*     */   public static void show(Image icon) {
/*  85 */     SwingTools.invokeLater(() -> display(icon));
/*     */   }
/*     */   
/*     */   private static void display(Image icon) {
/*  89 */     (new PrintStyleDialog(icon)).display();
/*     */   }
/*     */   
/*     */   public static void main(String... args) {
/*  93 */     show((Image)null);
/*     */   }
/*     */   
/*  96 */   private final JButton okButton = new JButton("OK");
/*     */   
/*  98 */   private final JTextField textField = new JTextField();
/*     */   
/* 100 */   private final Border defaultBorder = this.textField.getBorder();
/*     */   
/*     */   public PrintStyleDialog(Image icon) {
/* 103 */     super("Escolha de páginas", icon, true);
/* 104 */     setLayout(new BorderLayout());
/* 105 */     add(north(), "North");
/* 106 */     add(center(), "Center");
/* 107 */     add(south(), "South");
/* 108 */     setupPages();
/* 109 */     setResizable(false);
/* 110 */     setAutoRequestFocus(true);
/* 111 */     setAlwaysOnTop(true);
/* 112 */     setDefaultCloseOperation(2);
/* 113 */     pack();
/* 114 */     toCenter();
/*     */   }
/*     */   
/*     */   private JPanel north() {
/* 118 */     JPanel panel = new JPanel((LayoutManager)new MigLayout("fillx"));
/* 119 */     JLabel label = new JLabel("Informe o intervalo de páginas:");
/* 120 */     panel.add(label, "wrap");
/* 121 */     panel.add(this.textField, "growx");
/* 122 */     return panel;
/*     */   }
/*     */   
/*     */   private JPanel center() {
/* 126 */     JPanel textPanel = new JPanel((LayoutManager)new MigLayout());
/* 127 */     JPanel panelHelp = new JPanel();
/* 128 */     panelHelp.setBorder(BorderFactory.createTitledBorder("Exemplos:"));
/* 129 */     panelHelp.add(new JLabel("<html><style>.c{ color: 'blue';}</style><table><tr>  <td class='c'>1 <b>;</b> 2 <b>;</b> 5</td>  <td>Três arquivos: [1], [2] e [5]</td></tr><tr>  <td class='c'>10 <b>-</b> 13</td>  <td>Um arquivo: [10, 11, 12, 13]</td></tr><tr>  <td class='c'>7 <b>;</b> 9 <b>-</b> 12 <b>;</b> 21</td>  <td>Três arquivos: [7], [9, 10, 11, 12] e [21]</td></tr><tr>  <td class='c'>11 <b>-</b> *</td>  <td>Um arquivo: [11, 12, 13.... última página]</td></tr><tr>  <td class='c'>3 <b>;</b> 27 <b>-</b> *</td>  <td>Dois arquivos: [3], [27, 28... última página]</td></tr></html>"));
/* 130 */     textPanel.add(panelHelp);
/* 131 */     return textPanel;
/*     */   }
/*     */   
/*     */   private JPanel south() {
/* 135 */     this.okButton.addActionListener(e -> onOk(e));
/* 136 */     JButton cancelButton = new JButton("Cancelar");
/* 137 */     cancelButton.addActionListener(e -> onEscPressed(e));
/* 138 */     this.okButton.setPreferredSize(cancelButton.getPreferredSize());
/* 139 */     JPanel southPane = new JPanel();
/* 140 */     southPane.setLayout((LayoutManager)new MigLayout("fillx", "push[][]", "[][]"));
/* 141 */     southPane.add(this.okButton);
/* 142 */     southPane.add(cancelButton);
/* 143 */     return southPane;
/*     */   }
/*     */   
/*     */   private void setupPages() {
/* 147 */     this.textField.setText(Strings.empty());
/* 148 */     this.textField.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e) {
/* 151 */             PrintStyleDialog.this.textField.setBorder(PrintStyleDialog.this.defaultBorder);
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/* 156 */             PrintStyleDialog.this.textField.setBorder(PrintStyleDialog.this.defaultBorder);
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/* 161 */             PrintStyleDialog.this.textField.setBorder(PrintStyleDialog.this.defaultBorder);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected boolean checkSyntax() {
/* 167 */     String text = Strings.trim(this.textField.getText());
/* 168 */     for (int i = 0; i < text.length(); i++) {
/* 169 */       char chr = text.charAt(i);
/* 170 */       if (!Character.isDigit(chr) && !Character.isWhitespace(chr))
/*     */       {
/* 172 */         if (chr != '-' && chr != ';' && chr != '*')
/* 173 */           return false; 
/*     */       }
/*     */     } 
/* 176 */     String[] parts = text.split(";");
/* 177 */     for (String part : parts) {
/* 178 */       if (!Strings.hasText(part)) {
/* 179 */         return false;
/*     */       }
/* 181 */       String[] pages = part.split("-");
/* 182 */       if (pages.length > 2) {
/* 183 */         return false;
/*     */       }
/* 185 */       int previous = 0;
/* 186 */       for (String page : pages) {
/* 187 */         if ((page = Strings.trim(page)).length() == 0) {
/* 188 */           return false;
/*     */         }
/* 190 */         int p = "*".equals(page) ? Integer.MAX_VALUE : Strings.toInt(page, -1);
/* 191 */         if (p < previous || p == 0) {
/* 192 */           return false;
/*     */         }
/* 194 */         previous = p;
/*     */       } 
/*     */     } 
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {
/* 202 */     setAlwaysOnTop(false);
/* 203 */     Dialogs.Choice choice = Dialogs.getBoolean("Deseja mesmo cancelar a operação?", "Cancelamento da operação", false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (choice == Dialogs.Choice.YES) {
/* 209 */       this.textField.setText(Strings.empty());
/* 210 */       close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void onOk(ActionEvent e) {
/* 216 */     Optional<String> fileName = Strings.optional(Strings.trim(this.textField.getText()));
/* 217 */     if (fileName.isPresent() && checkSyntax()) {
/* 218 */       close();
/*     */     } else {
/* 220 */       this.textField.setBorder(RED_BORDER);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> getPagesInterval() {
/* 226 */     setVisible(true);
/* 227 */     return Strings.optional(this.textField.getText());
/*     */   }
/*     */ 
/*     */   
/*     */   private void display() {
/* 232 */     toCenter();
/* 233 */     setVisible(true);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/PrintStyleDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */