class Sun extends Planet {
  
  Sun(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    super(pos_new, vel_new, acc_new, mass, r, tex_name); 
    solver = new ODEEulerSolver();
    radius = r;
    bounding_sphere_radius = r;

  }

  void render() {
    //lightFalloff(0.0,0.0000005,0.0);
    ambientLight(255,255,255,(float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
    
    super.render();
  }
}
