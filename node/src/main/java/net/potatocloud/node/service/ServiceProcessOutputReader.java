package net.potatocloud.node.service;

import net.potatocloud.api.service.Service;

import java.io.BufferedReader;
import java.io.IOException;

public class ServiceProcessOutputReader extends Thread {

    private final Process process;
    private final BufferedReader reader;
    private final Service service;

    public ServiceProcessOutputReader(
            Process process,
            BufferedReader reader,
            Service service
    ) {
        this.process = process;
        this.reader = reader;
        this.service = service;

        setDaemon(true);
        setName("ServiceProcessOutputReader-" + service.getName());
    }

    @Override
    public void run() {
        try {
            String line;

            while (process != null && process.isAlive() && (line = reader.readLine()) != null) {
                if (service instanceof ServiceImpl impl) {
                    impl.addLog(line);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
