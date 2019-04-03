package in.justrobotics.text_to_speech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Button buttonSpeak;
    private EditText editText;
    float speechRate,speechPitch;
    String textToBeSpoken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SeekBar speechRateControl = findViewById(R.id.speechSpeed);
        speechRateControl.setProgress(75);
        final SeekBar speechPitchControl = findViewById(R.id.speechPitch);
        speechPitchControl.setProgress(100);
        tts = new TextToSpeech(this, this);
        buttonSpeak = (Button) findViewById(R.id.speak);
        editText = (EditText) findViewById(R.id.textToSpeak);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int vol_lvl = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max_vol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (vol_lvl==0 || vol_lvl<=3){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max_vol/2, 0);
        }
        vol_lvl = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(vol_lvl==0 || vol_lvl<=3){
            toastShort("Please turn the volume up");
        }
        buttonSpeak.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textToBeSpoken=editText.getText().toString();
                speechRate = (float) ((speechRateControl.getProgress()+50)/100.0);
                speechPitch = (float) (speechPitchControl.getProgress()/100.0);

                if (speechPitch==0){
                    speechPitch=0.05f;
                }
                tts.setSpeechRate(speechRate);
                tts.setPitch(speechPitch);
                tts.speak(textToBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
                //speakOut();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                buttonSpeak.setEnabled(true);
//                speakOut();
            }
        } else {
            Log.e("TTS", "Init Fail");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.about:
                displayAboutApp();
                break;
            case R.id.open_on_gp:
                openOnGooglePlay();
                break;
        }
        return true;
    }
    public void displayAboutApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Text-to-Speech");
        builder.setMessage(R.string.about_app);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Send Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                composeEmail();
            }
        });

        builder.show();
    }

    public void composeEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "shlokj@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Speech Synthesis App");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void openOnGooglePlay(){
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void toastShort(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    public void toastLong(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}