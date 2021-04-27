#version 410

in vec4 a_position;	
in vec3 a_normal;	

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;
uniform mat3 u_normalMatrix;

out vec3 v_normal;

void main() {
	v_normal = u_normalMatrix * a_normal;
    gl_Position = u_projectionMatrix * u_viewMatrix * u_modelMatrix * a_position;
}

