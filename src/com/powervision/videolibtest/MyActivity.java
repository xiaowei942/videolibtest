package com.powervision.videolibtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import com.powervision.videolib.codec.*;
import com.powervision.videolib.extractor.*;
import com.powervision.videolib.writer.Mp4FileWriter;

public class MyActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    final boolean HASSURFACE = true;
    final boolean WINDOWDISPLAY = false;

    public Surface mSurface = null;
    public SurfaceHolder mSurfaceHolder = null;

    int mWidth = 1280;
    int mHeight = 720;
    BaseCodec codec;
    H264FrameExtractor extractor = null;
    public SurfaceView sv;
    public Button start_btn;
    public Button stop_input_btn;
    public Button capture_btn;
    public Button save_btn;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(HASSURFACE) {
            if (!WINDOWDISPLAY) {
                setContentView(R.layout.main);
                sv = (SurfaceView) findViewById(R.id.surface_view);
                sv.getHolder().addCallback(this);
            } else {
                sv = new SurfaceView(this);
                sv.getHolder().addCallback(this);
                setContentView(sv);
            }
        } else {
            setContentView(R.layout.main);
            sv = (SurfaceView)findViewById(R.id.surface_view);
            capture_btn = (Button)findViewById(R.id.capture_btn);
            capture_btn.setOnClickListener(this);
            save_btn = (Button)findViewById(R.id.save_btn);
            save_btn.setOnClickListener(this);
            sv.setOnClickListener(this);
            sv.getHolder().addCallback(this);
        }
        start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(this);
        stop_input_btn = (Button)findViewById(R.id.stop_input_btn);
        stop_input_btn.setOnClickListener(this);
    }

    private void prepare() {
        extractor = ExtractorFactory.createFileDataExtractor("/mnt/sdcard/test.h264");
        extractor.open();
        extractor.start();

        CodecParam param = new CodecParam();
        param.extractor = extractor;
        param.surface = mSurface;
        param.width = 1280;
        param.height = 720;
        param.obj = this;
        CodecParam.codecType = Codec.CODEC_TYPE_MEDIACODEC;
        codec = CodecFactory.createCodec(param);

        codec.initCodec(null);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(HASSURFACE) {
            mSurface = surfaceHolder.getSurface();
        } else {
            mSurface = null;
        }
        mSurfaceHolder = surfaceHolder;
        prepare();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                codec.start();
                break;
            case R.id.stop_input_btn:
                break;
            case R.id.capture_btn:
                ((PowerCodec) codec).setCaptureFrame(true);
                break;
            case R.id.save_btn:
                PowerCodec.setCloseWriter(true);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
        }
        return super.onKeyDown(keyCode, event);
    }
}
