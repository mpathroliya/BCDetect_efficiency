package com.edge.iitbhu.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Duration;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Testing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Testing extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Classifier classifier;
    TextView resultText;
    TextView textView;
    Button start;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Testing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Testing.
     */
    // TODO: Rename and change types and number of parameters
    public static Testing newInstance(String param1, String param2) {
        Testing fragment = new Testing();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testing, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        classifier = new Classifier(getActivity());
        resultText = view.findViewById(R.id.testing_result);
        textView = view.findViewById(R.id.test_wait);
        start= view.findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                textView.setText(R.string.testing_wating);
                textView.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = testTime(classifier);
                textView.setText(R.string.testing_resultText);
                resultText.setText(result);
                resultText.setVisibility(View.VISIBLE);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String testTime(Classifier classifier){

        LocalDateTime start = LocalDateTime.now();
        CharSequence iterString = textView.getText();
        for(int i=0;i<50;i++){
            textView.setText(iterString+"\n"+i+"/100");
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    testImageEnum.picArray[0][i]);
            int digit = classifier.classify(bitmap);
//            Log.v("inference",Integer.toString(digit));

        }
        for(int i=0;i<50;i++){
            textView.setText(iterString+"\n"+(50+i)+"/100");
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    testImageEnum.picArray[1][i]);
            int digit = classifier.classify(bitmap);
//            Log.v("inference ",Integer.toString(digit));
        }
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start,end);

        String res = "\nTime Taken: "+(duration.getSeconds())+" s";

        return res;
    }

    private Bitmap convertImageViewToBitmap(ImageView iView){
        BitmapDrawable drawable = (BitmapDrawable) iView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }
}