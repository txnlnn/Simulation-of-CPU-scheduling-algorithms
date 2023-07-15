import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import javax.swing.*;

public class GUI {

    ArrayList<Data> processer = new ArrayList<Data>();
    ArrayList<Data> grant = new ArrayList<Data>();

    GUI() {
        JFrame frame = new JFrame("Operating System Assignment");
        String[] choice = { "3", "4", "5", "6", "7", "8", "9", "10" };
        JComboBox<String> combo = new JComboBox<String>(choice);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Please select how many process you want:");
        ActionListener cbActionListener = new ActionListener() // after user choose how many process he/she want, let
                                                               // user insert input
        {
            public void actionPerformed(ActionEvent e) {
                int index = 0;
                JFrame frame2 = new JFrame("Table");
                JPanel panel2 = new JPanel();
                String item = (String) combo.getSelectedItem();
                int number = Integer.parseInt(item) + 1;
                JTable table = new JTable(number, 4);
                JButton btn = new JButton("Next");
                table.setValueAt("Process", 0, 0);
                table.setValueAt("Arrive Time", 0, 1);
                table.setValueAt("Burst Time", 0, 2);
                table.setValueAt("Priority", 0, 3);
                for (int i = 1; i < number; i++) {
                    table.setValueAt("P" + index, i, 0); // set each process initial identity(process number)
                    table.setValueAt(0, i, 3); // set 0 for each cell of priority
                    index++;
                }

            ActionListener btnActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    for(int i=1; i<number; i++){
                        String id = (String)table.getValueAt(i,0);
                        Object a = table.getValueAt(i,1);
                        Object b = table.getValueAt(i,2);
                        Object p = table.getValueAt(i,3);
                        try{
                            int at = Integer.parseInt(a.toString());
                            int bt = Integer.parseInt(b.toString());
                            int pt = Integer.parseInt(p.toString());
                            processer.add(new Data(id,at,bt,pt,false));

                        } catch (NullPointerException ex)
                        {
                            JDialog dialog = new JDialog(frame2,"Error!");
                            JPanel errorPanel = new JPanel();
                            JLabel errorMessage = new JLabel("Error: Null Input,please check again");
                            errorPanel.add(errorMessage);
                            dialog.add(errorPanel);
                            dialog.setSize(350,100);
                            dialog.setVisible(true);
                            dialog.setResizable(false);
                            break;
                        }
                    } 
                    //Generate grant chart
                    grant  = RR3(processer);
                    JFrame frame3 = new JFrame("Gantt Chart");      
                    JTable ganttChart = new JTable(2,grant.size());   
                    for (int i = 0; i < grant.size(); i++) {
                        ganttChart.setValueAt(grant.get(i).getProcesser(),0,i);
                        ganttChart.setValueAt(grant.get(i).getTime(),1,i);
                    }

                    //test  grant chart
                    System.out.println();
                    for (int i = 0; i < grant.size(); i++){
                        System.out.println(grant.get(i).toString2());
                    }
                    //-----------------------------
                    JButton calculate = new JButton("Calculation");
                    ActionListener calListener = new ActionListener() 
                    {
                        public void actionPerformed(ActionEvent cal)
                        {
                            ArrayList<Data> finishtime = new ArrayList<Data>();
                            finishtime = finish(grant,processer);
                            ArrayList<Data> Turn= new ArrayList<Data>();
                            ArrayList<Data> Waiting = new ArrayList<Data>();
                            int turn,wait = 0;

                            for (int i=0; i<finishtime.size(); i++){
                                turn = finishtime.get(i).getTime() - processer.get(i).getArrtime();
                                wait = turn - processer.get(i).getBrttime();
                                Turn.add(new Data(finishtime.get(i).getProcesser(),turn));
                                Waiting.add(new Data(finishtime.get(i).getProcesser(),wait));
                            }
                            int totalTurn = Sum (Turn);
                            int totalWait = Sum (Waiting);
                            double averageTurn = (double)totalTurn / (number -1);
                            double averageWait = (double)totalWait / (number -1);

                            int index = 0;
                            JFrame frame4 = new JFrame("Calculation");
                            JLabel text = new JLabel("Robin Round Scheduling");
                            JPanel panel = new JPanel(new BorderLayout());

                            JTable caltable = new JTable(number, 6);
                            caltable.setValueAt("Process", 0, 0);
                            caltable.setValueAt("Arrival Time", 0, 1);
                            caltable.setValueAt("Burst Time", 0, 2);
                            caltable.setValueAt("Finish Time", 0, 3);
                            caltable.setValueAt("Turnaround Time", 0, 4);
                            caltable.setValueAt("Waiting Time", 0, 5);

                            for (int i = 1; i < number; i++) {
                                caltable.setValueAt("P" + index, i, 0); 
                                caltable.setValueAt(processer.get(i-1).getArrtime(), i, 1); 
                                caltable.setValueAt(processer.get(i-1).getBrttime(), i, 2); 
                                caltable.setValueAt(finishtime.get(i-1).getTime(), i, 3);
                                caltable.setValueAt(Turn.get(i-1).getTime(), i, 4); 
                                caltable.setValueAt(Waiting.get(i-1).getTime(), i, 5);  
                                index++;
                            }
                            JTextArea calresult = new JTextArea("Total turnaround time is "  +  totalTurn + ".\n" + 
                                                                "Average turnaround time is " + averageTurn+ ".\n" +
                                                                "Total waiting time is " + totalWait + ".\n" + 
                                                                "Average waiting time is " + averageWait + ".\n");
                                                                
                            panel.add(text,"North");
                            panel.add(caltable,"Center");
                            panel.add(calresult,"South");
                            frame4.add(panel);
                            frame4.setSize(800,400);
                            frame4.setVisible(true);
                        }
                    };
                    JPanel board = new JPanel(new BorderLayout());
                    board.add(calculate,"South");
                    board.add(ganttChart,"North");
                    calculate.addActionListener(calListener);
                    frame3.add(board);
                    frame3.setSize(800,300);
                    frame3.setVisible(true);     
                }
                
            };
                btn.addActionListener(btnActionListener);
                panel2.add(table);
                panel2.add(btn);
                frame2.add(panel2);
                frame2.setSize(500, 300);
                frame2.setVisible(true);
                frame2.setResizable(false);
            }

        };

        combo.addActionListener(cbActionListener);
        panel.add(combo);
        panel.add(label);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
        frame.setResizable(false);
    }

//-------------------------------------------------------------------------------------

    public static int findfirst(ArrayList<Data> process){
        int i = 0;
        int arr = process.get(0).getArrtime(); 
        int pri = process.get(0).getPriority();
        int brt  = process.get(0).getBrttime();
        for (int j =1; j < process.size(); j++){

            if (process.get(j).getArrtime() < arr ){

                arr = process.get(j).getArrtime();
                i = j;
            }
            else if(process.get(j).getArrtime() == arr){
                
                if(process.get(j).getPriority() < pri  || process.get(j).getBrttime() < brt){
                    pri = process.get(j).getPriority();
                    i = j;
                    continue;
                }
            }
            j++;
        }     
        return i;
    }

    public static int Sum(ArrayList<Data> Turn){
        int sum = 0;
        for (int i = 0; i < Turn.size(); i++) {
            sum += Turn.get(i).getTime();
        }
        return sum;
    }

    public static void updatelist(int i,int start,int exetime,ArrayList<Data> temp){
        if(temp.get(i).getBrttime() <= 3) {
            temp.set(i,new Data(temp.get(i).getProcesser(),start + exetime,0,temp.get(i).getPriority(),true));
        }
        else
        temp.set(i,new Data(temp.get(i).getProcesser(),start + exetime,temp.get(i).getBrttime() - exetime,temp.get(i).getPriority(),false));
    }
    public static int checkBtime(int i, ArrayList<Data> process){
        if(process.get(i).getBrttime() >= 3)
            return 3;
        else if(process.get(i).getBrttime() == 2)
            return 2;
        else 
            return 1;
    }
    public static boolean check(int p, ArrayList<Data> temp) {
        int arrtime = temp.get(p).getArrtime();
        for (int i = 0; i < temp.size(); i++){
            if (temp.get(i).getArrtime() < arrtime &&  !temp.get(i).getStatus()){
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Data> RR3(ArrayList<Data> processer) {
        ArrayList<Data> temp = new ArrayList<Data>();
        ArrayList<Data> grant = new ArrayList<Data>();
        temp.addAll(processer);
        int first = findfirst(temp);
        int p = 0;
        int num  = p;
        boolean flag = false;
        
        grant.add(new Data(temp.get(first).getProcesser(), temp.get(first).getArrtime()));
        int start = temp.get(first).getArrtime();
        int exetime = checkBtime(first, temp);
        updatelist(first, start, exetime, temp);

        start = temp.get(first).getArrtime();

        for (int n = 0; n < temp.size(); n++) {
            while (temp.get(n).getStatus() == false) {

                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getStatus())
                        continue;
                    for (int j = 0; j < temp.size(); j++) {
                        if (i == j)
                            continue;

                        if (temp.get(j).getArrtime() < start && !temp.get(j).getStatus() 
                                && temp.get(j).getArrtime() < temp.get(i).getArrtime()){
                                    p = j;
                                    flag = check(p,temp);
                                    if (flag)
                                        break;
                                    else
                                        continue; 
                                }

                        else if (temp.get(j).getArrtime() < start && !temp.get(j).getStatus()
                                && temp.get(j).getArrtime() == temp.get(i).getArrtime()){

                                    if((temp.get(j).getPriority() < temp.get(i).getPriority())){
                                        p = j; break;}
                                    else
                                        {p = i;  break;}
                                } 
                            else 
                                p = i;                
                    }
                    num = p;
                    exetime = checkBtime(p, temp);
                    grant.add(new Data(temp.get(p).getProcesser(), start));
                    updatelist(p, start, exetime, temp);
                    start = temp.get(p).getArrtime();
                }
            }
        }
        grant.add(new Data(null, start));

        System.out.println();
        for (int i = 0; i < grant.size(); i++) {
            System.out.println(grant.get(i).toString2());
        }


        return grant;
    }
    public static int findfinish(String p, ArrayList<Data> grant) {
        int index = 0;
        int initial = 0;
        int time = 0;
        boolean flag = false;
        for (int i = 0; i < grant.size() - 1; i++) {
            if (p.equals(grant.get(i).getProcesser())) {
                time = grant.get(i).getTime();
                initial = i;
                break;
            }
        }
        for (int i = 0; i < grant.size(); i++) {

            if (i == grant.size() - 1) {
                if (p.equals(grant.get(i - 1).getProcesser()) && time < grant.get(i - 1).getTime()) {
                    index = i - 1;
                    break;
                }
            }
            if (p.equals(grant.get(i).getProcesser()) && time < grant.get(i).getTime()) {
                index = i;
                flag = checkfinishProcess(index,p,grant);
                if (flag)
                    break;
                else
                    continue;

            } else
                index = initial;

            
        }

        return index;
    }
    public static boolean checkfinishProcess (int index,String process, ArrayList<Data> grant){
        String p = process;
        for (int i = index + 1; i < grant.size(); i++){
            if(p.equals(grant.get(i).getProcesser()))
                return false;
        }
        return true;

    }
    
    public static ArrayList<Data>  finish(ArrayList<Data> grant,ArrayList<Data> processer){

        ArrayList<Data> finishlist = new ArrayList<Data>();
        
        int index;
        String p;
        for(int i=0; i<processer.size(); i++){
            p = processer.get(i).getProcesser();
            index = findfinish(p,grant);
            finishlist.add(new Data(p,grant.get(index+1).getTime()));
        }
        return finishlist;
    } 
//--------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        new GUI();
    }

}
