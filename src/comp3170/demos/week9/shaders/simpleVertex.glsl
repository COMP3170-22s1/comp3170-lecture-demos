#version 410

in vec4 a_position;	

uniform mat4 u_mvpMatrix;

void main() {
    gl_Position = u_mvpMatrix * a_position;
}

