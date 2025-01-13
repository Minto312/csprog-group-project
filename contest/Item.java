
public class Item {

    public static class Feather {
        public static final int id = 2;
        public static final String imagePath = "png/feather.png";

        public static void taken() {
            System.out.println("Feather taken! Move speed increased!");
        }
    }

    public static class Coin {
        public static final int id = 3;
        public static final String imagePath = "png/coin.png";

        public static void taken() {
            System.out.println("Coin taken! Count increased!");
        }
    }
}
