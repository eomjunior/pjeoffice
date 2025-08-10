/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.cfg.CoercionAction;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.IgnorePropertiesUtil;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BeanDeserializer
/*      */   extends BeanDeserializerBase
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   protected transient Exception _nullFromCreator;
/*      */   private volatile transient NameTransformer _currentlyTransforming;
/*      */   
/*      */   @Deprecated
/*      */   public BeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
/*   59 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, (Set<String>)null, hasViews);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, Set<String> includableProps, boolean hasViews) {
/*   73 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, includableProps, hasViews);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src) {
/*   82 */     super(src, src._ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src, boolean ignoreAllUnknown) {
/*   86 */     super(src, ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializer(BeanDeserializerBase src, NameTransformer unwrapper) {
/*   90 */     super(src, unwrapper);
/*      */   }
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, ObjectIdReader oir) {
/*   94 */     super(src, oir);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public BeanDeserializer(BeanDeserializerBase src, Set<String> ignorableProps) {
/*  102 */     super(src, ignorableProps);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, Set<String> ignorableProps, Set<String> includableProps) {
/*  109 */     super(src, ignorableProps, includableProps);
/*      */   }
/*      */   
/*      */   public BeanDeserializer(BeanDeserializerBase src, BeanPropertyMap props) {
/*  113 */     super(src, props);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer transformer) {
/*  121 */     if (getClass() != BeanDeserializer.class) {
/*  122 */       return (JsonDeserializer<Object>)this;
/*      */     }
/*      */ 
/*      */     
/*  126 */     if (this._currentlyTransforming == transformer) {
/*  127 */       return (JsonDeserializer<Object>)this;
/*      */     }
/*  129 */     this._currentlyTransforming = transformer;
/*      */     
/*  131 */     try { return (JsonDeserializer<Object>)new BeanDeserializer(this, transformer); }
/*  132 */     finally { this._currentlyTransforming = null; }
/*      */   
/*      */   }
/*      */   
/*      */   public BeanDeserializer withObjectIdReader(ObjectIdReader oir) {
/*  137 */     return new BeanDeserializer(this, oir);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDeserializer withByNameInclusion(Set<String> ignorableProps, Set<String> includableProps) {
/*  143 */     return new BeanDeserializer(this, ignorableProps, includableProps);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase withIgnoreAllUnknown(boolean ignoreUnknown) {
/*  148 */     return new BeanDeserializer(this, ignoreUnknown);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props) {
/*  153 */     return new BeanDeserializer(this, props);
/*      */   }
/*      */ 
/*      */   
/*      */   protected BeanDeserializerBase asArrayDeserializer() {
/*  158 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/*  159 */     return (BeanDeserializerBase)new BeanAsArrayDeserializer(this, props);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  175 */     if (p.isExpectedStartObjectToken()) {
/*  176 */       if (this._vanillaProcessing) {
/*  177 */         return vanillaDeserialize(p, ctxt, p.nextToken());
/*      */       }
/*      */ 
/*      */       
/*  181 */       p.nextToken();
/*  182 */       if (this._objectIdReader != null) {
/*  183 */         return deserializeWithObjectId(p, ctxt);
/*      */       }
/*  185 */       return deserializeFromObject(p, ctxt);
/*      */     } 
/*  187 */     return _deserializeOther(p, ctxt, p.currentToken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _deserializeOther(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/*  194 */     if (t != null) {
/*  195 */       switch (t) {
/*      */         case AsEmpty:
/*  197 */           return deserializeFromString(p, ctxt);
/*      */         case AsNull:
/*  199 */           return deserializeFromNumber(p, ctxt);
/*      */         case TryConvert:
/*  201 */           return deserializeFromDouble(p, ctxt);
/*      */         case null:
/*  203 */           return deserializeFromEmbedded(p, ctxt);
/*      */         case null:
/*      */         case null:
/*  206 */           return deserializeFromBoolean(p, ctxt);
/*      */         case null:
/*  208 */           return deserializeFromNull(p, ctxt);
/*      */         
/*      */         case null:
/*  211 */           return _deserializeFromArray(p, ctxt);
/*      */         case null:
/*      */         case null:
/*  214 */           if (this._vanillaProcessing) {
/*  215 */             return vanillaDeserialize(p, ctxt, t);
/*      */           }
/*  217 */           if (this._objectIdReader != null) {
/*  218 */             return deserializeWithObjectId(p, ctxt);
/*      */           }
/*  220 */           return deserializeFromObject(p, ctxt);
/*      */       } 
/*      */     
/*      */     }
/*  224 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected Object _missingToken(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  229 */     throw ctxt.endOfInputException(handledType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*      */     String propName;
/*  241 */     p.setCurrentValue(bean);
/*  242 */     if (this._injectables != null) {
/*  243 */       injectValues(ctxt, bean);
/*      */     }
/*  245 */     if (this._unwrappedPropertyHandler != null) {
/*  246 */       return deserializeWithUnwrapped(p, ctxt, bean);
/*      */     }
/*  248 */     if (this._externalTypeIdHandler != null) {
/*  249 */       return deserializeWithExternalTypeId(p, ctxt, bean);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  254 */     if (p.isExpectedStartObjectToken()) {
/*  255 */       propName = p.nextFieldName();
/*  256 */       if (propName == null) {
/*  257 */         return bean;
/*      */       }
/*      */     }
/*  260 */     else if (p.hasTokenId(5)) {
/*  261 */       propName = p.currentName();
/*      */     } else {
/*  263 */       return bean;
/*      */     } 
/*      */     
/*  266 */     if (this._needViewProcesing) {
/*  267 */       Class<?> view = ctxt.getActiveView();
/*  268 */       if (view != null) {
/*  269 */         return deserializeWithView(p, ctxt, bean, view);
/*      */       }
/*      */     } 
/*      */     while (true) {
/*  273 */       p.nextToken();
/*  274 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*      */       
/*  276 */       if (prop != null) {
/*      */         try {
/*  278 */           prop.deserializeAndSet(p, ctxt, bean);
/*  279 */         } catch (Exception e) {
/*  280 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } else {
/*      */         
/*  284 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*  285 */       }  if ((propName = p.nextFieldName()) == null) {
/*  286 */         return bean;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t) throws IOException {
/*  303 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */     
/*  305 */     p.setCurrentValue(bean);
/*  306 */     if (p.hasTokenId(5)) {
/*  307 */       String propName = p.currentName();
/*      */       do {
/*  309 */         p.nextToken();
/*  310 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*      */         
/*  312 */         if (prop != null)
/*      */         { try {
/*  314 */             prop.deserializeAndSet(p, ctxt, bean);
/*  315 */           } catch (Exception e) {
/*  316 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }  }
/*      */         else
/*      */         
/*  320 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  321 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  323 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  339 */     if (this._objectIdReader != null && this._objectIdReader.maySerializeAsObject() && 
/*  340 */       p.hasTokenId(5) && this._objectIdReader
/*  341 */       .isValidReferencePropertyName(p.currentName(), p)) {
/*  342 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/*  345 */     if (this._nonStandardCreation) {
/*  346 */       if (this._unwrappedPropertyHandler != null) {
/*  347 */         return deserializeWithUnwrapped(p, ctxt);
/*      */       }
/*  349 */       if (this._externalTypeIdHandler != null) {
/*  350 */         return deserializeWithExternalTypeId(p, ctxt);
/*      */       }
/*  352 */       Object object = deserializeFromObjectUsingNonDefault(p, ctxt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  365 */       return object;
/*      */     } 
/*  367 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */     
/*  369 */     p.setCurrentValue(bean);
/*  370 */     if (p.canReadObjectId()) {
/*  371 */       Object id = p.getObjectId();
/*  372 */       if (id != null) {
/*  373 */         _handleTypedObjectId(p, ctxt, bean, id);
/*      */       }
/*      */     } 
/*  376 */     if (this._injectables != null) {
/*  377 */       injectValues(ctxt, bean);
/*      */     }
/*  379 */     if (this._needViewProcesing) {
/*  380 */       Class<?> view = ctxt.getActiveView();
/*  381 */       if (view != null) {
/*  382 */         return deserializeWithView(p, ctxt, bean, view);
/*      */       }
/*      */     } 
/*  385 */     if (p.hasTokenId(5)) {
/*  386 */       String propName = p.currentName();
/*      */       do {
/*  388 */         p.nextToken();
/*  389 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  390 */         if (prop != null)
/*      */         { try {
/*  392 */             prop.deserializeAndSet(p, ctxt, bean);
/*  393 */           } catch (Exception e) {
/*  394 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }  }
/*      */         else
/*      */         
/*  398 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  399 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  401 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     Object bean;
/*  416 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*  417 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*  418 */     TokenBuffer unknown = null;
/*  419 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*      */     
/*  421 */     JsonToken t = p.currentToken();
/*  422 */     List<BeanReferring> referrings = null;
/*  423 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  424 */       String propName = p.currentName();
/*  425 */       p.nextToken();
/*  426 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*      */       
/*  428 */       if (!buffer.readIdProperty(propName) || creatorProp != null)
/*      */       {
/*      */ 
/*      */         
/*  432 */         if (creatorProp != null) {
/*      */ 
/*      */           
/*  435 */           if (activeView != null && !creatorProp.visibleInView(activeView)) {
/*  436 */             p.skipChildren();
/*      */           } else {
/*      */             
/*  439 */             Object value = _deserializeWithErrorWrapping(p, ctxt, creatorProp);
/*  440 */             if (buffer.assignParameter(creatorProp, value)) {
/*  441 */               Object object; p.nextToken();
/*      */               
/*      */               try {
/*  444 */                 object = creator.build(ctxt, buffer);
/*  445 */               } catch (Exception e) {
/*  446 */                 object = wrapInstantiationProblem(e, ctxt);
/*      */               } 
/*  448 */               if (object == null) {
/*  449 */                 return ctxt.handleInstantiationProblem(handledType(), null, 
/*  450 */                     _creatorReturnedNullException());
/*      */               }
/*      */               
/*  453 */               p.setCurrentValue(object);
/*      */ 
/*      */               
/*  456 */               if (object.getClass() != this._beanType.getRawClass()) {
/*  457 */                 return handlePolymorphic(p, ctxt, object, unknown);
/*      */               }
/*  459 */               if (unknown != null) {
/*  460 */                 object = handleUnknownProperties(ctxt, object, unknown);
/*      */               }
/*      */               
/*  463 */               return deserialize(p, ctxt, object);
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/*  468 */           SettableBeanProperty prop = this._beanProperties.find(propName);
/*  469 */           if (prop != null) {
/*      */             try {
/*  471 */               buffer.bufferProperty(prop, _deserializeWithErrorWrapping(p, ctxt, prop));
/*  472 */             } catch (UnresolvedForwardReference reference) {
/*      */ 
/*      */ 
/*      */               
/*  476 */               BeanReferring referring = handleUnresolvedReference(ctxt, prop, buffer, reference);
/*      */               
/*  478 */               if (referrings == null) {
/*  479 */                 referrings = new ArrayList<>();
/*      */               }
/*  481 */               referrings.add(referring);
/*      */             
/*      */             }
/*      */           
/*      */           }
/*  486 */           else if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/*  487 */             handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */ 
/*      */           
/*      */           }
/*  491 */           else if (this._anySetter != null) {
/*      */             try {
/*  493 */               buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*  494 */             } catch (Exception e) {
/*  495 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */ 
/*      */             
/*      */             }
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*  503 */           else if (this._ignoreAllUnknown) {
/*      */             
/*  505 */             p.skipChildren();
/*      */           }
/*      */           else {
/*      */             
/*  509 */             if (unknown == null) {
/*  510 */               unknown = ctxt.bufferForInputBuffering(p);
/*      */             }
/*  512 */             unknown.writeFieldName(propName);
/*  513 */             unknown.copyCurrentStructure(p);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */     try {
/*  519 */       bean = creator.build(ctxt, buffer);
/*  520 */     } catch (Exception e) {
/*  521 */       wrapInstantiationProblem(e, ctxt);
/*  522 */       bean = null;
/*      */     } 
/*      */     
/*  525 */     if (this._injectables != null) {
/*  526 */       injectValues(ctxt, bean);
/*      */     }
/*      */     
/*  529 */     if (referrings != null) {
/*  530 */       for (BeanReferring referring : referrings) {
/*  531 */         referring.setBean(bean);
/*      */       }
/*      */     }
/*  534 */     if (unknown != null) {
/*      */       
/*  536 */       if (bean.getClass() != this._beanType.getRawClass()) {
/*  537 */         return handlePolymorphic((JsonParser)null, ctxt, bean, unknown);
/*      */       }
/*      */       
/*  540 */       return handleUnknownProperties(ctxt, bean, unknown);
/*      */     } 
/*  542 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BeanReferring handleUnresolvedReference(DeserializationContext ctxt, SettableBeanProperty prop, PropertyValueBuffer buffer, UnresolvedForwardReference reference) throws JsonMappingException {
/*  554 */     BeanReferring referring = new BeanReferring(ctxt, reference, prop.getType(), buffer, prop);
/*  555 */     reference.getRoid().appendReferring(referring);
/*  556 */     return referring;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object _deserializeWithErrorWrapping(JsonParser p, DeserializationContext ctxt, SettableBeanProperty prop) throws IOException {
/*      */     try {
/*  564 */       return prop.deserialize(p, ctxt);
/*  565 */     } catch (Exception e) {
/*  566 */       wrapAndThrow(e, this._beanType.getRawClass(), prop.getName(), ctxt);
/*      */       
/*  568 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeFromNull(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  586 */     if (p.requiresCustomCodec()) {
/*      */       
/*  588 */       TokenBuffer tb = ctxt.bufferForInputBuffering(p);
/*  589 */       tb.writeEndObject();
/*  590 */       JsonParser p2 = tb.asParser(p);
/*  591 */       p2.nextToken();
/*      */ 
/*      */       
/*  594 */       Object ob = this._vanillaProcessing ? vanillaDeserialize(p2, ctxt, JsonToken.END_OBJECT) : deserializeFromObject(p2, ctxt);
/*  595 */       p2.close();
/*  596 */       return ob;
/*      */     } 
/*  598 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  605 */     JsonDeserializer<Object> delegateDeser = this._arrayDelegateDeserializer;
/*      */     
/*  607 */     if (delegateDeser != null || (delegateDeser = this._delegateDeserializer) != null) {
/*  608 */       Object bean = this._valueInstantiator.createUsingArrayDelegate(ctxt, delegateDeser
/*  609 */           .deserialize(p, ctxt));
/*  610 */       if (this._injectables != null) {
/*  611 */         injectValues(ctxt, bean);
/*      */       }
/*  613 */       return bean;
/*      */     } 
/*  615 */     CoercionAction act = _findCoercionFromEmptyArray(ctxt);
/*  616 */     boolean unwrap = ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
/*      */     
/*  618 */     if (unwrap || act != CoercionAction.Fail) {
/*  619 */       JsonToken t = p.nextToken();
/*  620 */       if (t == JsonToken.END_ARRAY) {
/*  621 */         switch (act) {
/*      */           case AsEmpty:
/*  623 */             return getEmptyValue(ctxt);
/*      */           case AsNull:
/*      */           case TryConvert:
/*  626 */             return getNullValue(ctxt);
/*      */         } 
/*      */         
/*  629 */         return ctxt.handleUnexpectedToken(getValueType(ctxt), JsonToken.START_ARRAY, p, null, new Object[0]);
/*      */       } 
/*  631 */       if (unwrap) {
/*      */ 
/*      */         
/*  634 */         if (p.nextToken() == JsonToken.START_ARRAY) {
/*  635 */           JavaType targetType = getValueType(ctxt);
/*  636 */           return ctxt.handleUnexpectedToken(targetType, JsonToken.START_ARRAY, p, "Cannot deserialize value of type %s from deeply-nested JSON Array: only single wrapper allowed with `%s`", new Object[] {
/*      */                 
/*  638 */                 ClassUtil.getTypeDescription(targetType), "DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS"
/*      */               });
/*      */         } 
/*  641 */         Object value = deserialize(p, ctxt);
/*  642 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/*  643 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  645 */         return value;
/*      */       } 
/*      */     } 
/*  648 */     return ctxt.handleUnexpectedToken(getValueType(ctxt), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object deserializeWithView(JsonParser p, DeserializationContext ctxt, Object bean, Class<?> activeView) throws IOException {
/*  661 */     if (p.hasTokenId(5)) {
/*  662 */       String propName = p.currentName();
/*      */       do {
/*  664 */         p.nextToken();
/*      */         
/*  666 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/*  667 */         if (prop != null)
/*  668 */         { if (!prop.visibleInView(activeView)) {
/*  669 */             p.skipChildren();
/*      */           } else {
/*      */             
/*      */             try {
/*  673 */               prop.deserializeAndSet(p, ctxt, bean);
/*  674 */             } catch (Exception e) {
/*  675 */               wrapAndThrow(e, bean, propName, ctxt);
/*      */             } 
/*      */           }  }
/*      */         else
/*  679 */         { handleUnknownVanilla(p, ctxt, bean, propName); } 
/*  680 */       } while ((propName = p.nextFieldName()) != null);
/*      */     } 
/*  682 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  699 */     if (this._delegateDeserializer != null) {
/*  700 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*      */     }
/*  702 */     if (this._propertyBasedCreator != null) {
/*  703 */       return deserializeUsingPropertyBasedWithUnwrapped(p, ctxt);
/*      */     }
/*  705 */     TokenBuffer tokens = ctxt.bufferForInputBuffering(p);
/*  706 */     tokens.writeStartObject();
/*  707 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*      */ 
/*      */     
/*  710 */     p.setCurrentValue(bean);
/*      */     
/*  712 */     if (this._injectables != null) {
/*  713 */       injectValues(ctxt, bean);
/*      */     }
/*  715 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  716 */     String propName = p.hasTokenId(5) ? p.currentName() : null;
/*      */     
/*  718 */     for (; propName != null; propName = p.nextFieldName()) {
/*  719 */       p.nextToken();
/*  720 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  721 */       if (prop != null) {
/*  722 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  723 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  727 */             prop.deserializeAndSet(p, ctxt, bean);
/*  728 */           } catch (Exception e) {
/*  729 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  734 */       } else if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/*  735 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  742 */       else if (this._anySetter == null) {
/*      */         
/*  744 */         tokens.writeFieldName(propName);
/*  745 */         tokens.copyCurrentStructure(p);
/*      */       }
/*      */       else {
/*      */         
/*  749 */         TokenBuffer b2 = ctxt.bufferAsCopyOfValue(p);
/*  750 */         tokens.writeFieldName(propName);
/*  751 */         tokens.append(b2);
/*      */         try {
/*  753 */           this._anySetter.deserializeAndSet(b2.asParserOnFirstToken(), ctxt, bean, propName);
/*  754 */         } catch (Exception e) {
/*  755 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } 
/*  758 */     }  tokens.writeEndObject();
/*  759 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*  760 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*  768 */     JsonToken t = p.currentToken();
/*  769 */     if (t == JsonToken.START_OBJECT) {
/*  770 */       t = p.nextToken();
/*      */     }
/*  772 */     TokenBuffer tokens = ctxt.bufferForInputBuffering(p);
/*  773 */     tokens.writeStartObject();
/*  774 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  775 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  776 */       String propName = p.currentName();
/*  777 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  778 */       p.nextToken();
/*  779 */       if (prop != null) {
/*  780 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  781 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  785 */             prop.deserializeAndSet(p, ctxt, bean);
/*  786 */           } catch (Exception e) {
/*  787 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         } 
/*  791 */       } else if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/*  792 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  799 */       else if (this._anySetter == null) {
/*      */         
/*  801 */         tokens.writeFieldName(propName);
/*  802 */         tokens.copyCurrentStructure(p);
/*      */       } else {
/*      */         
/*  805 */         TokenBuffer b2 = ctxt.bufferAsCopyOfValue(p);
/*  806 */         tokens.writeFieldName(propName);
/*  807 */         tokens.append(b2);
/*      */         try {
/*  809 */           this._anySetter.deserializeAndSet(b2.asParserOnFirstToken(), ctxt, bean, propName);
/*  810 */         } catch (Exception e) {
/*  811 */           wrapAndThrow(e, bean, propName, ctxt);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  816 */     tokens.writeEndObject();
/*  817 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*  818 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     Object bean;
/*  829 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*  830 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*      */     
/*  832 */     TokenBuffer tokens = ctxt.bufferForInputBuffering(p);
/*  833 */     tokens.writeStartObject();
/*      */     
/*  835 */     JsonToken t = p.currentToken();
/*  836 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  837 */       String propName = p.currentName();
/*  838 */       p.nextToken();
/*      */       
/*  840 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*      */       
/*  842 */       if (!buffer.readIdProperty(propName) || creatorProp != null)
/*      */       {
/*      */         
/*  845 */         if (creatorProp != null) {
/*      */           
/*  847 */           if (buffer.assignParameter(creatorProp, 
/*  848 */               _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
/*  849 */             Object object; t = p.nextToken();
/*      */             
/*      */             try {
/*  852 */               object = creator.build(ctxt, buffer);
/*  853 */             } catch (Exception e) {
/*  854 */               object = wrapInstantiationProblem(e, ctxt);
/*      */             } 
/*      */             
/*  857 */             p.setCurrentValue(object);
/*      */             
/*  859 */             while (t == JsonToken.FIELD_NAME) {
/*      */               
/*  861 */               tokens.copyCurrentStructure(p);
/*  862 */               t = p.nextToken();
/*      */             } 
/*      */ 
/*      */             
/*  866 */             if (t != JsonToken.END_OBJECT)
/*  867 */               ctxt.reportWrongTokenException((JsonDeserializer)this, JsonToken.END_OBJECT, "Attempted to unwrap '%s' value", new Object[] {
/*      */                     
/*  869 */                     handledType().getName()
/*      */                   }); 
/*  871 */             tokens.writeEndObject();
/*  872 */             if (object.getClass() != this._beanType.getRawClass()) {
/*      */ 
/*      */               
/*  875 */               ctxt.reportInputMismatch((BeanProperty)creatorProp, "Cannot create polymorphic instances with unwrapped values", new Object[0]);
/*      */               
/*  877 */               return null;
/*      */             } 
/*  879 */             return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, object, tokens);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  884 */           SettableBeanProperty prop = this._beanProperties.find(propName);
/*  885 */           if (prop != null) {
/*  886 */             buffer.bufferProperty(prop, _deserializeWithErrorWrapping(p, ctxt, prop));
/*      */ 
/*      */           
/*      */           }
/*  890 */           else if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/*  891 */             handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*  898 */           else if (this._anySetter == null) {
/*      */             
/*  900 */             tokens.writeFieldName(propName);
/*  901 */             tokens.copyCurrentStructure(p);
/*      */           } else {
/*      */             
/*  904 */             TokenBuffer b2 = ctxt.bufferAsCopyOfValue(p);
/*  905 */             tokens.writeFieldName(propName);
/*  906 */             tokens.append(b2);
/*      */             try {
/*  908 */               buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter
/*  909 */                   .deserialize(b2.asParserOnFirstToken(), ctxt));
/*  910 */             } catch (Exception e) {
/*  911 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  920 */       bean = creator.build(ctxt, buffer);
/*  921 */     } catch (Exception e) {
/*  922 */       wrapInstantiationProblem(e, ctxt);
/*  923 */       return null;
/*      */     } 
/*  925 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  938 */     if (this._propertyBasedCreator != null) {
/*  939 */       return deserializeUsingPropertyBasedWithExternalTypeId(p, ctxt);
/*      */     }
/*  941 */     if (this._delegateDeserializer != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  947 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer
/*  948 */           .deserialize(p, ctxt));
/*      */     }
/*      */     
/*  951 */     return deserializeWithExternalTypeId(p, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
/*  958 */     return _deserializeWithExternalTypeId(p, ctxt, bean, this._externalTypeIdHandler
/*  959 */         .start());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean, ExternalTypeHandler ext) throws IOException {
/*  966 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*  967 */     for (JsonToken t = p.currentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/*  968 */       String propName = p.currentName();
/*  969 */       t = p.nextToken();
/*  970 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*  971 */       if (prop != null) {
/*      */         
/*  973 */         if (t.isScalarValue()) {
/*  974 */           ext.handleTypePropertyValue(p, ctxt, propName, bean);
/*      */         }
/*  976 */         if (activeView != null && !prop.visibleInView(activeView)) {
/*  977 */           p.skipChildren();
/*      */         } else {
/*      */           
/*      */           try {
/*  981 */             prop.deserializeAndSet(p, ctxt, bean);
/*  982 */           } catch (Exception e) {
/*  983 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  988 */       } else if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/*  989 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*      */ 
/*      */       
/*      */       }
/*  993 */       else if (!ext.handlePropertyValue(p, ctxt, propName, bean)) {
/*      */ 
/*      */ 
/*      */         
/*  997 */         if (this._anySetter != null) {
/*      */           try {
/*  999 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/* 1000 */           } catch (Exception e) {
/* 1001 */             wrapAndThrow(e, bean, propName, ctxt);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1006 */           handleUnknownProperty(p, ctxt, bean, propName);
/*      */         } 
/*      */       } 
/* 1009 */     }  return ext.complete(p, ctxt, bean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1017 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/* 1018 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 1019 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/* 1020 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/*      */     
/* 1022 */     JsonToken t = p.currentToken();
/* 1023 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 1024 */       String propName = p.currentName();
/* 1025 */       t = p.nextToken();
/*      */       
/* 1027 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/*      */       
/* 1029 */       if (!buffer.readIdProperty(propName) || creatorProp != null)
/*      */       {
/*      */         
/* 1032 */         if (creatorProp != null) {
/*      */ 
/*      */ 
/*      */           
/* 1036 */           if (!ext.handlePropertyValue(p, ctxt, propName, null))
/*      */           {
/*      */ 
/*      */             
/* 1040 */             if (buffer.assignParameter(creatorProp, 
/* 1041 */                 _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
/* 1042 */               Object bean; t = p.nextToken();
/*      */               
/*      */               try {
/* 1045 */                 bean = creator.build(ctxt, buffer);
/* 1046 */               } catch (Exception e) {
/* 1047 */                 wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/*      */               } 
/*      */               
/* 1050 */               if (bean.getClass() != this._beanType.getRawClass())
/*      */               {
/*      */                 
/* 1053 */                 return ctxt.reportBadDefinition(this._beanType, String.format("Cannot create polymorphic instances with external type ids (%s -> %s)", new Object[] { this._beanType, bean
/*      */                         
/* 1055 */                         .getClass() }));
/*      */               }
/* 1057 */               return _deserializeWithExternalTypeId(p, ctxt, bean, ext);
/*      */             }
/*      */           
/*      */           }
/*      */         } else {
/*      */           
/* 1063 */           SettableBeanProperty prop = this._beanProperties.find(propName);
/* 1064 */           if (prop != null) {
/*      */             
/* 1066 */             if (t.isScalarValue()) {
/* 1067 */               ext.handleTypePropertyValue(p, ctxt, propName, null);
/*      */             }
/*      */             
/* 1070 */             if (activeView != null && !prop.visibleInView(activeView)) {
/* 1071 */               p.skipChildren();
/*      */             } else {
/* 1073 */               buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1078 */           else if (!ext.handlePropertyValue(p, ctxt, propName, null)) {
/*      */ 
/*      */ 
/*      */             
/* 1082 */             if (IgnorePropertiesUtil.shouldIgnore(propName, this._ignorableProps, this._includableProps)) {
/* 1083 */               handleIgnoredProperty(p, ctxt, handledType(), propName);
/*      */ 
/*      */             
/*      */             }
/* 1087 */             else if (this._anySetter != null) {
/* 1088 */               buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter
/* 1089 */                   .deserialize(p, ctxt));
/*      */             }
/*      */             else {
/*      */               
/* 1093 */               handleUnknownProperty(p, ctxt, this._valueClass, propName);
/*      */             } 
/*      */           } 
/*      */         }  } 
/*      */     }  try {
/* 1098 */       return ext.complete(p, ctxt, buffer, creator);
/* 1099 */     } catch (Exception e) {
/* 1100 */       return wrapInstantiationProblem(e, ctxt);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Exception _creatorReturnedNullException() {
/* 1111 */     if (this._nullFromCreator == null) {
/* 1112 */       this._nullFromCreator = new NullPointerException("JSON Creator returned null");
/*      */     }
/* 1114 */     return this._nullFromCreator;
/*      */   }
/*      */ 
/*      */   
/*      */   static class BeanReferring
/*      */     extends ReadableObjectId.Referring
/*      */   {
/*      */     private final DeserializationContext _context;
/*      */     
/*      */     private final SettableBeanProperty _prop;
/*      */     
/*      */     private Object _bean;
/*      */ 
/*      */     
/*      */     BeanReferring(DeserializationContext ctxt, UnresolvedForwardReference ref, JavaType valueType, PropertyValueBuffer buffer, SettableBeanProperty prop) {
/* 1129 */       super(ref, valueType);
/* 1130 */       this._context = ctxt;
/* 1131 */       this._prop = prop;
/*      */     }
/*      */     
/*      */     public void setBean(Object bean) {
/* 1135 */       this._bean = bean;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 1141 */       if (this._bean == null) {
/* 1142 */         this._context.reportInputMismatch((BeanProperty)this._prop, "Cannot resolve ObjectId forward reference using property '%s' (of type %s): Bean not yet resolved", new Object[] { this._prop
/*      */               
/* 1144 */               .getName(), this._prop.getDeclaringClass().getName() });
/*      */       }
/* 1146 */       this._prop.set(this._bean, value);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/BeanDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */