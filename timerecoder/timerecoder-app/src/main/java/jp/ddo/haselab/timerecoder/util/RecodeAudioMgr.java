
package jp.ddo.haselab.timerecoder.util;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * Recode audio and write file.
 * 
 * @author hasegawa
 */
public final class RecodeAudioMgr {

    /**
     * can recode?
     * 
     * @return true now recode ok/false can not recode
     */
    public static boolean canRecode() {

        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    private final MediaRecorder mrec;

    private boolean             recodingNow = false;

    /**
     * constractor.
     */
    public RecodeAudioMgr() {

        this.mrec = new MediaRecorder();
        initRecoder();
    }

    /**
     * init recoder.
     */
    public void initRecoder() {

        this.mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    /**
     * is now Recoding?
     * @return true yes Now recoding/false no.I'm not recoding.
     */
    public boolean isRecodingNow() {

        return this.recodingNow;
    }

    /**
     * start recoding.
     * @param argFileName filename
     * @param argRecodeSecondTime recoding time (second)
     * @throws IOException when can not write file.
     */
    public void startRecodingExternalStrage(final String argFileName,
            final int argRecodeSecondTime) throws IOException {

        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, argFileName);
        this.mrec.setOutputFile(file.getAbsolutePath());
        this.mrec.prepare();
        this.mrec.start();
        this.recodingNow = true;
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

    /**
     * stop recoding.
     * @return true レコード中で、中止した/false すでにレコード中ではなかった。
     */
    public boolean stopRecording() {

        if (this.recodingNow == false){
                return false;
        }
        this.mrec.stop();
        this.recodingNow = false;
        this.mrec.release();
        return true;
    }
}
