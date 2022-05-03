#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 

uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_mvpMatrix;			// WORLD -> VIEW

out vec4 v_position;	// WORLD

void main() {
	v_position = u_modelMatrix * a_position;
    gl_Position = u_mvpMatrix * a_position;
}

