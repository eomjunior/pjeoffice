/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.gui.IThrowableTracker;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowStateListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.EtchedBorder;
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
/*     */ public final class ExceptionAlert
/*     */   extends SimpleFrame
/*     */ {
/*     */   public static void show(String message, Throwable cause) {
/*  68 */     show(message, Strings.empty(), cause);
/*     */   }
/*     */   
/*     */   public static void show(String message, String detail, Throwable cause) {
/*  72 */     show((Image)null, message, detail, cause);
/*     */   }
/*     */   
/*     */   public static void show(Image icon, String message, String detail, Throwable cause) {
/*  76 */     show(icon, message, detail, cause, IThrowableTracker.NOTHING);
/*     */   }
/*     */   
/*     */   public static void show(String message, String detail, Throwable cause, IThrowableTracker tracker) {
/*  80 */     show((Image)null, message, detail, cause, tracker);
/*     */   }
/*     */   
/*     */   public static void show(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
/*  84 */     SwingTools.invokeLater(() -> display(icon, message, detail, cause, tracker));
/*     */   }
/*     */   
/*     */   private static void display(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
/*  88 */     (new ExceptionAlert(icon, message, detail, cause, tracker)).display();
/*     */   }
/*     */   
/*  91 */   private static final Dimension MININUM_SIZE = new Dimension(420, 160);
/*     */   
/*  93 */   private final JPanel southPane = new JPanel();
/*     */   
/*  95 */   private final JTextArea textArea = new JTextArea();
/*     */   
/*  97 */   private final JScrollPane centerPane = new JScrollPane();
/*     */   
/*  99 */   private final JLabel detailLabel = new JLabel("<html><u>Ver detalhes</u></html>");
/*     */   
/*     */   private ExceptionAlert(Image icon, String message, String detail, Throwable cause, IThrowableTracker tracker) {
/* 102 */     super("Mensagem de erro", icon);
/* 103 */     setupLayout(message, detail, cause, tracker);
/* 104 */     setupListeners();
/* 105 */     setDefaultCloseOperation(2);
/* 106 */     setFixedMinimumSize(MININUM_SIZE);
/* 107 */     setAutoRequestFocus(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEscPressed(ActionEvent e) {
/* 112 */     super.onEscPressed(e);
/*     */   }
/*     */   
/*     */   private void display() {
/* 116 */     toCenter();
/* 117 */     showToFront();
/*     */   }
/*     */   
/*     */   private void setupLayout(String message, String detail, Throwable cause, IThrowableTracker tracker) {
/* 121 */     JPanel contentPane = new JPanel();
/* 122 */     contentPane.setBorder(new EtchedBorder(1, null, null));
/* 123 */     contentPane.setLayout(new BorderLayout(0, 0));
/* 124 */     contentPane.add(north(message), "North");
/* 125 */     contentPane.add(center(detail, cause, tracker), "Center");
/* 126 */     contentPane.add(south(), "South");
/* 127 */     setContentPane(contentPane);
/*     */   }
/*     */   
/*     */   private JScrollPane center(String detail, Throwable cause, IThrowableTracker tracker) {
/* 131 */     this.textArea.setRows(8);
/* 132 */     this.textArea.setEditable(false);
/* 133 */     this.centerPane.setViewportView(this.textArea);
/* 134 */     this.centerPane.setVisible(false);
/* 135 */     this.textArea.setText(causes(detail, cause, tracker));
/* 136 */     this.textArea.setCaretPosition(0);
/* 137 */     return this.centerPane;
/*     */   }
/*     */   
/*     */   private JPanel south() {
/* 141 */     JButton okButton = new JButton("OK");
/* 142 */     okButton.addActionListener(this::onEscPressed);
/* 143 */     this.southPane.setLayout((LayoutManager)new MigLayout("center"));
/* 144 */     this.southPane.add(okButton);
/* 145 */     this.southPane.setVisible(false);
/* 146 */     return this.southPane;
/*     */   }
/*     */   
/*     */   private void setDetail(JLabel seeDetailsPane) {
/* 150 */     boolean show = seeDetailsPane.getText().contains("Ver");
/* 151 */     setExtendedState(show ? 6 : 0);
/* 152 */     this.centerPane.setVisible(show);
/* 153 */     this.southPane.setVisible(show);
/*     */   }
/*     */   
/*     */   private void setupListeners() {
/* 157 */     addWindowStateListener(new WindowStateListener() {
/*     */           public void windowStateChanged(WindowEvent e) {
/* 159 */             if ((e.getNewState() & 0x6) == 6) {
/* 160 */               ExceptionAlert.this.detailLabel.setText("<html><u>Esconder detalhes</u></html>");
/* 161 */             } else if ((e.getNewState() & 0x0) == 0) {
/* 162 */               ExceptionAlert.this.detailLabel.setText("<html><u>Ver detalhes</u></html>");
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void setDetailLabel() {
/* 169 */     this.detailLabel.setVerticalAlignment(3);
/* 170 */     this.detailLabel.setCursor(Cursor.getPredefinedCursor(12));
/* 171 */     this.detailLabel.setHorizontalAlignment(0);
/* 172 */     this.detailLabel.setVerticalAlignment(0);
/* 173 */     this.detailLabel.setForeground(Color.BLUE);
/* 174 */     this.detailLabel.setFont(new Font("Tahoma", 0, 12));
/* 175 */     this.detailLabel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 177 */             ExceptionAlert.this.setDetail(ExceptionAlert.this.detailLabel);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private JPanel north(String message) {
/* 183 */     JLabel errorLabel = new JLabel("<html>&nbsp;" + Strings.trim(message) + "</html>");
/* 184 */     errorLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
/* 185 */     errorLabel.setFont(new Font("Tahoma", 0, 15));
/* 186 */     errorLabel.setHorizontalAlignment(2);
/*     */     
/* 188 */     setDetailLabel();
/*     */     
/* 190 */     JPanel detailPanel = new JPanel();
/* 191 */     detailPanel.setLayout((LayoutManager)new MigLayout("center"));
/* 192 */     detailPanel.add(this.detailLabel);
/*     */     
/* 194 */     JPanel northPane = new JPanel();
/* 195 */     northPane.setLayout((LayoutManager)new MigLayout());
/* 196 */     northPane.add(errorLabel, "wrap");
/* 197 */     northPane.add(detailPanel, "pushx, growx");
/* 198 */     return northPane;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String causes(String detail, Throwable cause, IThrowableTracker tracker) {
/* 204 */     StringBuilder sb = (new StringBuilder()).append(detail = Strings.trim(detail)).append((detail.length() > 0) ? "\n\n" : Strings.empty());
/* 205 */     String[] messages = IThrowableTracker.orNothing(tracker).track(cause);
/* 206 */     int size = messages.length;
/* 207 */     if (size > 0) {
/* 208 */       sb.append("IDENTIFICADA(S) A(S) SEGUINTE(S) CAUSA(S):\n");
/* 209 */       int i = 0, n = 1;
/*     */       while (true) {
/* 211 */         sb.append("\t")
/* 212 */           .append(Strings.padStart(n++, 2))
/* 213 */           .append(") ")
/* 214 */           .append(messages[i])
/* 215 */           .append('\n');
/* 216 */         if (++i >= size)
/* 217 */         { sb.append("\n********************************************************************\n"); break; } 
/*     */       } 
/* 219 */     }  sb.append("INFORMAÇÕES TÉCNICAS:\n")
/* 220 */       .append(Throwables.stackTrace(cause));
/* 221 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/ExceptionAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */