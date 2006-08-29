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

package org.compass.core.lucene.engine;

import org.compass.core.Resource;
import org.compass.core.engine.SearchEngineException;
import org.compass.core.engine.SearchEngineHighlighter;
import org.compass.core.engine.SearchEngineHits;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Explanation;

/**
 * @author kimchy
 */
public class EmptyLuceneSearchEngineHits implements LuceneSearchEngineHits {

    public Resource getResource(int n) throws SearchEngineException {
        throw new IndexOutOfBoundsException("No resource for hit [" + n + "], length is [0]");
    }

    public int getLength() {
        return 0;
    }


    public SearchEngineHighlighter getHighlighter() throws SearchEngineException {
        throw new IndexOutOfBoundsException("No highlighter for empty hits");
    }

    public void close() throws SearchEngineException {
        // do nothing
    }

    public float score(int i) throws SearchEngineException {
        throw new IndexOutOfBoundsException("No score for hit [" + i + "], length is [0]");
    }


    public Hits getHits() {
        throw new IndexOutOfBoundsException("No Lucenen hits for empty hits");
    }

    public Explanation explain(int i) throws SearchEngineException {
        throw new IndexOutOfBoundsException("No explanation for hit [" + i + "], length is [0]");
    }
}
