#version 410

in vec4 a_position;	// vertex position as a 3D vector in MODEL

void main() {
    gl_Position = a_position;
}

