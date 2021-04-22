#version 410

layout(location = 0) out vec4 o_colour;

void main() {
	float depth = gl_FragCoord.z;

	// blend between red (near), green (mid) and blue (far)
	if (depth < 0.5f) {
	    o_colour = mix(vec4(1,0,0,1), vec4(0,1,0,1), depth * 2);
	}
	else {
	    o_colour = mix(vec4(0,1,0,1), vec4(0,0,1,1), depth * 2 - 1);	
	}
}

