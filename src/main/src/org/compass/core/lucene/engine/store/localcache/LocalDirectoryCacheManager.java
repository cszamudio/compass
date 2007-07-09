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

package org.compass.core.lucene.engine.store.localcache;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.compass.core.CompassException;
import org.compass.core.config.CompassConfigurable;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.config.CompassSettings;
import org.compass.core.engine.SearchEngineException;
import org.compass.core.lucene.LuceneEnvironment;
import org.compass.core.lucene.engine.LuceneSearchEngineFactory;
import org.compass.core.lucene.engine.store.LuceneSearchEngineStoreFactory;

/**
 * @author kimchy
 */
public class LocalDirectoryCacheManager implements CompassConfigurable {

    private static final Log log = LogFactory.getLog(LocalDirectoryCacheManager.class);

    private boolean disableLocalCache = false;

    private Map subIndexLocalCacheGroups;

    private LuceneSearchEngineFactory searchEngineFactory;
    private String subContext;

    public LocalDirectoryCacheManager(LuceneSearchEngineFactory searchEngineFactory) {
        this.searchEngineFactory = searchEngineFactory;
    }

    public void configure(CompassSettings settings) throws CompassException {
        subContext = settings.getSetting(CompassEnvironment.CONNECTION_SUB_CONTEXT, "index");
        disableLocalCache = settings.getSettingAsBoolean(LuceneEnvironment.LocalCache.DISABLE_LOCAL_CACHE, false);
        this.subIndexLocalCacheGroups = settings.getSettingGroups(LuceneEnvironment.LocalCache.PREFIX);

        // just iterate through this to print out our cache
        for (Iterator it = subIndexLocalCacheGroups.keySet().iterator(); it.hasNext();) {
            String subIndexName = (String) it.next();
            CompassSettings subIndexSettings = (CompassSettings) subIndexLocalCacheGroups.get(subIndexName);
            String connection = subIndexSettings.getSetting(LuceneEnvironment.LocalCache.CONNECTION, LuceneSearchEngineStoreFactory.MEM_PREFIX);
            if (log.isDebugEnabled()) {
                log.debug("Local Cache for [" + subIndexName + "] configured with connection [" + connection + "]");
            }
        }
    }

    public Directory createLocalCache(String subIndex, Directory dir) throws SearchEngineException {
        if (disableLocalCache) {
            return dir;
        }
        CompassSettings settings = (CompassSettings) subIndexLocalCacheGroups.get(subIndex);
        if (settings == null) {
            settings = (CompassSettings) subIndexLocalCacheGroups.get(LuceneEnvironment.LocalCache.DEFAULT_NAME);
            if (settings == null) {
                return dir;
            }
        }
        String connection = settings.getSetting(LuceneEnvironment.LocalCache.CONNECTION, LuceneSearchEngineStoreFactory.MEM_PREFIX);
        Directory localCacheDirectory;
        if (LuceneSearchEngineStoreFactory.MEM_PREFIX.startsWith(connection)) {
            localCacheDirectory = new RAMDirectory();
        } else if (LuceneSearchEngineStoreFactory.FILE_PREFIX.startsWith(connection) ||
                LuceneSearchEngineStoreFactory.MMAP_PREFIX.startsWith(connection)) {
            String path = connection.substring(LuceneSearchEngineStoreFactory.FILE_PREFIX.length(), connection.length()) + "/" + subContext + "/" + subIndex;
            File filePath = new File(path);
            if (!filePath.exists()) {
                boolean created = filePath.mkdirs();
                if (!created) {
                    throw new SearchEngineException("Failed to create directory for local cache with path [" + path + "]");
                }
            }
            try {
                localCacheDirectory = FSDirectory.getDirectory(path, new SingleInstanceLockFactory());
            } catch (IOException e) {
                throw new SearchEngineException("Failed to create direcotry with path [" + path + "]", e);
            }
        } else {
            throw new SearchEngineException("Local cache does not supprt the following connection [" + connection + "]");
        }
        return new LocalDirectoryCache(subIndex, dir, localCacheDirectory, searchEngineFactory);
    }
}
