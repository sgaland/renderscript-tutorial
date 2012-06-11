
package com.genymobile.tutorial.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Matrix3f;
import android.renderscript.RenderScript;

public class RenderScriptFilter extends AbstractFilter {

    private Context mContext;

    // Script parameters
    private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private ScriptC_filter mScript;

    public RenderScriptFilter(Context context) {
        mContext = context;

        // Création du script
        mRS = RenderScript.create(mContext);
        mScript = new ScriptC_filter(mRS, mContext.getResources(), R.raw.filter);
    }

    @Override
    public void applyFilter(Bitmap inputBitmap, Bitmap outpuBitmap) {

        // Allocation de la mémoire content le bitmap
        mInAllocation = Allocation.createFromBitmap(
                mRS,
                inputBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);

        // Allocation de la mémoire pour récupérer l'image générée
        mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

        // Mise en place des paramètres
        Matrix3f sepiaMatrix = new Matrix3f(mMatrix);
        mScript.set_filter(sepiaMatrix);

        // Appel du script (rsForEach)
        mScript.forEach_root(mInAllocation, mOutAllocation);

        // Copie du résultat dans le bitmap de sortie
        mOutAllocation.copyTo(outpuBitmap);

        // Cleaning allocations
        mInAllocation.destroy();
        mInAllocation = null;
        mOutAllocation.destroy();
        mOutAllocation = null;
    }

}
