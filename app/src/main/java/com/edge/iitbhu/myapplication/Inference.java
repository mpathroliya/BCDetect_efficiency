package com.edge.iitbhu.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inference#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inference extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "amount";
    private static final String ARG_PARAM2 = "param2";
    private Classifier classifier;
    ImageView imageView;
    Button chooseButton;
    TextView resultText;
    Boolean detectFlag = false;
    private static final int IMAGE_PICK_CODE = 1000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Inference() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inference.
     */
    // TODO: Rename and change types and number of parameters
    public static Inference newInstance(String param1, String param2) {
        Inference fragment = new Inference();
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
        return inflater.inflate(R.layout.fragment_inference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
//        TextView textView = view.findViewById(R.id.inference_text);
//        textView.setText(mParam1);
        imageView = view.findViewById(R.id.inference_image);
        chooseButton = view.findViewById(R.id.choose_button);
        resultText = view.findViewById(R.id.inference_text);
        classifier = new Classifier(getActivity());

        //defining on click behavior
        chooseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                // Create a new intent to pick image for external app
                Intent imageIntent =new Intent(Intent.ACTION_PICK);
                // set type of file being excepted, image in our case, hence it opens gallery
                imageIntent.setType("image/*");
                // Run the activity for when we've picked the image
                startActivityForResult(imageIntent,IMAGE_PICK_CODE);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detectFlag = false;
        if(resultCode == RESULT_OK &&  requestCode == IMAGE_PICK_CODE){
            //set image to image view
            detectFlag = true;
            imageView.setImageURI(data.getData());

            // Convert it to a bitmap which will be used for inference
            Bitmap imageBitmap = convertImageViewToBitmap(imageView);
            imageView.setImageBitmap(imageBitmap);

            Bitmap bitmap = convertImageViewToBitmap(imageView);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int imageSize = Math.min(width, height);
            bitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, false);

            int digit = classifier.classify(bitmap);
            String res = ""+(digit);
            if(digit==1) res = "Malignant";
            else res = "Benign";
            resultText.setText(res);
        }
    }

    private Bitmap convertImageViewToBitmap(ImageView iView){
        BitmapDrawable drawable = (BitmapDrawable) iView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }
}
