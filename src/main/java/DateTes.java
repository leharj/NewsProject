import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTes {
    public static void main(String args[]) throws Exception{
        String date1 = "Thu, 17 Aug 2017 05:17:58 GMT";
        String date2 = "Thu, 17 Aug 2017 11:36:08 +0530";
        System.out.println(System.currentTimeMillis());
        Date d1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(date1);
        Date d2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(date2);
        Date d3 = new SimpleDateFormat("EEEE, MMMM dd, yyyy, HH:mm z").parse("Thursday, August 17, 2017, 11:32  GMT +0530");
        Date d4 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-08-17");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        System.out.println(format.parse("2014-10-05T15:23:01+00:00"));
        Date d5 = new SimpleDateFormat("EEEE,MMMM dd,yyyy h:mm a").parse("Thursday,August 17,2017 3:23 pm");
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
        System.out.println(d4);
        System.out.println(d5);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,0);
        today.set(Calendar.MINUTE,0);
        Date d = today.getTime();
        System.out.println(d);
    }
}
