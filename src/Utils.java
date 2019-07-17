public class Utils
{
    public static int[][] toIntArray(long[][] longArray) {
        int[][] intArray = new int[longArray.length][longArray[0].length];
        for (int i = 0; i < longArray.length; i++) {
            for (int j = 0; j < longArray[0].length; j++) {
                intArray[i][j] = (int) longArray[i][j];
            }
        }
        return intArray;
    }

    public static byte[] toBytes(int[][] pixels) {
        byte[] b = new byte[pixels.length * pixels[0].length * 4];
        int bi = 0;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                b[bi + 0] = (byte) (i >> 24);
                b[bi + 1] = (byte) (i >> 16);
                b[bi + 2] = (byte) (i >> 8);
                b[bi + 3] = (byte) (i /* >> 0 */);
                bi = bi + 4;
            }
        }
        return b;
    }

    public static int[][] toInts(byte[] b, int ic, int jc) {
        int[][] intResult = new int[ic][jc];
        int bi = 0;
        for (int i = 0; i < ic; i++) {
            for (int j = 0; j < jc; j++) {
                intResult[i][j] = (b[bi] << 24) + (b[bi + 1] << 16) + (b[bi + 2] << 8) + b[bi + 3];
                bi = bi + 4;
            }
        }
        return intResult;
    }
}