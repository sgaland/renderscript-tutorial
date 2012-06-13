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

#pragma version(1)
#pragma rs java_package_name(com.genymobile.tutorial.renderscript)

#include "rs_graphics.rsh"

rs_program_fragment programFragment;
rs_program_raster programRaster;
rs_program_store programStore;

rs_allocation texture;

// Paramètres 
bool debug;
int width;
int height;

// Internal parameters
float rotation;
float last_time;
int frame;
int fps;

void init() {
	rotation = 0.0f;
	last_time = rsUptimeMillis();
	frame = 0;
	debug = false;
}

void printFPS(){
	if ((rsUptimeMillis() - last_time)  > 1000) {
		last_time = rsUptimeMillis();
		fps = frame;
		frame = 0;
		rsDebug("FPS", fps);
	}
	frame++;
}


int root() {

	// Nettoyage de l’écran avec une couleur unie (ici blanc)
	rsgClearColor(1.0f, 1.0f, 1.0f, 0.0f);  

	float startX = -width/2, startY = - height/2;
    
    // Mise en place de la matrice de projection
	rs_matrix4x4 projection;
	rsMatrixLoadOrtho(&projection, 0, rsgGetWidth(), rsgGetHeight(), 0, -width/2, width/2);
	rsgProgramVertexLoadProjectionMatrix(&projection);
	
	// Binding 
	rsgBindProgramFragment(programFragment);
	rsgBindTexture(programFragment, 0, texture);
	rsgBindProgramStore(programStore);
	rsgBindProgramRaster(programRaster);
	
	// Debug logs
	if (debug) printFPS();
	
	// Définition et application  de la matrice transformation (rotation+centrage)
	rs_matrix4x4 matrix;
    rsMatrixLoadIdentity(&matrix);
    rsMatrixTranslate(&matrix, rsgGetWidth()/2, rsgGetHeight()/2, 0.0f);
   	rsMatrixRotate(&matrix, rotation++, 0.0f, 1.0f, 0.0f);
    rsgProgramVertexLoadModelMatrix(&matrix);
    
    // Dessin de la texture
    rsgDrawQuadTexCoords(startX, startY, 0, 0, 0,
                         startX, startY + height, 0, 0, 1,
                         startX + width, startY + height, 0, 1, 1,
                         startX + width, startY, 0, 1, 0);

	return 16; // temps en ms entre les appels à root() ~60fps 
}