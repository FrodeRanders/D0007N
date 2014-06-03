package ltu.d0007n;


public class App 
{
    public static void main( String[] args ) {

        java.io.BufferedReader reader = null;
        try {
            // HTTP protocol-level stuff
            java.net.URL url = new java.net.URL("http://www.ltu.se");
            java.io.InputStream input = url.openStream();

            // Reading the reply
            reader = new java.io.BufferedReader(new java.io.InputStreamReader(input));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                System.out.flush();
            }

        } catch (java.io.IOException ioe) {
            String info = "Could not communicate with web server: ";
            info += ioe.getMessage();

            System.err.println(info);
            System.exit(1);

        } finally {
            try {
                if (null != reader) reader.close();
            } catch (Throwable ignore) {
                // We don't care if we fail now
            }
        }
    }
}
