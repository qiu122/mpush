/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   ohun@live.cn (夜色)
 */

package com.mpush.bootstrap.job;

import com.mpush.tools.log.Logs;

/**
 * Created by yxx on 2016/5/15.
 *
 * @author ohun@live.cn
 */
public final class BootChain {
    private final BootJob first = first();

    public void start() {
        first.start();
    }

    public void stop() {
        first.stop();
    }

    public static BootChain chain() {
        return new BootChain();
    }

    private BootJob first() {
        return new BootJob() {
            @Override
            public void start() {
                Logs.Console.error("begin start bootstrap chain...");
                startNext();
            }

            @Override
            protected void stop() {
                Logs.Console.error("begin stop bootstrap chain...");
                stopNext();
            }
        };
    }

    public BootJob boot() {
        return first;
    }
}