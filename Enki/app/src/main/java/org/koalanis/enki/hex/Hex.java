package org.koalanis.enki.hex;

/**
 * Created by kaleb on 8/10/16.
 */
public class Hex {
    private static Hex ourInstance = new Hex();

    public static Hex I() {
        return ourInstance;
    }

    private Hex() {
    }

    public class Axeal {
        private int r,c;

        public Axeal(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Axeal axeal = (Axeal) o;

            if (r != axeal.r) return false;
            return c == axeal.c;

        }

        @Override
        public int hashCode() {
            int result = r;
            result = 31 * result + c;
            return result;
        }
    }

    public Axeal makeAxeal(int r, int c) {
        return new Axeal(r,c);
    }



    public class Offset {
        private int r,c;

        public Offset(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Offset offset = (Offset) o;

            if (r != offset.r) return false;
            return c == offset.c;

        }

        @Override
        public int hashCode() {
            int result = r;
            result = 31 * result + c;
            return result;
        }
    }

    public Offset makeOffset(int r, int c) {
        return new Offset(r,c);
    }


    public class Cubic {
        private int x,y,z;



        public Cubic(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cubic cubic = (Cubic) o;

            if (x != cubic.x) return false;
            if (y != cubic.y) return false;
            return z == cubic.z;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }

    public Cubic makeCubic(int x, int y, int z) {
        return new Cubic(x,y,z);
    }

}
