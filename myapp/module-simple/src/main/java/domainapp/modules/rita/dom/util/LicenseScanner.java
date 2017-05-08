package domainapp.modules.rita.dom.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class LicenseScanner {

    public static JSONObject requestWebService(String licensePlate) {

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL("https://api.datamarket.azure.com/Data.ashx/opendata.rdw/VRTG.Open.Data/v1/KENT_VRTG_O_DAT('" + licensePlate + "')?$format=json");
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            if (in != null) {
                return new JSONObject(getResponseText(in));
            }

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

}
