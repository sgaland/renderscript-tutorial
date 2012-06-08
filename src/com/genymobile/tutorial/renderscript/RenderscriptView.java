
package com.genymobile.tutorial.renderscript;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PixelFormat;
import android.renderscript.Allocation;
import android.renderscript.Matrix4f;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramVertex;
import android.renderscript.ProgramVertexFixedFunction;
import android.renderscript.RSTextureView;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;

public class RenderscriptView extends RSTextureView {

    // Attributs du scripts
    private RenderScriptGL mRs;
    private ScriptC_render mScript;

    
    private ProgramVertex mProgVertex;
    private ProgramVertexFixedFunction.Constants mProgramVertexConstant;
    private ProgramStore mProgrammeStore;
    private ProgramFragment mProgramFragment;
    private ProgramRaster mProgramRaster;

    private Allocation mTexture;

    public RenderscriptView(Context context) {
        super(context);
    }
    
    public RenderscriptView(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        // Chargement du script
        RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
        mRs = createRenderScriptGL(sc);
        mScript = new ScriptC_render(mRs, getResources(), R.raw.render);

        // Récupération de l'image
        loadTexture(getResources(), R.drawable.logo);
        
        // Initialisation des différents paramètres graphiques
        initProgramVertex();
        initProgramFragment();
        initProgramRaster();
        initProgramStore();

        mRs.bindRootScript(mScript);
    }

    private void loadTexture(Resources res, int id) {
        Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap b = BitmapFactory.decodeResource(res, id, options);
        mTexture = Allocation.createFromBitmap(mRs, b,
                Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
                Allocation.USAGE_GRAPHICS_TEXTURE);
        mScript.set_texture(mTexture);
        mScript.set_height(b.getHeight());
        mScript.set_width(b.getWidth());
    }

    private void initProgramFragment() {
        ProgramFragmentFixedFunction.Builder pgBuilder = new ProgramFragmentFixedFunction.Builder(mRs);
        pgBuilder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.REPLACE,
                ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
        mProgramFragment = pgBuilder.create();
        mScript.set_programFragment(mProgramFragment);
    }

    private void initProgramVertex() {
        ProgramVertexFixedFunction.Builder pvb = new ProgramVertexFixedFunction.Builder(mRs);
        mProgVertex = pvb.create();

        mProgramVertexConstant = new ProgramVertexFixedFunction.Constants(mRs);
        ((ProgramVertexFixedFunction) mProgVertex).bindConstants(mProgramVertexConstant);
        Matrix4f proj = new Matrix4f();
        proj.loadOrthoWindow(512, 512);
        mProgramVertexConstant.setProjection(proj);
        mScript.set_programVertex(mProgVertex);
    }
    
    private void initProgramStore() {
        mProgrammeStore = ProgramStore.BLEND_ALPHA_DEPTH_NONE(mRs);
        mScript.set_programStore(mProgrammeStore);
    }

    private void initProgramRaster() {
        mProgramRaster = ProgramRaster.CULL_NONE(mRs);
        mScript.set_programRaster(mProgramRaster);
    }
}
