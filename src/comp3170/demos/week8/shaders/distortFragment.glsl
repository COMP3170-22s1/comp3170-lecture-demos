#version 410

in vec3 v_colour;	// RGB

uniform bool u_maximise;

layout(location = 0) out vec4 o_colour;

void main() {
	vec3 c = v_colour;
	
	if (u_maximise) {
		// only display whichever component is greatest	
		c = (c.r > c.b) ? vec3(1,0,0) : vec3(0,0,1);	
		c = (c.g > c.r + c.b) ? vec3(0,1,0) : c;	
	}
			
    o_colour = vec4(c, 1);
}

