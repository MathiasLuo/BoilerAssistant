package me.mathiasluo.model.schedule;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


public class Schedule {
    private UserInfo userInfo;
    private List<Class> classes;
    //all the cookies we need to get the schedule (may include not needed ones)
    private final static String[] okCookies = new String[]{
            "UserAgentId",
            "BIGipServer~BAN~pool_wl.mypurdue_443",
            "fos.web.server",
            "fos.secure.web.server",
            "runId",
            "JSESSIONID_LP4",
            "usid",
            "usidsec",
            "SESSID",
            "TESTID",
            "BANSSO",
            "CPSESSID"
    };

    //day map
    private final static int[] days = new int[]{
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
    };

    /**
     * Set up a connection to a url (helper function)
     *
     * @param url  Url of request
     * @param host Host header for request
     * @return connection you requested
     */
    private static HttpsURLConnection setup(String url, String host) throws Exception {
        HttpsURLConnection conn =
                (HttpsURLConnection) new URL(url).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
        return conn;
    }

    /**
     * Set up a connection to a url (helper function)
     *
     * @param url     Url of request
     * @param host    host header for request
     * @param referer referer header
     * @param cookies Cookie structure to be converted to Cookie header
     * @return connection you requested
     */
    private static HttpsURLConnection setup(String url, String host, String referer, Cookies cookies) throws Exception {
        HttpsURLConnection conn = setup(url, host);
        conn.setRequestProperty("Referer", referer);
        conn.setRequestProperty("Cookie", cookies.toString());
        return conn;
    }

    /**
     * Get contents of a connection (helper function)
     *
     * @param conn connection
     * @return contents of response as 1 string
     */
    private static String getContents(HttpsURLConnection conn) throws Exception {
        Scanner in = new Scanner(conn.getInputStream());
        in.useDelimiter("\\Z");
        String content = in.next();
        in.close();
        return content;
    }

    public Schedule(String user, String password) throws Exception {
        classes = new ArrayList<Class>();
        parseRawSchedule(getRawSchedule(user, password));
    }


    private String getRawSchedule(String user, String password) throws Exception {
        //extract login cookies from page
        Log.e("===============>>>>>>>>>>>>>>>>>", "这里开始了网路请求");
        HttpsURLConnection conn = setup("https://wl.mypurdue.purdue.edu/cp/home/displaylogin", "wl.mypurdue.purdue.edu");
        Log.e("===============>>>>>>>>>>",conn.getHeaderFields().toString());
        Cookies cookies = new Cookies(okCookies, conn.getHeaderFields());
        String contents = getContents(conn);
        Log.e("===============>>>>>>>>>>>>>>>>>", "这里开始了网路请求");
        //extract clientServerDelta (current time - constant) from page
        Pattern pattern = Pattern.compile("[0-9]+;");
        Matcher matcher = pattern.matcher(contents);
        long clientServerDelta;
        Log.e("===============>>>>>>>>>>>>>>>>>", "这里开始了网路请求");
        if (matcher.find()) {
            String rawMatch = matcher.group();
            String constant = rawMatch.substring(0, rawMatch.length() - 1);
            clientServerDelta = System.currentTimeMillis() - Long.parseLong(constant);
        } else {
            throw new Exception("couldn't find clientServerDelta");
        }
        //post login info to next step
        conn = setup("https://wl.mypurdue.purdue.edu/cp/home/login", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/cp/home/displaylogin", cookies);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //simulate uuid calculation (submit time - clientServerDelta) with sleep
        Random r = new Random();
        long time = r.nextInt(10000);
        Thread.sleep(time);
        long uuid = System.currentTimeMillis() - clientServerDelta;
        String content = "pass=" + password + "&user=" + user + "&uuid=" + uuid;
        conn.setRequestProperty("Content-Length", Integer.toString(content.length()));
        OutputStream out = conn.getOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.print(content);
        writer.close();
        out.close();
        //check for failure
        if (conn.getResponseCode() != 302 || !conn.getHeaderFields().get("Location").get(0).equals("https://wl.mypurdue.purdue.edu/cps/welcome/loginok.html")) {
            throw new Exception("failed to login");
        }
        //go through login steps
        cookies.setCookies(conn.getHeaderFields());
        conn = setup("https://wl.mypurdue.purdue.edu/cps/welcome/loginok.html", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/cp/home/login", cookies);
        String response = getContents(conn);
        if (!response.contains("https://wl.mypurdue.purdue.edu/cp/home/next")) {
            throw new Exception("Failed to get next");
        }
        conn = setup("https://wl.mypurdue.purdue.edu/cp/home/next", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/cps/welcome/loginok.html", cookies);
        if (conn.getResponseCode() != 302 || !conn.getHeaderFields().get("Location").get(0).equals("https://wl.mypurdue.purdue.edu/render.userLayoutRootNode.uP?uP_root=root")) {
            throw new Exception("Failed to get root page!");
        }
        conn = setup("https://wl.mypurdue.purdue.edu/render.userLayoutRootNode.uP?uP_root=root", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/cps/welcome/loginok.html", cookies);
        response = getContents(conn);
        //set up sctSession cookie (increases with time)
        cookies.setCookie("sctSession", Integer.toString(r.nextInt(5) + 1));
        //go through steps to get week at a glance
        conn = setup("https://wl.mypurdue.purdue.edu/jsp/misc/ss_redir.jsp?pg=24", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/tag.7cee4c7293654c6f.render.userLayoutRootNode.uP?uP_root=root&uP_sparam=activeTab&activeTab=u12l1s2&uP_tparam=frm&frm=", cookies);

        cookies.setCookie("sctSession", Integer.toString(8 + r.nextInt(5)));
        conn = setup("https://wl.mypurdue.purdue.edu/cp/ip/login?sys=sctssb&url=https://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", "wl.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/render.UserLayoutRootNode.uP?uP_tparam=utf&utf=%2fcp%2fip%2flogin%3fsys%3dsctssb%26url%3dhttps://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", cookies);
        if (conn.getHeaderFields().get("Location") == null) {
            throw new Exception("unable to get schedule setup!");
        }
        String url = conn.getHeaderFields().get("Location").get(0);
        conn = setup(url, "selfservice.mypurdue.purdue.edu", "	https://wl.mypurdue.purdue.edu/render.UserLayoutRootNode.uP?uP_tparam=utf&utf=%2fcp%2fip%2flogin%3fsys%3dsctssb%26url%3dhttps://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", cookies);
        cookies.setCookies(conn.getHeaderFields());
        conn = setup("https://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", "selfservice.mypurdue.purdue.edu", "https://wl.mypurdue.purdue.edu/render.UserLayoutRootNode.uP?uP_tparam=utf&utf=%2fcp%2fip%2flogin%3fsys%3dsctssb%26url%3dhttps://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", cookies);
        cookies.setCookies(conn.getHeaderFields());
        //finally actually get week at a glance
        conn = setup("https://selfservice.mypurdue.purdue.edu/prod/bwskfshd.P_CrseSchd", "selfservice.mypurdue.purdue.edu", "https://selfservice.mypurdue.purdue.edu/prod/tzwkwbis.P_CheckAgreeAndRedir?ret_code=STU_WEEKGLANCE", cookies);
        return getContents(conn);
    }

    private void parseRawSchedule(String rawSchedule) throws Exception {
        Document doc = Jsoup.parse(rawSchedule);
        Elements elems = doc.select("[class=staticheaders]");
        if (elems.size() != 0) {
            userInfo = new UserInfo(elems.first().text());
        }
        elems = doc.select("[class=datadisplaytable]");
        if (elems.size() != 0) {
            Elements data = elems.first().select("tr");
            //keep track of occupied rows
            boolean tableLayout[][] = new boolean[7][90];
            int curRow = 0;
            int curCol = 0;
            for (int k = 1; k < data.size(); ++k) {
                Element e = data.get(k);
                Elements rawClasses = e.children().select("td");
                for (int i = 0; i < rawClasses.size(); ++i) {
                    Element curClass = rawClasses.get(i);
                    while (tableLayout[curCol][curRow]) {
                        curCol++;
                        if (curCol == 7) {
                            curCol = 0;
                            curRow++;
                        }
                    }
                    //if this is a class label
                    if (curClass.className().equals("ddlabel")) {
                        //occupy rowspan rows in the current column if specified
                        if (curClass.attr("rowspan") != null || curClass.attr("rowspan").length() != 0) {
                            Integer rowspan = Integer.parseInt(curClass.attr("rowspan"));
                            int addRow = curRow;
                            for (int j = 0; j < rowspan; ++j) {
                                tableLayout[curCol][addRow] = true;
                                addRow++;
                            }
                        }
                        classes.add(new Class(days[curCol], curClass.text()));
                    }
                    //the current row and col are always occupied
                    tableLayout[curCol][curRow] = true;
                }
            }
        }
    }

    public List<Class> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
