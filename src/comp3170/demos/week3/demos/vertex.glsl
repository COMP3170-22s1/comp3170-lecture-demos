#version 410

in vec2 a_position;	// vertex position as a 2D vector in MODEL

void main() {
    gl_Position = vec4(a_position,0,1);
}

