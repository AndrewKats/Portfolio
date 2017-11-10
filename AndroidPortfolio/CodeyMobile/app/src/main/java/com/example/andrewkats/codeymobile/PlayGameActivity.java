package com.example.andrewkats.codeymobile;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class PlayGameActivity extends AppCompatActivity implements GLSurfaceView.Renderer, View.OnClickListener, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener
{
    ArrayList<CodeySprite> _sprites = new ArrayList<CodeySprite>();
    CodeySprite _codeySprite = null;

    CodeySprite _cloud1NoRain = null;
    CodeySprite _cloud1Rain1 = null;
    CodeySprite _cloud1Rain2 = null;

    CodeySprite _cloud2NoRain = null;
    CodeySprite _cloud2Rain1 = null;
    CodeySprite _cloud2Rain2 = null;

    CodeySprite _lavaSprite = null;
    CodeySprite _lavaSprite2 = null;

    CodeySprite _lavaColdSprite = null;
    CodeySprite _lavaColdSprite2 = null;


    boolean _raining = false;

    int _levelId = -1;
    int _playerId = -1;
    int[][] levelGrid = null;

    SquareButton rightButton = null;
    SquareButton upButton = null;
    Button ifButton = null;
    SquareButton playButton = null;
    Button clearButton = null;

    SquareButton loopActionButton1 = null;
    SquareButton loopActionButton2 = null;
    SquareButton loopActionButton3 = null;
    EditText numPicker = null;
    // 0=none, 1=right, 2=up
    int loopActionState1 = 0;
    int loopActionState2 = 0;
    int loopActionState3 = 0;

    SquareButton loopButton = null;

    LinearLayout queueLayout = null;

    LevelGrids levels = new LevelGrids();

    ArrayList<CodeyAction> doActions = null;
    ArrayList<CodeyAction> actions = new ArrayList<CodeyAction>();

    int codeyX = 0;
    int codeyY = 1;

    int condCount = 0;

    float _unitX = 2.0f/15.0f;
    float _unitY = 2.0f/7.0f;


    int loopPosition = 1;
    boolean inLoop = false;
    int currentIndex = 0;
    boolean wonTheGame = false;

    Bitmap codeyBitmap = null;
    Bitmap fireproofBitmap = null;
    Bitmap umbrellaBitmap = null;
    Bitmap friedCodeyBitmap = null;
    Bitmap jetpackCodeyBitmap = null;

    Bitmap cloudRain1Bitmap = null;
    Bitmap cloudRain2Bitmap = null;
    Bitmap cloudNoRainBitmap = null;
    Bitmap lavaBitmap = null;
    Bitmap lavaColdBitmap = null;

    DataModel dataModel = null;

    boolean _playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataModel = DataModel.getInstance();

        try
        {
            dataModel = DataModel.retrieveData(this);
            DataModel._Instance = dataModel;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(getIntent().getExtras() != null)
        {
            _levelId = getIntent().getExtras().getInt("Level");
            _playerId = getIntent().getExtras().getInt("Player");
        }

        _playerId = dataModel.currentPlayerId;

        levelGrid = levels.getLevel(_levelId);




        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout gameAndQueue = new LinearLayout(this);
        gameAndQueue.setOrientation(LinearLayout.VERTICAL);



        GLSurfaceView surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.setRenderer(this);
        gameAndQueue.addView(surfaceView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));

        CodeySprite backgroundSprite = new CodeySprite();
        backgroundSprite._scaleX = 2.0f;
        backgroundSprite._scaleY = 2.0f;

        setSurfaceBackground(backgroundSprite);
        _sprites.add(backgroundSprite);

        _codeySprite = new CodeySprite();
        _codeySprite._scaleX = _unitX;
        _codeySprite._scaleY = _unitY;
        _codeySprite._translateX = -1.0f + 0.5f*_unitX;
        _codeySprite._translateY = -1.0f + 1.5f*_unitY;
        codeyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.codeyhd);
        fireproofBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.codeyfireproof);
        umbrellaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.codeyumbrella);
        friedCodeyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.friedcodey);
        jetpackCodeyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flamecodeyhandsup);
        _codeySprite.setTexture(codeyBitmap);
        _sprites.add(_codeySprite);


        if(_levelId == 7)
        {
            cloudRain1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudrain1);
            cloudRain2Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudrain2);
            cloudNoRainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudnorain);
            lavaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava2);
            lavaColdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava3);


            _lavaSprite = new CodeySprite();
            _lavaSprite._scaleX = _unitX;
            _lavaSprite._scaleY = _unitY;
            _lavaSprite._translateX = -1.0f + 3.5f*_unitX;
            _lavaSprite._translateY = -1.0f + 0.5f*_unitY;
            _lavaSprite.setTexture(lavaBitmap);
            _sprites.add(_lavaSprite);

            _lavaSprite2 = new CodeySprite();
            _lavaSprite2._scaleX = _unitX;
            _lavaSprite2._scaleY = _unitY;
            _lavaSprite2._translateX = -1.0f + 7.5f*_unitX;
            _lavaSprite2._translateY = -1.0f + 0.5f*_unitY;
            _lavaSprite2.setTexture(lavaBitmap);
            _sprites.add(_lavaSprite2);

            _lavaColdSprite = new CodeySprite();
            _lavaColdSprite._scaleX = _unitX;
            _lavaColdSprite._scaleY = _unitY;
            _lavaColdSprite._translateX = -5;
            _lavaColdSprite._translateY = -1.0f + 0.5f*_unitY;
            _lavaColdSprite.setTexture(lavaColdBitmap);
            _sprites.add(_lavaColdSprite);

            _lavaColdSprite2 = new CodeySprite();
            _lavaColdSprite2._scaleX = _unitX;
            _lavaColdSprite2._scaleY = _unitY;
            _lavaColdSprite2._translateX = -5;
            _lavaColdSprite2._translateY = -1.0f + 0.5f*_unitY;
            _lavaColdSprite2.setTexture(lavaColdBitmap);
            _sprites.add(_lavaColdSprite2);


            _cloud1NoRain = new CodeySprite();
            _cloud1NoRain._scaleX = _unitX;
            _cloud1NoRain._scaleY = 3.0f*_unitY;
            _cloud1NoRain._translateX = -1.0f + 3.5f*_unitX;
            _cloud1NoRain._translateY = -1.0f + 2.5f*_unitY;
            _cloud1NoRain.setTexture(cloudNoRainBitmap);
            _sprites.add(_cloud1NoRain);

            _cloud1Rain1 = new CodeySprite();
            _cloud1Rain1._scaleX = _unitX;
            _cloud1Rain1._scaleY = 3.0f*_unitY;
            _cloud1Rain1._translateX = -5;
            _cloud1Rain1._translateY = -1.0f + 2.5f*_unitY;
            _cloud1Rain1.setTexture(cloudRain1Bitmap);
            _sprites.add(_cloud1Rain1);

            _cloud1Rain2 = new CodeySprite();
            _cloud1Rain2._scaleX = _unitX;
            _cloud1Rain2._scaleY = 3.0f*_unitY;
            _cloud1Rain2._translateX = -5;
            _cloud1Rain2._translateY = -1.0f + 2.5f*_unitY;
            _cloud1Rain2.setTexture(cloudRain2Bitmap);
            _sprites.add(_cloud1Rain2);



            _cloud2NoRain = new CodeySprite();
            _cloud2NoRain._scaleX = _unitX;
            _cloud2NoRain._scaleY = 3.0f*_unitY;
            _cloud2NoRain._translateX = -1.0f + 7.5f*_unitX;
            _cloud2NoRain._translateY = -1.0f + 2.5f*_unitY;
            _cloud2NoRain.setTexture(cloudNoRainBitmap);
            _sprites.add(_cloud2NoRain);

            _cloud2Rain1 = new CodeySprite();
            _cloud2Rain1._scaleX = _unitX;
            _cloud2Rain1._scaleY = 3.0f*_unitY;
            _cloud2Rain1._translateX = -5;
            _cloud2Rain1._translateY = -1.0f + 2.5f*_unitY;
            _cloud2Rain1.setTexture(cloudRain1Bitmap);
            _sprites.add(_cloud2Rain1);

            _cloud2Rain2 = new CodeySprite();
            _cloud2Rain2._scaleX = _unitX;
            _cloud2Rain2._scaleY = 3.0f*_unitY;
            _cloud2Rain2._translateX = -5;
            _cloud2Rain2._translateY = -1.0f + 2.5f*_unitY;
            _cloud2Rain2.setTexture(cloudRain2Bitmap);
            _sprites.add(_cloud2Rain2);


            ValueAnimator cloud1Animator = new ValueAnimator();
            cloud1Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    if (_raining)
                    {
                        if ((int) animation.getAnimatedValue() % 2 == 0)
                        {
                            _cloud1NoRain._translateX = -5.0f;
                            _cloud1Rain1._translateX = -1.0f + 3.5f*_unitX;
                            _cloud1Rain2._translateX = -5.0f;

                            _cloud2NoRain._translateX = -5.0f;
                            _cloud2Rain1._translateX = -1.0f + 7.5f*_unitX;
                            _cloud2Rain2._translateX = -5.0f;
                        }
                        else
                        {
                            _cloud1NoRain._translateX = -5.0f;
                            _cloud1Rain1._translateX = -5.0f;
                            _cloud1Rain2._translateX = -1.0f + 3.5f*_unitX;

                            _cloud2NoRain._translateX = -5.0f;
                            _cloud2Rain1._translateX = -5.0f;
                            _cloud2Rain2._translateX = -1.0f + 7.5f*_unitX;
                        }

                        _lavaColdSprite._translateX = -1.0f + 3.5f*_unitX;
                        _lavaColdSprite2._translateX = -1.0f + 7.5f*_unitX;

                        _lavaSprite._translateX = -5;
                        _lavaSprite2._translateX = -5;

                    }
                    else
                    {
                        _cloud1NoRain._translateX = -1.0f + 3.5f*_unitX;
                        _cloud1Rain1._translateX = -5.0f;
                        _cloud1Rain2._translateX = -5.0f;

                        _cloud2NoRain._translateX = -1.0f + 7.5f*_unitX;
                        _cloud2Rain1._translateX = -5.0f;
                        _cloud2Rain2._translateX = -5.0f;

                        _lavaColdSprite._translateX = -5;
                        _lavaColdSprite2._translateX = -5;

                        if ((int) animation.getAnimatedValue() % 2 == 0)
                        {
                            _lavaSprite._translateX = -1.0f + 3.5f*_unitX;
                            _lavaSprite2._translateX = -1.0f + 7.5f*_unitX;
                        }
                        else
                        {
                            _lavaSprite._translateX = -5;
                            _lavaSprite2._translateX = -5;
                        }
                    }
                }
            });
            cloud1Animator.setIntValues(0,99);
            cloud1Animator.setRepeatCount(ValueAnimator.INFINITE);
            cloud1Animator.setDuration(20000);
            cloud1Animator.start();


        }
        if(_levelId == 8)
        {
            cloudRain1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudrain1);
            cloudRain2Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudrain2);
            cloudNoRainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloudnorain);
            lavaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava2);
            lavaColdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lava3);


            _lavaSprite = new CodeySprite();
            _lavaSprite._scaleX = _unitX;
            _lavaSprite._scaleY = _unitY;
            _lavaSprite._translateX = -1.0f + 11.5f*_unitX;
            _lavaSprite._translateY = -1.0f + 2.5f*_unitY;
            _lavaSprite.setTexture(lavaBitmap);
            _sprites.add(_lavaSprite);

            _lavaColdSprite = new CodeySprite();
            _lavaColdSprite._scaleX = _unitX;
            _lavaColdSprite._scaleY = _unitY;
            _lavaColdSprite._translateX = -5;
            _lavaColdSprite._translateY = -1.0f + 2.5f*_unitY;
            _lavaColdSprite.setTexture(lavaColdBitmap);
            _sprites.add(_lavaColdSprite);


            _cloud1NoRain = new CodeySprite();
            _cloud1NoRain._scaleX = _unitX;
            _cloud1NoRain._scaleY = 3.0f*_unitY;
            _cloud1NoRain._translateX = -1.0f + 3.5f*_unitX;
            _cloud1NoRain._translateY = -1.0f + 4.5f*_unitY;
            _cloud1NoRain.setTexture(cloudNoRainBitmap);
            _sprites.add(_cloud1NoRain);

            _cloud1Rain1 = new CodeySprite();
            _cloud1Rain1._scaleX = _unitX;
            _cloud1Rain1._scaleY = 3.0f*_unitY;
            _cloud1Rain1._translateX = -5;
            _cloud1Rain1._translateY = -1.0f + 4.5f*_unitY;
            _cloud1Rain1.setTexture(cloudRain1Bitmap);
            _sprites.add(_cloud1Rain1);

            _cloud1Rain2 = new CodeySprite();
            _cloud1Rain2._scaleX = _unitX;
            _cloud1Rain2._scaleY = 3.0f*_unitY;
            _cloud1Rain2._translateX = -5;
            _cloud1Rain2._translateY = -1.0f + 4.5f*_unitY;
            _cloud1Rain2.setTexture(cloudRain2Bitmap);
            _sprites.add(_cloud1Rain2);



            ValueAnimator cloud1Animator = new ValueAnimator();
            cloud1Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    if (_raining)
                    {
                        if ((int) animation.getAnimatedValue() % 2 == 0)
                        {
                            _cloud1NoRain._translateX = -5.0f;
                            _cloud1Rain1._translateX = -1.0f + 11.5f*_unitX;
                            _cloud1Rain2._translateX = -5.0f;
                        }
                        else
                        {
                            _cloud1NoRain._translateX = -5.0f;
                            _cloud1Rain1._translateX = -5.0f;
                            _cloud1Rain2._translateX = -1.0f + 11.5f*_unitX;
                        }

                        _lavaColdSprite._translateX = -1.0f + 11.5f*_unitX;
                        _lavaSprite._translateX = -5;
                    }
                    else
                    {
                        _cloud1NoRain._translateX = -1.0f + 11.5f*_unitX;
                        _cloud1Rain1._translateX = -5.0f;
                        _cloud1Rain2._translateX = -5.0f;

                        _lavaColdSprite._translateX = -5;

                        if ((int) animation.getAnimatedValue() % 2 == 0)
                        {
                            _lavaSprite._translateX = -1.0f + 11.5f*_unitX;
                        }
                        else
                        {
                            _lavaSprite._translateX = -5;
                        }
                    }
                }
            });
            cloud1Animator.setIntValues(0,99);
            cloud1Animator.setRepeatCount(ValueAnimator.INFINITE);
            cloud1Animator.setDuration(30000);
            cloud1Animator.start();

        }



        LinearLayout clearAndQueue = new LinearLayout(this);
        clearButton = new Button(this);
        clearButton.setText("Clear");
        clearButton.setOnClickListener(this);
        LinearLayout.LayoutParams caqParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        caqParams.height = 200;
        clearAndQueue.addView(clearButton, caqParams);

        queueLayout = new LinearLayout(this);
        //queueLayout.setBackgroundColor(Color.RED);
        clearAndQueue.addView(queueLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        gameAndQueue.addView(clearAndQueue, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout sidebar = new LinearLayout(this);
        sidebar.setOrientation(LinearLayout.VERTICAL);

        rightButton = new SquareButton(this);
        //rightButton.setText("right");
        rightButton.setBackgroundResource(R.drawable.forward);
        rightButton.setOnClickListener(this);
        sidebar.addView(rightButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));

        upButton = new SquareButton(this);
        //upButton.setText("up");
        upButton.setBackgroundResource(R.drawable.up);
        upButton.setOnClickListener(this);
        sidebar.addView(upButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));

        ifButton = new Button(this);
        //ifButton.setText("if");
        ifButton.setBackgroundResource(R.drawable.ifelse2);
        ifButton.setOnClickListener(this);
        //ifButton.setMinimumHeight(400);
        //ifButton.setMinimumWidth(800);
        sidebar.addView(ifButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 50));

        LinearLayout loopLayout = new LinearLayout(this);
        loopLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout labelAndTimes = new LinearLayout(this);
        //labelAndTimes.setBackgroundColor(Color.MAGENTA);
        labelAndTimes.setOrientation(LinearLayout.HORIZONTAL);

        TextView loopLabel = new TextView(this);
        //loopLabel.setBackgroundColor(Color.BLUE);
        loopLabel.setText("Loop");
        loopLabel.setTextSize(14);
        labelAndTimes.addView(loopLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        numPicker = new EditText(this);
        numPicker.setInputType(InputType.TYPE_CLASS_NUMBER);
        numPicker.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
        numPicker.setHint("Times");
        numPicker.setTextSize(12);
        //numPicker.setBackgroundColor(Color.RED);
        labelAndTimes.addView(numPicker, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        loopLayout.addView(labelAndTimes, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout setLoopLayout = new LinearLayout(this);
        setLoopLayout.setOrientation(LinearLayout.HORIZONTAL);

        loopActionButton1 = new SquareButton(this);
        loopActionButton1.setBackgroundResource(0);
        loopActionButton1.setText("-");
        loopActionButton1.setTextSize(10);
        loopActionButton1.setOnClickListener(this);
        LinearLayout.LayoutParams loop1Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        setLoopLayout.addView(loopActionButton1, loop1Params);

        loopActionButton2 = new SquareButton(this);
        loopActionButton2.setBackgroundResource(0);
        loopActionButton2.setText("-");
        loopActionButton2.setTextSize(10);
        loopActionButton2.setOnClickListener(this);
        setLoopLayout.addView(loopActionButton2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        loopActionButton3 = new SquareButton(this);
        loopActionButton3.setBackgroundResource(0);
        loopActionButton3.setText("-");
        loopActionButton3.setTextSize(10);
        loopActionButton3.setOnClickListener(this);
        setLoopLayout.addView(loopActionButton3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        loopButton = new SquareButton(this);
        loopButton.setText("+");
        loopButton.setTextSize(10);
        loopButton.setOnClickListener(this);
        setLoopLayout.addView(loopButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        //setLoopLayout.setPadding(100,10,100,10);
        loopLayout.addView(setLoopLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));




        sidebar.addView(loopLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        playButton = new SquareButton(this);
        //playButton.setText("play");
        playButton.setBackgroundResource(R.drawable.play);
        playButton.setOnClickListener(this);
        sidebar.addView(playButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));


        masterLayout.addView(gameAndQueue, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10));
        masterLayout.addView(sidebar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5));
        setContentView(masterLayout);
    }

    private void setSurfaceBackground(CodeySprite backgroundSprite)
    {
        if(_levelId == 1)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level1));
        }
        if(_levelId == 2)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level2));
        }
        if(_levelId == 3)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level3));
        }
        if(_levelId == 4)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level4));
        }
        if(_levelId == 5)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level5));
        }
        if(_levelId == 6)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level6));
        }
        if(_levelId == 7)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level7));
        }
        if(_levelId == 8)
        {
            backgroundSprite.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.level8));
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        GLES20.glClearColor(0.2f,0.2f,0.2f,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {

    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        for(CodeySprite sprite : _sprites)
        {
            sprite.draw();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v == rightButton)
        {
            SquareButton newRightButton = new SquareButton(this);
            newRightButton.setBackgroundResource(R.drawable.forward);
            queueLayout.addView(newRightButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            queueLayout.invalidate();

            actions.add(new CodeyForward());
            System.out.println("ADDED RIGHT");
        }
        else if(v == upButton)
        {
            SquareButton newUpButton = new SquareButton(this);
            newUpButton.setBackgroundResource(R.drawable.up);
            queueLayout.addView(newUpButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            queueLayout.invalidate();

            actions.add(new CodeyJump());
            System.out.println("ADDED JUMP");
        }
        else if(v == ifButton)
        {
            SquareButton ifDisplay = new SquareButton(this);
            //ifDisplay.setText("if");
            //ifDisplay.setBackgroundColor(Color.BLUE);
            ifDisplay.setBackgroundResource(R.drawable.ifelsedisplay);
            queueLayout.addView(ifDisplay, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            queueLayout.invalidate();

            actions.add(new CodeyConditional());
            System.out.println("ADDED CONDITIONAL");
        }
        else if(v == loopButton)
        {


            CodeyAction first = null;
            if(loopActionState1 == 1)
            {
                first = new CodeyForward();
            }
            else if(loopActionState1 == 2)
            {
                first = new CodeyJump();
            }

            CodeyAction second = null;
            if(loopActionState2 == 1)
            {
                second = new CodeyForward();
            }
            else if(loopActionState2 == 2)
            {
                second = new CodeyJump();
            }

            CodeyAction third = null;
            if(loopActionState3 == 1)
            {
                third = new CodeyForward();
            }
            else if(loopActionState3 == 2)
            {
                third = new CodeyJump();
            }



            try
            {
                int inputNum = Integer.parseInt(numPicker.getText().toString());
                actions.add(new CodeyLoop(inputNum, first, second, third));
                System.out.println("ADDED LOOP");

                SquareButton loopDisplay = new SquareButton(this);
                //loopDisplay.setText("L");
                loopDisplay.setBackgroundResource(R.drawable.loopsymbol);
                queueLayout.addView(loopDisplay, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                queueLayout.invalidate();
            }
            catch(Exception e)
            {
                System.out.println("CAN'T ADD LOOP");
            }


        }
        else if(v == playButton)
        {
            if(!_playing)
            {
                _codeySprite.setTexture(codeyBitmap);

                doActions = new ArrayList<CodeyAction>();

                for (CodeyAction action : actions)
                {
                    if (action instanceof CodeyLoop)
                    {
                        ((CodeyLoop) action).timesDone = 0;
                    }
                    doActions.add(action);
                }

                System.out.println("PRESSED PLAY");
                codeyX = 0;
                codeyY = 1;
                _codeySprite._translateX = -1.0f + 0.5f * _unitX;
                _codeySprite._translateY = -1.0f + 1.5f * _unitY;

                Random rand = new Random();
                _raining = rand.nextBoolean();

                condCount = 0;
                inLoop = false;
                wonTheGame = false;
                loopPosition = 1;
                currentIndex = 0;
                _playing = true;
                playButton.setBackgroundResource(R.drawable.dotdotdot);
                play();
            }
        }
        else if(v == clearButton)
        {
            queueLayout.removeAllViews();
            queueLayout.invalidate();

            actions.clear();

            System.out.println("PRESSED CLEAR");
        }
        else if(v == loopActionButton1)
        {
            // none
            if(loopActionState1 == 0)
            {
                loopActionState1 = 1;
                loopActionButton1.setBackgroundResource(R.drawable.forward);
                loopActionButton1.setText("");
            }
            // right
            else if(loopActionState1 == 1)
            {
                loopActionState1 = 2;
                loopActionButton1.setBackgroundResource(R.drawable.up);
                loopActionButton1.setText("");
            }
            // up
            else if(loopActionState1 == 2)
            {
                loopActionState1 = 0;
                loopActionButton1.setBackgroundResource(0);
                loopActionButton1.setText("-");
            }
        }
        else if(v == loopActionButton2)
        {
            // none
            if(loopActionState2 == 0)
            {
                loopActionState2 = 1;
                loopActionButton2.setBackgroundResource(R.drawable.forward);
                loopActionButton2.setText("");
            }
            // right
            else if(loopActionState2 == 1)
            {
                loopActionState2 = 2;
                loopActionButton2.setBackgroundResource(R.drawable.up);
                loopActionButton2.setText("");
            }
            // up
            else if(loopActionState2 == 2)
            {
                loopActionState2 = 0;
                loopActionButton2.setBackgroundResource(0);
                loopActionButton2.setText("-");
            }
        }
        else if(v == loopActionButton3)
        {
            // none
            if(loopActionState3 == 0)
            {
                loopActionState3 = 1;
                loopActionButton3.setBackgroundResource(R.drawable.forward);
                loopActionButton3.setText("");
            }
            // right
            else if(loopActionState3 == 1)
            {
                loopActionState3 = 2;
                loopActionButton3.setBackgroundResource(R.drawable.up);
                loopActionButton3.setText("");
            }
            // up
            else if(loopActionState3 == 2)
            {
                loopActionState3 = 0;
                loopActionButton3.setBackgroundResource(0);
                loopActionButton3.setText("-");
            }
        }
    }

    public void play()
    {

        if(!_playing)
        {
            return;
        }

        if(condCount>0)
        {
            if(_raining)
            {
                _codeySprite.setTexture(umbrellaBitmap);
            }
            else
            {
                _codeySprite.setTexture(fireproofBitmap);
            }
        }
        else
        {
            _codeySprite.setTexture(codeyBitmap);
        }



        if(wonTheGame)
        {
            return;
        }

        System.out.println("PLAY: Index=" + currentIndex + " ActionsSize=" + doActions.size() + " Position: (" + codeyX + "," + codeyY + ")");

        if(currentIndex == -1)
        {
            _codeySprite.setTexture(friedCodeyBitmap);
            playButton.setBackgroundResource(R.drawable.play);
            _playing = false;
            System.out.println("DEATH");
            return;
        }
        if(currentIndex >= doActions.size())
        {
            playButton.setBackgroundResource(R.drawable.play);
            _playing = false;
            System.out.println("FAIL 1");
            return;
        }
        if(doActions.get(currentIndex) == null)
        {
            playButton.setBackgroundResource(R.drawable.play);
            _playing = false;
            System.out.println("FAIL 2");
            return;
        }
        if(doActions.get(currentIndex).actionString().equals("forward"))
        {
            System.out.println("FORWARD");
            inLoop = false;
            currentIndex++;
            walk();
        }
        else if(doActions.get(currentIndex).actionString().equals("jump"))
        {
            System.out.println("JUMP");
            currentIndex++;
            inLoop = false;
            jump(false);
        }
        else if(doActions.get(currentIndex).actionString().equals("cond"))
        {
            System.out.println("COND");
            condCount = 3;
            currentIndex++;
            play();
            return;
        }
        // LOOP
        else if(doActions.get(currentIndex).actionString().equals("loop"))
        {
            System.out.println("LOOP");

            CodeyLoop loopAction = (CodeyLoop) doActions.get(currentIndex);

            if(loopAction.timesDone == loopAction.times)
            {
                inLoop = false;
                currentIndex++;
                loopAction.timesDone = 0;
                play();
                return;
            }


            if(loopPosition == 1)
            {
                loopPosition = 2;
                if (loopAction.action1 != null)
                {
                    if (loopAction.action1.actionString().equals("forward"))
                    {
                        inLoop = true;
                        walk();
                    }
                    else if (loopAction.action1.actionString().equals("jump"))
                    {
                        inLoop = true;
                        jump(false);
                    }
                }
                else
                {
                    play();
                }
            }
            else if(loopPosition == 2)
            {
                loopPosition = 3;
                if (loopAction.action2 != null)
                {
                    if (loopAction.action2.actionString().equals("forward"))
                    {
                        inLoop = true;
                        walk();
                    }
                    else if (loopAction.action2.actionString().equals("jump"))
                    {
                        inLoop = true;
                        jump(false);
                    }
                }
                else
                {
                    play();
                }
            }
            else if(loopPosition == 3)
            {
                loopPosition = 1;
                loopAction.timesDone++;
                if (loopAction.action3 != null)
                {
                    if (loopAction.action3.actionString().equals("forward"))
                    {
                        inLoop = true;
                        walk();
                    }
                    else if (loopAction.action3.actionString().equals("jump"))
                    {
                        inLoop = true;
                        jump(false);
                    }
                }
                else
                {
                    play();
                }
            }


        }
    }


    public void walk()
    {
        // flag in front [DONE]
        if(levelGrid[codeyX+1][codeyY] == 2)
        {
            System.out.println("FLAG");
            codeyX++;
            wonTheGame = true;
            walkAnimation();
            return;
        }
        // wall in front [DONE]
        else if(levelGrid[codeyX+1][codeyY] == 1)
        {
            System.out.println("WALL");

            wallInFrontAnimation();
        }
        else if(levelGrid[codeyX+1][codeyY] == 3)
        {
            // walk [DONE]
            if(condCount > 0)
            {
                System.out.println("COND WALK");
                codeyX++;

                walkAnimation();
            }
            // death [DONEISH]
            else
            {
                currentIndex = -1;
                walkAnimation();
                return;
            }
        }
        // ground in front (walk) [DONE]
        else if(levelGrid[codeyX+1][codeyY] == 0 && levelGrid[codeyX+1][codeyY-1] == 1)
        {
            System.out.println("WALK");
            codeyX++;

            walkAnimation();
        }
        // fall
        else if(levelGrid[codeyX+1][codeyY] == 0 && levelGrid[codeyX+1][codeyY-1] == 0)
        {
            System.out.println("FALL");

            // fall to ground 2 below [DONE]
            if(codeyY-3 >=0 && levelGrid[codeyX+1][codeyY-3] == 1)
            {
                codeyX++;
                codeyY -= 2;

                fallToGroundAnimation();
            }
            // fall off edge [DONE?]
            else
            {
                currentIndex = -1;
                fallToGroundAnimation();
                return;
            }
        }
    }

    public void jump(boolean knowSafe)
    {
        if(condCount < 1)
        {
            _codeySprite.setTexture(jetpackCodeyBitmap);
        }

        // try to jump but block above head [DONE]
        if(levelGrid[codeyX][codeyY+1] == 1)
        {
            System.out.println("HIT HEAD");
            hitHeadAnimation();
        }
        // win [DONE]
        else if(levelGrid[codeyX+1][codeyY+2] == 2 || levelGrid[codeyX+2][codeyY+2] == 2)
        {
            System.out.println("FLAG");
            wonTheGame = true;
            jumpOntoPlatformAnimation();
            return;
        }
        // try to jump into cloud [DONEISH]
        else if((levelGrid[codeyX][codeyY+2] == 3 || levelGrid[codeyX+1][codeyY+2] == 3 || levelGrid[codeyX+2][codeyY+2] == 3 || levelGrid[codeyX+2][codeyY+1] == 3) && !knowSafe)
        {
            if(condCount > 0)
            {
                jump(true);
                return;
            }
            else
            {
                System.out.println("JUMPED INTO CLOUD AND DIED");
                currentIndex = -1;
                regularJumpAnimation();
                return;
            }
        }
        // jump up onto platform [DONE]
        else if(levelGrid[codeyX+2][codeyY+1] == 1 || levelGrid[codeyX+2][codeyY+1] == 3)
        {
            codeyX += 2;
            codeyY += 2;
            System.out.println("JUMPED ON PLATFORM");

            jumpOntoPlatformAnimation();
        }
        // jump onto ground [DONE]
        else if(levelGrid[codeyX+2][codeyY-1] == 1)
        {
            codeyX += 2;
            System.out.println("JUMPED ON GROUND");

            regularJumpAnimation();
        }
        // jump far down and win [DONE]
        else if(codeyY-1 >= 0 && levelGrid[codeyX+2][codeyY-1] == 2)
        {
            System.out.println("FLAG");
            wonTheGame = true;
            jumpFarDownAnimation();
            return;
        }
        // jump far down [DONE]
        else if(codeyY-3 >= 0 && levelGrid[codeyX+2][codeyY-3] == 1)
        {
            codeyX += 2;
            codeyY -= 2;
            System.out.println("JUMPED WAY DOWN");

            jumpFarDownAnimation();
        }
        // jump off edge
        else
        {
            System.out.println("JUMPED OFF EDGE");
            currentIndex = -1;
            jumpOffEdgeAnimation();
            return;
        }

    }

    public void winTheGame()
    {
        int loopSum = 0;
        for(CodeyAction action : actions)
        {
            if(action instanceof CodeyLoop)
            {
                loopSum += ((CodeyLoop) action).times;
            }
        }
        int score = 500 - 20*actions.size() - 2*loopSum;


        Intent winIntent = new Intent();
        winIntent.setClass(this, WinLevelActivity.class);
        winIntent.putExtra("Level", _levelId);
        winIntent.putExtra("Score", score);
        winIntent.putExtra("Player", _playerId);
        startActivity(winIntent);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {
            _codeySprite._translateX = (float) animation.getAnimatedValue();
    }

    @Override
    public void onAnimationStart(Animator animation)
    {

    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        System.out.println("Animation over");

        if(wonTheGame)
        {
            winTheGame();
        }

        condCount--;

        play();

    }

    @Override
    public void onAnimationCancel(Animator animation)
    {

    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {

    }


    public void walkAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX+_unitX);
        animator.setDuration(500);
        animator.start();
    }

    public void wallInFrontAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX+0.25f*_unitX, _codeySprite._translateX);
        animator.setDuration(500);
        animator.start();
    }

    public void fallToGroundAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX+_unitX, _codeySprite._translateX+_unitX);
        animator.setDuration(500);
        animator.start();

        ValueAnimator animatorDown = new ValueAnimator();
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorDown.setFloatValues(_codeySprite._translateY, _codeySprite._translateY, _codeySprite._translateY-2.0f*_unitY);
        animatorDown.setDuration(500);
        animatorDown.start();
    }

    public void hitHeadAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX, _codeySprite._translateX);
        animator.setDuration(500);
        animator.start();

        ValueAnimator animatorY = new ValueAnimator();
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorY.setFloatValues(_codeySprite._translateY, _codeySprite._translateY+0.25F*_unitY, _codeySprite._translateY);
        animatorY.setDuration(500);
        animatorY.start();
    }



    public void regularJumpAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX, _codeySprite._translateX+2.0f*_unitX, _codeySprite._translateX+2.0f*_unitX);
        animator.setDuration(1000);
        animator.start();

        ValueAnimator animatorDown = new ValueAnimator();
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorDown.setFloatValues(_codeySprite._translateY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY);
        animatorDown.setDuration(1000);
        animatorDown.start();
    }

    public void jumpOntoPlatformAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX, _codeySprite._translateX+2.0f*_unitX);
        animator.setDuration(1000);
        animator.start();

        ValueAnimator animatorDown = new ValueAnimator();
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorDown.setFloatValues(_codeySprite._translateY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY+2.0f*_unitY);
        animatorDown.setDuration(1000);
        animatorDown.start();
    }

    public void jumpFarDownAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX, _codeySprite._translateX+2.0f*_unitX, _codeySprite._translateX+2.0f*_unitX);
        animator.setDuration(1000);
        animator.start();

        ValueAnimator animatorDown = new ValueAnimator();
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorDown.setFloatValues(_codeySprite._translateY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY-2.0f*_unitY);
        animatorDown.setDuration(1000);
        animatorDown.start();
    }

    public void jumpOffEdgeAnimation()
    {
        ValueAnimator animator = new ValueAnimator();
        animator.addUpdateListener(this);
        animator.addListener(this);
        animator.setFloatValues(_codeySprite._translateX, _codeySprite._translateX, _codeySprite._translateX+2.0f*_unitX, _codeySprite._translateX+2.0f*_unitX, _codeySprite._translateX+2.0f*_unitX);
        animator.setDuration(1500);
        animator.start();

        ValueAnimator animatorDown = new ValueAnimator();
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                _codeySprite._translateY = (float) animation.getAnimatedValue();
            }
        });
        animatorDown.setFloatValues(_codeySprite._translateY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY+2.0f*_unitY, _codeySprite._translateY-2.0f*_unitY, _codeySprite._translateY-5.0f*_unitY);
        animatorDown.setDuration(1500);
        animatorDown.start();
    }

    @Override
    public void onBackPressed()
    {
        _playing = false;
        Intent winIntent = new Intent();
        winIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        winIntent.setClass(this, LevelSelectActivity.class);
        winIntent.putExtra("PlayerId", "" + _playerId);
        startActivity(winIntent);
    }
}
