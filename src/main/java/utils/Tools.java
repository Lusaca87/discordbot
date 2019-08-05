package utils;

public class Tools {

    private static String homepageUrl = "http://www.plug-inn.eu";



    public static String getHomepageUrl()
    {
        return homepageUrl;

    }

    public static String buildCustomPhpUrl(String pPhpfile)
    {
        return String.format("%s/%s", getHomepageUrl(), pPhpfile);
    }

}
