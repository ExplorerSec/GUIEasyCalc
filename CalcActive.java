import java.awt.*;
import java.awt.event.*;

public class CalcActive{
    private static void runGUI(CalcScreen thescreen){
        
        // 窗体与关闭按钮
        Frame frame = new Frame();
        frame.setTitle("简易计算器0.3 布局样式，可显示反馈");
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
        for(int i=0;i<buttonlabel_list.length;i++)
        {
            buttons[i]=new Button(buttonlabel_list[i]);
            MyButtonListener myButtonListener =new MyButtonListener();
            myButtonListener.labelAu(label);
            myButtonListener.screenAu(thescreen);
            buttons[i].addActionListener(myButtonListener);

            panel2.add(buttons[i]);
        }

        frame.add(panel2,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        CalcScreen theScreen = new CalcScreen();
        runGUI(theScreen);
    }
}

class CalcFunction{  // 波兰式转化解析器 -未实现
    private StringBuffer stringBuffer;

    private void inputString(StringBuffer s){ // 读入待处理字符缓冲区
        this.stringBuffer = s;
    }

    private void preAnalyse(){ // 字符串预处理
        // 运算符开头的情况
        char s_head=this.stringBuffer.charAt(0);
        if(s_head == '+'|| s_head == '-'){
            this.stringBuffer.insert(0, "0");
        }
        if(s_head == '*'|| s_head == '/'){
            this.stringBuffer.delete(0, 1);
        }
        // 去括号
    }

    private int errorAnalyse(){ // 错误分析
        // 加减乘除的重复(多个运算符挨着)
        String tmp1 = new String(this.stringBuffer.toString());
        int funcPosition=-2;
        for(int i=0;i<tmp1.length();i++){
            if(tmp1.charAt(i)=='+'||tmp1.charAt(i)=='-'||tmp1.charAt(i)=='*'||tmp1.charAt(i)=='/')
                if(i-funcPosition==1) return 0xf1; // 运算符冗余
                else funcPosition = i; 
        }
        // 小数点重复(一个数有2个及以上小数点)
        funcPosition = -1;
        for(int i=0;i<tmp1.length();i++){ // 寻找第一个小数点
            if(tmp1.charAt(i)=='.'){
                funcPosition = i;
                break;
            }//to do
        }
        // 无错误
        return 0; 
    }

    private int calcString(){ // 最终运算
        
        return 114514;
    }
    
    private void outString(){
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append(calcString());
    }

    public void calcFunc(StringBuffer s){
        inputString(s);
        preAnalyse();
        errorAnalyse();
        calcString();
        outString();
    }
}


class CalcScreen{ // 计算器显示屏

    private StringBuffer screen = new StringBuffer("0"); // 屏幕字符缓冲区
    public String getCalcScreen(){ // 返回屏幕字符串
        return screen.toString();
    }
    public void updateCalcScreen(String c){ // 更新屏幕字符串
        if(c.equals("AC")){ // 清屏的情况
            screen.delete(0, screen.length());
            screen.append("0");
        }
        else if(c.equals("CE")){ // 退格的情况
            screen.delete(screen.length()-1, screen.length());
        }
        else if(c.equals("=")){ // 计算结果的情况
            // 波兰式转化与解析
            CalcFunction cc = new CalcFunction();
            cc.calcFunc(screen);

        }
        else{ // 否则将按下的对应标签追加到显示屏
            
            if(screen.toString().equals("0")) // 判断删除多余前导0
                screen.delete(0,screen.length()); 

            screen.append(c);
        }
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


/* 未使用的代码
 *  
 * 
 */
