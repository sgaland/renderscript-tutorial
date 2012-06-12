/*
*   Copyright 2012 - Genymobile
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.genymobile.tutorial.renderscript;

import android.graphics.Bitmap;

public class DalvikFilter extends AbstractFilter {

    @Override
    public void applyFilter(Bitmap inputBitmap, Bitmap outputBitmap) {

        // Création des tableaux
        int[] mInputPixels = new int[inputBitmap.getHeight() * inputBitmap.getWidth()];
        int[] mOutputPixels = new int[outputBitmap.getHeight() * outputBitmap.getWidth()];

        // Récupération des données de l'image
        inputBitmap.getPixels(
                mInputPixels,
                0, inputBitmap.getWidth(),
                0, 0,
                inputBitmap.getWidth(), inputBitmap.getHeight());

        // Itération sur tout les pixels de l'image
        for (int i = 0; i < mInputPixels.length; i++) {
            // Récupération des paramètres de couleur
            int a = (mInputPixels[i] >> 24) & 0xff;
            int r = (mInputPixels[i] >> 16) & 0xff;
            int g = (mInputPixels[i] >> 8) & 0xff;
            int b = mInputPixels[i] & 0xff;

            // Application du filtre
            float tr = r * mMatrix[0] + g * mMatrix[3] + b * mMatrix[6];
            float tg = r * mMatrix[1] + g * mMatrix[4] + b * mMatrix[7];
            float tb = r * mMatrix[2] + g * mMatrix[5] + b * mMatrix[8];

            // Normalisation du résultat entre 0 et 255
            r = tr > 255 ? 255 : tr < 0 ? 0 : (int) tr;
            g = tg > 255 ? 255 : tg < 0 ? 0 : (int) tg;
            b = tb > 255 ? 255 : tb < 0 ? 0 : (int) tb;

            // Récupération de la valeur finale
            mOutputPixels[i] = a << 24 | r << 16 | g << 8 | b;
        }

        // Ecriture du résultat dans le bitmap résultat
        outputBitmap.setPixels(
                mOutputPixels,
                0, outputBitmap.getWidth(),
                0, 0,
                outputBitmap.getWidth(), outputBitmap.getHeight());

        mInputPixels = null;
        mOutputPixels = null;
    }
}
