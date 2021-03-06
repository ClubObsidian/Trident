/*
 *    Copyright 2021 Club Obsidian and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.clubobsidian.trident.eventbus.javassist;

import com.clubobsidian.trident.Event;
import com.clubobsidian.trident.EventBus;
import com.clubobsidian.trident.MethodExecutor;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@inheritDoc}
 */
public class JavassistEventBus extends EventBus {

    private static final Map<String, AtomicInteger> map = new ConcurrentHashMap<>();

    private final ClassPool pool;

    public JavassistEventBus() {
        this.pool = new ClassPool(true);
        this.setupPool();
    }

    private void setupPool() {
        this.addClassToPool(Event.class);
    }

    public ClassPool getClassPool() {
        return this.pool;
    }

    @Override
    protected MethodExecutor createMethodExecutor(Object listener, Method method, boolean ignoreCancelled) {
        return this.generateMethodExecutor(listener, method, ignoreCancelled);
    }

    private MethodExecutor generateMethodExecutor(Object listener, final Method method, final boolean ignoreCancelled) {
        if(listener == null || method == null) {
            return null;
        }

        try {
            ClassLoader classLoader = listener.getClass().getClassLoader();

            Class<?> listenerClass = Class.forName(listener.getClass().getName(), true, classLoader);

            this.addClassToPool(Event.class);

            LoaderClassPath loaderClassPath = new LoaderClassPath(classLoader);

            this.pool.insertClassPath(loaderClassPath);
            this.addClassToPool(listenerClass);

            String callbackClassName = listener.getClass().getName() + method.getName();

            AtomicInteger collision = map.get(callbackClassName);
            int classNum = -1;
            if(collision == null) {
                collision = new AtomicInteger(0);
                classNum = 0;
                JavassistEventBus.map.put(callbackClassName, collision);
            } else {
                classNum = collision.incrementAndGet();
            }

            callbackClassName += classNum;

            CtClass checkMethodExecutorClass = this.pool.getOrNull(callbackClassName);
            if(checkMethodExecutorClass != null) {
                if(checkMethodExecutorClass.isFrozen()) {
                    return null;
                }
            }

            CtClass methodExecutorClass = this.pool.makeClass(callbackClassName);


            methodExecutorClass.setSuperclass(this.pool.get("com.clubobsidian.trident.MethodExecutor"));

            String eventType = method.getParameterTypes()[0].getName();
            String listenerType = listener.getClass().getName();

            StringBuilder sb = new StringBuilder();
            sb.append("public void execute(" + Event.class.getName() + " event)");
            sb.append("{");
            sb.append(eventType + " ev = " + "((" + eventType + ") event);");
            sb.append("((" + listenerType + ")" + "this.getListener())." + method.getName() + "(ev);");
            sb.append("}");

            CtMethod call = CtNewMethod.make(sb.toString(), methodExecutorClass);
            methodExecutorClass.addMethod(call);

            Class<?> cl = methodExecutorClass.toClass(classLoader, JavassistEventBus.class.getProtectionDomain());
            return (MethodExecutor) cl.getDeclaredConstructors()[0].newInstance(listener, method, ignoreCancelled);
        } catch(NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addClassToPool(Class<?> clazz) {
        ClassClassPath classClassPath = new ClassClassPath(clazz);
        this.pool.insertClassPath(classClassPath);
    }
}