#version 410

in vec3 a_position;		// MODEL
uniform mat4 u_mvpMatrix;	// MODEL -> NDC

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position, 1);    
}

