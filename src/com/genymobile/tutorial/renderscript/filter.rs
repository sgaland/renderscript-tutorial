// Version de Renderscript utilisé
#pragma version(1)

// Nom du package du script
#pragma rs java_package_name(com.genymobile.tutorial.renderscript)

// Matrice du filtre à appliquer
rs_matrix3x3 matrix;

void init() {
	rsMatrixLoadIdentity(&matrix);
}

// Point d'entrée principal du script effectuant l'opération sur un pixel
void root(const uchar4 *in, uchar4 *out) {
    float3 pixel = convert_float4(in[0]).rgb; // Récupération des couleurs
    pixel = rsMatrixMultiply(&matrix, pixel); // Application du filtre
    pixel = clamp(pixel, 0.f, 255.f); // Normalisation des valeurs (0..255)
    out->a = in->a; // Récupération de la valeur alpha
    out->rgb = convert_uchar3(pixel); // Conversion
}