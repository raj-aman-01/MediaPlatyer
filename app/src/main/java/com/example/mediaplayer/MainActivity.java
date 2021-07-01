package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener
, MediaPlayer.OnCompletionListener {


    //view object Declaration

    ListView lvSongs;
    SeekBar  music;
    SeekBar volume;
    Button play,pause,next,previous;


    //data sources
    //1.Songs Names
    String[] songsName= {"Bekhayali","Tera Ban Jaunga","Tujhe Kitna Cahne Lage","Waqt Ki Batien"};

    //2 Audio Resources ID's
    int[] songId={R.raw.bekhyali,R.raw.terabanjaunga,R.raw.tujhekitnacahnelage,R.raw.waqtkibatien};

    //declaring media player variable
    MediaPlayer mp;

    //current Song
    int currentSongIdx;

    //current Volume
    float currentVolume=0.5f;

    //background thread which sets the progress of music with seek bar
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            music.setProgress(mp.getCurrentPosition());

            handler.postDelayed(runnable,1000); // start thread again after 1 sec delay
        }
    };

    //handler
    Handler handler = new Handler();

    // Android handles all ui operation and input events  from one single thread
    // if you need to update UI  from other thread then we need to synchronize with UI thread or Main thread

    // A handler class allows communication back with UI thread or main thread with other background thread


    //  Two main uses of Handler
    //1. to schedule a runnable thread to be executed at some point in future
    //2. to enque an action to be performed on differnt thread



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setup Ui is called

        setupUi();





    }



    void setupAudioClip(int idx)
    {
        currentSongIdx= idx;
        if(mp!=null)
        {
            mp.stop();
            //media player reset for stopping the playing song
            mp.reset();
        }
        mp= MediaPlayer.create(this,songId[idx]);
        //attach on Completion
        mp.setOnCompletionListener(this);
        mp.start();

        // schedule background thread to update seekbar
        handler.post(runnable);// add to queue and execute
        handler.postAtFrontOfQueue(runnable); // add thread at front of queue( fast execution)
        handler.postAtTime(runnable,1000);// add thread to queue  and execute after 1 sec





        //setting the default volume to 50%
        mp.setVolume(currentVolume,currentVolume);
        volume.setProgress((int)(currentVolume*100));

        //set
        music.setMax(mp.getDuration());

    }

    //Button Event handler method

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.play:

                if(mp!=null)
                {

                    mp.start();
                }
                break;
            case R.id.pause:
                if(mp!=null)
                {

                    mp.pause();
                }
                break;
            case R.id.next:
                if(mp!=null)
                {
                    if(currentSongIdx<songId.length-1)
                    {
                        setupAudioClip(currentSongIdx+1);
                    }
                    else
                    {
                        setupAudioClip(0);
                    }

                }
                break;
            case R.id.previous:
                if(mp!=null)
                {
                    if(currentSongIdx>0)
                    {
                        setupAudioClip(currentSongIdx-1);
                    }
                    else
                    {
                        setupAudioClip(songId.length-1);
                    }

                }
                break;
        }

    }



    void setupUi()
    {
        //initalize the  view objects


        lvSongs= findViewById(R.id.Songs);
        music= findViewById(R.id.seekBarMusic);
        volume= findViewById(R.id.seekBarVolume);
        play= findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        next = findViewById(R.id.next);
        previous= findViewById(R.id.previous);


        // Attach the on clicl handler method with button
        play.setOnClickListener(this);
        pause.setOnClickListener(this);

        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        //fill data source value in List View

        //Adapter is required to bind the Data source with the List View
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(

                this, android.R.layout.simple_spinner_dropdown_item,songsName
        );

        lvSongs.setAdapter(adapter);

        //Attach on item click on LIST View
        lvSongs.setOnItemClickListener(this);


        volume.setOnSeekBarChangeListener(this);
        music.setOnSeekBarChangeListener(this);



    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //1. Adapter View   : Source view Adapter
        //2. View : refernce of  view of each item
        //3 position : index of click element


        ///song play
        setupAudioClip(position);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

       switch (seekBar.getId())
       {
           case R.id.seekBarMusic:
               if(fromUser && mp!=null)
               {
                   mp.seekTo(progress);
               }


               break;

           case R.id.seekBarVolume:
         if(fromUser && mp!=null)
           {
               if(fromUser && mp!=null)
               {
                   currentVolume= progress*0.01f;
                   mp.setVolume(progress*0.01f,progress*0.01f);
               }
           }
         break;


       }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp!=null)
        {
            if(currentSongIdx<songId.length-1)
            {
                setupAudioClip(currentSongIdx+1);
            }
            else
            {
                setupAudioClip(0);
            }

        }


    }
}