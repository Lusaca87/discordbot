package utils;

public class Tools {

    private static String homepageUrl = "http://www.plug-inn.eu";
    private static String Splitter = "Ξ";


    public static String getHomepageUrl()
    {
        return homepageUrl;

    }

    public static String buildCustomPhpUrl(String pPhpfile)
    {
        return String.format("%s/%s", getHomepageUrl(), pPhpfile);
    }

    public static String getSplitter()
    {
        return Splitter;
    }

}
