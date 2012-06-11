
package com.genymobile.tutorial.renderscript;

import android.graphics.Bitmap;

public abstract class AbstractFilter {

    // Matrice du filtre a appliquer (initialisée en matrice identité)
    protected float[] mMatrix = {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public void setMatrix(float[] matrix) {
        mMatrix = matrix;
    }

    // Abstract
    public abstract void applyFilter(Bitmap inputBitmap, Bitmap outputBitmap);
}
