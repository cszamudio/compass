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

package org.compass.core.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ConstantScoreRangeQuery;

/**
 * Extends Lucene {@link QueryParser} and overrides {@link #getRangeQuery(String, String, String, boolean)}
 * since lucene performs data parsing which is a performance killer. Anyhow, handling dates in Compass
 * is different and simpler than Lucene.
 *
 * @author kimchy
 */
public class LuceneQueryParser extends QueryParser {

    public LuceneQueryParser(String f, Analyzer a) {
        super(f, a);
    }

    /**
     * Override it so we won't use the date format to try and parse dates
     */
    protected Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException {
        if (getLowercaseExpandedTerms()) {
            part1 = part1.toLowerCase();
            part2 = part2.toLowerCase();
        }

        return new ConstantScoreRangeQuery(field,
                "*".equals(part1) ? null : part1,
                "*".equals(part2) ? null : part2,
                inclusive, inclusive);
    }
}