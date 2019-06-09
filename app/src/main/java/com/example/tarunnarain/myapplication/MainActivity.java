package com.example.tarunnarain.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Runnable{
private MaterialSearchView searchView;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private MediaPlayer mpintro;
    private TextView end;
    private Thread main;
    private SeekBar seekBar;
    private ImageView playpause;
    private RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<ExampleItem> al;
    static TextView totalfiles;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            ExampleItem thisItem = al.get(position);
            Toast.makeText(MainActivity.this, "Playing " + thisItem.getTitle(), Toast.LENGTH_SHORT).show();
           if(mpintro!=null){
               mpintro.stop();
        seekBar.setProgress(0);
           }
            mpintro=MediaPlayer.create(MainActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + thisItem.getPath()+thisItem.getTitle()));
          main= new Thread(MainActivity.this);
          main.start();
            mpintro.start();
            playpause.setImageResource(R.drawable.ic_pause);
            seekBar.setMax(mpintro.getDuration());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playpause=findViewById(R.id.playpause);
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mpintro==null)
                {
                    System.out.println("FOUND NULL )_____________");
                    if(al.size()<1)
                        Toast.makeText(getApplicationContext(), "Empty List", Toast.LENGTH_SHORT).show();
                    else
                    {
                        mpintro=MediaPlayer.create(MainActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + al.get(0).getPath()+al.get(0).getTitle()));
              mpintro.start(); playpause.setImageResource(R.drawable.ic_pause);
              new Thread(MainActivity.this).start();
               }
                }
                else if(mpintro.isPlaying()) {
                    playpause.setImageResource(R.drawable.ic_play);
                    mpintro.pause();
                }
                else {
                    playpause.setImageResource(R.drawable.ic_pause);
                    mpintro.start();
                    new Thread(MainActivity.this).start();
                }
            }
        });
        end=findViewById(R.id.end);
        seekBar=findViewById(R.id.seekbar);
        totalfiles=findViewById(R.id.totalfiles);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        searchView=findViewById(R.id.search_view);
        getSupportActionBar().setTitle("Music Build 1");
new Thread(this).start();

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        }

        al=new ArrayList<>();
      // new Thread(this).start();
        mRecyclerView=findViewById(R.id.recyclerView);
        mLayoutManager =new LinearLayoutManager(this);
        mAdapter=new ExampleAdapter(al);
        mAdapter.setOnItemClickListener(onItemClickListener);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                if(mpintro==null)
                    return;
                System.out.println("Progress : "+progress);
                end.setText(((progress/1000)/(60))+":"+((progress/1000)%(60)));
                if(progress/1000==mpintro.getDuration()/1000)
                    playpause.setImageResource(R.drawable.ic_play);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mpintro.seekTo((seekBar.getProgress()));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 addSongs();
                    mRecyclerView=findViewById(R.id.recyclerView);
                    mLayoutManager =new LinearLayoutManager(this);
                    mAdapter=new ExampleAdapter(al);

                    mAdapter.setOnItemClickListener(onItemClickListener);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(onItemClickListener);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Bhai Kya Chahte ho??", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
    public static void addSongs()
    {

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Music/");
        File fileD = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/");
        File[] files = file.listFiles();
        File[] filesD = fileD.listFiles();
        if(files==null)
        {System.out.println("EMPTY FOUND");
        totalfiles.setText("Empty Music Folder");
        return ;}
        if(filesD==null)
            totalfiles.setText("Empty Downloads Folder");
    int i=0;
        if(files != null){
            for(File f : files){ // loop and print all file
                String fileName = f.getName(); // this is file name
                System.out.println("-------here ------"+fileName);

            if(fileName.endsWith(".mp3")){
                i++;al.add(new ExampleItem( fileName,"NONE", R.drawable.ic_android, R.drawable.ic_star_no, "/Music/"));}
            }
            totalfiles.setText("Found "+i+" Songs From Music");
        }
        if(filesD != null){
            for(File f : filesD){ // loop and print all file
                String fileName = f.getName(); // this is file name
                System.out.println("-------here ------"+fileName);

                if(fileName.endsWith(".mp3")){
                    i++;al.add(new ExampleItem( fileName,"NONE", R.drawable.ic_android, R.drawable.ic_star_no, "/Download/"));}
            }
            totalfiles.setText("Found "+i+" Songs From Downloads, Music");
        }

    }


    @Override
    public void run() {

if(mpintro==null)
{
    return;
}
    int currentPosition = mpintro.getCurrentPosition();
System.out.println("Runnable Running");
seekBar.setMax(mpintro.getDuration());
    int total = mpintro.getDuration();


        while (mpintro != null && mpintro.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(500);
                currentPosition = mpintro.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception Cought HERE________________");
                return;
            }
            seekBar.setProgress(currentPosition);

        }
    }

}
