package Models;

public class TrendsOccurance implements Comparable{

    private String trend;
    private int last_6_hrs;
    private int last_12_hrs;
    private  int last_1_day;
    private int last_2_days;

    public TrendsOccurance(String trend){
        this.trend = trend;
        last_1_day = 0;
        last_2_days = 0;
        last_6_hrs = 0;
        last_12_hrs = 0;
    }

    public void setLast_6_hrs(int num){
        this.last_6_hrs = num;
    }

    public void setLast_1_day(int last_1_day) {
        this.last_1_day = last_1_day;
    }

    public void setLast_2_days(int last_2_days) {
        this.last_2_days = last_2_days;
    }

    public void setLast_12_hrs(int last_12_hrs) {
        this.last_12_hrs = last_12_hrs;
    }

    public int getLast_1_day() {
        return last_1_day;
    }

    public int getLast_6_hrs() {
        return last_6_hrs;
    }

    public int getLast_2_days() {
        return last_2_days;
    }

    public int getLast_12_hrs() {
        return last_12_hrs;
    }

    public String getTrend() {
        return trend;
    }

    @Override
    public int compareTo(Object o) {
        TrendsOccurance to = (TrendsOccurance)o;
        int diff_6_hrs = to.last_6_hrs-this.last_6_hrs;
        int diff_12_hrs = to.last_12_hrs - this.last_12_hrs;
        int diff_1_day = to.last_1_day-this.last_1_day;
        int diff_2_days = to.last_2_days - this.last_2_days;
        return diff_6_hrs==0?(diff_12_hrs==0?(diff_1_day==0?(diff_2_days==0?0:diff_2_days):diff_1_day):diff_12_hrs):diff_6_hrs;
    }

    public int getSum() {
        return this.last_6_hrs+this.last_12_hrs+this.last_1_day+this.last_2_days;
    }
}
