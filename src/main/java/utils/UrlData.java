package utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *  Die statische Klasse für Aufgaben die mit Webdaten arbeitet.
 */
public class UrlData {

    /**
     *  Öffnet eine Werbseite und sendet Post Daten an diese. Gibt darauf den Inhalt zurück.
     *
     * @param pUrl Die Webseite die benutzt werden soll.
     * @param action Die Aktion unter $_POST['action'] was gesendet wird.
     * @param urlParams Die Parameter die gesendet werden sollen. Verwendung als Map<String, String>. z.B Map<"getconfig", "yes"> ist "getconfig=yes" als Post Parameter.
     * @return Die zurückgegebene Inhalt der Webseite.
     */
    public static String SendPost(String pUrl, String action, Map<String,String> urlParams) throws Exception {

        URL url = new URL(pUrl);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", action);

        // Paramter durchlaufen und Werte setzen.
        for (String key : urlParams.keySet())
            params.put(key, urlParams.get(key));

        // Paramter löschen und Speicher freigeben.
        urlParams.clear();
        urlParams = null;

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);


        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);

        return sb.toString();
    }


    /**
     * Öffnet eine HTTP Verindung zu einer Webseite und liest den Inhalt aus.
     *
     * @param pUrl Die Url der Webseite die aufgerufen werden soll.
     * @return Der Inhalt der Webseite.
     */
    public static String getPlainSite(String pUrl) throws Exception
    {
        URL url = new URL(pUrl);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);

        return sb.toString();
    }

}