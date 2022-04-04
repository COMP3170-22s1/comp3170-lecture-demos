#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in model 

void main() {
    gl_Position = a_position;
}

