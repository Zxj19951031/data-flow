package org.example.plugins.core.runner;

import org.example.plugins.common.Writer;
import org.example.plugins.common.tunnels.BufferTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriterRunner extends AbstractRunner implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(WriterRunner.class);
    private final BufferTunnel tunnel;
    private final Writer.Task taskWriter;

    public WriterRunner(BufferTunnel tunnel, Writer.Task taskWriter) {
        super();
        this.tunnel = tunnel;
        this.taskWriter = taskWriter;
    }

    @Override
    public void run() {
        try {
            this.running();

            taskWriter.init();

            taskWriter.startWrite(tunnel);

            taskWriter.destroy();

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            this.error();
        } finally {
            this.finish();
        }
    }
}
