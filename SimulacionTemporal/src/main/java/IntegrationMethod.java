import java.util.function.Function;
import java.util.function.Supplier;

public abstract class IntegrationMethod implements Supplier<Result> {

    ForceFunction forceFunction;

    public IntegrationMethod(ForceFunction forceFunction) {
        this.forceFunction = forceFunction;
    }


}
