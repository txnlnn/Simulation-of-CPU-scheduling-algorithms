
public class Data implements Comparable<Data> {

    private String p;
    private int arrtime;
    private int brttime;
    private int processbrttime;
    private int priority;
    private boolean finish;
    private int fntime;
    private int startime;

    

    Data() {
    }


    Data(String p, int arrtime, int brttime, int processbrttime, int priority, boolean finish) {
        this.p = p;
        this.arrtime = arrtime;
        this.brttime = brttime;
        this.processbrttime = processbrttime;
        this.priority = priority;
        this.finish = finish;
    }
    
    Data(String p, int fntime) {
        this.p = p;
        this.fntime = fntime;
    }


    Data(String p, int startime, int fntime) {
        this.p = p;
        this.startime = startime;
        this.fntime = fntime;
    }

    public void setprocessBrttime(int processbrttime) {
        this.processbrttime = processbrttime;
    }

   
    public void setStatus(boolean s) {
        finish= s;
    }

    public String getProcesser() {
        return p;
    }

    public int getArrtime() {
        return arrtime;
    }

    public int getStarttime() {
        return startime;
    }

    public int getPriority() {
        return priority;
    }
    public int getProcessbrttime() {
        return processbrttime;
    }

    public int getBrttime() {
        return brttime;
    }

    public int getTime() {
        return fntime;
    }

    public boolean getStatus() {
        return finish;
    }

    public String toString() {
        return p + " , " + arrtime + " , " + brttime + " , " + processbrttime + " , " + priority + " , " + finish;
    }

    public String toString2() {
        return p  + "  " + startime + "  " + fntime;
    }


    public int compareTo(Data o) {
        return  getProcessbrttime() - o.getProcessbrttime();
        
    }

    

}
