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
package com.clubobsidian.trident;

import java.lang.reflect.Method;

/**
 * Abstract class for MethodExecutor implementations
 * to extend. {@link MethodExecutor#execute(Event)}
 * to be overridden in implementation classes.
 *
 * @author virustotalop
 */
public abstract class MethodExecutor {

    private final Object listener;
    private final Method method;
    private final boolean ignoreCancelled;

    public MethodExecutor(Object listener, Method method, boolean ignoreCancelled) {
        this.listener = listener;
        this.method = method;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * @return listener to execute events on
     */
    public Object getListener() {
        return this.listener;
    }

    /**
     * @return method to execute
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * @return whether or not the method executor is ignoring cancelled
     */
    public boolean isIgnoringCancelled() {
        return this.ignoreCancelled;
    }

    /**
     * @param event that is executed on the class listener
     */
    public abstract void execute(Event event);
}
