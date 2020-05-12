#version 410

in vec3 a_position;	
in vec3 a_colour;	

out vec3 v_colour;

uniform mat4 u_mvpMatrix;

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position,1);
    v_colour = a_colour;
}

