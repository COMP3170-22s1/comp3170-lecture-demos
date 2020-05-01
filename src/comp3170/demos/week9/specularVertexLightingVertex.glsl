#version 410

in vec3 a_position;	// model coordinates	
in vec3 a_normal;	// model coordinates

uniform mat4 u_mvpMatrix;		// model -> NDC
uniform mat3 u_normalMatrix;	// model normal -> world

uniform vec3 u_lightDir; 		// in world coordinates
uniform vec3 u_viewDir; 		// in world coordinates
uniform vec3 u_specularMaterial;	// RGB

uniform float u_specularity;	// Phong coefficient 

out vec3 v_colour;	// RGB

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position, 1);
    
    // convert the normal to world coordinates
    vec3 normal = u_normalMatrix * a_normal;

    // make sure the lightDir, viewDir and normal have length 1
    normal = normalize(normal);
    vec3 lightDir = normalize(u_lightDir);    
    vec3 viewDir = normalize(u_viewDir);    
    
    vec3 reflected = reflect(lightDir, normal);  
    
    // assuming the light is white
    vec3 specular = u_specularMaterial * pow(dot(reflected, viewDir), u_specularity);
    
    // interpolate to fragment colour
    v_colour = normal;    
}

