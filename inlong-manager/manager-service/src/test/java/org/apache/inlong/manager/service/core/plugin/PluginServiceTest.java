/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.manager.service.core.plugin;

import org.apache.inlong.manager.service.ServiceBaseTest;
import org.apache.inlong.manager.workflow.plugin.Plugin;
import org.apache.inlong.manager.workflow.plugin.ProcessPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * Test class for reload plugin.
 */
public class PluginServiceTest extends ServiceBaseTest {

    @Autowired
    PluginService pluginService;

    @Test
    public void testReloadPlugin() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
        pluginService.setPluginLoc(path + "plugins");
        pluginService.pluginReload();
        List<Plugin> pluginList = pluginService.getPlugins();
        Assert.assertTrue(pluginList.size() > 0);
        Assert.assertTrue(pluginList.get(0) instanceof ProcessPlugin);
    }

}
