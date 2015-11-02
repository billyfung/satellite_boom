double[][] multMatrix(double[][] m1, double[][] m2) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = 0;
      for (int k=0; k<3; k++) {
        res[i][j] += m1[i][k] * m2[k][j];
      }
    }
  }
  return res;
}

double[][] addMatrix(double[][] m1, double[][] m2) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m1[i][j] + m2[i][j];
    }
  }
  return res;
}

double[][] multMatrix(double[][] m1, double s) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m1[i][j]*s;
    }
  }
  return res;
}

Vector3D multMatrix(double[][] m, Vector3D v) {
  Vector3D res = new Vector3D(
    m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2] * v.getZ(),
    m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2] * v.getZ(),
    m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ()
  );
  return res;
}

Vector3D multMatrix(Vector3D v, double[][] m) {
  Vector3D res = new Vector3D(
    v.getX() * m[0][0] + v.getY() * m[1][0] + v.getZ() * m[2][0],
    v.getX() * m[0][1] + v.getY() * m[1][1] + v.getZ() * m[2][1],
    v.getX() * m[0][2] + v.getY() * m[1][2] + v.getZ() * m[2][2]
  );
  /*
  for (int j=0; j<3; j++) {
    res[0][j] = v.getX() * (m[0][j] + m[1][j] + m[2][j]);
    res[1][j] = v.getY() * (m[0][j] + m[1][j] + m[2][j]);
    res[2][j] = v.getZ() * (m[0][j] + m[1][j] + m[2][j]);
  }
  */
  return res;
}

double[][] multMatrix(double[][] m1, double[][] m2, double[][] m3) {
  return multMatrix(m1, multMatrix(m2, m3));
}

double[][] orthogonalizeMatrix(double[][] m) {
  Vector3D v1 = new Vector3D(m[0][0], m[0][1], m[0][2]);
  Vector3D v2 = new Vector3D(m[1][0], m[1][1], m[1][2]);
  Vector3D v3 = Vector3D.crossProduct(v1, v2).normalize();
  v2 = Vector3D.crossProduct(v3, v1).normalize();
  v1 = v1.normalize();
  
  double[][] res = new double[3][3];
  res[0][0] = v1.getX();
  res[0][1] = v1.getY();
  res[0][2] = v1.getZ();
  res[1][0] = v2.getX();
  res[1][1] = v2.getY();
  res[1][2] = v2.getZ();
  res[2][0] = v3.getX();
  res[2][1] = v3.getY();
  res[2][2] = v3.getZ();
  return res;
  
  /*
  Vector3D v1 = new Vector3D(m[0][0], m[1][0], m[2][0]);
  Vector3D v2 = new Vector3D(m[0][1], m[1][1], m[2][1]);
  Vector3D v3 = Vector3D.crossProduct(v1, v2).normalize();
  v2 = Vector3D.crossProduct(v3, v1).normalize();
  v1 = v1.normalize();
  
  double[][] res = new double[3][3];
  res[0][0] = v1.getX();
  res[0][1] = v2.getX();
  res[0][2] = v3.getX();
  res[1][0] = v1.getY();
  res[1][1] = v2.getY();
  res[1][2] = v3.getY();
  res[2][0] = v1.getZ();
  res[2][1] = v2.getZ();
  res[2][2] = v3.getZ();
  return res;
  */
}

/*
// multiply 4x4 matrix by a vector
double[] multMatrix4(double[][] m, double[] v) {
  double[] res = new double[4];
  res[0] = m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2] + m[0][3] * v[3];
  res[1] = m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2] + m[1][3] * v[3];
  res[2] = m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2] + m[2][3] * v[3];
  res[3] = m[3][0] * v[0] + m[3][1] * v[1] + m[3][2] * v[2] + m[3][3] * v[3];
  return res;
}
*/

double[][] transposeMatrix(double[][] m) {
  double res[][] = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m[j][i];
    }
  }
  return res;
}

double determinantMatrix(double[][] m) {
  double det = m[0][0] * (m[1][1]*m[2][2] - m[1][2] * m[2][1]);
  det -= m[0][1] * (m[1][0]*m[2][2] - m[1][2] * m[2][0]);
  det += m[0][2] * (m[1][0]*m[2][1] - m[1][1] * m[2][0]);
  return det;
}

double[][] invertMatrix(double[][] m) {
   double det = determinantMatrix(m);
   double tmp = 1.0 / det;
   
   double res[][] = new double[3][3];
   
   res[0][0] = tmp * (m[1][1] * m[2][2] - m[1][2] * m[2][1]);
   res[1][0] = tmp * (m[1][2] * m[2][0] - m[1][0] * m[2][2]);
   res[2][0] = tmp * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

   res[0][1] = tmp * (m[0][2] * m[2][1] - m[0][1] * m[2][2]);
   res[1][1] = tmp * (m[0][0] * m[2][2] - m[0][2] * m[2][0]);
   res[2][1] = tmp * (m[0][1] * m[2][0] - m[0][0] * m[2][1]);

   res[0][2] = tmp * (m[0][1] * m[1][2] - m[0][2] * m[1][1]);
   res[1][2] = tmp * (m[0][2] * m[1][0] - m[0][0] * m[1][2]);
   res[2][2] = tmp * (m[0][0] * m[1][1] - m[0][1] * m[1][0]);
   
   return res;
}

String makeString(double[][] m) {
  return "[[ " + m[0][0] + ", " + m[0][1] + ", " + m[0][2] + "];\n"
       + " [ " + m[1][0] + ", " + m[1][1] + ", " + m[1][2] + "];\n"
       + " [ " + m[2][0] + ", " + m[2][1] + ", " + m[2][2] + "]]";
}

static String makeString(Vector3D v) {
  return "[" + v.getX() + ", " + v.getY() + ", " + v.getZ() + "]";
}

static String makeString(Quaternion r) {
  //return "[" + r.x0 + ", " + r.x1 + ", " + r.x2 + ", " + r.x3 + "]";
  return "[" + r.getAngle() + ", [" + r.getAxis().getX() + ", " + r.getAxis().getY() + ", " + r.getAxis().getZ() + "]]";
}
