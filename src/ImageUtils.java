import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


public class ImageUtils
{
    private static final int REQUIRED_INPUT_IMAGE_SIZE = 513;

    public static BufferedImage scaledImage(String imagePath) {
        try {
            return scaledImage(ImageIO.read(new File(imagePath)));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Image from: " + imagePath, e);
        }
    }

    public static BufferedImage scaledImage(byte[] image) {
        try {
            return scaledImage(ImageIO.read(new ByteArrayInputStream(image)));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Image from byte array", e);
        }
    }

    public static BufferedImage scaledImage(BufferedImage image) {
        double scaleRatio = 1.0 * REQUIRED_INPUT_IMAGE_SIZE / Math.max(image.getWidth(), image.getHeight());
        return scale(image, scaleRatio);
    }

    private static BufferedImage scale(BufferedImage originalImage, double scale) {
        int newWidth = (int) (originalImage.getWidth() * scale);
        int newHeight = (int) (originalImage.getHeight() * scale);

        Image tmpImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        // BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
        // TYPE_INT_BGR);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
        // BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
        // originalImage.getType());

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmpImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    public static BufferedImage blendMask(BufferedImage mask, BufferedImage background) {
        overlayImages(background, mask, 0, 0);
        return background;
    }

    public static BufferedImage overlayImages(BufferedImage bgImage, BufferedImage fgImage, int fgX, int fgY) {
        // Foreground image width and height cannot be greater than background image
        // width and height.
        if (fgImage.getHeight() > bgImage.getHeight() || fgImage.getWidth() > fgImage.getWidth()) {
            throw new IllegalArgumentException("Foreground Image Is Bigger In One or Both Dimensions"
                    + "nCannot proceed with overlay." + "nn Please use smaller Image for foreground");
        }

        // Create a Graphics from the background image
        Graphics2D g = bgImage.createGraphics();

        // Set Antialias Rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image at location (0,0)
        g.drawImage(bgImage, 0, 0, null);

        // Draw foreground image at location (fgX,fgy)
        g.drawImage(fgImage, fgX, fgY, null);

        g.dispose();
        return bgImage;
    }

    public static BufferedImage createMaskImage(int[][] maskPixels, int width, int height, double transparency) {

        maskPixels = rotate(maskPixels);

        int maskWidth = maskPixels.length;
        int maskHeight = maskPixels[0].length;

        int[] maskArray = new int[maskWidth * maskHeight];
        int k = 0;
        for (int i = 0; i < maskHeight; i++) {
            for (int j = 0; j < maskWidth; j++) {
                Color c = (maskPixels[j][i] == 0) ? Color.BLACK : getClassColor(maskPixels[j][i]);
                int t = (int) (255 * (1 - transparency));
                maskArray[k++] = new Color(c.getRed(), c.getGreen(), c.getBlue(), t).getRGB();
            }
        }

        // Turn the pixel array into image;
        BufferedImage maskImage = new BufferedImage(maskWidth, maskHeight, BufferedImage.TYPE_INT_ARGB);
        maskImage.setRGB(0, 0, maskWidth, maskHeight, maskArray, 0, maskWidth);

        // Stretch the image to fit the target box width and height!
        return toBufferedImage(maskImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    private static int[][] rotate(int[][] input) {

        int w = input.length;
        int h = input[0].length;

        int[][] output = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = w - 1; x >= 0; x--) {
                output[y][x] = input[x][y];
            }
        }
        return output;
    }

    public static BufferedImage toBufferedImage(Image img) {
        // if (img instanceof BufferedImage) {
        // return (BufferedImage) img;
        // }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static byte[] bgrToRgb(byte[] brgImage) {
        byte[] rgbImage = new byte[brgImage.length];
        for (int i = 0; i < brgImage.length; i += 3) {
            rgbImage[i] = brgImage[i + 2];
            rgbImage[i + 1] = brgImage[i + 1];
            rgbImage[i + 2] = brgImage[i];
        }
        return rgbImage;
    }

    public static byte[] toBytes(BufferedImage bufferedImage) {
        return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }

    public static final Color aliceblue = new Color(240, 248, 255);
    public static final Color antiquewhite = new Color(250, 235, 215);
    public static final Color aqua = new Color(0, 255, 255);
    public static final Color aquamarine = new Color(127, 255, 212);
    public static final Color azure = new Color(240, 255, 255);
    public static final Color beige = new Color(245, 245, 220);
    public static final Color bisque = new Color(255, 228, 196);
    public static final Color black = new Color(0, 0, 0);
    public static final Color blanchedalmond = new Color(255, 255, 205);
    public static final Color blue = new Color(0, 0, 255);
    public static final Color blueviolet = new Color(138, 43, 226);
    public static final Color brown = new Color(165, 42, 42);
    public static final Color burlywood = new Color(222, 184, 135);
    public static final Color cadetblue = new Color(95, 158, 160);
    public static final Color chartreuse = new Color(127, 255, 0);
    public static final Color chocolate = new Color(210, 105, 30);
    public static final Color coral = new Color(255, 127, 80);
    public static final Color cornflowerblue = new Color(100, 149, 237);
    public static final Color cornsilk = new Color(255, 248, 220);
    public static final Color crimson = new Color(220, 20, 60);
    public static final Color cyan = new Color(0, 255, 255);
    public static final Color darkblue = new Color(0, 0, 139);
    public static final Color darkcyan = new Color(0, 139, 139);
    public static final Color darkgoldenrod = new Color(184, 134, 11);
    public static final Color darkgray = new Color(169, 169, 169);
    public static final Color darkgreen = new Color(0, 100, 0);
    public static final Color darkkhaki = new Color(189, 183, 107);
    public static final Color darkmagenta = new Color(139, 0, 139);
    public static final Color darkolivegreen = new Color(85, 107, 47);
    public static final Color darkorange = new Color(255, 140, 0);
    public static final Color darkorchid = new Color(153, 50, 204);
    public static final Color darkred = new Color(139, 0, 0);
    public static final Color darksalmon = new Color(233, 150, 122);
    public static final Color darkseagreen = new Color(143, 188, 143);
    public static final Color darkslateblue = new Color(72, 61, 139);
    public static final Color darkslategray = new Color(47, 79, 79);
    public static final Color darkturquoise = new Color(0, 206, 209);
    public static final Color darkviolet = new Color(148, 0, 211);
    public static final Color deeppink = new Color(255, 20, 147);
    public static final Color deepskyblue = new Color(0, 191, 255);
    public static final Color dimgray = new Color(105, 105, 105);
    public static final Color dodgerblue = new Color(30, 144, 255);
    public static final Color firebrick = new Color(178, 34, 34);
    public static final Color floralwhite = new Color(255, 250, 240);
    public static final Color forestgreen = new Color(34, 139, 34);
    public static final Color fuchsia = new Color(255, 0, 255);
    public static final Color gainsboro = new Color(220, 220, 220);
    public static final Color ghostwhite = new Color(248, 248, 255);
    public static final Color gold = new Color(255, 215, 0);
    public static final Color goldenrod = new Color(218, 165, 32);
    public static final Color gray = new Color(128, 128, 128);
    public static final Color green = new Color(0, 128, 0);
    public static final Color greenyellow = new Color(173, 255, 47);
    public static final Color honeydew = new Color(240, 255, 240);
    public static final Color hotpink = new Color(255, 105, 180);
    public static final Color indianred = new Color(205, 92, 92);
    public static final Color indigo = new Color(75, 0, 130);
    public static final Color ivory = new Color(255, 240, 240);
    public static final Color khaki = new Color(240, 230, 140);
    public static final Color lavender = new Color(230, 230, 250);
    public static final Color lavenderblush = new Color(255, 240, 245);
    public static final Color lawngreen = new Color(124, 252, 0);
    public static final Color lemonchiffon = new Color(255, 250, 205);
    public static final Color lightblue = new Color(173, 216, 230);
    public static final Color lightcoral = new Color(240, 128, 128);
    public static final Color lightcyan = new Color(224, 255, 255);
    public static final Color lightgoldenrodyellow = new Color(250, 250, 210);
    public static final Color lightgreen = new Color(144, 238, 144);
    public static final Color lightgrey = new Color(211, 211, 211);
    public static final Color lightpink = new Color(255, 182, 193);
    public static final Color lightsalmon = new Color(255, 160, 122);
    public static final Color lightseagreen = new Color(32, 178, 170);
    public static final Color lightskyblue = new Color(135, 206, 250);
    public static final Color lightslategray = new Color(119, 136, 153);
    public static final Color lightsteelblue = new Color(176, 196, 222);
    public static final Color lightyellow = new Color(255, 255, 224);
    public static final Color lime = new Color(0, 255, 0);
    public static final Color limegreen = new Color(50, 205, 50);
    public static final Color linen = new Color(250, 240, 230);
    public static final Color magenta = new Color(255, 0, 255);
    public static final Color maroon = new Color(128, 0, 0);
    public static final Color mediumaquamarine = new Color(102, 205, 170);
    public static final Color mediumblue = new Color(0, 0, 205);
    public static final Color mediumorchid = new Color(186, 85, 211);
    public static final Color mediumpurple = new Color(147, 112, 219);
    public static final Color mediumseagreen = new Color(60, 179, 113);
    public static final Color mediumslateblue = new Color(123, 104, 238);
    public static final Color mediumspringgreen = new Color(0, 250, 154);
    public static final Color mediumturquoise = new Color(72, 209, 204);
    public static final Color mediumvioletred = new Color(199, 21, 133);
    public static final Color midnightblue = new Color(25, 25, 112);
    public static final Color mintcream = new Color(245, 255, 250);
    public static final Color mistyrose = new Color(255, 228, 225);
    public static final Color mocassin = new Color(255, 228, 181);
    public static final Color navajowhite = new Color(255, 222, 173);
    public static final Color navy = new Color(0, 0, 128);
    public static final Color oldlace = new Color(253, 245, 230);
    public static final Color olive = new Color(128, 128, 0);
    public static final Color olivedrab = new Color(107, 142, 35);
    public static final Color orange = new Color(255, 165, 0);
    public static final Color orangered = new Color(255, 69, 0);
    public static final Color orchid = new Color(218, 112, 214);
    public static final Color palegoldenrod = new Color(238, 232, 170);
    public static final Color palegreen = new Color(152, 251, 152);
    public static final Color paleturquoise = new Color(175, 238, 238);
    public static final Color palevioletred = new Color(219, 112, 147);
    public static final Color papayawhip = new Color(255, 239, 213);
    public static final Color peachpuff = new Color(255, 218, 185);
    public static final Color peru = new Color(205, 133, 63);
    public static final Color pink = new Color(255, 192, 203);
    public static final Color plum = new Color(221, 160, 221);
    public static final Color powderblue = new Color(176, 224, 230);
    public static final Color purple = new Color(128, 0, 128);
    public static final Color red = new Color(255, 0, 0);
    public static final Color rosybrown = new Color(188, 143, 143);
    public static final Color royalblue = new Color(65, 105, 225);
    public static final Color saddlebrown = new Color(139, 69, 19);
    public static final Color salmon = new Color(250, 128, 114);
    public static final Color sandybrown = new Color(244, 164, 96);
    public static final Color seagreen = new Color(46, 139, 87);
    public static final Color seashell = new Color(255, 245, 238);
    public static final Color sienna = new Color(160, 82, 45);
    public static final Color silver = new Color(192, 192, 192);
    public static final Color skyblue = new Color(135, 206, 235);
    public static final Color slateblue = new Color(106, 90, 205);
    public static final Color slategray = new Color(112, 128, 144);
    public static final Color snow = new Color(255, 250, 250);
    public static final Color springgreen = new Color(0, 255, 127);
    public static final Color steelblue = new Color(70, 138, 180);
    public static final Color tan = new Color(210, 180, 140);
    public static final Color teal = new Color(0, 128, 128);
    public static final Color thistle = new Color(216, 191, 216);
    public static final Color tomato = new Color(253, 99, 71);
    public static final Color turquoise = new Color(64, 224, 208);
    public static final Color violet = new Color(238, 130, 238);
    public static final Color wheat = new Color(245, 222, 179);
    public static final Color white = new Color(255, 255, 255);
    public static final Color whitesmoke = new Color(245, 245, 245);
    public static final Color yellow = new Color(255, 255, 0);
    public static final Color yellowgreen = new Color(154, 205, 50);

    private static final Color[] CLASS_COLOR = new Color[] { aliceblue, chartreuse, aqua, aquamarine, azure, beige,
            bisque, blanchedalmond, blueviolet, burlywood, cadetblue, antiquewhite, chocolate, coral, cornflowerblue,
            cornsilk, crimson, cyan, darkcyan, darkgoldenrod, darkgray, darkkhaki, darkorange, darkorchid, darksalmon,
            darkseagreen, darkturquoise, darkviolet, deeppink, deepskyblue, dodgerblue, firebrick, floralwhite,
            forestgreen, fuchsia, gainsboro, ghostwhite, gold, goldenrod, salmon, tan, honeydew, hotpink, indianred,
            ivory, khaki, lavender, lavenderblush, lawngreen, lemonchiffon, lightblue, lightcoral, lightcyan,
            lightgoldenrodyellow, lightgreen, lightgrey, lightgreen, lightpink, lightsalmon, lightseagreen,
            lightskyblue, lightslategray, lightslategray, lightsteelblue, lightyellow, lime, limegreen, linen, magenta,
            mediumaquamarine, mediumorchid, mediumpurple, mediumseagreen, mediumslateblue, mediumspringgreen,
            mediumturquoise, mediumvioletred, mintcream, mistyrose, mocassin, navajowhite, oldlace, olive, olivedrab,
            orange, orangered, orchid, palegoldenrod, palegreen, paleturquoise, palevioletred, papayawhip, peachpuff,
            peru, pink, plum, powderblue, purple, red, rosybrown, royalblue, saddlebrown, green, sandybrown, seagreen,
            seashell, sienna, silver, skyblue, slateblue, slategray, slategray, snow, springgreen, steelblue,
            greenyellow, teal, thistle, tomato, turquoise, violet, wheat, white, whitesmoke, yellow, yellowgreen };

    public static Color getClassColor(int id) {
        return CLASS_COLOR[id % CLASS_COLOR.length];
    }
}