public class GearPredictor extends IntegrationMethod {

    double[] states;
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


    public GearPredictor(ForceFunction forceFunction, double pos, double vel, double mass, double dt) {
        super(forceFunction);
        this.states = new double[NUM_PREDICTIONS];
        this.predictionCoefficients = new double[NUM_PREDICTIONS];

        this.forceFunction = forceFunction;

        this.dt = dt;

        this.mass = mass;

        states[0] = pos;
        states[1] = vel;
        states[2] = forceFunction.getForce(pos, vel) / mass;
        states[3] = forceFunction.getForce(states[1], states[2]) / mass;
        states[4] = forceFunction.getForce(states[2], states[3]) / mass;
        states[5] = forceFunction.getForce(states[3], states[4]) / mass;

        int factorial = 1;
        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            predictionCoefficients[i] = Math.pow(dt, i) / factorial;
            factorial *= (i + 1);
        }
    }

    @Override
    public Result get() {

        double[] predictions = new double[NUM_PREDICTIONS];

        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            for (int j = i; j < NUM_PREDICTIONS; j++) {
                predictions[i] += states[j] * predictionCoefficients[j-i];
            }
        }

        double accel = forceFunction.getForce(predictions[0], predictions[1]) / mass;

        double deltaR2 = (accel - predictions[2]) * dt * dt / 2;

        for (int i = 0; i < NUM_PREDICTIONS; i++) {
            states[i]  = predictions[i] + coefficients[i] * deltaR2 / predictionCoefficients[i];
        }

        return new Result(states[0], states[1], states[2]);
    }
}
