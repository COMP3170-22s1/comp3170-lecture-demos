#version 410

//uniform vec3 u_colour;			// colour as a 3D vector (r,g,b)
in vec3 v_colour;	// interpolated fragment colour (RGB)

layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

void main() {
    o_colour = vec4(v_colour, 1);
}
