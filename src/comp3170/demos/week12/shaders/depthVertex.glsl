#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 

uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_viewMatrix;			// WORLD -> VIEW
uniform mat4 u_projectionMatrix;	// VIEW -> NDC

out vec4 v_viewPos; // VIEW

void main() {
	v_viewPos = u_viewMatrix * u_modelMatrix * a_position;
    gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * a_position;
}

