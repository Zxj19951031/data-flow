package org.example.plugins.common;

import org.example.plugins.common.commons.JsonObject;
import org.example.plugins.common.plugins.AbstractJobPlugin;
import org.example.plugins.common.plugins.AbstractTaskPlugin;
import org.example.plugins.common.tunnels.RecordConsumer;

import java.util.List;

public abstract class Writer {

    public static abstract class Job extends AbstractJobPlugin {


        public abstract List<JsonObject> split(int channel);

    }

    public static abstract class Task extends AbstractTaskPlugin {


        public abstract void startWrite(RecordConsumer consumer);

    }
}
