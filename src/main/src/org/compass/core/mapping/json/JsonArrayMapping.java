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

package org.compass.core.mapping.json;

import org.compass.core.mapping.Mapping;
import org.compass.core.mapping.support.AbstractMapping;

/**
 * @author kimchy
 */
public class JsonArrayMapping extends AbstractMapping implements JsonMapping {

    private Mapping elementMapping;

    private String fullPath;

    public Mapping copy() {
        JsonArrayMapping copy = new JsonArrayMapping();
        super.copy(copy);
        copy.setElementMapping(getElementMapping().copy());
        copy.setFullPath(getFullPath());
        return copy;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Mapping getElementMapping() {
        return elementMapping;
    }

    public void setElementMapping(Mapping elementMapping) {
        this.elementMapping = elementMapping;
    }
}