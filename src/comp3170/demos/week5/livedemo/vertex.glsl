#version 410

in vec4 a_position;	// vertex position as a homogeneous 3D point in MODEL 
in vec3 a_colour;	// vertex colour (RGB) 

uniform mat4 u_mvpMatrix;  		// MODEL -> NDC (= M_projection * M_view * M_model)

out vec3 v_colour;	// interpolated fragment colour (RGB)

void main() {
	v_colour = a_colour;	
    gl_Position =  u_mvpMatrix * a_position;
}

