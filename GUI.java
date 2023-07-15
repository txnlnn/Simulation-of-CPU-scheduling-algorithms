import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;

public class GUI {

    private String combo;
    private int number;

    ArrayList<Data> processer = new ArrayList<Data>();
    ArrayList<Data> SJFfinallist = new ArrayList<>();
    ArrayList<Data> nSJFlist = new ArrayList<>();
    ArrayList<Data> SJFlist = new ArrayList<>();

    GUI() {

        JFrame frame = new JFrame("Operating System Assignment");
        JPanel panel = new JPanel();

        String[] choice = { "3", "4", "5", "6", "7", "8", "9", "10" };
        JComboBox<String> combo = new JComboBox<String>(choice);

        JLabel label = new JLabel("Please select how many process you want:");
        combo.addActionListener(new cbActionListener());

        panel.add(combo);
        panel.add(label);
        frame.add(panel);

        frame.setSize(400, 200);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private class cbActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JComboBox cb = (JComboBox) e.getSource();
            combo = (String) cb.getSelectedItem();
            number = Integer.parseInt(combo) + 1;
            tableview();

        }

    }

    void tableview() {

        JFrame frame = new JFrame("Table View");
        JPanel panel2 = new JPanel();
        JTable table = new JTable(number, 4);

        table.setValueAt("Process", 0, 0);
        table.setValueAt("Arrive Time", 0, 1);
        table.setValueAt("Burst Time", 0, 2);
        table.setValueAt("Priority", 0, 3);
        int index = 0;
        for (int i = 1; i < number; i++) {
            table.setValueAt("P" + index, i, 0); // set each process initial identity(process number)
            table.setValueAt(0, i, 3); // set 0 for each cell of priority
            index++;
        }

        JButton btn = new JButton("Next");

        ActionListener btnActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                processer.removeAll(processer);

                for (int i = 1; i < number; i++) {
                    String id = (String) table.getValueAt(i, 0);
                    Object a = table.getValueAt(i, 1);
                    Object b = table.getValueAt(i, 2);
                    Object p = table.getValueAt(i, 3);
                    try {
                        int at = Integer.parseInt(a.toString());
                        int bt = Integer.parseInt(b.toString());
                        int pt = Integer.parseInt(p.toString());
                        processer.add(new Data(id, at, bt, bt, pt, false));

                    } catch (NullPointerException ex) {
                        JDialog dialog = new JDialog(frame, "Error!");
                        JPanel errorPanel = new JPanel();
                        JLabel errorMessage = new JLabel("Error: Null Input,please check again");
                        errorPanel.add(errorMessage);
                        dialog.add(errorPanel);
                        dialog.setSize(350, 100);
                        dialog.setVisible(true);
                        dialog.setResizable(false);
                        break;
                    }
                }

                SJFfinallist.removeAll(SJFfinallist);
                nSJFlist.removeAll(nSJFlist);
                SJFlist.removeAll(SJFlist);
                getNonPreemptiveSJF();
                gantchart(nSJFlist, nSJFlist, "Non-Preemptive Short Job Scheduling");

                getPreemptiveSJF();
                gantchart(SJFlist, SJFfinallist, "Preemptive Short Job Scheduling");
            }

        };

        btn.addActionListener(btnActionListener);
        panel2.add(table);
        panel2.add(btn);

        frame.add(panel2);
        frame.setSize(500, 300);
        frame.setResizable(false);
        frame.setVisible(true);

    }

    void gantchart(ArrayList<Data> list, ArrayList<Data> list2, String string) {

        JFrame frame2 = new JFrame(string);
        JPanel topPnl = new JPanel();
        ArrayList<Data> gantt = new ArrayList<Data>();

        gantt = list;
        JTable ganttChart = new JTable(2, gantt.size() + 1);
        for (int i = 0; i < gantt.size(); i++) {
            ganttChart.setValueAt(gantt.get(i).getProcesser(), 0, i);
            ganttChart.setValueAt(gantt.get(i).getStarttime(), 1, i);

        }

        ganttChart.setValueAt("", 0, gantt.size());
        ganttChart.setValueAt(gantt.get(gantt.size() - 1).getTime(), 1, gantt.size());

        topPnl.add(ganttChart, BorderLayout.CENTER);

        calculate(frame2, topPnl, list2, string);

        frame2.setSize(900, 400);
        frame2.setResizable(true);
        frame2.setVisible(true);

    }

    void calculate(JFrame frame2, JPanel topPnl, ArrayList<Data> finallist, String string) {

        ArrayList<Data> Turn = new ArrayList<Data>();
        ArrayList<Data> Waiting = new ArrayList<Data>();
        int turn, wait = 0;

        finallist = sortOrderList(finallist);

        for (int i = 0; i < finallist.size(); i++) {
            turn = finallist.get(i).getTime() - processer.get(i).getArrtime();
            wait = turn - processer.get(i).getBrttime();
            Turn.add(new Data(finallist.get(i).getProcesser(), turn));
            Waiting.add(new Data(finallist.get(i).getProcesser(), wait));
        }

        System.out.println();
        for (int i = 0; i < finallist.size(); i++) {
            System.out.println(finallist.get(i).toString2());
        }

        JTable caltable = new JTable(number, 6);
        caltable.setValueAt("Process", 0, 0);
        caltable.setValueAt("Arrival Time", 0, 1);
        caltable.setValueAt("Burst Time", 0, 2);
        caltable.setValueAt("Finish Time", 0, 3);
        caltable.setValueAt("Turnaround Time", 0, 4);
        caltable.setValueAt("Waiting Time", 0, 5);

        for (int i = 1; i < number; i++) {
            caltable.setValueAt(finallist.get(i - 1).getProcesser(), i, 0);
            caltable.setValueAt(processer.get(i-1).getArrtime(), i, 1); 
            caltable.setValueAt(processer.get(i-1).getBrttime(), i, 2); 
            caltable.setValueAt(finallist.get(i-1).getTime(), i, 3);
            caltable.setValueAt(Turn.get(i-1).getTime(), i, 4); 
            caltable.setValueAt(Waiting.get(i-1).getTime(), i, 5);  
        }


        calculate2(frame2, topPnl, Turn, Waiting, caltable);

    }

    private ArrayList<Data> sortOrderList(ArrayList<Data> orderArrayList) {
        Collections.sort(orderArrayList, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                String s1 = o1.getProcesser();
                String s2 = o2.getProcesser();

                return s1.compareToIgnoreCase(s2);

            }
        });

        return orderArrayList;
    }

    void calculate2(JFrame frame2, JPanel topPnl, ArrayList<Data> turn, ArrayList<Data> wait, JTable caltable) {

        JPanel panel3 = new JPanel(new BorderLayout());
        JPanel board = new JPanel(new BorderLayout());

        int totalTurn = Sum(turn);
        int totalWait = Sum(wait);
        double averageTurn = (double) totalTurn / (number - 1);
        double averageWait = (double) totalWait / (number - 1);

        JTextArea calresult = new JTextArea("Total turnaround time is " + totalTurn + ".\n" +
                "Average turnaround time is " + averageTurn + ".\n" +
                "Total waiting time is " + totalWait + ".\n" +
                "Average waiting time is " + averageWait + ".\n");

        panel3.add(caltable, "Center");
        panel3.add(calresult, "South");
        board.add(topPnl, BorderLayout.NORTH);
        board.add(panel3, BorderLayout.CENTER);
        frame2.add(board);

        frame2.setSize(800, 400);
        frame2.setResizable(false);
        frame2.setVisible(true);

    }

    void sorting(ArrayList<Data> list) {

        Collections.sort(list); // sort burst time
        for (int y = 0; y < list.size(); y++) { // when burst time the same, make sure FCFS
            for (int i = 0; i < list.size(); i++) {
                if (list.get(y).getProcessbrttime() == list.get(i).getProcessbrttime() && y != i) {
                    if (list.get(y).getArrtime() > list.get(i).getArrtime()) {

                        list.add(list.get(y));
                        list.remove(y);

                    } else if (list.get(y).getArrtime() < list.get(i).getArrtime() && y != i) {

                        list.add(list.get(i));
                        list.remove(i);

                    }

                }
            }
        }
        Collections.sort(list);

    }

    void getNonPreemptiveSJF() {

        ArrayList<Data> nSJF = new ArrayList<>();
        nSJF.addAll(processer); // copy the processer item

        sorting(nSJF);

        int p = 0, starttime, tot = 0, processtime = 0, fntime = 0;

        while (true) {

            if (tot == nSJF.size())// all nSJF item is true will break
                break;

            for (int j = 0; j < nSJF.size(); j++) { // Ensure all the process have run once
                starttime = processtime;
                for (int i = 0; i < nSJF.size(); i++) {
                    if (nSJF.get(i).getStatus() == false && p == 0 && nSJF.get(i).getArrtime() <= processtime) {

                        nSJF.get(i).setStatus(true);

                        fntime = processtime + nSJF.get(i).getProcessbrttime();

                        nSJFlist.add(new Data(nSJF.get(i).getProcesser(), starttime, fntime));

                        processtime = fntime;

                        tot++;
                        p = 1; // restart the for loop once it already process
                    }
                }
                p = 0;
            }
            processtime++; // if the process arrival time is bigger than processtime
        }

    }

    void getPreemptiveSJF() {

        ArrayList<Data> pSJF = new ArrayList<>();
        pSJF.addAll(processer);

        for (int e = 0; e < pSJF.size(); e++) { // processer will also set true while non SJF set true
            pSJF.get(e).setStatus(false);
        }

        sorting(pSJF);

        int p = 0, starttime, processtime = 0, fntime = 0;

        while (true) {

            for (int j = 0; j < (number - 1) * 3; j++) { // Ensure all the process have run once
                for (int i = 0; i < pSJF.size(); i++) {
                    starttime = processtime;

                    if ((pSJF.get(i).getStatus() == false && p == 0 && pSJF.get(i).getArrtime() <= processtime)) {

                        if (i == 0) { // if the i is the the smallest burst time

                            fntime = processtime + pSJF.get(i).getProcessbrttime();
                            pSJF.get(i).setprocessBrttime(0);
                            processtime = fntime;

                        }

                        else {

                            boolean y = true;
                            int o = 0, flag = 0;
                            for (int k = 0; k < i; k++) { // get nearest arrive time from the smaller burst time
                                for (int t = 0; t < i; t++) { // compare the nearest arrive time
                                    if (t != k) {// cannot compare it own
                                        if (pSJF.get(k).getArrtime() >= pSJF.get(t).getArrtime() && y == true) {
                                            y = false;// if arrive time bigger than one of arrive time ->false
                                        }
                                        flag = 1;
                                    }
                                }
                                o = k; // no one to compare, so directly equal
                                if (y == true && flag == 1) { // make sure it have pass through second if condition
                                    o = k;
                                    break;
                                }
                                y = true;
                            }

                            while (processtime < pSJF.get(o).getArrtime() // add until the next arrive time
                                    && pSJF.get(i).getProcessbrttime() >= 1) {

                                processtime++;
                                pSJF.get(i).setprocessBrttime(pSJF.get(i).getProcessbrttime() - 1);

                            }
                            fntime = processtime;

                        }

                        p = 1; // restart the for loop once it already process
                        SJFlist.add(
                                new Data(pSJF.get(i).getProcesser(), starttime, fntime));
                        if (pSJF.get(i).getProcessbrttime() == 0) {

                            pSJF.get(i).setStatus(true);
                            SJFfinallist.add(
                                    new Data(pSJF.get(i).getProcesser(), starttime, fntime));

                        }
                    }
                }
                for (int i = 0; i < pSJF.size(); i++) { // remove the status which is true
                    if (pSJF.get(i).getStatus() == true) {
                        pSJF.remove(i);
                    }
                }
                sorting(pSJF);

                p = 0;

                if (pSJF.size() == 0)
                    break;

            }
            if (pSJF.size() == 0)
                break;

            processtime++; // if the process arrival time is bigger than processtime

        }

    }

    public static int Sum(ArrayList<Data> Turn) {
        int sum = 0;
        for (int i = 0; i < Turn.size(); i++) {
            sum += Turn.get(i).getTime();
        }
        return sum;
    }

    public static void main(String[] args) {
        new GUI();
    }

}
