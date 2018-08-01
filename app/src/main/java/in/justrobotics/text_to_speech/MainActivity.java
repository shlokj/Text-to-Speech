package in.justrobotics.text_to_speech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Button buttonSpeak;
    private EditText editText;
    String textToBeSpoken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        buttonSpeak = (Button) findViewById(R.id.speak);
        editText = (EditText) findViewById(R.id.textToSpeak);

        buttonSpeak.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textToBeSpoken=editText.getText().toString();
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
        }
        return true;
    }
    public void displayAboutApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Text-to-Speech");
        builder.setMessage("This app is a realisation of Shlok Jhawar.\nDeveloper email: shlokj@gmail.com");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}