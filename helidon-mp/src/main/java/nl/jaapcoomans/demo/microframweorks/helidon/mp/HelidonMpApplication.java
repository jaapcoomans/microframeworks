package nl.jaapcoomans.demo.microframweorks.helidon.mp;

import io.helidon.microprofile.server.Server;

public class HelidonMpApplication  {
    public static void main(String[] args) {
        Server.create().start();
    }
}
