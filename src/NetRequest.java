import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetRequest implements Runnable, IResponseHandler {
    private String url;
    private String data;
    private String result;

    public NetRequest(String url) {
        this(url, null);
    }

    public NetRequest(String url, String data) {
        this.url = url;
        this.data = data;
    }

    @Override
    public void run() {
        try {

            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(this.data == null ? "GET" : "POST");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                this.OnComplete(false, "Failed : HTTP error code : " + conn.getResponseCode());
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuffer result = new StringBuffer();
            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }

            conn.disconnect();

            this.OnComplete(true, result.toString());
        } catch (Exception e) {
            this.OnComplete(false, e.toString());
        }
    }

    @Override
    public void OnComplete(boolean succeeded, String response) {
        if (succeeded) {
            this.result = response;
        }
    }

    public String getResult() {
        Thread t = new Thread(new NetRequest(url));
        t.start();
        try
        {
            t.join();
        }
        catch (InterruptedException e)
        {
            return null;
        }

        return this.result;
    }
}