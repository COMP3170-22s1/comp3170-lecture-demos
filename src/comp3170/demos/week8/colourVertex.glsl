#version 410

in vec3 a_position;	
in vec4 a_colour;	

uniform mat4 u_worldMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

out vec4 v_colour;

void main() {	
	gl_Position = u_projectionMatrix * u_viewMatrix * u_worldMatrix * vec4(a_position, 1);
	
	// pass vertex colour to fragment shader
	v_colour = a_colour;
}

