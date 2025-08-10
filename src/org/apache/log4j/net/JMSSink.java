/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import javax.jms.Topic;
/*     */ import javax.jms.TopicConnection;
/*     */ import javax.jms.TopicConnectionFactory;
/*     */ import javax.jms.TopicSession;
/*     */ import javax.jms.TopicSubscriber;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PropertyConfigurator;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.xml.DOMConfigurator;
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
/*     */ public class JMSSink
/*     */   implements MessageListener
/*     */ {
/*  49 */   static Logger logger = Logger.getLogger(JMSSink.class);
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  52 */     if (args.length != 5) {
/*  53 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  56 */     String tcfBindingName = args[0];
/*  57 */     String topicBindingName = args[1];
/*  58 */     String username = args[2];
/*  59 */     String password = args[3];
/*     */     
/*  61 */     String configFile = args[4];
/*     */     
/*  63 */     if (configFile.endsWith(".xml")) {
/*  64 */       DOMConfigurator.configure(configFile);
/*     */     } else {
/*  66 */       PropertyConfigurator.configure(configFile);
/*     */     } 
/*     */     
/*  69 */     new JMSSink(tcfBindingName, topicBindingName, username, password);
/*     */     
/*  71 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/*     */     
/*  73 */     System.out.println("Type \"exit\" to quit JMSSink.");
/*     */     while (true) {
/*  75 */       String s = stdin.readLine();
/*  76 */       if (s.equalsIgnoreCase("exit")) {
/*  77 */         System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public JMSSink(String tcfBindingName, String topicBindingName, String username, String password) {
/*     */     try {
/*  86 */       Context ctx = new InitialContext();
/*     */       
/*  88 */       TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)JNDIUtil.lookupObject(ctx, tcfBindingName);
/*     */       
/*  90 */       TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
/*  91 */       topicConnection.start();
/*     */       
/*  93 */       TopicSession topicSession = topicConnection.createTopicSession(false, 1);
/*     */       
/*  95 */       Topic topic = (Topic)JNDIUtil.lookupObject(ctx, topicBindingName);
/*     */       
/*  97 */       TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
/*     */       
/*  99 */       topicSubscriber.setMessageListener(this);
/*     */     }
/* 101 */     catch (JMSException e) {
/* 102 */       logger.error("Could not read JMS message.", (Throwable)e);
/* 103 */     } catch (NamingException e) {
/* 104 */       logger.error("Could not read JMS message.", e);
/* 105 */     } catch (RuntimeException e) {
/* 106 */       logger.error("Could not read JMS message.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMessage(Message message) {
/*     */     try {
/* 115 */       if (message instanceof ObjectMessage) {
/* 116 */         ObjectMessage objectMessage = (ObjectMessage)message;
/* 117 */         LoggingEvent event = (LoggingEvent)objectMessage.getObject();
/* 118 */         Logger remoteLogger = Logger.getLogger(event.getLoggerName());
/* 119 */         remoteLogger.callAppenders(event);
/*     */       } else {
/* 121 */         logger.warn("Received message is of type " + message.getJMSType() + ", was expecting ObjectMessage.");
/*     */       } 
/* 123 */     } catch (JMSException jmse) {
/* 124 */       logger.error("Exception thrown while processing incoming message.", (Throwable)jmse);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/* 129 */     System.err.println(msg);
/* 130 */     System.err.println("Usage: java " + JMSSink.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password configFile");
/*     */     
/* 132 */     System.exit(1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/JMSSink.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */