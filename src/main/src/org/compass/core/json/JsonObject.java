/*
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.compass.core.json;

import java.util.Map;

/**
 * @author kimchy
 */
public interface JsonObject {

    Map<String, Object> entries();

    /**
     * Returns the given object under the key. Returns <code>null</code> if nothing
     * is registerd under the key.
     */
    Object opt(String key);

    boolean isNullValue(Object value);
}