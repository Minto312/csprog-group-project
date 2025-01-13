
public class Item {

    public static class Feather {
        public static final int id = 2;
        public static final String imagePath = "png/feather.png";

        public static void taken(MoveChara chara) {
            System.out.println("Feather taken! Move speed increase to " + chara.getSpeed() + "!");
            chara.setSpeed(chara.getSpeed() + 1);
        }
    }

    public static class Coin {
        public static final int id = 3;
        public static final String imagePath = "png/coin.png";

        private static int count = 0;

        public static void taken() {
            count++;
            System.out.println("Coin taken! Total: " + count);
        }

        public static int getCount() {
            return count;
        }
    }
}
