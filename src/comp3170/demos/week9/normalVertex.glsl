#version 410

in vec3 a_position;	// model coordinates	
in vec3 a_normal;	// model coordinates

uniform mat4 u_mvpMatrix;		// model -> NDC
uniform mat3 u_normalMatrix;	// model normal -> world

out vec3 v_colour;	// RGB

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position, 1);
    
    // convert the normal to world coordinates
    //vec3 normal = u_normalMatrix * a_normal;
    vec3 normal = a_normal;

    // make sure the lightDir and normal have length 1
    normal = normalize(normal);
    
    // colour vertex with normal
    v_colour = normal;    
}

