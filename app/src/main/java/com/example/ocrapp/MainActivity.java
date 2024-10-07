package com.example.ocrapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 123;

    TextView t1;

    InputImage inputImage;

    ImageView imageView2;

    TextRecognizer recognizer;

    TextView textView;

    Button image,speech;


    public Bitmap textImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
       // recognizer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
       // recognizer = TextRecognition.getClient(new JapaneseTextRecognizerOptions.Builder().build());
       // recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        t1 = findViewById(R.id.textView);
        image = findViewById(R.id.choose_image);
        imageView2 = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);
        speech = findViewById(R.id.speech);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                t1.setVisibility(TextView.INVISIBLE);
                openGallery();
            }
        });

        TextToSpeech  t = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t.setLanguage(Locale.getDefault());
//                    t.setLanguage(new Locale("bn_BD"));
                }
            }
        });

        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t.speak(textView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {

            if (data != null) {

                byte[] byteArray = new byte[0];
                String filePath = null;

                try {
                    inputImage = InputImage.fromFilePath(this, data.getData());

                    Bitmap resultUri = inputImage.getBitmapInternal();

                    Glide.with(MainActivity.this)
                            .load(resultUri)
                            .into(imageView2);



                    Task<Text> result =
                            recognizer.process(inputImage)
                                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                                        @Override
                                        public void onSuccess(Text visionText) {
                                            processTextBlock(visionText);
                                        }
                                    })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                }
                                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            textView.append("\n");
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
//                textView.append("\n");
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    textView.append(" ");
                    String elementText = element.getText();
                    textView.append(elementText);
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }

    }
    protected void onPause() {
        if(!t.isSpeaking()){
            super.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}