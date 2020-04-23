#version 410

layout(location = 0) out vec4 colour;

void main() {
	float depth = gl_FragCoord.z; 
	
	// blend between red (near) and blue (far)
    colour = mix(vec4(1,0,0,1),vec4(0,0,1,1),depth);
}

