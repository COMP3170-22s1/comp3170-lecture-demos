#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 

uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_viewMatrix;			// WORLD -> VIEW
uniform mat4 u_projectionMatrix;	// VIEW -> NDC

out vec2 v_texcoord;	// UV 

void main() {
	v_texcoord = a_texcoord;
    gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * a_position;
}

