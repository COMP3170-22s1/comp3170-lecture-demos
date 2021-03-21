#version 410

in vec4 a_position;	// vertex position as a homogeneous 2D point in model 

uniform mat4 u_modelMatrix;	// MODEL -> WORLD (= NDC)

void main() {
	// pad to a homogeneous 3D point
    gl_Position = u_modelMatrix * a_position;
}

