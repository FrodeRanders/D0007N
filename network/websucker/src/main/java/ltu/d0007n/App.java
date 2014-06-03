package ltu.d0007n;


/**
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // A human-readable server name
        String serverName = "www.ltu.se";

        // IP-level (IP-address)
        java.net.InetAddress ipAddress = null;
        try {
            ipAddress = java.net.InetAddress.getByName(serverName);

        } catch (java.net.UnknownHostException uhe) {
            String info = "Could not determine IP-address from name \"" + serverName + "\": ";
            info += uhe.getMessage();

            System.err.println(info);
            System.exit(1); // program ends here
        }

        // TCP-level (IP-address + TCP-port)
        int tcpPort = 80; // Well-known HTTP (webserver) port

        java.net.Socket client = null;
        java.io.OutputStream output = null;
        java.io.InputStream input = null;
        try {
            try {
                // Connect to server
                client = new java.net.Socket(ipAddress, tcpPort);

                output = client.getOutputStream();
                input = client.getInputStream();

            } catch (java.io.IOException ioe) {
                String info = "Could not connect to (" + ipAddress.getHostAddress() + ":" + tcpPort + "): ";
                info += ioe.getMessage();

                System.err.println(info);
                System.exit(2);
            }

            // HTTP protocol-level
            //
            //  GET / HTTP/1.1
            //  Host: www.ltu.se
            //  User-Agent: websucker-1.0
            //  Accept: text/html;q=0.9,*/*;q=0.8
            //  Accept-Language: en;q=0.5
            //  Accept-Encoding: gzip, deflate
            //
            //
            StringBuilder buf = new StringBuilder();
            buf.append("GET / HTTP/1.1\n");
            buf.append("Host: www.ltu.se\n");
            buf.append("User-Agent: websucker-1.0\n");
            buf.append("Accept: ")
                    .append("text/html;")
                    .append("q=0.9,*/*;q=0.8\n");
            buf.append("Accept-Language: ")
                    .append("en;")
                    .append("q=0.5").append("\n");
            buf.append("Accept-Encoding: ")
                    .append("gzip,")
                    .append("deflate\n");
            buf.append("\n");

            // Send request to Web server
            java.io.PrintStream printStream = new java.io.PrintStream(output);
            printStream.print(buf.toString());
            printStream.flush();

            // Read reply from Web server
            try {
                java.io.InputStreamReader is = new java.io.InputStreamReader(input);
                java.io.BufferedReader br = new java.io.BufferedReader(is);
                String read = br.readLine();

                while (read != null) {
                    System.out.println(read);
                    System.out.flush();

                    read = br.readLine();
                }

            } catch (java.io.IOException ioe) {
                String info = "Could not read reply from " + serverName + ": ";
                info += ioe.getMessage();

                System.err.println(info);
                System.exit(3);
            }
        } finally {
            try {
                if (null != input) input.close();
                if (null != output) output.close();
                if (null != client) client.close();
            } catch (Throwable ignore) {
                // We don't care if we fail now
            }
        }
    }
}
