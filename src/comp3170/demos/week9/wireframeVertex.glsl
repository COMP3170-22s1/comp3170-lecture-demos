#version 410

in vec3 a_position;
in vec3 a_barycentric;

uniform mat4 u_mvpMatrix;

out vec3 v_barycentric;
out vec3 v_position;

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position,1);
    v_barycentric = a_barycentric;
    v_position = a_position;    
}

