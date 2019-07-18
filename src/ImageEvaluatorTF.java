import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.tensorflow.*;
import org.tensorflow.types.UInt8;

public class ImageEvaluatorTF
{
    public static final String INPUT_TENSOR_NAME = "ImageTensor:0";
    public static final String OUTPUT_TENSOR_NAME = "SemanticPredictions:0";

    private static final int BATCH_SIZE = 1;
    private static final long CHANNELS = 3;

    private SavedModelBundle savedModelBundle = null;

    public ImageEvaluatorTF()
    {
        savedModelBundle = SavedModelBundle.load("./deeplabv3_mnv2_cityscapes_train");
        // Graph graph = savedModelBundle.graph();
        // printOperations(graph);
    }

    public Tensor<?> Evaluate(byte[] inputImage)
    {
        BufferedImage scaledImage = ImageUtils.scaledImage(inputImage);

        Tensor<UInt8> inTensor = createInputTensor(scaledImage);

        Tensor<?> maskPixelsTensor = savedModelBundle.session().runner()
            .feed(INPUT_TENSOR_NAME, inTensor)
            .fetch(OUTPUT_TENSOR_NAME).run().get(0);

        int height = (int) maskPixelsTensor.shape()[1];
        int width = (int) maskPixelsTensor.shape()[2];
        long[][] maskPixels = maskPixelsTensor.copyTo(new long[BATCH_SIZE][height][width])[0]; // take 0 because the batch size is 1.
        int[][] maskPixelsInt = Utils.toIntArray(maskPixels);
        BufferedImage maskImage = ImageUtils.createMaskImage(maskPixelsInt, scaledImage.getWidth(), scaledImage.getHeight(), 0.35);
        BufferedImage blended = ImageUtils.blendMask(maskImage, scaledImage);

        //ImageIO.write(maskImage, "png", new File("mask.jpg"));
        //ImageIO.write(blended, "png", new File("blended.jpg"));

        return maskPixelsTensor;
    }

    public static Tensor<UInt8> createInputTensor(BufferedImage scaledImage) {
        if (scaledImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            throw new IllegalArgumentException(
                    String.format("Expected 3-byte BGR encoding in BufferedImage, found %d", scaledImage.getType()));
        }

        // ImageIO.read produces BGR-encoded images, while the model expects RGB.
        byte[] data = ImageUtils.bgrToRgb(ImageUtils.toBytes(scaledImage));

        // Expand dimensions since the model expects images to have shape: [1, None, None, 3]
        long[] shape = new long[] { BATCH_SIZE, scaledImage.getHeight(), scaledImage.getWidth(), CHANNELS };

        return Tensor.create(UInt8.class, shape, ByteBuffer.wrap(data));
    }
}