package jp.ddo.haselab.timerecoder.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.media.MediaRecorder;

public final class RecodeAudioMgr {

    private final MediaRecorder mrec;

    private boolean recodingNow = false;

    public RecodeAudioMgr() {
        mrec = new MediaRecorder();
        initRecoder();
    }

    public void initRecoder() {
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    public static boolean canRecode() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public void startRecodingExternalStrage(final String argFileName,
            final int argRecodeSecondTime) throws IOException {

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, argFileName);
        mrec.setOutputFile(file.getAbsolutePath());
        mrec.prepare();
        mrec.start();
        recodingNow = true;
        if (argRecodeSecondTime <= 0) {
            return;
        }
        try {
            Thread.sleep(argRecodeSecondTime * 1000); // mill second
        } catch (InterruptedException e) {
            MyLog.getInstance().error("sleep error.", e);
        }
        stopRecording();
        return;
    }

    public void stopRecording() {
        mrec.stop();
        recodingNow = false;
        mrec.release();
    }

    public boolean isRecodingNow() {
        return recodingNow;
    }
}
