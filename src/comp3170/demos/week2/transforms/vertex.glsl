#version 410

in vec2 a_position;	// vertex position as a 2D vector in MODEL
uniform mat4 u_modelMatrix; // model->world transformation matrix 

void main() {
	// pad the vector to 4d homogeneous
	vec4 p = vec4(a_position, 0, 1);

	// apply the model->world transform
	gl_Position = u_modelMatrix * p;	
}

