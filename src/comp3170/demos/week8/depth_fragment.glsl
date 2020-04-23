#version 410

uniform vec3 u_colour;

layout(location = 0) out vec4 colour;

void main() {
	vec3 dummy = u_colour; // this is necessary so u_colour doesn't get deleted
	
	float depth = gl_FragCoord.z; 
	
	// blend between red (near) and blue (far)
    colour = mix(vec4(1,0,0,1),vec4(0,0,1,1),depth);
}

