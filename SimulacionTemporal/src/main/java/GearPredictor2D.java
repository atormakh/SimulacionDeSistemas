public class GearPredictor2D extends Integration2DMethod {

    Vector2[] states;
    double[] predictionCoefficients;
    double[] coefficients = new double[]{
            3d / 16,
            251d / 360,
            1d,
            11d / 18,
            1d / 6,
            1d / 60,
    };

    double dt;

    double mass;

    int NUM_PREDICTIONS = 6;

    Force2DFunction forceFunction;


    public GearPredictor2D(Force2DFunction forceFunction, Vector2 pos, Vector2 vel, double mass, double dt) {
        this.states = new Vector2[NUM_PREDICTIONS];
        this.predictionCoefficients = new double[NUM_PREDICTIONS];

        this.forceFunction = forceFunction;
        this.dt = dt;

        this.mass = mass;

        states[0] = pos;
        states[1] = vel;
        states[2] = forceFunction.getForce(pos, vel).scalarMultiply(1d / mass);
        for (int i = 3; i < NUM_PREDICTIONS; i++) {
            states[i] = new Vector2(0, 0);
        }


        int factorial = 1;
        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            predictionCoefficients[i] = Math.pow(dt, i) / factorial;
            factorial *= (i + 1);
        }
    }

    public Result2D get() {

        Vector2[] predictions = new Vector2[NUM_PREDICTIONS];

        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            predictions[i] = new Vector2(0, 0);
            for (int j = i; j < NUM_PREDICTIONS; j++) {
                predictions[i] = predictions[i].add(states[j].scalarMultiply(predictionCoefficients[j-i]));
            }
        }

        Vector2 accel = forceFunction.getForce(predictions[0], predictions[1]).scalarMultiply(1d / mass);

        Vector2 deltaR2 = (accel.substract(predictions[2])).scalarMultiply( dt * dt / 2);

        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            states[i]  = predictions[i].add(
                    deltaR2.scalarMultiply(coefficients[i]/predictionCoefficients[i])
            );
        }

        return new Result2D(states[0], states[1], states[2]);
    }
}
