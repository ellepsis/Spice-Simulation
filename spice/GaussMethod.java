package spice;

/**
 * Created by EllepsisRT on 28.05.2015.
 */
public class GaussMethod {
    static double[] GaussProcess(Matrix matrix) throws Exception {
        double[][] m = matrix.matrix;
        double tmp;
        int xln = matrix.getXLength();
        int yln = matrix.getYLength();
        double result[];

        result = new double[xln];
        for (int k = 1; k < xln; k++) {
            if (m[k][k] == 0){
                if (k == xln-1) throw new Exception("Can't swap elements: infinity solutions");
                for (int q = 0; q < xln; q++){
                    if (m[q][k] != 0){
                        result = m[q];
                        m[q]=m[k];
                        m[k] = result;
                    }
                }
            }
            for (int j = k; j < xln; j++) {
                tmp = m[j][k - 1] / m[k - 1][k - 1];
                for (int i = 0; i < yln; i++) {
                    m[j][i] = m[j][i] - tmp * m[k - 1][i];
                }
            }
        }
        for (int i = xln-1; i >=0; i--){
            result[i] = m[i][yln-1];
            for (int j = yln-2; j>i; j--){
                result[i] -= m[i][j]*result[j];
            }
            result[i] /= m[i][i];
            if (m[i][i] == 0) throw new Exception("Infinity variants of solution");
        }
        return result;
    }
}
