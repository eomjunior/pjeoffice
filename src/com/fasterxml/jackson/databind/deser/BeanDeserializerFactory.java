/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*      */ import com.fasterxml.jackson.annotation.JsonIncludeProperties;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*      */ import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
/*      */ import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
/*      */ import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
/*      */ import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class BeanDeserializerFactory extends BasicDeserializerFactory implements Serializable {
/*   40 */   private static final Class<?>[] INIT_CAUSE_PARAMS = new Class[] { Throwable.class };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   52 */   public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
/*      */ 
/*      */   
/*      */   public BeanDeserializerFactory(DeserializerFactoryConfig config) {
/*   56 */     super(config);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
/*   67 */     if (this._factoryConfig == config) {
/*   68 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   76 */     ClassUtil.verifyMustOverride(BeanDeserializerFactory.class, this, "withConfig");
/*   77 */     return new BeanDeserializerFactory(config);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*   97 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*   99 */     JsonDeserializer<?> deser = _findCustomBeanDeserializer(type, config, beanDesc);
/*  100 */     if (deser != null) {
/*      */       
/*  102 */       if (this._factoryConfig.hasDeserializerModifiers()) {
/*  103 */         for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  104 */           deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*      */         }
/*      */       }
/*  107 */       return (JsonDeserializer)deser;
/*      */     } 
/*      */ 
/*      */     
/*  111 */     if (type.isThrowable()) {
/*  112 */       return buildThrowableDeserializer(ctxt, type, beanDesc);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  119 */     if (type.isAbstract() && !type.isPrimitive() && !type.isEnumType()) {
/*      */       
/*  121 */       JavaType concreteType = materializeAbstractType(ctxt, type, beanDesc);
/*  122 */       if (concreteType != null) {
/*      */ 
/*      */         
/*  125 */         beanDesc = config.introspect(concreteType);
/*  126 */         return buildBeanDeserializer(ctxt, concreteType, beanDesc);
/*      */       } 
/*      */     } 
/*      */     
/*  130 */     deser = findStdDeserializer(ctxt, type, beanDesc);
/*  131 */     if (deser != null) {
/*  132 */       return (JsonDeserializer)deser;
/*      */     }
/*      */ 
/*      */     
/*  136 */     if (!isPotentialBeanType(type.getRawClass())) {
/*  137 */       return null;
/*      */     }
/*      */     
/*  140 */     _validateSubType(ctxt, type, beanDesc);
/*      */ 
/*      */ 
/*      */     
/*  144 */     deser = _findUnsupportedTypeDeserializer(ctxt, type, beanDesc);
/*  145 */     if (deser != null) {
/*  146 */       return (JsonDeserializer)deser;
/*      */     }
/*      */ 
/*      */     
/*  150 */     return buildBeanDeserializer(ctxt, type, beanDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription valueBeanDesc, Class<?> builderClass) throws JsonMappingException {
/*      */     JavaType builderType;
/*  161 */     if (ctxt.isEnabled(MapperFeature.INFER_BUILDER_TYPE_BINDINGS)) {
/*  162 */       builderType = ctxt.getTypeFactory().constructParametricType(builderClass, valueType.getBindings());
/*      */     } else {
/*  164 */       builderType = ctxt.constructType(builderClass);
/*      */     } 
/*  166 */     BeanDescription builderDesc = ctxt.getConfig().introspectForBuilder(builderType, valueBeanDesc);
/*      */ 
/*      */     
/*  169 */     return buildBuilderBasedDeserializer(ctxt, valueType, builderDesc);
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
/*      */   protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*  182 */     JsonDeserializer<?> deser = findDefaultDeserializer(ctxt, type, beanDesc);
/*      */     
/*  184 */     if (deser != null && 
/*  185 */       this._factoryConfig.hasDeserializerModifiers()) {
/*  186 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  187 */         deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*      */       }
/*      */     }
/*      */     
/*  191 */     return deser;
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
/*      */   
/*      */   protected JsonDeserializer<Object> _findUnsupportedTypeDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*  209 */     String errorMsg = BeanUtil.checkUnsupportedType(type);
/*  210 */     if (errorMsg != null)
/*      */     {
/*      */       
/*  213 */       if (ctxt.getConfig().findMixInClassFor(type.getRawClass()) == null) {
/*  214 */         return (JsonDeserializer<Object>)new UnsupportedTypeDeserializer(type, errorMsg);
/*      */       }
/*      */     }
/*  217 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JavaType materializeAbstractType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*  225 */     for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
/*  226 */       JavaType concrete = r.resolveAbstractType(ctxt.getConfig(), beanDesc);
/*  227 */       if (concrete != null) {
/*  228 */         return concrete;
/*      */       }
/*      */     } 
/*  231 */     return null;
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
/*      */   public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     ValueInstantiator valueInstantiator;
/*      */     JsonDeserializer<?> deserializer;
/*      */     try {
/*  261 */       valueInstantiator = findValueInstantiator(ctxt, beanDesc);
/*  262 */     } catch (NoClassDefFoundError error) {
/*  263 */       return (JsonDeserializer<Object>)new ErrorThrowingDeserializer(error);
/*  264 */     } catch (IllegalArgumentException e0) {
/*      */ 
/*      */ 
/*      */       
/*  268 */       throw InvalidDefinitionException.from(ctxt.getParser(), 
/*  269 */           ClassUtil.exceptionMessage(e0), beanDesc, null)
/*      */         
/*  271 */         .withCause(e0);
/*      */     } 
/*  273 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/*  274 */     builder.setValueInstantiator(valueInstantiator);
/*      */     
/*  276 */     addBeanProps(ctxt, beanDesc, builder);
/*  277 */     addObjectIdReader(ctxt, beanDesc, builder);
/*      */ 
/*      */     
/*  280 */     addBackReferenceProperties(ctxt, beanDesc, builder);
/*  281 */     addInjectables(ctxt, beanDesc, builder);
/*      */     
/*  283 */     DeserializationConfig config = ctxt.getConfig();
/*  284 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  285 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  286 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  291 */     if (type.isAbstract() && !valueInstantiator.canInstantiate()) {
/*  292 */       deserializer = builder.buildAbstract();
/*      */     } else {
/*  294 */       deserializer = builder.build();
/*      */     } 
/*      */ 
/*      */     
/*  298 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  299 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  300 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*      */       }
/*      */     }
/*  303 */     return (JsonDeserializer)deserializer;
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
/*      */   protected JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription builderDesc) throws JsonMappingException {
/*      */     ValueInstantiator valueInstantiator;
/*      */     try {
/*  321 */       valueInstantiator = findValueInstantiator(ctxt, builderDesc);
/*  322 */     } catch (NoClassDefFoundError error) {
/*  323 */       return (JsonDeserializer<Object>)new ErrorThrowingDeserializer(error);
/*  324 */     } catch (IllegalArgumentException e) {
/*      */ 
/*      */ 
/*      */       
/*  328 */       throw InvalidDefinitionException.from(ctxt.getParser(), 
/*  329 */           ClassUtil.exceptionMessage(e), builderDesc, null);
/*      */     } 
/*      */     
/*  332 */     DeserializationConfig config = ctxt.getConfig();
/*  333 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
/*  334 */     builder.setValueInstantiator(valueInstantiator);
/*      */     
/*  336 */     addBeanProps(ctxt, builderDesc, builder);
/*  337 */     addObjectIdReader(ctxt, builderDesc, builder);
/*      */ 
/*      */     
/*  340 */     addBackReferenceProperties(ctxt, builderDesc, builder);
/*  341 */     addInjectables(ctxt, builderDesc, builder);
/*      */     
/*  343 */     JsonPOJOBuilder.Value builderConfig = builderDesc.findPOJOBuilderConfig();
/*      */     
/*  345 */     String buildMethodName = (builderConfig == null) ? "build" : builderConfig.buildMethodName;
/*      */ 
/*      */     
/*  348 */     AnnotatedMethod buildMethod = builderDesc.findMethod(buildMethodName, null);
/*  349 */     if (buildMethod != null && 
/*  350 */       config.canOverrideAccessModifiers()) {
/*  351 */       ClassUtil.checkAndFixAccess(buildMethod.getMember(), config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */     }
/*      */     
/*  354 */     builder.setPOJOBuilder(buildMethod, builderConfig);
/*      */     
/*  356 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  357 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  358 */         builder = mod.updateBuilder(config, builderDesc, builder);
/*      */       }
/*      */     }
/*  361 */     JsonDeserializer<?> deserializer = builder.buildBuilderBased(valueType, buildMethodName);
/*      */ 
/*      */ 
/*      */     
/*  365 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  366 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  367 */         deserializer = mod.modifyDeserializer(config, builderDesc, deserializer);
/*      */       }
/*      */     }
/*  370 */     return (JsonDeserializer)deserializer;
/*      */   }
/*      */   
/*      */   protected void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*      */     JavaType idType;
/*      */     SettableBeanProperty idProp;
/*      */     ObjectIdGenerator<?> gen;
/*  377 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/*  378 */     if (objectIdInfo == null) {
/*      */       return;
/*      */     }
/*  381 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  386 */     ObjectIdResolver resolver = ctxt.objectIdResolverInstance((Annotated)beanDesc.getClassInfo(), objectIdInfo);
/*      */ 
/*      */     
/*  389 */     if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/*  390 */       PropertyName propName = objectIdInfo.getPropertyName();
/*  391 */       idProp = builder.findProperty(propName);
/*  392 */       if (idProp == null)
/*  393 */         throw new IllegalArgumentException(String.format("Invalid Object Id definition for %s: cannot find property with name %s", new Object[] {
/*      */                 
/*  395 */                 ClassUtil.getTypeDescription(beanDesc.getType()), 
/*  396 */                 ClassUtil.name(propName)
/*      */               })); 
/*  398 */       idType = idProp.getType();
/*  399 */       PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*      */     } else {
/*  401 */       JavaType type = ctxt.constructType(implClass);
/*  402 */       idType = ctxt.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/*  403 */       idProp = null;
/*  404 */       gen = ctxt.objectIdGeneratorInstance((Annotated)beanDesc.getClassInfo(), objectIdInfo);
/*      */     } 
/*      */     
/*  407 */     JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/*  408 */     builder.setObjectIdReader(ObjectIdReader.construct(idType, objectIdInfo
/*  409 */           .getPropertyName(), gen, deser, idProp, resolver));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/*      */     ThrowableDeserializer throwableDeserializer;
/*      */     JsonDeserializer<?> jsonDeserializer1;
/*  417 */     DeserializationConfig config = ctxt.getConfig();
/*      */     
/*  419 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/*  420 */     builder.setValueInstantiator(findValueInstantiator(ctxt, beanDesc));
/*      */     
/*  422 */     addBeanProps(ctxt, beanDesc, builder);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  429 */     Iterator<SettableBeanProperty> it = builder.getProperties();
/*  430 */     while (it.hasNext()) {
/*  431 */       SettableBeanProperty prop = it.next();
/*  432 */       if ("setCause".equals(prop.getMember().getName())) {
/*      */         
/*  434 */         it.remove();
/*      */         break;
/*      */       } 
/*      */     } 
/*  438 */     AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
/*  439 */     if (am != null) {
/*  440 */       SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct((MapperConfig)ctxt.getConfig(), (AnnotatedMember)am, new PropertyName("cause"));
/*      */       
/*  442 */       SettableBeanProperty prop = constructSettableProperty(ctxt, beanDesc, (BeanPropertyDefinition)propDef, am
/*  443 */           .getParameterType(0));
/*  444 */       if (prop != null)
/*      */       {
/*      */         
/*  447 */         builder.addOrReplaceProperty(prop, true);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  452 */     builder.addIgnorable("localizedMessage");
/*      */     
/*  454 */     builder.addIgnorable("suppressed");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  461 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  462 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  463 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*      */       }
/*      */     }
/*  466 */     JsonDeserializer<?> deserializer = builder.build();
/*      */ 
/*      */ 
/*      */     
/*  470 */     if (deserializer instanceof BeanDeserializer) {
/*  471 */       throwableDeserializer = new ThrowableDeserializer((BeanDeserializer)deserializer);
/*      */     }
/*      */ 
/*      */     
/*  475 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  476 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  477 */         jsonDeserializer1 = mod.modifyDeserializer(config, beanDesc, (JsonDeserializer<?>)throwableDeserializer);
/*      */       }
/*      */     }
/*  480 */     return (JsonDeserializer)jsonDeserializer1;
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
/*      */   protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc) {
/*  496 */     return new BeanDeserializerBuilder(beanDesc, ctxt);
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
/*      */   protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*      */     Set<String> ignored;
/*  510 */     boolean isConcrete = !beanDesc.getType().isAbstract();
/*      */ 
/*      */     
/*  513 */     SettableBeanProperty[] creatorProps = isConcrete ? builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig()) : null;
/*  514 */     boolean hasCreatorProps = (creatorProps != null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  521 */     JsonIgnoreProperties.Value ignorals = ctxt.getConfig().getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc
/*  522 */         .getClassInfo());
/*      */     
/*  524 */     if (ignorals != null) {
/*  525 */       boolean ignoreAny = ignorals.getIgnoreUnknown();
/*  526 */       builder.setIgnoreUnknownProperties(ignoreAny);
/*      */       
/*  528 */       ignored = ignorals.findIgnoredForDeserialization();
/*  529 */       for (String propName : ignored) {
/*  530 */         builder.addIgnorable(propName);
/*      */       }
/*      */     } else {
/*  533 */       ignored = Collections.emptySet();
/*      */     } 
/*      */     
/*  536 */     JsonIncludeProperties.Value inclusions = ctxt.getConfig().getDefaultPropertyInclusions(beanDesc.getBeanClass(), beanDesc
/*  537 */         .getClassInfo());
/*  538 */     Set<String> included = null;
/*  539 */     if (inclusions != null) {
/*  540 */       included = inclusions.getIncluded();
/*  541 */       if (included != null) {
/*  542 */         for (String propName : included) {
/*  543 */           builder.addIncludable(propName);
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  549 */     AnnotatedMember anySetter = beanDesc.findAnySetterAccessor();
/*  550 */     if (anySetter != null) {
/*  551 */       builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetter));
/*      */     }
/*      */     else {
/*      */       
/*  555 */       Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
/*  556 */       if (ignored2 != null) {
/*  557 */         for (String propName : ignored2)
/*      */         {
/*      */           
/*  560 */           builder.addIgnorable(propName);
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/*  565 */     boolean useGettersAsSetters = (ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS) && ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
/*      */ 
/*      */     
/*  568 */     List<BeanPropertyDefinition> propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc
/*  569 */         .findProperties(), ignored, included);
/*      */     
/*  571 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  572 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  573 */         propDefs = mod.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  578 */     for (BeanPropertyDefinition propDef : propDefs) {
/*  579 */       SettableBeanProperty prop = null;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  584 */       if (propDef.hasSetter()) {
/*  585 */         AnnotatedMethod setter = propDef.getSetter();
/*  586 */         JavaType propertyType = setter.getParameterType(0);
/*  587 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/*  588 */       } else if (propDef.hasField()) {
/*  589 */         AnnotatedField field = propDef.getField();
/*  590 */         JavaType propertyType = field.getType();
/*  591 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/*      */       } else {
/*      */         
/*  594 */         AnnotatedMethod getter = propDef.getGetter();
/*  595 */         if (getter != null) {
/*  596 */           if (useGettersAsSetters && _isSetterlessType(getter.getRawType())) {
/*      */ 
/*      */             
/*  599 */             if (!builder.hasIgnorable(propDef.getName()))
/*      */             {
/*      */               
/*  602 */               prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*      */             }
/*  604 */           } else if (!propDef.hasConstructorParameter()) {
/*  605 */             PropertyMetadata md = propDef.getMetadata();
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  610 */             if (md.getMergeInfo() != null) {
/*  611 */               prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  619 */       if (hasCreatorProps && propDef.hasConstructorParameter()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  625 */         String name = propDef.getName();
/*  626 */         CreatorProperty cprop = null;
/*      */         
/*  628 */         for (SettableBeanProperty cp : creatorProps) {
/*  629 */           if (name.equals(cp.getName()) && cp instanceof CreatorProperty) {
/*  630 */             cprop = (CreatorProperty)cp;
/*      */             break;
/*      */           } 
/*      */         } 
/*  634 */         if (cprop == null) {
/*  635 */           List<String> n = new ArrayList<>();
/*  636 */           for (SettableBeanProperty cp : creatorProps) {
/*  637 */             n.add(cp.getName());
/*      */           }
/*  639 */           ctxt.reportBadPropertyDefinition(beanDesc, propDef, "Could not find creator property with name %s (known Creator properties: %s)", new Object[] {
/*      */                 
/*  641 */                 ClassUtil.name(name), n });
/*      */           continue;
/*      */         } 
/*  644 */         if (prop != null) {
/*  645 */           cprop.setFallbackSetter(prop);
/*      */         }
/*  647 */         Class<?>[] views = propDef.findViews();
/*  648 */         if (views == null) {
/*  649 */           views = beanDesc.findDefaultViews();
/*      */         }
/*  651 */         cprop.setViews(views);
/*  652 */         builder.addCreatorProperty(cprop);
/*      */         continue;
/*      */       } 
/*  655 */       if (prop != null) {
/*      */         
/*  657 */         Class<?>[] views = propDef.findViews();
/*  658 */         if (views == null) {
/*  659 */           views = beanDesc.findDefaultViews();
/*      */         }
/*  661 */         prop.setViews(views);
/*  662 */         builder.addProperty(prop);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _isSetterlessType(Class<?> rawType) {
/*  671 */     return (Collection.class.isAssignableFrom(rawType) || Map.class
/*  672 */       .isAssignableFrom(rawType));
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
/*      */   @Deprecated
/*      */   protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored) throws JsonMappingException {
/*  690 */     return filterBeanProps(ctxt, beanDesc, builder, propDefsIn, ignored, (Set<String>)null);
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
/*      */   
/*      */   protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored, Set<String> included) {
/*  708 */     ArrayList<BeanPropertyDefinition> result = new ArrayList<>(Math.max(4, propDefsIn.size()));
/*  709 */     HashMap<Class<?>, Boolean> ignoredTypes = new HashMap<>();
/*      */     
/*  711 */     for (BeanPropertyDefinition property : propDefsIn) {
/*  712 */       String name = property.getName();
/*      */       
/*  714 */       if (IgnorePropertiesUtil.shouldIgnore(name, ignored, included)) {
/*      */         continue;
/*      */       }
/*  717 */       if (!property.hasConstructorParameter()) {
/*  718 */         Class<?> rawPropertyType = property.getRawPrimaryType();
/*      */         
/*  720 */         if (rawPropertyType != null && 
/*  721 */           isIgnorableType(ctxt.getConfig(), property, rawPropertyType, ignoredTypes)) {
/*      */           
/*  723 */           builder.addIgnorable(name);
/*      */           continue;
/*      */         } 
/*      */       } 
/*  727 */       result.add(property);
/*      */     } 
/*  729 */     return result;
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
/*      */   protected void addBackReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*  743 */     List<BeanPropertyDefinition> refProps = beanDesc.findBackReferences();
/*  744 */     if (refProps != null) {
/*  745 */       for (BeanPropertyDefinition refProp : refProps) {
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
/*  762 */         String refName = refProp.findReferenceName();
/*  763 */         builder.addBackReferenceProperty(refName, constructSettableProperty(ctxt, beanDesc, refProp, refProp
/*  764 */               .getPrimaryType()));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected void addReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*  774 */     addBackReferenceProperties(ctxt, beanDesc, builder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
/*  785 */     Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
/*  786 */     if (raw != null) {
/*  787 */       for (Map.Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
/*  788 */         AnnotatedMember m = entry.getValue();
/*  789 */         builder.addInjectable(PropertyName.construct(m.getName()), m
/*  790 */             .getType(), beanDesc
/*  791 */             .getClassAnnotations(), m, entry.getKey());
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableAnyProperty constructAnySetter(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedMember mutator) throws JsonMappingException {
/*      */     BeanProperty.Std std;
/*      */     JavaType keyType, valueType;
/*  814 */     if (mutator instanceof AnnotatedMethod) {
/*      */       
/*  816 */       AnnotatedMethod am = (AnnotatedMethod)mutator;
/*  817 */       keyType = am.getParameterType(0);
/*  818 */       valueType = am.getParameterType(1);
/*  819 */       valueType = resolveMemberAndTypeAnnotations(ctxt, mutator, valueType);
/*  820 */       std = new BeanProperty.Std(PropertyName.construct(mutator.getName()), valueType, null, mutator, PropertyMetadata.STD_OPTIONAL);
/*      */ 
/*      */     
/*      */     }
/*  824 */     else if (mutator instanceof AnnotatedField) {
/*  825 */       AnnotatedField af = (AnnotatedField)mutator;
/*      */       
/*  827 */       JavaType mapType = af.getType();
/*  828 */       mapType = resolveMemberAndTypeAnnotations(ctxt, mutator, mapType);
/*  829 */       keyType = mapType.getKeyType();
/*  830 */       valueType = mapType.getContentType();
/*  831 */       std = new BeanProperty.Std(PropertyName.construct(mutator.getName()), mapType, null, mutator, PropertyMetadata.STD_OPTIONAL);
/*      */     } else {
/*      */       
/*  834 */       return (SettableAnyProperty)ctxt.reportBadDefinition(beanDesc.getType(), String.format("Unrecognized mutator type for any setter: %s", new Object[] { mutator
/*  835 */               .getClass() }));
/*      */     } 
/*      */ 
/*      */     
/*  839 */     KeyDeserializer keyDeser = findKeyDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/*  840 */     if (keyDeser == null) {
/*  841 */       keyDeser = (KeyDeserializer)keyType.getValueHandler();
/*      */     }
/*  843 */     if (keyDeser == null) {
/*  844 */       keyDeser = ctxt.findKeyDeserializer(keyType, (BeanProperty)std);
/*      */     }
/*  846 */     else if (keyDeser instanceof ContextualKeyDeserializer) {
/*      */       
/*  848 */       keyDeser = ((ContextualKeyDeserializer)keyDeser).createContextual(ctxt, (BeanProperty)std);
/*      */     } 
/*      */     
/*  851 */     JsonDeserializer<Object> deser = findContentDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/*  852 */     if (deser == null) {
/*  853 */       deser = (JsonDeserializer<Object>)valueType.getValueHandler();
/*      */     }
/*  855 */     if (deser != null)
/*      */     {
/*  857 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)std, valueType);
/*      */     }
/*  859 */     TypeDeserializer typeDeser = (TypeDeserializer)valueType.getTypeHandler();
/*  860 */     return new SettableAnyProperty((BeanProperty)std, mutator, valueType, keyDeser, deser, typeDeser);
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
/*      */   protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef, JavaType propType0) throws JsonMappingException {
/*      */     FieldProperty fieldProperty;
/*      */     SettableBeanProperty settableBeanProperty;
/*  877 */     AnnotatedMember mutator = propDef.getNonConstructorMutator();
/*      */ 
/*      */ 
/*      */     
/*  881 */     if (mutator == null) {
/*  882 */       ctxt.reportBadPropertyDefinition(beanDesc, propDef, "No non-constructor mutator available", new Object[0]);
/*      */     }
/*  884 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, mutator, propType0);
/*      */     
/*  886 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*      */     
/*  888 */     if (mutator instanceof AnnotatedMethod) {
/*      */       
/*  890 */       MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedMethod)mutator);
/*      */     }
/*      */     else {
/*      */       
/*  894 */       fieldProperty = new FieldProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedField)mutator);
/*      */     } 
/*  896 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, (Annotated)mutator);
/*  897 */     if (deser == null) {
/*  898 */       deser = (JsonDeserializer)type.getValueHandler();
/*      */     }
/*  900 */     if (deser != null) {
/*  901 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)fieldProperty, type);
/*  902 */       settableBeanProperty = fieldProperty.withValueDeserializer(deser);
/*      */     } 
/*      */     
/*  905 */     AnnotationIntrospector.ReferenceProperty ref = propDef.findReferenceType();
/*  906 */     if (ref != null && ref.isManagedReference()) {
/*  907 */       settableBeanProperty.setManagedReferenceName(ref.getName());
/*      */     }
/*  909 */     ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
/*  910 */     if (objectIdInfo != null) {
/*  911 */       settableBeanProperty.setObjectIdInfo(objectIdInfo);
/*      */     }
/*  913 */     return settableBeanProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SettableBeanProperty constructSetterlessProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef) throws JsonMappingException {
/*      */     SettableBeanProperty settableBeanProperty;
/*  924 */     AnnotatedMethod getter = propDef.getGetter();
/*  925 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, (AnnotatedMember)getter, getter.getType());
/*  926 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*      */     
/*  928 */     SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), getter);
/*  929 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, (Annotated)getter);
/*  930 */     if (deser == null) {
/*  931 */       deser = (JsonDeserializer)type.getValueHandler();
/*      */     }
/*  933 */     if (deser != null) {
/*  934 */       deser = ctxt.handlePrimaryContextualization(deser, (BeanProperty)setterlessProperty, type);
/*  935 */       settableBeanProperty = setterlessProperty.withValueDeserializer(deser);
/*      */     } 
/*  937 */     return settableBeanProperty;
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
/*      */ 
/*      */   
/*      */   protected boolean isPotentialBeanType(Class<?> type) {
/*  956 */     String typeStr = ClassUtil.canBeABeanType(type);
/*  957 */     if (typeStr != null) {
/*  958 */       throw new IllegalArgumentException("Cannot deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*      */     }
/*  960 */     if (ClassUtil.isProxyType(type)) {
/*  961 */       throw new IllegalArgumentException("Cannot deserialize Proxy class " + type.getName() + " as a Bean");
/*      */     }
/*      */ 
/*      */     
/*  965 */     typeStr = ClassUtil.isLocalType(type, true);
/*  966 */     if (typeStr != null) {
/*  967 */       throw new IllegalArgumentException("Cannot deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*      */     }
/*  969 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isIgnorableType(DeserializationConfig config, BeanPropertyDefinition propDef, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
/*  979 */     Boolean status = ignoredTypes.get(type);
/*  980 */     if (status != null) {
/*  981 */       return status.booleanValue();
/*      */     }
/*      */     
/*  984 */     if (type == String.class || type.isPrimitive()) {
/*  985 */       status = Boolean.FALSE;
/*      */     } else {
/*      */       
/*  988 */       status = config.getConfigOverride(type).getIsIgnoredType();
/*  989 */       if (status == null) {
/*  990 */         BeanDescription desc = config.introspectClassAnnotations(type);
/*  991 */         status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
/*      */         
/*  993 */         if (status == null) {
/*  994 */           status = Boolean.FALSE;
/*      */         }
/*      */       } 
/*      */     } 
/*  998 */     ignoredTypes.put(type, status);
/*  999 */     return status.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _validateSubType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
/* 1009 */     SubTypeValidator.instance().validateSubType(ctxt, type, beanDesc);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/BeanDeserializerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */