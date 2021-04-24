#version 410

in vec2 a_position;	// vertex position as a homogeneous 2D point in NDC 

void main() {
    gl_Position = vec4(a_position, 0, 1);
}

