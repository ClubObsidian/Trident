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

package com.clubobsidian.trident.test;

import com.clubobsidian.trident.EventBus;
import com.clubobsidian.trident.test.impl.TestEventBus;
import com.clubobsidian.trident.test.impl.TestListener;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class OtherEventBusTest {

    @Test
    public void testNullExecutor() {
        EventBus bus = new TestEventBus();
        boolean registered = bus.registerEvents(new TestListener("data"));
        assertFalse("Listener was registered even though MethodExecutor is null", registered);
    }
}