package com.android.parteek.chat;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecognitionListener, AdapterView.OnItemClickListener {
    CustomAdapter adapter;
    ListView listView;
//    EditText editText;
    ImageButton button;
    boolean side=false;
    SpeechRecognizer speechRecognizer;
    TextToSpeech textToSpeech;
    ProgressDialog pd;
    String msg;
    boolean isOn;
   CameraManager manager;
    boolean isAvail;
    String id;
    String time,date;
    String msg3,name="";
    String[] name1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean namee=false;
    Handler handler;
    String msgg="";
    int iBinderFlag;
    Messenger messenger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
////        Intent intent=new Intent(this,MyService.class);
////        this.startService(intent);
//        Intent i=getIntent();
        handler=new Handler();
        preferences=getSharedPreferences(Util.chatPrefs,MODE_PRIVATE);
        editor=preferences.edit();
        name1=new String[2];
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isAvail=getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        manager=(CameraManager)getSystemService(CAMERA_SERVICE);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                id=manager.getCameraIdList()[0];
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        isOn=false;
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Toast.makeText(MainActivity.this, "Welcome Sir,How can i help You", Toast.LENGTH_SHORT).show();
            }
        });
        date= DateFormat.getDateInstance().format(new Date());
        time=DateFormat.getTimeInstance().format(new Date());
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Listening.......");
        button=(ImageButton) findViewById(R.id.sendbut);
        listView=(ListView)findViewById(R.id.list_item);
        adapter=new CustomAdapter(this, R.layout.right);
        listView.setAdapter(adapter);
//        editText=(EditText)findViewById(R.id.edittext);
//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)){
//                    return sendChatMsg();
//                }
//                return false;
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setBackgroundResource(R.drawable.mijee);
//                pd.show();
                speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
            }
        });
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(editText.getText().toString().isEmpty()){
//                    button.setEnabled(false);
//                }else{
//                    button.setEnabled(true);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            sendChatMsg();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount()-1);
            }
        });
//        msg=i.getStringExtra("msgs");
//        sendChatMsg();


    }
    boolean sendChatMsg(){
       String time1=DateFormat.getTimeInstance().format(new Date());
       // adapter.add(new ChatMessage(editText.getText().toString(),side));
        adapter.add(new ChatMessage(msg,side,time1));
//        editText.setText("");
        side=!side;
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(conn!=null) {
//            bindService(new Intent(this, MyService.class), conn, BIND_AUTO_CREATE);
//        }
    }
//    ServiceConnection conn=new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            messenger=new Messenger(service);
//            Message message=new Message();
//            message.what=MyService.START_LISTENUNG;
//            try {
//                messenger.send(message);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
        turnOff();
//        if(conn!=null){
//            unbindService(conn);
//            conn=null;
//        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
//        pd.dismiss();
        button.setBackgroundResource(R.drawable.mije);
    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> rl=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(rl!=null&&rl.size()>0){
            msg=rl.get(0);
            sendChatMsg();
            chat();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    void turnOn(){

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setTorchMode(id,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void turnOff(){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setTorchMode(id,false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    void chat(){

        if(msg.toLowerCase().contains("hello")&&msg.toLowerCase().contains("baby")){
            msg= Util.baby;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.baby,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }
        else if(msg.toLowerCase().contains("how")&&msg.toLowerCase().contains("you")){
            msg= Util.howYou;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.howYou,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }
        else if(msg.toLowerCase().contains("your")&&msg.toLowerCase().contains("name")){
            //textToSpeech.speak(Util.name,TextToSpeech.QUEUE_FLUSH,null);
            msg= Util.name;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.name,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }
        else if(msg.toLowerCase().contains("who")&&msg.toLowerCase().contains("inventor")){
            //textToSpeech.speak(Util.inventor,TextToSpeech.QUEUE_FLUSH,null);
            msg= Util.inventor;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.inventor,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }
        else if(msg.toLowerCase().contains("on")&&msg.toLowerCase().contains("flash")){
            if(isAvail) {
                if(!isOn) {
                    //textToSpeech.speak(Util.flashOn, TextToSpeech.QUEUE_FLUSH, null);
                    msg = Util.flashOn;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.flashOn,TextToSpeech.QUEUE_FLUSH,null);
                            turnOn();
                            isOn=true;
                            sendChatMsg();
                        }
                    },2000);
                    //sendChatMsg();
                }else{
                    //textToSpeech.speak(Util.alreadyOn, TextToSpeech.QUEUE_FLUSH, null);
                    msg = Util.alreadyOn;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.alreadyOn,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    //sendChatMsg();
                }
            }else {
              //  textToSpeech.speak(Util.notflashOn,TextToSpeech.QUEUE_FLUSH,null);
                msg= Util.notflashOn;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.notflashOn,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                    }
                },2000);
               // sendChatMsg();
            }

        }
        else if(msg.toLowerCase().contains("off")&&msg.toLowerCase().contains("flash")){
            if(isOn){
               // textToSpeech.speak(Util.flashOff, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.flashOff;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.flashOff,TextToSpeech.QUEUE_FLUSH,null);
                        turnOff();
                        isOn=false;
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }else {
               // textToSpeech.speak(Util.alreadyOff, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.alreadyOff;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.alreadyOff,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }
        }else if(msg.toLowerCase().contains("hello")
                ||msg.toLowerCase().contains("hi")
                ||msg.toLowerCase().contains("hey")
                ||msg.toLowerCase().contains("hola")){
            name=preferences.getString(Util.namePrefs,"");
          // Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
            if(name.isEmpty()) {
            //    textToSpeech.speak(Util.userName, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.userName;
              //  sendChatMsg();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.userName,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                        namee=false;
                    }
                },2000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundResource(R.drawable.mijee);
                        speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                    }
                },4000);

            }else{
                namee=true;
                time();
            }
           // speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
        }
        else if(msg.toLowerCase().contains("my")&&msg.toLowerCase().contains("name")&&msg.toLowerCase().contains("is")
                ||msg.toLowerCase().contains("i")&&msg.toLowerCase().contains("am")
                ||msg.toLowerCase().contains("myself")){
            String hell="";
            if(msg.toLowerCase().contains("is")) {
                hell=msg.substring(msg.lastIndexOf("is")+2);
                if(!hell.matches(".*[a-z].*")) {
                    //textToSpeech.speak(Util.userName1, TextToSpeech.QUEUE_FLUSH, null);
                    msg = Util.userName1;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.userName1,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(R.drawable.mijee);
                            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                        }
                    },4000);
                    //sendChatMsg();
                }else {
                    name1 = msg.split("is");
                    time();
                }
            }else if(msg.toLowerCase().contains("myself")){
                hell=msg.substring(msg.lastIndexOf("myself")+6);
                if(!hell.matches(".*[a-z].*")) {
                    //textToSpeech.speak(Util.userName1, TextToSpeech.QUEUE_FLUSH, null);
                    msg = Util.userName1;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.userName1,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(R.drawable.mijee);
                            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                        }
                    },4000);
                    //sendChatMsg();
                }else {
                    name1 = msg.split("myself");
                    time();
                }
            }else if(msg.toLowerCase().contains("am")) {
                hell=msg.substring(msg.lastIndexOf("am")+2);
                if(!hell.matches(".*[a-z].*")) {
                   // textToSpeech.speak(Util.userName1, TextToSpeech.QUEUE_FLUSH, null);
                    msg = Util.userName1;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.userName1,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(R.drawable.mijee);
                            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                        }
                    },4000);
                   // sendChatMsg();
                }else {
                    name1 = msg.split("am");
                    time();
                }
            }


        }else if((msg.toLowerCase().contains("what's")&&msg.toLowerCase().contains("today"))||(msg.toLowerCase().contains("today's")&&msg.toLowerCase().contains("date"))||(msg.toLowerCase().contains("hey what's today"))){
            msg = Util.date1+": "+date+""+ Util.time1+": "+time;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.date1+date+ Util.time1+time, TextToSpeech.QUEUE_FLUSH, null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }else if((msg.toLowerCase().contains("search")&&msg.toLowerCase().contains("for"))||
                (msg.toLowerCase().contains("look")&&msg.toLowerCase().contains("for"))||
                (msg.toLowerCase().contains("google")&&msg.toLowerCase().contains("for"))||
                (msg.toLowerCase().contains("can")&&msg.toLowerCase().contains("search")&&msg.toLowerCase().contains("for"))||
                (msg.toLowerCase().contains("please")&&msg.toLowerCase().contains("look")&&msg.toLowerCase().contains("for")))
        {
            String hell=msg.substring(msg.lastIndexOf("for")+3);
            if(!hell.matches(".*[a-z].*")){
               // textToSpeech.speak(Util.webView3, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.webView3;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.webView3,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                    }
                },2000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundResource(R.drawable.mijee);
                        speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                    }
                },4000);
                //sendChatMsg();
            }else  {
                String[] msgs = msg.split("for");
                msg3 = msgs[1];
                msg = Util.webView1 + " " + msg3;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.webView2 + msg3, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }
        }
        else if((msg.toLowerCase().contains("google")&&msg.toLowerCase().contains("can"))||
                (msg.toLowerCase().contains("google")&&msg.toLowerCase().contains("please"))){
            String hell=msg.substring(msg.lastIndexOf("Google")+6);
            if(!hell.matches(".*[a-z].*")){
               // textToSpeech.speak(Util.webView4, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.webView4;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.webView4,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                    }
                },2000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundResource(R.drawable.mijee);
                        speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                    }
                },4000);
                //sendChatMsg();
            }else {
                String[] msgs = msg.split("Google");
                msg3 = msgs[1];
                msg = Util.webView1 + "" + msg3;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.webView2 + msg3, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }
        }else if(msg.toLowerCase().contains("delete")&&msg.toLowerCase().contains("name")){

            //textToSpeech.speak(Util.delete, TextToSpeech.QUEUE_FLUSH, null);
            msg = Util.delete;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editor.clear();
                    editor.commit();
                    textToSpeech.speak(Util.delete,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }else if(msg.toLowerCase().contains("you")&&msg.toLowerCase().contains("know my name")
                ||msg.toLowerCase().contains("do you know me")){
            name=preferences.getString(Util.namePrefs,"");
            if(name.length()>0) {
                msg = Util.myName + " " + name;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.myName + " " + name, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
               // sendChatMsg();
            }else{
               // textToSpeech.speak(Util.userName2, TextToSpeech.QUEUE_FLUSH, null);
                msg = Util.userName2;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.userName2,TextToSpeech.QUEUE_FLUSH,null);
                        sendChatMsg();
                    }
                },2000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundResource(R.drawable.mijee);
                        speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                    }
                },4000);
                //sendChatMsg();
            }
        }else if(msg.toLowerCase().contains("x")||msg.toLowerCase().contains("*")){
            String [] mul=msg.split("x");
            //String [] mul1=msg.split("by");
            int x= Integer.parseInt(mul[0].trim());
            int y= Integer.parseInt(mul[1].trim());
            int z=x*y;
            //textToSpeech.speak(Util.solution, TextToSpeech.QUEUE_FLUSH, null);
            msg = x+"*"+y+"="+z;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.solution,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }else if(msg.toLowerCase().contains("/")||
                msg.toLowerCase().contains("divided")&&msg.toLowerCase().contains("by")||
                msg.toLowerCase().contains("divide")&&msg.toLowerCase().contains("by")){
            String [] dev;
            String [] dev1;
            String [] dev2;
            int x=0,y=0;
            if(msg.contains("divide")){
                dev1=msg.split("divide");
                dev2=msg.split("by");
                x= Integer.parseInt(dev1[0].trim());
                y= Integer.parseInt(dev2[1].trim());
            }else if(msg.contains("/")){
                dev=msg.split("/");
                x= Integer.parseInt(dev[0].trim());
                y= Integer.parseInt(dev[1].trim());
            }else{
                dev1=msg.split("divided");
                dev2=msg.split("by");
                x= Integer.parseInt(dev1[0].trim());
                y= Integer.parseInt(dev2[1].trim());
            }
            float z=x/y;
            //textToSpeech.speak(Util.solution, TextToSpeech.QUEUE_FLUSH, null);
            msg = x+"/"+y+"="+z;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.solution,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
            //sendChatMsg();
        }
        else if(msg.toLowerCase().contains("+")||
                msg.toLowerCase().contains("plus")||
                msg.toLowerCase().contains("add")){
            String [] add;
            String [] add1;
            String [] add2;
            int x=0,y=0;
            if(msg.toLowerCase().contains("+")){
                add1=msg.split("\\+");
                x= Integer.parseInt(add1[0].trim());
                y= Integer.parseInt(add1[1].trim());
            }else if(msg.toLowerCase().contains("plus")){
                add1=msg.split("plus");
                x= Integer.parseInt(add1[0].trim());
                y= Integer.parseInt(add1[1].trim());
            }else if(msg.toLowerCase().contains("add")&&msg.toLowerCase().contains("and")){
                add=msg.split("add");
                String sdd=add[1];
                add2=sdd.split("and");
                x = Integer.parseInt(add2[0].trim());
                y= Integer.parseInt(add2[1].trim());
            }
            float z=x+y;
            //textToSpeech.speak(Util.solution, TextToSpeech.QUEUE_FLUSH, null);
            msg = x+"+"+y+"="+z;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.speak(Util.solution,TextToSpeech.QUEUE_FLUSH,null);
                    sendChatMsg();
                }
            },2000);
         //   sendChatMsg();
        }else if(msg.toLowerCase().contains("yes")||
                msg.toLowerCase().contains("yeah")||
                msg.toLowerCase().contains("yo")||
                msg.toLowerCase().contains("yep")||
                msg.contains("sure")){
            if(msgg.length()>0) {
                textToSpeech.speak(Util.webView2+msgg,TextToSpeech.QUEUE_FLUSH,null);
                msg = Util.webView1 + " " + msgg;
                sendChatMsg();
                msgg = "";
            } else{
                if(msg.contains("yes")){
                    //textToSpeech.speak(Util.what1,TextToSpeech.QUEUE_FLUSH,null);
                    msg=Util.what1;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.what1,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    //sendChatMsg();
                }else if(msg.contains("yeah")){
                    msg=Util.what2;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.what2,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                }else if(msg.contains("yo")){
                    msg=Util.what3;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.what3,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                }else if(msg.contains("yep")){
                    msg=Util.what4;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textToSpeech.speak(Util.what4,TextToSpeech.QUEUE_FLUSH,null);
                            sendChatMsg();
                        }
                    },2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.setBackgroundResource(R.drawable.mijee);
                            speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                        }
                    },4000);
                }
            }
        }
        else{
            textToSpeech.speak(Util.workInProg, TextToSpeech.QUEUE_FLUSH, null);
            msgg=msg;
            //msg = Util.webView1+" "+msg;
            msg = Util.workInProg;
            sendChatMsg();
            msg="";
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setBackgroundResource(R.drawable.mijee);
                    speechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(MainActivity.this));
                }
            },4000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(MainActivity.this,Help.class);
        startActivity(intent);
        overridePendingTransition(R.anim.side_in_right, R.anim.side_out_left);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatMessage chatMessage=adapter.getItem(position);
        if (chatMessage.message.contains(Util.webView1)){
            String msq=chatMessage.getMessage();
            String[] msq1=msq.split("about");
           Intent intent=new Intent(this,webview.class);
           intent.putExtra("keyword",msq1[1]);
           startActivity(intent);
            overridePendingTransition(R.anim.side_in_right, R.anim.side_out_left);
        }
    }
    void time(){
        if(!namee) {
            name = name1[1];
        }
        editor.putString(Util.namePrefs,name);
        editor.commit();
        //Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
        String[] ti=time.split(":");
        String[] ti1=time.split("\\s+");
        int t= Integer.parseInt(ti[0]);
        if(ti1[1].contains("AM")){
            if(t>=5&&t<12){
                msg = "Hello"+name+ Util.wishMor;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak("Hello "+name+ Util.wishMor, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
          //      sendChatMsg();
            }else{

                msg = "Hello"+name+ Util.wishnight;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak("Hello "+name+ Util.wishnight, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
               // sendChatMsg();
            }
        }else{
            if(t<=6&&t==12){
                msg = "Hello"+name+ Util.wisheve;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak("Hello "+name+ Util.wishaft, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }else if(t>=6&&t<=10){
                msg = "Hello"+name+ Util.wisheve;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak("Hello "+name+ Util.wisheve, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
                //sendChatMsg();
            }else {

                msg = "Hello"+name+ Util.wishnight;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech.speak(Util.wishnight, TextToSpeech.QUEUE_FLUSH, null);
                        sendChatMsg();
                    }
                },2000);
               // sendChatMsg();
            }
        }
    }

}
