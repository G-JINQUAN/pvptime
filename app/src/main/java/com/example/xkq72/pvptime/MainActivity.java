package com.example.xkq72.pvptime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button btnGameStart,btnAddTime,btnReduceTime,btnKillDragon;
    private TextView tvGameTime,tvCountDown,tvDragonNum;

    public boolean runState=false;
    private Integer i=0;
    private Timer timer;
    private TimerTask task;
    public long gameTime=0,gameStartTime=0,gameTimeNew=0,minute,second,countDownTime,countDown;
    public int dragonNum=0;//拿龙次数
    public String[] gameTimeStr={"00","00"};
    public Boolean dragon=false;//是否正在刷龙（非兵）
    public String dragonStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnGameStart=(Button)findViewById(R.id.btnGameStart);
        tvGameTime=(TextView)findViewById(R.id.tvGameTime);
        tvCountDown=(TextView)findViewById(R.id.tvCountDown);
        btnAddTime=(Button)findViewById(R.id.btnAddTime);
        btnReduceTime=(Button)findViewById(R.id.btnReduceTime);
        btnKillDragon=(Button)findViewById(R.id.btnKillDragon);
        tvDragonNum=(TextView)findViewById(R.id.tvDragonNum);


        //游戏开始按钮点击事件
        btnGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(runState){
                    runState=false;
                    timer.cancel();
                    task.cancel();
                    btnGameStart.setText("游戏开始");
                }else{

                    gameStartInit();//初始化
                    //初始化一些变量值
                    dragonNum=0;//初始拿龙次数
                    dragon=false;//初始刷兵模式
                    dragonStr="";
                    i=0;//刷龙的波数记数

                    //一点击按钮，获取当前的时间戳
                    gameStartTime=System.currentTimeMillis();
                    //记录第一次刷兵的时间，即加上10秒，
                    countDownTime=gameStartTime+10000;//第一次刷兵是10秒后。
                    //这里进入时钟周期循环操作，
                    runState=true;
                    timer.schedule(task, 0, 10);
                    btnGameStart.setText("游戏结束");
                }
            }
        });
        //对方拿龙按钮事件
        btnKillDragon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                killDragon();
            }
        });
        //游戏时间+1秒
        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTime();
            }
        });
        //游戏时间-1秒
        btnReduceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceTime();
            }
        });
    }

    public void gameStartInit(){
        task=new TimerTask() {
            @Override
            public void run() {
                //这里的循环是每10毫秒获取一次，
                //gameTimeNew=System.currentTimeMillis();//获取系统最新时间戳（毫秒）
                gameTime=System.currentTimeMillis()-gameStartTime;//这里算得实际游戏进行的时间（毫秒）

                //转换成为分和秒格式。
                minute=gameTime/60000;//分钟
                second=gameTime/1000-minute*60;//秒
                //下面进行格式处理，处理为00：00格式
                if(minute<10){
                    gameTimeStr[0]="0"+String.valueOf(minute);
                }else{
                    gameTimeStr[0]=String.valueOf(minute);
                }
                if(second<10){
                    gameTimeStr[1]="0"+String.valueOf(second);
                }else{
                    gameTimeStr[1]=String.valueOf(second);
                }
                //进行时间显示输出
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvGameTime.setText(gameTimeStr[0]+":"+gameTimeStr[1]);
                    }
                });

                //处理刷兵倒计时，
                countDown=countDownTime-System.currentTimeMillis();

                if(countDown<=0){
                    //当前的倒计时到0之后，怎么处理
                    if(i>=3){
                        //如果已经刷了三波龙。则退出刷龙状态
                        dragon=false;
                    }
                    if(dragon){
                        //如果对方拿龙了。倒计时间变少
                        countDownTime=System.currentTimeMillis()+31000;
                        //刷龙共三次，计数。
                        i++;
                        dragonStr="("+i.toString()+"/3)";
                    }else{
                        i=0;//不是刷龙状态，设置变量为0；
                        countDownTime=System.currentTimeMillis()+33250;
                        dragonStr="";
                    }
                }
                //进行输出显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCountDown.setText(String.format("%.2f",((double) countDown/1000)));
                        tvDragonNum.setText(String.valueOf(dragonNum)+dragonStr);
                    }
                });
            }
        };
        timer=new Timer();
    }
    public void killDragon(){
        if(!dragon){
            //非刷龙状态，才会响应。
            dragonNum++;//拿龙次数+1
            dragon=true;//并设置当前为刷龙状态，而非刷兵状态。
            dragonStr="(0/3)";
        }
    }
    public void addTime(){
        gameStartTime=gameStartTime-1000;
        countDownTime=countDownTime-1000;
    }
    public void reduceTime(){
        gameStartTime=gameStartTime+1000;
        countDownTime=countDownTime+1000;
    }
}
