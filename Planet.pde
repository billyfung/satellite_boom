class Planet extends Object  {
  protected GLTexture texmap;
  private int sDetail = 50; // Increase to make the sphere more detailed
  float radius;

  Planet(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    super(pos_new, vel_new, acc_new, mass);
 
    texmap = new GLTexture(Phys350Project.this, tex_name); 
    solver = new ODERK4Solver();
    radius = r;
    bounding_sphere_radius = r;
    init_model();
  }
  
  // Create the model and store it for future rendering
  void init_model() {
    float delta = 360.0/sDetail;
    float[] sphereX, sphereY, sphereZ; // used for drawing
    float[] cx = new float[sDetail];
    float[] cz = new float[sDetail];
    ArrayList vertices;
    ArrayList normals;   
    ArrayList uv_map;
    int v1,v11,v2;
    
    // Calc unit circle in XZ plane
    for (int i = 0; i < sDetail; i++) {
      cx[i] = -trig.ncos((int) (i*delta));
      cz[i] = trig.nsin((int) (i*delta));
    }
    
    // Computing vertexlist vertexlist starts at south pole
    int vertCount = sDetail * (sDetail-1) + 2;
    int currVert = 0;
    
    // Re-init arrays to store vertices
    sphereX = new float[vertCount];
    sphereY = new float[vertCount];
    sphereZ = new float[vertCount];
    float angle_step = (360.0*0.5f)/sDetail;
    float angle = angle_step;
    
    // Step along Y axis
    for (int i = 1; i < sDetail; i++) {
      float curradius = trig.nsin((int) angle % trig.SINCOS_LENGTH);
      float currY = -trig.ncos((int) angle % trig.SINCOS_LENGTH);
      for (int j = 0; j < sDetail; j++) {
        sphereX[currVert] = cx[j] * curradius;
        sphereY[currVert] = currY;
        sphereZ[currVert++] = cz[j] * curradius;
      }
      angle += angle_step;
    }
    
    vertices = new ArrayList();
    normals = new ArrayList();
    uv_map = new ArrayList();
    
    float iu = (float) (1.0 / (sDetail));
    float iv = (float) (1.0 / (sDetail));

    float u=0,v=iv;
    for (int i = 0; i < sDetail; i++) {
      addVertex(vertices, normals,uv_map,0, -radius, 0,u,0);
      addVertex(vertices, normals,uv_map,sphereX[i]*radius, sphereY[i]*radius, sphereZ[i]*radius, u, v);
      u+=iu;
    }
    addVertex(vertices, normals,uv_map,0, -radius, 0,u,0);
    addVertex(vertices, normals,uv_map,sphereX[0]*radius, sphereY[0]*radius, sphereZ[0]*radius, u, v);
     
    
    // Middle rings
    int voff = 0;
    for(int i = 2; i < sDetail; i++) {
      v1=v11=voff;
      voff += sDetail;
      v2=voff;
      u=0;

      for (int j = 0; j < sDetail; j++) {
        addVertex(vertices, normals,uv_map,sphereX[v1]*radius, sphereY[v1]*radius, sphereZ[v1++]*radius, u, v);
        addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2++]*radius, u, v+iv);
        u+=iu;
      }
    
      // Close each ring
      v1=v11;
      v2=voff;
      addVertex(vertices, normals,uv_map,sphereX[v1]*radius, sphereY[v1]*radius, sphereZ[v1]*radius, u, v);
      addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2]*radius, u, v+iv);
     
      v+=iv;
    }
    u=0;
    
    // Add the northern cap

    for (int i = 0; i < sDetail; i++) {
      v2 = voff + i;
      addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2]*radius, u, v);
      addVertex(vertices, normals,uv_map,0, radius, 0,u,v+iv);    
      u+=iu;
    }
    addVertex(vertices, normals,uv_map,sphereX[voff]*radius, sphereY[voff]*radius, sphereZ[voff]*radius, u, v); 
    
    model = new GLModel(Phys350Project.this, vertices.size(), TRIANGLE_STRIP, GLModel.STATIC);
    // Sets the coordinates.
    model.updateVertices(vertices);    
    
    // Sets the normals.    
    model.initNormals();
    model.updateNormals(normals);    
    
    model.initTextures(1);    
    model.setTexture(0, texmap);

    model.updateTexCoords(0,uv_map);
    model.initColors();
    model.setColors(255);
  }
  
  private void addVertex(ArrayList vertices, ArrayList normals, ArrayList uv_map, float x, float y, float z, float u, float v)
  {
      PVector vert = new PVector(x, y, z);
      PVector vertNorm = PVector.div(vert, vert.mag()); 
      PVector uv = new PVector(u, v, 0);
      vertices.add(vert);
      normals.add(vertNorm);
      uv_map.add(uv);
  } 
    
  
  void render()
  {
    pushMatrix();
      noStroke();
      translate((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
      rotateX(-PI/2.0); // rotate so z axis up goes through north pole
      model.render();
    popMatrix();
  }
  
  void tick(float dt) {
    Vector3D temp = new Vector3D();
   
    // Calculate the forces between each object
    /*
    for(int i = 0; i < objects.size(); i++) {
      Vector3D x;
      Object o = (Object)objects.get(i);
      if(o != this) { // Don't need to calculate force on self
        x = o.getPosition().subtract(getPosition()); // Distance vector between the two objects
        temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*o.getMass()/Math.pow(x.getNorm(), 3)));
      }
    }
    
    // RK4
    setAcceleration(temp);
    solver.setValue(getPosition(), getVelocity(), temp);
    solver.calcRK4(dt, this);
    
    setPosition(solver.getPosition());
    setVelocity(solver.getVelocity());
    setTime(current_time);
    saveValues();
    
    */
  }      
}
