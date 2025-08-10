/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressHandler;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Stack;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ abstract class ProgressHandler<T extends ProgressHandler<T>>
/*     */   extends JPanel
/*     */   implements IProgressHandler<T>
/*     */ {
/*  62 */   private static final Logger LOGGER = LoggerFactory.getLogger(IProgressHandler.class);
/*     */   
/*  64 */   private final JTextArea textArea = new JTextArea();
/*     */   
/*  66 */   private final Stack<ProgressState> stackState = new Stack();
/*     */   
/*  68 */   private final Map<Thread, List<Runnable>> cancelCodes = new HashMap<>(2);
/*     */   
/*  70 */   private long lineNumber = 0L;
/*     */   private volatile boolean disposed;
/*  72 */   private volatile boolean canceled = this.disposed = false;
/*     */   
/*  74 */   protected final JScrollPane scrollPane = new JScrollPane();
/*     */   
/*  76 */   protected final JProgressBar progressBar = new JProgressBar();
/*     */   
/*  78 */   private final BehaviorSubject<Boolean> cancelClick = BehaviorSubject.create();
/*     */   
/*  80 */   protected final BehaviorSubject<Boolean> detailStatus = BehaviorSubject.create();
/*     */   
/*     */   protected ProgressHandler() {
/*  83 */     setupLayout();
/*     */   }
/*     */   
/*     */   protected void onCancel(ActionEvent e) {
/*  87 */     Dialogs.Choice choice = Dialogs.getBoolean("Deseja mesmo cancelar esta a operação?", "Cancelamento da operação", false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     if (choice == Dialogs.Choice.YES) {
/*  93 */       yesCancel();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void yesCancel() {
/*  98 */     cancel();
/*     */   }
/*     */   
/*     */   private final void setupLayout() {
/* 102 */     setupScroll();
/* 103 */     resetProgress();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Boolean> detailStatus() {
/* 108 */     return (Observable<Boolean>)this.detailStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Boolean> cancelClick() {
/* 113 */     return (Observable<Boolean>)this.cancelClick;
/*     */   }
/*     */   
/*     */   private final void setupScroll() {
/* 117 */     this.textArea.setRows(8);
/* 118 */     this.textArea.setColumns(10);
/* 119 */     this.textArea.setEditable(false);
/* 120 */     this.textArea.getCaret().setDot(2147483647);
/* 121 */     this.scrollPane.setViewportView(this.textArea);
/*     */   }
/*     */   
/*     */   protected final void onClear(ActionEvent e) {
/* 125 */     this.textArea.setText("");
/*     */   }
/*     */ 
/*     */   
/*     */   public void showSteps(boolean visible) {
/* 130 */     SwingTools.invokeLater(() -> this.scrollPane.setVisible(visible));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStepsVisible() {
/* 135 */     return this.scrollPane.isVisible();
/*     */   }
/*     */   
/*     */   private void resetProgress() {
/* 139 */     this.textArea.setText("");
/* 140 */     this.progressBar.setIndeterminate(false);
/* 141 */     this.progressBar.setMaximum(0);
/* 142 */     this.progressBar.setMinimum(0);
/* 143 */     this.progressBar.setValue(-1);
/* 144 */     this.progressBar.setStringPainted(true);
/* 145 */     this.progressBar.setString("");
/* 146 */     this.stackState.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCanceled() {
/* 151 */     return this.canceled;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void dispose() {
/* 156 */     if (!this.disposed) {
/* 157 */       this.disposed = true;
/* 158 */       this.cancelCodes.clear();
/*     */ 
/*     */       
/* 161 */       SwingTools.invokeLater(this::resetProgress);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void stepToken(IStepEvent e) {
/*     */     String log;
/* 167 */     Args.requireNonNull(e, "step event is null");
/* 168 */     int step = e.getStep();
/* 169 */     int total = e.getTotal();
/* 170 */     String message = e.getMessage();
/* 171 */     boolean indeterminated = e.isIndeterminated();
/* 172 */     StringBuilder text = Strings.computeTabs(e.getStackSize());
/*     */     
/* 174 */     if (indeterminated || e.isInfo()) {
/* 175 */       log = text.append(message).toString();
/*     */     } else {
/* 177 */       log = text.append(String.format("Passo %s de %s: %s", new Object[] { Integer.valueOf(step), Integer.valueOf(total), message })).toString();
/*     */     } 
/* 179 */     LOGGER.info(log);
/* 180 */     SwingTools.invokeLater(() -> {
/*     */           if (!indeterminated) {
/*     */             this.progressBar.setValue(step);
/*     */           }
/*     */           if (this.lineNumber++ > 800L) {
/*     */             this.textArea.setText(Strings.empty());
/*     */             this.lineNumber = 0L;
/*     */           } 
/*     */           this.textArea.append(log + "\n\r");
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stageToken(IStageEvent e) {
/* 194 */     Args.requireNonNull(e, "stage event is null");
/* 195 */     StringBuilder tabSize = Strings.computeTabs(e.getStackSize());
/* 196 */     String message = e.getMessage();
/* 197 */     String text = tabSize.append(message).toString();
/* 198 */     LOGGER.info(text);
/* 199 */     SwingTools.invokeLater(() -> {
/*     */           if (e.isStart()) {
/*     */             this.stackState.push(new ProgressState(this.progressBar));
/*     */           }
/*     */           boolean indeterminated = e.isIndeterminated();
/*     */           this.progressBar.setIndeterminate(indeterminated);
/*     */           if (!indeterminated) {
/*     */             this.progressBar.setMaximum(e.getTotal());
/*     */             this.progressBar.setMinimum(0);
/*     */             this.progressBar.setValue(e.getStep());
/*     */           } 
/*     */           this.progressBar.setString(message);
/*     */           this.textArea.append(text + "\n\r");
/*     */           if (e.isEnd() && !this.stackState.isEmpty()) {
/*     */             ((ProgressState)this.stackState.pop()).restore(this.progressBar);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public final synchronized void cancel() {
/* 219 */     if (!this.canceled) {
/* 220 */       this.canceled = true;
/* 221 */       Map<Thread, List<Runnable>> copy = new HashMap<>(this.cancelCodes);
/* 222 */       this.cancelCodes.clear();
/* 223 */       Runnable interrupt = () -> copy.entrySet().stream().peek(()).map(Map.Entry::getValue).flatMap(Collection::stream).forEach(());
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
/* 234 */       SwingTools.invokeLater(interrupt);
/* 235 */       this.cancelClick.onNext(Boolean.valueOf(true));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void cancelCode(Runnable code) throws InterruptedException {
/* 242 */     Args.requireNonNull(code, "cancelCode is null");
/* 243 */     if (isCanceled())
/* 244 */       throw new InterruptedException("Este progresso já foi cancelado"); 
/* 245 */     bind(Thread.currentThread(), code);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void bind(Thread thread) {
/* 250 */     Args.requireNonNull(thread, "thread is null");
/* 251 */     bind(thread, () -> {
/*     */         
/*     */         });
/*     */   }
/*     */   public synchronized boolean isFrom(Thread thread) {
/* 256 */     return (this.cancelCodes.get(thread) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private final synchronized void bind(Thread thread, Runnable code) {
/* 261 */     List<Runnable> codes = this.cancelCodes.get(thread);
/* 262 */     if (codes == null)
/* 263 */       this.cancelCodes.put(thread, codes = new ArrayList<>(2)); 
/* 264 */     codes.add(code);
/*     */   }
/*     */   protected abstract void setMode(Mode paramMode);
/*     */   protected abstract Mode getMode();
/*     */   
/*     */   private static class ProgressState { private final String message;
/*     */     private final int maximum;
/*     */     
/*     */     private ProgressState(JProgressBar bar) {
/* 273 */       this.value = bar.getValue();
/* 274 */       this.message = bar.getString();
/* 275 */       this.maximum = bar.getMaximum();
/* 276 */       this.minimum = bar.getMinimum();
/* 277 */       this.indeterminated = bar.isIndeterminate();
/*     */     }
/*     */     private final int minimum; private final int value; private final boolean indeterminated;
/*     */     private void restore(JProgressBar bar) {
/* 281 */       bar.setValue(this.value);
/* 282 */       bar.setString(this.message);
/* 283 */       bar.setMaximum(this.maximum);
/* 284 */       bar.setMinimum(this.minimum);
/* 285 */       bar.setIndeterminate(this.indeterminated);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */