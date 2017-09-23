package com.android.parteek.chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyService extends Service {
    AudioManager audioManager;
    SpeechRecognizer speechRecognizer;
    Intent speechIntent;
    Messenger messenger=new Messenger(new IncomingHandler(this));
    static final int START_LISTENUNG =1;
    static final int CANCEL =2;
    boolean mIsListening;
    volatile boolean mIsCountDownOn;
    private boolean mIsStreamSolo;
    MainActivity mainActivity;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainActivity=new MainActivity();
        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechRecognizerListener());
        speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
    }

    class IncomingHandler extends Handler{
        WeakReference<MyService> mtarget;
        IncomingHandler(MyService target){
            mtarget=new WeakReference<MyService>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MyService target =mtarget.get();
            switch (msg.what){
                case START_LISTENUNG:
                    if(!mIsStreamSolo){
                        audioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL,true);
                        //audioManager.setStreamSolo(AudioManager.STREAM_ALARM,true);
//                        audioManager.setStreamSolo(AudioManager.STREAM_NOTIFICATION,true);
//                        audioManager.setStreamSolo(AudioManager.STREAM_RING,true);
//                        audioManager.setStreamSolo(AudioManager.STREAM_MUSIC,true);
//                        audioManager.setStreamSolo(AudioManager.STREAM_SYSTEM,true);

                        mIsStreamSolo=true;
                    }
                    if(!target.mIsListening){
                        target.speechRecognizer.startListening(target.speechIntent);
                        target.mIsListening=true;
                    }
                    break;
                case CANCEL:
                    if(mIsStreamSolo){
                        audioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL,false);
                        mIsStreamSolo=false;
                    }
                    target.speechRecognizer.cancel();
                    target.mIsListening=false;
                    break;
            }
        }
    }
    CountDownTimer countDownTimer=new CountDownTimer(5000,5000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            mIsCountDownOn=false;
            Message message=Message.obtain(null,CANCEL);
            try {
                messenger.send(message);
                message=Message.obtain(null,START_LISTENUNG);
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mIsCountDownOn){
            countDownTimer.cancel();
        }
        if(speechRecognizer!=null){
            speechRecognizer.destroy();
        }
    }
    class SpeechRecognizerListener implements RecognitionListener{

        @Override
        public void onReadyForSpeech(Bundle params) {
            mIsCountDownOn=true;
            countDownTimer.start();
        }

        @Override
        public void onBeginningOfSpeech() {
            if(mIsCountDownOn){
                mIsCountDownOn=false;
                countDownTimer.cancel();
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            if(mIsCountDownOn){
                mIsCountDownOn=false;
                countDownTimer.cancel();
            }
            mIsListening=false;
            Message message=Message.obtain(null,START_LISTENUNG);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> rl=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(rl!=null&&rl.size()>0){
                String msg=rl.get(0);
                mainActivity.sendChatMsg();
//                Intent i=new Intent(MyService.this,MainActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.putExtra("msgs",msg);
//                startActivity(i);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

}
