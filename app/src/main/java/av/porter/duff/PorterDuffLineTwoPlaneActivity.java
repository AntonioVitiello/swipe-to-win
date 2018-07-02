package av.porter.duff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PorterDuffLineTwoPlaneActivity extends AppCompatActivity {
    private PorterDuffLineTwoPlanesView mPorterDuffView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff_line);

        /**
         * Set above/below drawable image by layout XML or here by code
         * mPorterDuffView.setAboveResource(R.drawable.img4);
         * mPorterDuffView.setBelowResource(R.drawable.image1);
         * mPorterDuffView.setScaleRatio(4);
         */

    }

}
