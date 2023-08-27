import java.awt.*;
import java.awt.event.*;

public class CalcActive{ // 主 GUI 布局
    private static void runGUI(CalcScreen thescreen){
        // 窗体与关闭按钮
        Frame frame = new Frame();
        frame.setTitle("简易计算器0.4-加减乘除");
        frame.setFont(new Font("宋体", Font.BOLD, 27));
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        // 整体边界布局构成两个容器
        BorderLayout borderLayout = new BorderLayout();
        frame.setLayout(borderLayout);  
        // 上方液晶屏
        Panel panel1 = new Panel();
        panel1.setLayout(new GridLayout());
        panel1.setBackground(Color.GRAY);
        Label label = new Label("0",Label.RIGHT);
        panel1.add(label);
        frame.add(panel1,BorderLayout.NORTH);
        // 下方按钮区
        Panel panel2 = new Panel();
        panel2.setBackground(Color.GRAY);// 灰色背景确认容器区域位置
        GridLayout layout2 = new GridLayout();
        layout2.setRows(4);
        panel2.setLayout(layout2);
        // 按钮表
        String[] buttonlabel_list={"7","8","9","CE","AC","4","5","6","+","-","1","2","3","*","/","(","0",")",".","="};
        Button[] buttons = new Button[buttonlabel_list.length];
        // 按钮监听器
        MyButtonListener myButtonListener =new MyButtonListener();
            myButtonListener.labelAu(label);
            myButtonListener.screenAu(thescreen);
        // 绑定按钮监听器
        for(int i=0;i<buttonlabel_list.length;i++)
        {
            buttons[i]=new Button(buttonlabel_list[i]);
            buttons[i].addActionListener(myButtonListener);
            panel2.add(buttons[i]);
        }
        // 绑定 frame
        frame.add(panel2,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        CalcScreen theScreen = new CalcScreen();
        runGUI(theScreen);
    }
}

class CalcScreen{ // 计算器显示屏与按钮读入

    private StringBuffer screen = new StringBuffer("0"); // 屏幕字符缓冲区
    private boolean calc_ed = false; // 记录是否已经计算了结果 

    public void updateCalcScreen(String c){ // 更新屏幕字符串
 
        if(c.equals("AC")){ // 清屏的情况
            screen.delete(0, screen.length());
            screen.append("0");
            this.calc_ed = false;
        }
        else if(c.equals("CE")){ // 退格的情况
            screen.delete(screen.length()-1, screen.length());
            this.calc_ed = false;
        }
        else if(c.equals("+")||c.equals("-")||c.equals("*")||c.equals("/")){ // 运算符的情况
            char tmp_c = screen.charAt(screen.length()-1);
            if(tmp_c == '+' || tmp_c == '-' ||tmp_c == '*' || tmp_c == '/') // 防止出现紧邻两个运算符的情况
                screen.delete(screen.length()-1, screen.length());      
            screen.append(c); // 添加运算符
            this.calc_ed = false;
        }
        else if(c.equals("=")){ // 计算结果
            // 简单计算功能
            SimpleCalcModule scm = new SimpleCalcModule();
            double calc_result = scm.calcFunc(screen.toString());
            // 结果写入屏幕
            screen.delete(0, screen.length());
            screen.append(""+calc_result); ;
            // 标记已计算结果
            this.calc_ed = true ;
        }
        else{ // 否则将按下的对应标签追加到显示屏
            
            if(calc_ed){ // 判断是否已经计算过结果，如果是，清空屏幕
                screen.delete(0, screen.length());
                calc_ed = false;
            }

            if(screen.toString().equals("0")) // 判断删除多余前导0
                screen.delete(0,screen.length()); 

            screen.append(c); // 写入按下的数字
        }
    }
    public String getCalcScreen(){ // 返回屏幕字符串
        return screen.toString();
    }
}

class MyButtonListener implements ActionListener{ // 按钮监听器
    
    private Label label_point; // 标签引用
    public void labelAu(Label label){
        this.label_point = label;
    }
    
    private CalcScreen myScreen;  // 显示屏引用
    public void screenAu(CalcScreen cs){
        this.myScreen = cs;
    }
    
    public void actionPerformed(ActionEvent e){ // 重写实现的监听反馈      
        
        myScreen.updateCalcScreen(e.getActionCommand()); 
        
        label_point.setText(myScreen.getCalcScreen());

    }
}

class SimpleCalcModule{ // 简单计算功能模块-仅支持加减乘除，不支持括号
    private String[] regexes;

    private void hugeToSmall(String s){ // 读取四则运算表达式，并拆为只有乘除的加起来的小式子
        regexes = s.replaceAll("-", "+-").split("\\+"); 
    }

    private double SingleMulAndDvi(String regex){ // 计算仅含乘除号的数字表达式的结果，不能有括号
        // 分离运算符号
        StringBuilder symbols = new StringBuilder("@");
        for(int i=0;i<regex.length();i++){
            if(regex.charAt(i) =='*'|| regex.charAt(i) =='/')
                symbols.append(regex.charAt(i));
        }
        // 分离数字
        String[] numbers = regex.split("\\/|\\*") ;
        // 乘除运算
        double sum = Double.valueOf(numbers[0]) ;
        for(int i=1;i<symbols.length();i++){
            if(symbols.charAt(i)=='*'){
                sum = sum * Double.valueOf(numbers[i]);
            }
            else if(symbols.charAt(i)=='/'){
                sum = sum / Double.valueOf(numbers[i]);
            }
        }
        // 返回
        return sum;
    }

    public double calcFunc(String s){
        hugeToSmall(s);
        double sum = 0;
        for(String i:regexes){
            sum = sum + SingleMulAndDvi(i);
        }
        return sum;
    }
    
}
