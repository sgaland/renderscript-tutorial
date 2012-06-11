
package com.genymobile.tutorial.renderscript;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class RenderScriptTutorialActivity extends Activity {

    private float[] sepiaMatrix = {
            0.3588f, 0.2990f, 0.2392f,
            0.7044f, 0.5870f, 0.4696f,
            0.1368f, 0.1140f, 0.0912f
    };

    private Bitmap mInBitmap;
    private Bitmap mOutBitmap;

    private DalvikFilter dalvikFilter;
    private RenderScriptFilter renderFilter;

    private ImageView outImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mInBitmap = loadBitmap(R.raw.lena_std);

        outImageView = (ImageView) findViewById(R.id.out);

        mOutBitmap = Bitmap.createBitmap(
                mInBitmap.getWidth(),
                mInBitmap.getHeight(),
                mInBitmap.getConfig());

        outImageView.setImageBitmap(mOutBitmap);

        dalvikFilter = new DalvikFilter();
        dalvikFilter.setMatrix(sepiaMatrix);

        renderFilter = new RenderScriptFilter(this);
        renderFilter.setMatrix(sepiaMatrix);
    }

    public void executeDalvikFiltering(View v) {
        computeDalvikFiltering(mInBitmap, mOutBitmap);
        outImageView.invalidate();
    }

    public void executeRenderScriptFiltering(View v) {
        computeRenderScriptFiltering(mInBitmap, mOutBitmap);
        outImageView.invalidate();
    }

    private void computeDalvikFiltering(Bitmap inputBitmap, Bitmap outputBitmap) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 50; i++)
        {
            dalvikFilter.applyFilter(inputBitmap, outputBitmap);
        }

        Toast.makeText(this,
                "Dalvik running time: " + (System.currentTimeMillis() - t) / 50 + " ms",
                Toast.LENGTH_SHORT).show();
    }

    private void computeRenderScriptFiltering(Bitmap inputBitmap, Bitmap outputBitmap) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 50; i++)
        {
            renderFilter.applyFilter(inputBitmap, outputBitmap);
        }
        Toast.makeText(this,
                "Renderscript running time: " + (System.currentTimeMillis() - t) / 50 + " ms",
                Toast.LENGTH_SHORT).show();
    }

    private Bitmap loadBitmap(int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap b = BitmapFactory.decodeResource(getResources(), resource, options);
        return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Render View");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        startActivity(new Intent(this, RenderScriptViewActivity.class));
        return super.onMenuItemSelected(featureId, item);
    }

}
