// Renders the space background

class Skybox {
  
  private GLTexture sky_tex;
  
  Skybox() {
    sky_tex = new GLTexture(Phys350Project.this,"spaceskybox.jpg");
  }
  
  void render_skybox() {
    pushMatrix();
      resetMatrix();
  
      hint(DISABLE_DEPTH_TEST);
      noStroke();
      scale(Config.FAR_Z_CLIP, Config.FAR_Z_CLIP, Config.FAR_Z_CLIP);
      textureMode(NORMALIZED);
      // Face intersects +x axis
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(1, -1, 1, 0, 1);
        vertex(1, -1, -1, 1, 1);
        vertex(1, 1, -1, 1, 0);
  
      endShape();
      // Face intercepts -x
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(-1, 1, 1, 0, 0);
        vertex(-1, -1, 1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(-1, 1, -1, 1, 0);
  
      endShape();
      // Face intercepts +y
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(-1, 1, 1, 0, 1);
        vertex(-1, 1, -1, 1, 1);
        vertex(1, 1, -1, 1, 0);
  
      endShape();   
      // Face intercepts -y
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, -1, 1, 0, 0);
        vertex(-1, -1, 1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(1, -1, -1, 1, 0);
  
      endShape();     
      // Face intercepts +z
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(-1, 1, 1, 0, 1);
        vertex(-1, -1, 1, 1, 1);
        vertex(1, -1, 1, 1, 0);
  
      endShape();   
      // Face intercepts -z
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, -1, 0, 0);
        vertex(-1, 1, -1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(1, -1, -1, 1, 0);
  
      endShape();
      hint(ENABLE_DEPTH_TEST);
    popMatrix();
  }
}
