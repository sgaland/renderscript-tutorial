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

// Version de Renderscript utilisée
#pragma version(1)

// Nom du package du script et des classes générées
#pragma rs java_package_name(com.genymobile.tutorial.renderscript)

// Matrice du filtre à appliquer
rs_matrix3x3 filter;

// Fonction d'initialisation executée à la création du script
void init() {
	rsMatrixLoadIdentity(&filter);
}

// Point d'entrée principal du script effectuant l'opération sur un pixel
void root(const uchar4 *in, uchar4 *out) {
    float3 pixel = convert_float4(in[0]).rgb; // Récupération des couleurs
    pixel = rsMatrixMultiply(&filter, pixel); // Application du filtre
    pixel = clamp(pixel, 0.f, 255.f); // Normalisation des valeurs (0..255)
    out->a = in->a; // Récupération de la valeur alpha
    out->rgb = convert_uchar3(pixel); // Conversion
}