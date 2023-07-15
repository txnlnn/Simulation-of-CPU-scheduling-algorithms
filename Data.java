public class Data {

    private String p;
    private int arrtime;
    private int brttime;
    private int priority;
    private boolean finish;
    private int time;

    Data(){}

    Data(String p, int arrtime, int brttime, int priority, boolean finish){
        this.p = p;
        this.arrtime = arrtime;
        this.brttime = brttime;
        this.priority = priority;
        this.finish = finish;
    }
    
    Data(String p, int time){
        this.p = p;
        this.time = time;
    }

    public String getProcesser(){
        return p;
    }
    public int getArrtime(){
        return arrtime;   
    }
    public int getPriority(){
        return priority;
    }
    public int getBrttime(){
        return brttime;
    }
    public int getTime(){
        return time;
    }
    public boolean getStatus(){
        return finish;
    }
    public String toString(){
        return p + " , " + arrtime + " , " + brttime + " , " + priority;
    }
    public String toString2(){
        return p + " , " + time;
    }
}