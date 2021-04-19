#version 410

in vec4 a_position;	

uniform mat4 u_worldMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

void main() {	
	gl_Position = u_projectionMatrix * u_viewMatrix * u_worldMatrix * a_position;
}	
	

