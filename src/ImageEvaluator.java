import org.tensorflow.*;

public class ImageEvaluator
{
    private SavedModelBundle savedModelBundle = null;

    public ImageEvaluator()
    {
        savedModelBundle = SavedModelBundle.load("./deeplabv3_mnv2_cityscapes_train");
        // Graph graph = savedModelBundle.graph();
        // printOperations(graph);
    }

    public Tensor Evaluate()
    {
        return savedModelBundle.session().runner().feed("in", tensorInput).fetch("out").run().get(0);
    }
}