#version 410

uniform mat4 u_modelMatrix;

in vec2 a_position;	// vertex position as a 3D vector in MODEL

void main() {
	vec4 p = vec4(a_position, 0, 1);
    gl_Position = u_modelMatrix * p;
}

