package av.porter.duff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PorterDuffActivity extends AppCompatActivity {
    private PorterDuffView mPorterDuffView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff);

        mPorterDuffView = findViewById(R.id.porterDuffView);

        /**
         * Set above/below drawable image by layout XML or here by code
         * mPorterDuffView.setAboveResource(R.drawable.img4);
         * mPorterDuffView.setBelowResource(R.drawable.image1);
         */

    }

}