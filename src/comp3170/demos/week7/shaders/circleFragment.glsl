#version 410

uniform vec2 u_screenSize;
uniform vec3 u_colour;

layout(location = 0) out vec4 o_colour;

void main() {
	vec2 p = floor(gl_FragCoord.xy) + gl_SamplePosition;
	p = (p / u_screenSize) * 2 - 1;
	
	if (length(p) <= 1) {
		o_colour = vec4(u_colour, 1);
	}	
	else {
	    o_colour = vec4(0, 0, 0, 1);	
	}
}

