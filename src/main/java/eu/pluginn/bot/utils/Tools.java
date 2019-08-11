package eu.pluginn.bot.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Eine statische Klasse die verschiene Funktionen beinhaltet.
 *
 * Copyright Delta
 *
 * Kontakt: christian.delta@gmx.net
 */
public class Tools {


    /**
     *  Gibt eine zufällige Zahl zurück.
     *
     * @param min Der Wert der mindestens vorkommen soll.
     * @param max Der Wert der maximal gültig ist (Höchste Zahl.)
     * @return Der zurückgebende Wert.
     */
    public static int getRandomNumber(int min, int max)
    {
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }

    /**
     * Gibt einen formatierten String aus der eine float Variable enthält.
     *
     * @param pText Der Text. Ein float variable ist gültig. Diest ist mit %f im String anzugeben.
     * @param pFloat Die float Variable mit der das %f ersetzt werden soll.
     * @param decimal Die Anzahl der Nachkomma-Stellen.
     */
    public static void printFormat(String pText, float pFloat, int decimal)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(decimal);
        df.setMaximumFractionDigits(decimal);
        pText = pText.replace("%f", df.format(pFloat));
        System.out.printf("%s\n", pText);
    }

    /**
     * Gibt einen formatierten String aus der eine int Variable enthält.
     *
     * @param pText Der Text. Ein int Variable ist gültig. Diest ist mit %i im String anzugeben.
     * @param pInt Die int Variable mit der das %i ersetzt werden soll.
     */
    public static void printFormat(String pText, int pInt)
    {
        pText = pText.replace("%i", String.format("%s", pInt));
        System.out.printf("%s\n", pText);
    }

    /**
     * Gibt einen formatierten String aus der eine double Variable enthält.
     *
     * @param pText Der Text. Ein double variable ist gültig. Diest ist mit %d im String anzugeben.
     * @param pDouble Die double Variable mit der das %d ersetzt werden soll.
     * @param decimal Die Anzahl der Nachkomma-Stellen.
     */
    public static void printFormat(String pText, double pDouble, int decimal)
    {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(decimal);
        df.setMaximumFractionDigits(decimal);
        pText = pText.replace("%d", df.format(pDouble));
        System.out.printf("%s\n", pText);

    }

    /**
     * Gibt einen formatierten String aus.
     *
     * @param pText Der Text der ausgegeben werden soll.
     */
    public static void printFormat(String pText)
    {
        System.out.printf("%s\n", pText);
    }

    /**
     *  Öffnet eine Werbseite und sendet Post Daten an diese. Gibt darauf den Inhalt zurück.
     *
     * @param pUrl Die Webseite die benutzt werden soll.
     * @param action Die Aktion unter $_POST['action'] was gesendet wird.
     * @param urlParams Die Parameter die gesendet werden sollen. Verwendung als Map<String, String>. z.B Map.put("getconfig", "yes"); ist "getconfig=yes" als Post Parameter.
     * @return Die zurückgegebene Inhalt der Webseite.
     */
    public static String sendPost(String pUrl, String action, Map<String,String> urlParams) throws Exception {

        URL url = new URL(pUrl);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", action);

        // Paramter durchlaufen und Werte setzen.
        for (String key : urlParams.keySet())
            params.put(key, urlParams.get(key));

        // Paramter löschen und Speicher freigeben.
        urlParams.clear();

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

    /**
     * Eine Methode um den Unix Timstamp zu erzeugen.
     * @return Gibt die aktuelle Zeit als Unix Timestamp zurück.
     */
    public static long getUnixTimestamp()
    {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * Eine Methode um die aktuelle Uhrzeit zu erzeugen.
     *
     * @return Gibt die Uhrzeit als Std:Min:Sek zurück.
     */
    public static String getCurrentTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        ZonedDateTime zdt = ZonedDateTime.now();
        java.util.Date date = java.util.Date.from( zdt.toInstant() );

        return sdf.format(date.getTime());
    }


    /**
     * Eine Methode um das jetzige Datum zu generieren.
     *
     * @return Gibt das aktuelle Datum als "Tag.Monat.Jahr" zurück.
     */
    public static String getCurrentDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();

        return dtf.format(localDate);
    }


}
