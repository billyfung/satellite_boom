class InertiaTensor {
  double matrix[][];
  double invertedMatrix[][];
  static final double rows = 3;
  static final double cols = 3;

  InertiaTensor(double matrix_[][]) {
    matrix = new double[3][3];
    matrix[0][0] = matrix_[0][0];
    matrix[0][1] = matrix_[0][1];
    matrix[0][2] = matrix_[0][2];
    matrix[1][0] = matrix_[1][0];
    matrix[1][1] = matrix_[1][1];
    matrix[1][2] = matrix_[1][2];
    matrix[2][0] = matrix_[2][0];
    matrix[2][1] = matrix_[2][1];
    matrix[2][2] = matrix_[2][2];
    
    invertedMatrix = invertMatrix(matrix);
  }
  
  InertiaTensor(double xx, double xy, double xz, double yx, double yy, double yz, double zx, double zy, double zz) {
    matrix = new double[3][3];
    matrix[0][0] = xx;
    matrix[0][1] = xy;
    matrix[0][2] = xz;
    matrix[1][0] = yx;
    matrix[1][1] = yy;
    matrix[1][2] = yz;
    matrix[2][0] = zx;
    matrix[2][1] = zy;
    matrix[2][2] = zz;
    
    invertedMatrix = invertMatrix(matrix);
  }
  
  double xx() {
    return matrix[0][0];
  }
  
  double xy() {
    return matrix[0][1];
  }
  
  double xz() {
    return matrix[0][2];
  }
  
  double yx() {
    return matrix[1][0];
  }
  
  double yy() {
    return matrix[1][1];
  }
  
  double yz() {
    return matrix[1][2];
  }
  
  double zx() {
    return matrix[2][0];
  }
  
  double zy() {
    return matrix[2][1];
  }
  
  double zz() {
    return matrix[2][2];
  }
  
  double[][] rotatedMatrix(Quaternion r) {
    // Apply a rotation to the inertia tensor
    // this is done by I' = Rl*I*trans(Rl) where Rl is the left rotation matrix of the quaternion
    // trans(Rr)*I*Rr would also work where Rr is the right rotation matrix
    double rotMatrix[][] = r.getRightMatrix();
    return multMatrix(rotMatrix, matrix, transposeMatrix(rotMatrix));
  }
  
  double[][] inverseRotatedMatrix(Quaternion r) {
    // see above
    double rotMatrix[][] = r.getRightMatrix();
    return multMatrix(rotMatrix, invertedMatrix, transposeMatrix(rotMatrix));
  }
  
  Vector3D mult(Vector3D vec) {
    return multMatrix(matrix, vec);
  }
  
  Vector3D multInverse(Vector3D vec) {
    return multMatrix(invertedMatrix, vec);
  }
  
  String toString() {
    return makeString(matrix);
  }
}
