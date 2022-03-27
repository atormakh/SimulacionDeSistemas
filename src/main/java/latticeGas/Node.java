package latticeGas;

public class Node {

    char in_state;
    char out_state;

    final char R = 1 << 6;
    final char S = 1 << 7;

    char[] rotations = {
            0b001001, 0b010010, 0b100100,
            0b010101, 0b101010,
            0b110110, 0b101101, 0b011011,
    };

    public Node() {
        in_state = out_state = Math.random() < 0.5 ? R : 0;

    }

    public void setBoundary(boolean b) {
        if (b) {
            in_state |= S;
            out_state |= S;
        } else {
            in_state &= ~S;
            out_state &= ~S;
        }

    }

    public void setInDirection(int direction, boolean b) {
        if (b) {
            in_state |= (1 << direction);
        } else {
            in_state &= ~(1 << direction);
        }
    }


    public boolean getOutDirection(int direction) {
        return (out_state & (1 << direction)) != 0;
    }

    public boolean getInDirection(int direction) {
        return (in_state & (1 << direction)) != 0;
    }

    public int getBits(char bits) {
        int sum = 0;


        while (bits != 0) {
            if ((bits & 1) != 0)
                sum++;
            bits = (char) (bits >> 1);
        }
        return sum;
    }


    public char rotateDirection(char bits, boolean right) {
        return right ? (char) (bits << 1 | bits >> 5) : (char) (bits >> 1 | bits << 5);
    }

    public void recalculate() {
        out_state = in_state;
        in_state = (char) (out_state & (R | S));

        if ((out_state & S) != 0) {
            char new_state = (char) (out_state & (R | S));
            for (int i = 0; i < 6; i++) {
                if ((out_state & (1 << i)) != 0) {
                    new_state |= (1 << ((i + 3) % 6));
                }
            }
            out_state = new_state;
            return;
        }


        char directions = (char) (out_state & ~(S | R));

        for (char rotation : rotations) {
            if (rotation == directions) {
                out_state = rotateDirection(directions, (out_state & R) != 0);
                out_state |= (char) (in_state & (R | S));
                return;
            }


        }
    }
}
