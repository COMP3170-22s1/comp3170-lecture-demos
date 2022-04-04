#version 410

uniform vec2 u_screenSize;

layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

const float TAU = 2. * 355. / 113;

void main() {
	vec2 p = gl_FragCoord.xy / u_screenSize;  // convert to range (0,0) - (1,1)

	// divide the screen into 100 x 100 cells

	p = fract(p * 100.) * 2 - 1;

	// draw a circle in each cell
	
	vec3 colour = vec3(0,0,0);

	if (length(p) < 0.5) {
		colour = vec3(1,1,1); 
	}

    o_colour = vec4(colour,1);
}
