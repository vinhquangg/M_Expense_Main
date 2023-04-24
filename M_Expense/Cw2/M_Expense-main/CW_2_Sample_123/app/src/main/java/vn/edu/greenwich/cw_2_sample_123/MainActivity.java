package vn.edu.greenwich.cw_2_sample_123;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected static final String _FILE_NAME = "cw_2_image_list.txt";

    protected ImageView imageView;
    protected EditText etImageLink;
    protected Button btnPrevious, btnNext, btnAdd;

    protected ArrayList<String> _imageList;
    protected int _currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        etImageLink = findViewById(R.id.etImageLink);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> addImage());
        btnNext.setOnClickListener(v -> nextImage());
        btnPrevious.setOnClickListener(v -> previousImage());

        _imageList = getImageList();
        _currentIndex = 0;

        loadImage();
    }

//    Function 1
    protected ArrayList<String> getImageList() {
        ArrayList<String> imageList = new ArrayList<>();

        imageList.add("https://www.traveloffpath.com/wp-content/uploads/2022/03/road-trip.jpg");
        imageList.add("https://hedonistshedonist.com/wp-content/uploads/2021/12/road-trip.jpg");
        imageList.add("https://contents.mediadecathlon.com/s860057/k$2b04047f45dd91d00d3bcfffa7589049/800x0/2304pt1945/3456xcr3456/default.jpg?format=auto&quality=80");

        getImageListFromFile(imageList);

        Toast.makeText(this, "Get list successfully.", Toast.LENGTH_SHORT).show();

        return imageList;
    }

    protected void loadImage() {
        Picasso.with(this).load(_imageList.get(_currentIndex)).into(imageView);
    }
    //  Function 2
    protected void addImage() {
        String imageURL = etImageLink.getText().toString();
        if(imageURL.length() == 0)
        {
            Toast.makeText(this, "Add not successful.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            _imageList.add(imageURL);
            writeURLToFile(imageURL);
            _currentIndex = _imageList.size()-1;
            etImageLink.setText("");
            Toast.makeText(this, "Added successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void nextImage() {
        ++_currentIndex;
        if(_currentIndex == _imageList.size())
        {
            _currentIndex = 0;
        }
        loadImage();
    }

    protected void previousImage() {
        --_currentIndex;
        if(_currentIndex < 0)
        {
            _currentIndex = _imageList.size()-1;
        }
        loadImage();
    }

    protected void writeURLToFile(String url) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(_FILE_NAME, MODE_APPEND));
            outputStreamWriter.write(url);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getImageListFromFile(ArrayList<String> imageList) {
        try {
            InputStream inputStream = openFileInput(_FILE_NAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String url = "";
                while ((url = bufferedReader.readLine()) != null) {
                    imageList.add(url);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}