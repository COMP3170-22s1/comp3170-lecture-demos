#version 410

uniform vec4 u_lightPosition;
in vec4 v_position;

layout(location = 0) out vec4 depth;

void main() {
	// calculate the distance from the light to the fragment 
	vec4 v = u_lightPosition - v_position;
	float d = length(v);
	 		
	// write it to the buffer
	depth = vec4(d,d,d,1);
}

