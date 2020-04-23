#version 410

uniform vec4 u_colour;
in vec3 v_barycentric;


layout(location = 0) out vec4 colour;

void main() {

	// find the smallest of the barycentric coordinates
	float b = min(v_barycentric.x, min(v_barycentric.y, v_barycentric.z));
	
	// discard fragments in the middle of the triangle
	if (b > 0.1) {
		discard;
	}	 

    colour = u_colour;
}

