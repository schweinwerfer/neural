/**
 * @author Deus Jeraldy
 * @Email: deusjeraldy@gmail.com
 * BSD License
 */

// np.java -> https://gist.github.com/Jeraldy/7d4262db0536d27906b1e397662512bc

import java.util.Arrays;

public class NN {

    public static void main(String[] args) {

        double[][] inputData = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] expectedOutputData = {{0}, {1}, {1}, {0}};

        int sizeOfTrainingData = 4;
        int nodes = 2;

        inputData = np.T(inputData);
        expectedOutputData = np.T(expectedOutputData);

        double[][] W1 = np.random(nodes, 2);
        double[][] b1 = new double[nodes][sizeOfTrainingData];

        double[][] W2 = np.random(1, nodes);
        double[][] b2 = new double[1][sizeOfTrainingData];

        for (int i = 0; i < 10000; i++) {
            // Foward Prop
            // LAYER 1
            double[][] rawOutputLayer1 = np.add(np.dot(W1, inputData), b1);
            double[][] outputLayer1 = np.sigmoid(rawOutputLayer1);

            //LAYER 2
            double[][] rawOutputLayer2 = np.add(np.dot(W2, outputLayer1), b2);
            double[][] outputLayer2 = np.sigmoid(rawOutputLayer2);

            double cost = np.cross_entropy(sizeOfTrainingData, expectedOutputData, outputLayer2);
            //costs.getData().add(new XYChart.Data(i, cost));

            // Back Prop
            //LAYER 2
            double[][] dZ2 = np.subtract(outputLayer2, expectedOutputData);
            double[][] dW2 = np.divide(np.dot(dZ2, np.T(outputLayer1)), sizeOfTrainingData);
            double[][] db2 = np.divide(dZ2, sizeOfTrainingData);

            //LAYER 1
            double[][] dZ1 = np.multiply(np.dot(np.T(W2), dZ2), np.subtract(1.0, np.power(outputLayer1, 2)));
            double[][] dW1 = np.divide(np.dot(dZ1, np.T(inputData)), sizeOfTrainingData);
            double[][] db1 = np.divide(dZ1, sizeOfTrainingData);

            // G.D
            W1 = np.subtract(W1, np.multiply(0.01, dW1));
            b1 = np.subtract(b1, np.multiply(0.01, db1));

            W2 = np.subtract(W2, np.multiply(0.01, dW2));
            b2 = np.subtract(b2, np.multiply(0.01, db2));

            if (i % 400 == 0) {
                System.out.println("==============");
                System.out.println("Cost = " + cost);
                System.out.println("Predictions = " + Arrays.deepToString(outputLayer2));
            }
        }
    }
}